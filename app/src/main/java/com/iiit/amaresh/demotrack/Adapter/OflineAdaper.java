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
    String imgUrl=null,image_name,video_url=null;
    ImageLoader imageLoader;
    String IPATH=null;
    MediaController media_Controller;
    DisplayMetrics dm;
    String targetFileName;
    String imgUrlnew;
    String new_wordd;
    String filetype;

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

        Oflinedata pos=alldataList.get(position);
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
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.Username.setTag(position);
        holder.Designation.setTag(position);
        holder.Title.setTag(position);
        holder.Time.setTag(position);
        holder.Address.setTag(position);
        holder.i_image.setTag(position);
        holder.ivVideo.setTag(position);
        holder.vshow_frame.setTag(position);
        holder.d_icon.setTag(position);
//        holder.Username.setText(pos.getUser_id());
       // holder.Designation.setText(pos.getd());
        holder.Title.setText(pos.getTitle());
        //holder.Time.setText(pos.gettime());
        holder.Address.setText(pos.getAddress());
        image_name=pos.getImage();
        String new_word = image_name.substring(image_name.length() - 4);
        if(new_word.contentEquals(".jpg") || new_word.contentEquals(".png")|| new_word.contains("jpeg")){
            holder.i_image.setVisibility(View.VISIBLE);
            holder.vshow_frame.setVisibility(View.GONE);
            imgUrl = Constants.DOWNLOAD_URL + image_name;
            imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
            imageLoader.get(imgUrl, ImageLoader.getImageListener(holder.i_image, com.iiit.amaresh.demotrack.R.drawable.rounded_image, android.R.drawable.ic_dialog_alert));
            holder.i_image.setImageUrl(imgUrl, imageLoader);
        }
        else {
            holder.i_image.setVisibility(View.GONE);
            holder.vshow_frame.setVisibility(View.VISIBLE);
            media_Controller = new MediaController(context);
            video_url=Constants.DOWNLOAD_URL + image_name;
            holder.ivVideo.setVideoPath(video_url);
            holder.i_image.requestFocus();
            holder.ivVideo.setMediaController(media_Controller);
            media_Controller.setAnchorView(holder.ivVideo);
            holder.ivVideo.start();
        }
        return convertView;
    }



}