package com.iiit.amaresh.demotrack.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
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
import com.iiit.amaresh.demotrack.Activity.FileView;
import com.iiit.amaresh.demotrack.Activity.MainActivity;
import com.iiit.amaresh.demotrack.Pojo.CustomVolleyRequest;
import com.iiit.amaresh.demotrack.Pojo.ImageAll;
import com.iiit.amaresh.demotrack.Pojo.Util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static com.iiit.amaresh.demotrack.Util.Constants.DOWNLOAD_URL;

/**
 * Created by LIPL on 16/02/17.
 */
public class AllImageAdaper extends BaseAdapter {
    Context context;
    List<ImageAll> allimagelist;
    Holder holder;
    String imgUrl=null,image_name,video_url=null,fileUrl=null;
    ImageLoader imageLoader;
    String IPATH=null;
    MediaController media_Controller;
    DisplayMetrics dm;
    String targetFileName;
    String imgUrlnew;
    String new_wordd;
    String filetype;

    public AllImageAdaper(Context galaryActivity, List<ImageAll> allimagelist) {
        this.context=galaryActivity;
        this.allimagelist=allimagelist;
       // imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return allimagelist.size();
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
        private TextView Username,Designation,Time,Title,Address;
    //    private ImageView i_image,d_icon;
        private ImageView d_icon,file_img;
        public NetworkImageView i_image;
        VideoView ivVideo;
        FrameLayout vshow_frame;
        Bitmap vdoBitmap;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageAll img_pos=allimagelist.get(position);
        holder = new Holder();

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(com.iiit.amaresh.demotrack.R.layout.allimage_adapter_layout, parent, false);

            holder.Username = (TextView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.tvImUsername);
            holder.Designation = (TextView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.tvImdesig);
            holder.Time = (TextView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.tvImtime);
            holder.Address = (TextView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.tvImaddress);
            holder.Title = (TextView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.tvImtitle);
            holder.i_image = (NetworkImageView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.ivImage);
            holder.ivVideo = (VideoView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.ivVideo);
            holder.d_icon = (ImageView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.downloadicon);
            holder.file_img = (ImageView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.file_img);
           // holder.vshow_frame = (FrameLayout) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.vshow_frame);
            convertView.setTag(holder);
        }
        else{
            holder = (AllImageAdaper.Holder) convertView.getTag();
        }
        holder.Username.setTag(position);
        holder.Designation.setTag(position);
        holder.Title.setTag(position);
        holder.Time.setTag(position);
        holder.Address.setTag(position);
        holder.i_image.setTag(position);
        holder.file_img.setTag(position);
        holder.ivVideo.setTag(position);
       // holder.vshow_frame.setTag(position);
        holder.d_icon.setTag(position);
        //imgUrl = Constants.DOWNLOAD_URL + img_pos.getD_URL();

        holder.Username.setText(img_pos.getIm_username());
        holder.Designation.setText(img_pos.getIm_userdesg());
        holder.Title.setText(img_pos.getIm_title());
        holder.Time.setText(img_pos.getIm_created());
        holder.Address.setText(img_pos.getIm_add());
        image_name=img_pos.getIm_filename();
        String new_word = image_name.substring(image_name.length() - 4);
        if(new_word.contentEquals(".jpg") || new_word.contentEquals(".png")|| new_word.contains("jpeg")){
            holder.file_img.setVisibility(View.GONE);
            holder.ivVideo.setVisibility(View.GONE);
            holder.i_image.setVisibility(View.VISIBLE);
            //holder.vshow_frame.setVisibility(View.GONE);
            imgUrl = DOWNLOAD_URL + image_name;
            imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
            imageLoader.get(imgUrl, ImageLoader.getImageListener(holder.i_image, com.iiit.amaresh.demotrack.R.drawable.rounded_image, android.R.drawable.ic_dialog_alert));
            holder.i_image.setImageUrl(imgUrl, imageLoader);
        }
        else if(new_word.contentEquals(".doc")||new_word.contentEquals(".pdf")){
            holder.file_img.setVisibility(View.VISIBLE);
            holder.i_image.setVisibility(View.GONE);
            holder.ivVideo.setVisibility(View.GONE);

            //fileUrl = Constants.DOWNLOAD_URL + image_name;

        }
        else {
            holder.file_img.setVisibility(View.GONE);
            holder.i_image.setVisibility(View.GONE);
            holder.ivVideo.setVisibility(View.VISIBLE);
            //holder.vshow_frame.setVisibility(View.VISIBLE);
            //media_Controller = new MediaController(context);
            video_url= DOWNLOAD_URL + image_name;
           // videofile=new File(selectedVideo.getPath());
           /* holder.ivVideo.setVideoPath(video_url);
            holder.ivVideo.requestFocus();
            holder.ivVideo.setMediaController(media_Controller);
            media_Controller.setAnchorView(holder.ivVideo);
            holder.ivVideo.start();
*/
            Uri video = Uri.parse(video_url);
            holder.ivVideo.setVideoURI(video);
            MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(holder.ivVideo);
            holder.ivVideo.setMediaController(mediaController);
            holder.ivVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    holder.ivVideo.start();
                }
            });
        }

        holder.d_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.getNetworkConnectivityStatus(context)) {


                     imgUrlnew = DOWNLOAD_URL + img_pos.getIm_filename();
                     new_wordd = imgUrlnew.substring(imgUrlnew.length() - 4);
                    //Toast.makeText(context,pos,Toast.LENGTH_LONG).show();
                    filetype=img_pos.getIm_filename();
                    DownloadImage asyncTask = new DownloadImage();
                    asyncTask.execute(imgUrlnew);


                } else {
                    Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_LONG).show();

                }
            }
        });

            holder.file_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FileView.class);
                    String file=DOWNLOAD_URL+img_pos.getIm_filename();
                    intent.putExtra("PATH",file);
                    context.startActivity(intent);
                }
            });

        return convertView;
    }

    private class DownloadImage extends AsyncTask<String, Void, Void> {

        private static final String TAG = "DownloadImage";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {

            progress = ProgressDialog.show(context,
                    "Wait", "Downloading Image");

        }

        @Override
        protected Void doInBackground(String... params) {
            int count;
            try{
                URL url = new URL(params[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                if(new_wordd.contains("jpg")){
                     targetFileName=filetype;//Change name and subname
                }
                else{
                     targetFileName=filetype;//Change name and subname

                }
                int lenghtOfFile = conexion.getContentLength();
                IPATH = Environment.getExternalStorageDirectory()+ "/"+"PRDownload"+"/";
                File folder = new File(IPATH);
                if(!folder.exists()){
                    folder.mkdir();//If there is no folder it will be created.
                }
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(IPATH+targetFileName);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishProgress ((int)(total*100/lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                e.printStackTrace();
            }


            return  null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.i("Async-Example", "onPostExecute Called");
            //downloadedImg.setImageBitmap(result);
            progress.dismiss();
            Toast.makeText(context,"Downnloaded Successfully",Toast.LENGTH_LONG).show();

            //Open Image
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String Ipath=IPATH+image_name;
            File file = new File(Ipath);
            String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
            String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (extension.equalsIgnoreCase("") || mimetype == null) {
                // if there is no extension or there is no definite mimetype, still try to open the file
                intent.setDataAndType(Uri.fromFile(file), "image/*");
            } else {
                intent.setDataAndType(Uri.fromFile(file), mimetype);
            }

            // custom message for the intent
            Intent appIntent = Intent.createChooser(intent, "Choose an Application:");
            if (appIntent                                                                                                                                                                                          != null) {
                context.startActivity(appIntent);
            } else {
                if (context != null && context instanceof MainActivity) {
                    AlertDialog.Builder builder = new AlertDialog.Builder((MainActivity) context, com.iiit.amaresh.demotrack.R.style.AppCompatAlertDialogStyle);
                    builder.setTitle(context.getResources().getString(com.iiit.amaresh.demotrack.R.string.no_app_found_title));
                    builder.setMessage(context.getResources().getString(com.iiit.amaresh.demotrack.R.string.no_app_found_message));
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            }

        }

    }
}
