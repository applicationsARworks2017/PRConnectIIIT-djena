package com.iiit.amaresh.demotrack.Activity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GmapSelf extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback {
    GoogleMap googleMap;
    SupportMapFragment fm;
    private String provider;
    Geocoder geocoder;
    MarkerOptions markerOptions;
    String address,city,state;
    String latitude,longitude;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.iiit.amaresh.demotrack.R.layout.activity_gmap_self);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            latitude = extras.getString("latitude");
            longitude = extras.getString("longitude");
        }

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        // Getting reference to SupportMapFragment of the activity_main
        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(com.iiit.amaresh.demotrack.R.id.self_map);

        // Getting Map for the SupportMapFragment
         fm.getMapAsync(this);






       /* double lat = location.getLatitude();
        double lng = location.getLongitude();
        latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));*/


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        double lat = Double.parseDouble(latitude);
        double lng = Double.parseDouble(longitude);
        LatLng latLng = new LatLng(lat, lng);
        // for getting the address
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            address=addresses.get(0).getAddressLine(0);
            city=addresses.get(0).getLocality();
            state=addresses.get(0).getAdminArea();
        } catch (IOException e) {
            e.printStackTrace();
        }


        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        markerOptions = new MarkerOptions();
        markerOptions.position(latLng).title(address+","+city+","+state);
        googleMap.addMarker(markerOptions);
        googleMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

    }
}
