package com.iiit.amaresh.demotrack.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iiit.amaresh.demotrack.Activity.GmapSelf;
import com.iiit.amaresh.demotrack.Activity.LocationListingByUser;
import com.iiit.amaresh.demotrack.Pojo.LocationList;
import com.iiit.amaresh.demotrack.R;

import java.util.List;

public class LocationListingAdapter extends BaseAdapter {
    Context context;
    ProgressDialog progress;
    List<LocationList> location_lis;
    Holder holder;


    public LocationListingAdapter(LocationListingByUser locationListingByUser, List<LocationList> data) {
        this.context=locationListingByUser;
        this.location_lis=data;

    }

    @Override
    public int getCount() {
        return location_lis.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder{
        private TextView Date;
        private TextView latitude;
        private TextView longitude;
        private TextView address;
        private ImageView GPSicon;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LocationList loc_pos=location_lis.get(position);

        holder = new LocationListingAdapter.Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.activity_location_listing_adapter, parent, false);

            holder.Date=(TextView)convertView.findViewById(R.id.tvdate);
            holder.latitude=(TextView)convertView.findViewById(R.id.tvLatitude);
            holder.longitude=(TextView)convertView.findViewById(R.id.tvlongitude);
            holder.address=(TextView)convertView.findViewById(R.id.tvaddress);
            holder.GPSicon=(ImageView)convertView.findViewById(R.id.gpsicon);
            convertView.setTag(holder);
        }
        else{
            holder = (LocationListingAdapter.Holder) convertView.getTag();
        }
        holder.Date.setTag(position);
        holder.latitude.setTag(position);
        holder.longitude.setTag(position);
        holder.address.setTag(position);
        holder.GPSicon.setTag(position);

        holder.Date.setText("Date :"+loc_pos.getLoc_created());
        holder.latitude.setText("Latitude :"+loc_pos.getLoc_latitude());
        holder.longitude.setText("Longitude :"+loc_pos.getLoc_longitude());
        holder.longitude.setText("Longitude :"+loc_pos.getLoc_longitude());
      //  holder.address.setText("Address :"+loc_pos.get());

        holder.GPSicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (Integer) v.getTag();
                String lat=loc_pos.getLoc_latitude();
                String lon=loc_pos.getLoc_longitude();
                //Toast.makeText(context,lat+lon,Toast.LENGTH_LONG).show();
                Intent intent=new Intent(context, GmapSelf.class);
                intent.putExtra("latitude",lat);
                intent.putExtra("longitude",lon);
                context.startActivity(intent);


            }
        });


        return convertView;
    }
}
