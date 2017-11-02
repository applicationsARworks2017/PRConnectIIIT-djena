package com.iiit.amaresh.demotrack.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.AutosendData.AutoStartUpdate;
import com.iiit.amaresh.demotrack.Extra.BaseActivity;
import com.iiit.amaresh.demotrack.Pojo.Constants;
import com.iiit.amaresh.demotrack.Pojo.Useronlineoffline;
import com.iiit.amaresh.demotrack.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class  Home extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,android.location.LocationListener {
    NavigationView navigationView;
    LinearLayout layout_points;
    SharedPreferences sharedPreferences;
    LinearLayout ur_position,ownpos,ass_upload,track_sub,get_move,galary,messages;
    int user_id,server_status;
    LocationManager locationManager;
    String provider,latitude,longitude,phone_number,name,server_response,id;
    TextView tv1,tv2;
    Useronlineoffline useronlineoffline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       // sharedPreferences = getApplication().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0);
       // String username = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_NAME, null);
         //user_id=sharedPreferences.getInt(Constants.SP_USER_ID,0);
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        navigationView.addHeaderView(header);

        phone_number = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_PHONE, null);
        name = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_NAME, null);
        user_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.SP_USER_ID, 0);
         id=String.valueOf(user_id);

        // when home screen will open, service class should stop sending data to server.
        this.stopService(new Intent(this, AutoStartUpdate.class));

        reeatcalltostatus();




        tv1=(TextView)header.findViewById(R.id.tvUserFullName);
        tv1.setText(name);
        tv2=(TextView)header.findViewById(R.id.tvUserPhone);
        tv2.setText(phone_number);


        ur_position=(LinearLayout) findViewById(R.id.urposition);
        ur_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Home.this,Trackme.class);
                startActivity(i);
            }
        });
        ownpos=(LinearLayout) findViewById(R.id.ownhis);
        ownpos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Home.this,OwnHistory.class);
                startActivity(i);
            }
        });
        ass_upload=(LinearLayout) findViewById(R.id.assets_ipload);
        ass_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Home.this,UploadAssets.class);
                startActivity(i);
            }
        });

        track_sub=(LinearLayout)findViewById(R.id.tracksubordinates);
        track_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Home.this,SubOrdinateHistory.class);
                startActivity(i);
            }
        });
        messages=(LinearLayout) findViewById(R.id.layoutMessage);
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Home.this,PrimaryNotices.class);
                startActivity(i);
            }
        });
        get_move=(LinearLayout)findViewById(R.id.getmovement);
        get_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Home.this,GetMovement.class);
                startActivity(i);
            }
        });
        galary=(LinearLayout)findViewById(R.id.galary_layout);
        galary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Home.this,GalaryActivity.class);
                startActivity(i);
            }
        });




        ///// geting the lat lng

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default

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
            Toast.makeText(Home.this,"Allow to GPS",Toast.LENGTH_LONG).show();
        }

        //////end getting the lat lng
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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if (id == R.id.viewAll) {
            Intent intent = new Intent(this, ViewAllUsers.class);
            startActivity(intent);
        }
       /*
        else if(id==R.id.attendance){
            Intent intent = new Intent(this, Attendance.class);
            startActivity(intent);
        }
        else if(id==R.id.marks){
            Intent intent=new Intent(this, SecuredMarks.class);
            startActivity(intent);
        }
        else if(id==R.id.exams){
            Intent intent=new Intent(this, Examination.class);
            startActivity(intent);
        }
        else if(id==R.id.notice){
            Intent intent=new Intent(this, ExamNotice.class);
            startActivity(intent);
        }
        else if(id==R.id.homeworks){
            Intent intent=new Intent(this, Homeworks.class);
            startActivity(intent);
        }*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(provider, 400, 1, this);
        }
        else {
            Toast.makeText(Home.this,"Allow to GPS",Toast.LENGTH_LONG).show();
        }
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        reeatcalltostatus();
        // write code here whatever you want to do after application close
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(provider, 4000, 1, this);
        }
        else {
            Toast.makeText(Home.this,"Allow to GPS",Toast.LENGTH_LONG).show();
        }
     /*   if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.removeUpdates(this);*/
    }


    @Override
    public void onLocationChanged(Location location) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latitude = String.valueOf(lat);
            longitude = String.valueOf(lng);
            sendlatlongtoserver(id,latitude,longitude,phone_number,name);
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

    public  void sendlatlongtoserver(String id, String latitude, String longitude, String phone_number, String name){
        this.latitude= latitude;
        this.longitude= longitude;
        this.id=id;
        this.phone_number=phone_number;
        this.name=name;
        uploadpos upload = new uploadpos();
        upload.execute(id, latitude, longitude, phone_number, name);


       /* if (Util.getNetworkConnectivityStatus(getApplicationContext())) {
            uploadpos upload = new uploadpos();
            upload.execute(id, latitude, longitude, phone_number, name);
        }
        else {
            Toast.makeText(getApplication(), "No internet Connection", Toast.LENGTH_LONG).show();

        }*/
    }
    public class uploadpos extends AsyncTask<String, Void, Void> {
        private static final String TAG = "register_user";
       // private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* if (progressDialog == null) {
                progressDialog = ProgressDialog.show(getApplication(), "Loading", "Please wait...");
            }*/
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                String _emp_id = params[0];
                String _lat = params[1];
                String _lng = params[2];
                String _ph = params[3];
                String _name = params[4];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.ONLINE_URL+Constants.UPLOAD_POS;
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setAllowUserInteraction(false);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("emp_id", _emp_id)
                        .appendQueryParameter("empphone", _ph)
                        .appendQueryParameter("title", _name)
                        .appendQueryParameter("latitude", _lat)
                        .appendQueryParameter("longitude", _lng);
                //.appendQueryParameter("deviceid", deviceid);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = conn.getInputStream();
                }
                if (in == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "", data = "";

                while ((data = reader.readLine()) != null) {
                    response += data + "\n";
                }

                Log.i(TAG, "Response : " + response);

                /**
                 *
                 {
                 "emp": {
                 "id": "394",
                 "empname": "Sri Deoranjan Kumar Singh",
                 "empphone": "8280405001",
                 "empmail": "demo@gmail.com",
                 "empdesg": "Commissioner-cum-Secretary",
                 "usertype": "0"
                 },
                 "status": 1,
                 "message": "Records available"
                 }
                 * */

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status == 1){
                        server_response="Updated Successfully";
                    }
                    else{
                        server_response="Error in Uploading";

                    }

                    // server_response = res.optString("message");


                    // int status = res.optInt("login_status");
                    //  message = res.optString("message");
                }

                return null;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
      //      Toast.makeText(Home.this,server_response,Toast.LENGTH_LONG).show();
           // progressDialog.cancel();









        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            //super.onBackPressed();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

}
