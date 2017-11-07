package com.iiit.amaresh.demotrack.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.iiit.amaresh.demotrack.Activity.MainActivity;
import com.iiit.amaresh.demotrack.Pojo.Constants;
import com.iiit.amaresh.demotrack.Pojo.CustomVolleyRequest;
import com.iiit.amaresh.demotrack.Pojo.ImageAll;
import com.iiit.amaresh.demotrack.Pojo.Oflinedata;
import com.iiit.amaresh.demotrack.Pojo.Util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by RN on 11/7/2017.
 */

public class OflineAdaper extends BaseAdapter {

    Holder holder;
    Context context;
    List<Oflinedata> alldataList;

    public OflineAdaper(Context context, List<Oflinedata> oflineList) {
        this.context = context;
        this.alldataList = oflineList;
        // imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return alldataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        private TextView Username, Designation, Time, Title, Address;
        //    private ImageView i_image,d_icon;
        private ImageView d_icon;
        public NetworkImageView i_image;
        VideoView ivVideo;
        FrameLayout vshow_frame;
        Bitmap vdoBitmap;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = new Holder();

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(com.iiit.amaresh.demotrack.R.layout.oflinelayout, parent, false);

            holder.Username = (TextView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.tvImUsername);
            holder.Designation = (TextView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.tvImdesig);
            holder.Time = (TextView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.tvImtime);
            holder.Address = (TextView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.tvImaddress);
            holder.Title = (TextView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.tvImtitle);
            holder.i_image = (NetworkImageView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.ivImage);
            holder.ivVideo = (VideoView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.ivVideo);
            holder.d_icon = (ImageView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.downloadicon);
            holder.vshow_frame = (FrameLayout) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.vshow_frame);
            convertView.setTag(holder);
        }

        return convertView;
    }
}