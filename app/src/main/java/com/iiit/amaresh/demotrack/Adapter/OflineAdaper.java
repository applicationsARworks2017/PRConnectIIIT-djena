package com.iiit.amaresh.demotrack.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.iiit.amaresh.demotrack.Database.DBHelper;
import com.iiit.amaresh.demotrack.Pojo.Constants;
import com.iiit.amaresh.demotrack.Pojo.CustomVolleyRequest;
import com.iiit.amaresh.demotrack.Pojo.MultipartUtility;
import com.iiit.amaresh.demotrack.Pojo.Oflinedata;
import com.iiit.amaresh.demotrack.Pojo.Util;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by RN on 11/7/2017.
 */

public class OflineAdaper extends BaseAdapter {

    Holder holder,holder_new;
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
    String file_name;
    Bitmap photo;
    File imageFile,videofile,filee;
    Long vfileSizeInMB,fileSizeInMB,FfileSizeInMB;
    int server_status;
    String server_message;
    DBHelper dbobj;
    String filetype,filedoc,file_path,file_type_doc;

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
        private ImageView d_icon,file_img;
        public NetworkImageView i_image;
        VideoView ivVideo;
        FrameLayout vshow_frame;
        Bitmap vdoBitmap;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Oflinedata pos=alldataList.get(position);
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
            holder.file_img = (ImageView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.file_img);
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
        holder.d_icon.setTag(holder);
//        holder.Username.setText(pos.getUser_id());
       // holder.Designation.setText(pos.getd());
        holder.Title.setText(pos.getTitle());
        //holder.Time.setText(pos.gettime());
        holder.Address.setText(pos.getAddress());
        image_name=pos.getImage();
        file_name=pos.getFile_file();

        if(image_name!=null && image_name!="") {
            String new_word = image_name.substring(image_name.length() - 4);
            if (new_word.contentEquals(".jpg") || new_word.contentEquals(".png") || new_word.contains("jpeg")) {
                holder.i_image.setVisibility(View.VISIBLE);
                holder.vshow_frame.setVisibility(View.GONE);
                imgUrl = Constants.DOWNLOAD_URL + image_name;
                imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
                imageLoader.get(imgUrl, ImageLoader.getImageListener(holder.i_image, com.iiit.amaresh.demotrack.R.drawable.rounded_image, android.R.drawable.ic_dialog_alert));
                holder.i_image.setImageUrl(imgUrl, imageLoader);
            }
        }
        if(file_name!=null && file_name!="") {
            String file_word = file_name.substring(file_name.length() - 4);
            if (file_word.contentEquals(".doc") || file_word.contentEquals(".pdf")) {
                holder.file_img.setVisibility(View.VISIBLE);
                //fileUrl = Constants.DOWNLOAD_URL + image_name;

            }
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

        holder.d_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.getNetworkConnectivityStatus(context)) {
                    holder_new = (Holder) view.getTag();
                    filetype=pos.getVideo();
                    if(filetype!=null){
                    if(filetype.contentEquals("image_file")) {
                        Uri image_uri = Uri.parse(pos.getImage());
                        imageFile = new File(image_uri.getPath());
                        long length = imageFile.length();
                        long fileSizeInKB = length / 1024;
                        fileSizeInMB = fileSizeInKB / 1024;
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Your File is " + fileSizeInMB + " MB. Do you want to upload")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        uploadImage upload = new uploadImage();
                                        upload.execute(String.valueOf(pos.getUser_id()), pos.getLatitude(), pos.getLongitude(), pos.getTitle(), pos.getAddress());
                                        String title = pos.getTitle();
                                        dbobj = new DBHelper(context);
                                        dbobj.deleteRow(pos.getTitle());
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
                    else if(Util.getNetworkConnectivityStatus(context)) {
                        holder_new = (Holder) view.getTag();
                        filedoc = pos.getFile_file();
                        if (filedoc != null) {
                            file_type_doc = filedoc.substring(filedoc.length() - 4);
                            if (file_type_doc.contentEquals(".doc") || file_type_doc.contentEquals(".pdf")) {

                                file_path = pos.getFile_file();
                                filee = new File(file_path);
                                long length = filee.length();
                                long fileSizeInKB = length / 1024;
                                FfileSizeInMB = fileSizeInKB / 1024;
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("Your File is " + FfileSizeInMB + " MB. Do you want to upload")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                uploadImage upload = new uploadImage();
                                                upload.execute(String.valueOf(pos.getUser_id()), pos.getLatitude(), pos.getLongitude(), pos.getTitle(), pos.getAddress());
                                                String title = pos.getTitle();
                                                dbobj = new DBHelper(context);
                                                dbobj.deleteRow(pos.getTitle());
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
                    else{
                        Uri vudeo_uri=Uri.parse(pos.getImage());
                        videofile=new File(vudeo_uri.getPath());
                        long v_length = videofile.length();
                        long vfileSizeInKB = v_length / 1024;
                        vfileSizeInMB = vfileSizeInKB / 1024;
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Your File is " + vfileSizeInMB + " MB. Do you want to upload")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        uploadImage upload = new uploadImage();
                                        upload.execute(String.valueOf(pos.getUser_id()),pos.getLatitude(),pos.getLongitude(),pos.getTitle(),pos.getAddress());
                                        dbobj=new DBHelper(context);
                                        dbobj.deleteRow(pos.getTitle());
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
                else{
                    Toast.makeText(context,"No Internet",Toast.LENGTH_LONG).show();
                }
            }
        });


        return convertView;
    }
    private class uploadImage extends AsyncTask<String, Void, Void> {

        String TAG = "FileUpload";
        private boolean is_success = false;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog.show(context, "Please Wait",
                    "Uploading Files...", true);
        }

        @Override
        protected Void doInBackground(String... params) {
            String charset = "UTF-8";
            String requestURL = "";
            //requestURL = Config.API_BASE_URL + Config.POST_CITIZEN_NEWS;
            requestURL = Constants.ONLINE_URL+Constants.UPLOAD_IMAGE;

            try {
                String User_id = params[0];
                String lat = params[1];
                String lng = params[2];
                String title = params[3];
                String address = params[4];
                //String is_anonymous = params[3];
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFormField("emp_id", User_id);
                multipart.addFormField("latitude", lat);
                multipart.addFormField("logitude", lng);
                multipart.addFormField("title", title);
                multipart.addFormField("address", address);
                // if (images_to_post != null && images_to_post.exists())
                if(filetype!=null) {
                    if (filetype.contentEquals("image_file")) {
                        multipart.addFilePart("file_name", imageFile);
                    }
                }
                if(file_type_doc!=null) {
                    if (file_type_doc.contentEquals(".doc") || file_type_doc.contentEquals(".pdf")) {
                        multipart.addFilePart("file_name", filee);

                    }
                }
                else{
                    multipart.addFilePart("file_name", videofile);
                }
                List<String> response = multipart.finish();
                System.out.println("SERVER REPLIED:");
                String res = "";
                for (String line : response) {
                    res = res + line + "\n";
                }
                Log.i(TAG, res);

                if(res != null && res.length() > 0) {
                    JSONObject res_server = new JSONObject(res.trim());
                    server_status = res_server.optInt("status");
                    if (server_status == 1) {
                        server_message="Uploaded Successfully";

                    } else {
                        server_message="Invalid Upload";
                    }
                }
               /* if (res != null && res.trim().length() > 0 && res.trim().equalsIgnoreCase("1")) {
                    is_success = true;
                    server_status=1;
                } else {
                    is_success = false;
                    server_status=0;
                }*/
            } catch (ConnectTimeoutException e) {
                server_message="Network Error";
                System.err.println(e);
            } catch (IOException ex) {
                server_message="Network Error";
                System.err.println(ex);
            } catch (JSONException e) {
                server_message="Network Error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            progress.dismiss();
            if(server_status==1){
                holder_new.d_icon.setVisibility(View.GONE);
            }
            else{
                Toast.makeText(context,server_message, Toast.LENGTH_SHORT).show();
            }

        }
    }



}