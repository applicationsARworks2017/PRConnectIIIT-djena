package com.iiit.amaresh.demotrack.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.maps.model.LatLng;
import com.iiit.amaresh.demotrack.Database.DBHelper;
import com.iiit.amaresh.demotrack.Extra.UtilImage;
import com.iiit.amaresh.demotrack.Pojo.Constants;
import com.iiit.amaresh.demotrack.Pojo.FilePath;
import com.iiit.amaresh.demotrack.Pojo.MultipartUtility;
import com.iiit.amaresh.demotrack.Pojo.Oflinedata;
import com.iiit.amaresh.demotrack.Pojo.Util;
import com.iiit.amaresh.demotrack.R;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class UploadAssets extends AppCompatActivity implements android.location.LocationListener {
    ImageView cam_bt,imshow,rec_bt;
    Button upload_bt;
    EditText title;
    String v_deop,im_file;
    String latitude,longitude,s_title,address,city,state;
    private Bitmap photo,bitmapRotate;
    private Boolean upflag = false;
    private Boolean vflag = false;
    private Uri selectedImage = null,imuri;
    File file;
    public static File mediaFile;
    Uri picUri=null;
    File images_to_post = null;
    String fname,imagepath="";
    SharedPreferences sharedPreferences;
    int user_id,server_status;
    String provider,server_message;
    LocationManager locationManager;
    ProgressDialog progress;
    String imPath;
    String sid,saddress;
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    long fileSizeInMB;
    long vfileSizeInMB;
    File video;
    VideoView vshow;
    ProgressBar progressBar;
    Uri fileUri;
    MediaController media_Controller;
    DisplayMetrics dm;
    FrameLayout vshow_frame;
    DBHelper db=new DBHelper(this);
    String selectedImagePath;
    int file_value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.iiit.amaresh.demotrack.R.layout.activity_upload_assets);
        user_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.SP_USER_ID, 0);
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
       /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling


            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(provider);

            // Initialize the location fields
            if (location != null) {
                System.out.println("Provider " + provider + " has been selected.");
                onLocationChanged(location);
            } else {
                Toast.makeText(UploadAssets.this, "GPS Not Available", Toast.LENGTH_LONG).show();
                //  latituteField.setText("Location not available");
                //  longitudeField.setText("Location not available");
            }

        } else {
            Toast.makeText(UploadAssets.this, "Allow to GPS", Toast.LENGTH_LONG).show();
        }


        cam_bt = (ImageView) findViewById(com.iiit.amaresh.demotrack.R.id.cam_bt);
        rec_bt = (ImageView) findViewById(com.iiit.amaresh.demotrack.R.id.rec_bt);
        imshow = (ImageView) findViewById(com.iiit.amaresh.demotrack.R.id.imgshow);
        vshow = (VideoView) findViewById(com.iiit.amaresh.demotrack.R.id.vshow);
        vshow_frame=(FrameLayout)findViewById(R.id.vshow_frame);
        imshow.setVisibility(View.GONE);
        vshow_frame.setVisibility(View.GONE);
        upload_bt = (Button) findViewById(com.iiit.amaresh.demotrack.R.id.upload);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        upload_bt.setVisibility(View.INVISIBLE);
        title=(EditText)findViewById(com.iiit.amaresh.demotrack.R.id.im_title);
        title.setVisibility(View.INVISIBLE);
        cam_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();            }
        });


        upload_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.getNetworkConnectivityStatus(getApplicationContext())) {
                    s_title = title.getText().toString();
                    if (!upflag && !vflag) {
                        Toast.makeText(UploadAssets.this, "File Not Captured..!", Toast.LENGTH_LONG).show();
                    } else if (s_title.length() <= 0 && s_title.equals("")) {
                        Toast.makeText(UploadAssets.this, "Please Enter Title For Image", Toast.LENGTH_LONG).show();
                    } else {

                        ImUpload();
                    }
                } else {
                    if(file==null){
                        s_title = title.getText().toString();
                        sid=String.valueOf(user_id);
                        saddress=address+","+city;
                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadAssets.this);
                        builder.setMessage("No Internet available. Do you want to save the file offline ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Uri video_uri=Uri.fromFile(mediaFile);
                                        String videouri=video_uri.toString();
                                        db.insertasset(new Oflinedata(user_id,latitude,longitude,s_title,saddress,"video_file",videouri));
                                        Toast.makeText(UploadAssets.this,"Saved",Toast.LENGTH_SHORT).show();

                                        Intent intent=new Intent(UploadAssets.this,Home.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(intent);                                }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                    else{
                        File Img = new File(file.getPath());
                        s_title = title.getText().toString();
                        sid=String.valueOf(user_id);
                        saddress=address+","+city;
                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadAssets.this);
                        builder.setMessage("No Internet available. Do you want to save the file offline ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String imguri=picUri.toString();
                                        db.insertasset(new Oflinedata(user_id,latitude,longitude,s_title,saddress,"image_file",imguri));
                                        Toast.makeText(UploadAssets.this,"Saved",Toast.LENGTH_SHORT).show();

                                        Intent intent=new Intent(UploadAssets.this,Home.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(intent);                                }
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
        });
        rec_bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // record video
                recordVideo();
            }
        });

    }
    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        // create a file to save the video
         fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set the image file name
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // set the video image quality to high
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        // start the Video Capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }
    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {

        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {

        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraVideo");


        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {

                //output.setText("Failed to create directory MyCameraVideo.");

                /*Toast.makeText(Context, "Failed to create directory MyCameraVideo.",
                        Toast.LENGTH_LONG).show();*/

                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }


        // Create a media file name

        // For unique file name appending current timeStamp with file name
        java.util.Date date = new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());

        if (type == MEDIA_TYPE_VIDEO) {

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");

        } else {
            return null;
        }

        return mediaFile;
    }


    private void captureImage() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (cameraIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                            "com.iiit.amaresh.demotrack",
                            photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        }
        else{
            imPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
            file = new File(imPath);
            picUri = Uri.fromFile(file); // convert path to Uri
            cameraIntent.putExtra( MediaStore.EXTRA_OUTPUT, picUri );
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }


    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = UploadAssets.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imPath = image.getAbsolutePath();
        file = new File(imPath);
        picUri = Uri.fromFile(image); // convert path to Uri
        return image;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            // imPath=picUri.getPath();
            // Bitmap photo = (Bitmap) data.getExtras().get("data");
            try {
                selectedImagePath= FilePath.getPath(this,picUri);
                Bitmap photo = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),picUri);
              //  Bitmap photo = ImageResizer.decodeSampledBitmapFromFile(file.getAbsolutePath(), 512, 342);
                if (Float.valueOf(getImageOrientation()) >= 0) {
                    bitmapRotate = rotateImage(photo, Float.valueOf(getImageOrientation()));
                } else {
                    bitmapRotate = photo;
                    photo.recycle();
                }

                upflag=true;
                vshow_frame.setVisibility(View.GONE);
                imshow.setVisibility(View.VISIBLE);
                cam_bt.setVisibility(View.GONE);
                rec_bt.setVisibility(View.GONE);
                title.setVisibility(View.VISIBLE);
                upload_bt.setVisibility(View.VISIBLE);
                imshow.setImageBitmap(bitmapRotate);
                file_value=1;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

              //  output.setText("Video File : " + data.getData());

                // Video captured and saved to fileUri specified in the Intent
              //  Toast.makeText(this, "Video saved to:" + data.getData(), Toast.LENGTH_LONG).show();

            } else if (resultCode == RESULT_CANCELED) {

               // output.setText("User cancelled the video capture.");

                // User cancelled the video capture
                Toast.makeText(this, "User cancelled the video capture.",
                        Toast.LENGTH_LONG).show();

            } else {

               // output.setText("Video capture failed.");

                // Video capture failed, advise user
                Toast.makeText(this, "Video capture failed.",
                        Toast.LENGTH_LONG).show();
            }
            vflag=true;
            cam_bt.setVisibility(View.GONE);
            rec_bt.setVisibility(View.GONE);
            vshow_frame.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            upload_bt.setVisibility(View.VISIBLE);
            media_Controller = new MediaController(this);
            dm = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int height = dm.heightPixels;
            int width = dm.widthPixels;
           // vshow.setMinimumWidth(width);
           // vshow.setMinimumHeight(height);
            media_Controller.setAnchorView(vshow);
            vshow.setMediaController(media_Controller);
            vshow.setVideoPath(String.valueOf(mediaFile));
            file_value=2;
            vshow.start();
        }
    }
    private void ImUpload() {
         sid=String.valueOf(user_id);
         saddress=address+","+city;
        if (Util.getNetworkConnectivityStatus(getApplicationContext())) {
            if(file==null){
                video = new File(mediaFile.getPath());
                long v_length = video.length();
                long vfileSizeInKB = v_length / 1024;
                vfileSizeInMB = vfileSizeInKB / 1024;

         }
         else{
                File Img = new File(file.getPath());
                long length = Img.length();
                long fileSizeInKB = length / 1024;
                fileSizeInMB = fileSizeInKB / 1024;
         }


            if (video == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Your File is " + fileSizeInMB + " MB. Do you want to upload")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                uploadImage upload = new uploadImage();
                                upload.execute(sid, latitude, longitude, s_title, saddress);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Your File is " + vfileSizeInMB + " MB. Do you want to upload")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                uploadImage upload = new uploadImage();
                                upload.execute(sid, latitude, longitude, s_title, saddress);
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
        else {
            Toast.makeText(getApplication(), "No internet Connection", Toast.LENGTH_LONG).show();


                    }
    }
    private class uploadImage extends AsyncTask<String, Void, Void> {

        String TAG = "FileUpload";
        private boolean is_success = false;
        //private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog.show(UploadAssets.this, "Please Wait",
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
                if(file==null){
                    multipart.addFilePart("file_name", video);
                }
                else{
                    multipart.addFilePart("file_name", file);
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
            Toast.makeText(UploadAssets.this,server_message,Toast.LENGTH_LONG).show();
            Intent intent=new Intent(UploadAssets.this,Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    //    In some mobiles image will get rotate so to correting that this code will help us
    private int getImageOrientation() {
        final String[] imageColumns = {MediaStore.Images.Media._ID, MediaStore.Images.ImageColumns.ORIENTATION};
        final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageColumns, null, null, imageOrderBy);

        if (cursor.moveToFirst()) {
            int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
            System.out.println("orientation===" + orientation);
            cursor.close();
            return orientation;
        } else {
            return 0;
        }
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
            Toast.makeText(UploadAssets.this,"Allow to GPS",Toast.LENGTH_LONG).show();
        }
    }
    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.removeUpdates(this);
    }



    @Override
    public void onLocationChanged(Location location) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latitude = String.valueOf(lat);
            longitude = String.valueOf(lng);
            LatLng latLng = new LatLng(lat, lng);
            // for getting the address
            List<Address> addresses;
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(lat, lng, 1);
                address=addresses.get(0).getAddressLine(0);
                city=addresses.get(0).getLocality();
                state=addresses.get(0).getAdminArea();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

}
