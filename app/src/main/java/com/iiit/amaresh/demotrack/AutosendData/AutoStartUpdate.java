package com.iiit.amaresh.demotrack.AutosendData;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Activity.Home;
import com.iiit.amaresh.demotrack.Util.Constants;
import com.iiit.amaresh.demotrack.Pojo.Useronlineoffline;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jagat on 2/22/2017.
 */
public class AutoStartUpdate extends Service implements android.location.LocationListener {
    //    MainActivity mainActivity;
    int user_id,server_status;

    LocationManager locationManager;
    String latitude,longitude,provider,name,id,phone_number;
    Home obj;
    Useronlineoffline useronlineoffline;




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        user_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.SP_USER_ID, 0);
        phone_number = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_PHONE, null);
        name = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_NAME, null);
        id=String.valueOf(user_id);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        //Toast.makeText(AutoStartUpdate.this,"hello",Toast.LENGTH_LONG).show();
        reeatcalltostatus();
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling


            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(provider);

            // Initialize the location fields
            if (location != null) {
                System.out.println("Provider " + provider + " has been selected.");
                onLocationChanged(location);
            } else {
                // latituteField.setText("Location not available");
                //longitudeField.setText("Location not available");
            }

        }
        else {
            Toast.makeText(AutoStartUpdate.this,"Allow to GPS",Toast.LENGTH_LONG).show();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(provider, 4000, 1, this);
        }
        else {
            Toast.makeText(AutoStartUpdate.this,"Allow to GPS",Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onLocationChanged(Location location) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latitude = String.valueOf(lat);
            longitude = String.valueOf(lng);
            Calendar calander;
            SimpleDateFormat simpledateformat;
             calander = Calendar.getInstance();
            simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
           String time = simpledateformat.format(calander.getTime());
           // if(time.contentEquals())
            //  sendlatlongtoserver();
            obj=new Home();
            obj.sendlatlongtoserver(id,latitude,longitude,phone_number,name);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled provider " + provider,
                Toast.LENGTH_SHORT).show();
        // gps turnes on

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
        // gps turnes off


    }
    private void reeatcalltostatus() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            useronlineoffline=new Useronlineoffline();
                            useronlineoffline.updatestatus(id,"Online");

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 4000);
    }

}
