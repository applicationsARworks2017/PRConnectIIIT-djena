package com.iiit.amaresh.demotrack.Tabs;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Activity.GalaryActivity;
import com.iiit.amaresh.demotrack.Adapter.AllImageAdaper;
import com.iiit.amaresh.demotrack.Pojo.Constants;
import com.iiit.amaresh.demotrack.Pojo.ImageAll;
import com.iiit.amaresh.demotrack.Pojo.Util;
import com.iiit.amaresh.demotrack.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RN on 11/5/2017.
 */

public class OnlineAssetGalleryFragment extends Fragment {
    ListView listView;
    int user_id;
    List<ImageAll> allimagelist;
    AllImageAdaper imadapter;
    int server_status;
    String id,im_title,im_filename,im_emp_id,im_latitude,im_longitude,im_created,server_message,im_usename,im_userdesg,im_useradd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmenttab2, container, false);
        user_id =getActivity(). getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.SP_USER_ID, 0);
        listView = (ListView)rootView.findViewById(R.id.listView_allimages);

        if (Util.getNetworkConnectivityStatus(getActivity())) {
            ImageList();
        }
        else{
            Toast.makeText(getActivity(),"No Internet",Toast.LENGTH_LONG).show();

        }
        return rootView;
    }

    private void ImageList() {
        String S_user_id="";
        SynchImages asyncTask = new SynchImages();
        asyncTask.execute(S_user_id);
    }
    private class SynchImages extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), "Please Wait",
                    "Loading Images...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String _empid = params[0];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINE_URL + Constants.IMAGE_LIST;
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
                        .appendQueryParameter("emp_id", _empid);

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

                /*
                *
                * {

                      "empfile": [
                        {
                          "id": "94",
                          "title": "iiit",
                          "file_name": "file_1487584712.jpg",
                          "emp_id": "68",
                          "address": null,
                          "latitude": "20.29420309",
                          "logitude": "85.74323804",
                          "created": "20-02-2017 03:28 PM",
                          "modified": "20-02-2017 03:28 PM",
                          "empdesg": "BDO",
                          "empname": "Smt. Diptimayee Barik"
                        },
                    },*/


                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status==1) {
                        JSONArray user_list = res.getJSONArray("empfile");
                        allimagelist = new ArrayList<ImageAll>();
                        for (int i = 0; i < user_list.length(); i++) {
                            JSONObject q_list_obj = user_list.getJSONObject(i);
                            id = q_list_obj.getString("id");
                            im_emp_id = q_list_obj.getString("emp_id");
                            im_filename = q_list_obj.getString("file_name");
                            im_title = q_list_obj.getString("title");
                            im_latitude = q_list_obj.getString("latitude");
                            im_longitude = q_list_obj.getString("logitude");
                            im_created = q_list_obj.getString("created");
                            im_usename = q_list_obj.getString("empname");
                            im_userdesg = q_list_obj.getString("empdesg");
                            im_useradd = q_list_obj.getString("address");
//                        server_status = q_list_obj.getInt("status");

                            ImageAll am_list = new ImageAll(id, im_emp_id, im_filename, im_title, im_latitude, im_longitude, im_created, im_usename, im_userdesg, im_useradd);
                            allimagelist.add(am_list);
                        }
                    }

                }

                return null;


            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            if(server_status==1) {
                imadapter = new AllImageAdaper(getActivity(), allimagelist);
                listView.setAdapter(imadapter);
            }
            progress.dismiss();

        }
    }
}
