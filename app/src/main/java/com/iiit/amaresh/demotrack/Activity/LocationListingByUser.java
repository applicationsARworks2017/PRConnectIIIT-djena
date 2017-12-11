package com.iiit.amaresh.demotrack.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Adapter.LocationListingAdapter;
import com.iiit.amaresh.demotrack.Util.Constants;
import com.iiit.amaresh.demotrack.Pojo.LocationList;
import com.iiit.amaresh.demotrack.Pojo.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocationListingByUser extends AppCompatActivity {
    String emp_phone,selected_date;
    ProgressDialog progressDialog;
    private ListView locListView = null;
    LocationListingAdapter locadapter;
    SearchView searchView1;
    int server_status;
    String server_message;
    List<LocationList> locationlist;
    TextView tvNoRecordFoundText;
    String id,emp_id,phone,latitude,longitude,created;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.iiit.amaresh.demotrack.R.layout.activity_location_listing_by_user);
        locListView = (ListView) findViewById(com.iiit.amaresh.demotrack.R.id.loc_listView);
        searchView1=(SearchView)findViewById(com.iiit.amaresh.demotrack.R.id.searchView1);
        searchView1.setQueryHint("Enter time to search");
        tvNoRecordFoundText=(TextView)findViewById(com.iiit.amaresh.demotrack.R.id.tvNoRecordFoundText);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            emp_phone = extras.getString("phone_number");
            selected_date = extras.getString("date");
        }
        selftrack();
        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setQuestionList(newText);
                return false;
            }
        });
    }
    private void setQuestionList(String filterText) {

        final ArrayList<LocationList> flatlist_search = new ArrayList<>();
        if (filterText != null && filterText.trim().length() > 0) {
            for (int i = 0; i < locationlist .size(); i++) {
                String q_title = locationlist.get(i).getLoc_created();
                if (q_title != null && filterText != null &&
                        q_title.toLowerCase().contains(filterText.toLowerCase())) {
                    flatlist_search.add(locationlist.get(i));
                }
            }
        }else{
            flatlist_search.addAll(locationlist);
        }
        // create an Object for Adapter
        locadapter = new LocationListingAdapter(LocationListingByUser.this, flatlist_search);
        locListView.setAdapter(locadapter);
        //  mAdapter.notifyDataSetChanged();


        if (flatlist_search.isEmpty()) {
            locListView.setVisibility(View.GONE);
            tvNoRecordFoundText.setVisibility(View.VISIBLE);
        } else {
            locListView.setVisibility(View.VISIBLE);
            tvNoRecordFoundText.setVisibility(View.GONE);
        }

        locadapter.notifyDataSetChanged();
    }



    private void selftrack() {
        if (Util.getNetworkConnectivityStatus(LocationListingByUser.this)) {
            selfTracking asyncTask = new selfTracking();
            asyncTask.execute(emp_phone,selected_date);
        }else {
            Toast.makeText(LocationListingByUser.this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Async task to get sync camp table from server
     * */
    private class selfTracking extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        //private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(LocationListingByUser.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _userphone = params[0];
                String _date = params[1];

                InputStream in = null;
                int resCode = -1;

                String link = Constants.ONLINE_URL + Constants.LOC_LIST;
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
                        .appendQueryParameter("empphone", _userphone)
                        .appendQueryParameter("created", _date);
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

                /*
                *"emplocation": [
                {
                  "id": "3",
                  "emp_id": "394",
                  "empphone": "8280405001",
                  "latitude": "20.297458333333335",
                  "longitude": "",
                  "title": "Sri Deoranjan Kumar Singh",
                  "created": "2017-01-17 06:41:42",
                  "modified": "2017-01-17 06:41:42"
                },
                            *
                *
                * */
                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status==1) {
                        JSONArray user_list = res.getJSONArray("emplocation");

                        locationlist = new ArrayList<LocationList>();

                        //db=new DBHelper(QAAnsweredListActivity.this);

                        for (int i = 0; i < user_list.length(); i++) {

                            JSONObject q_list_obj = user_list.getJSONObject(i);

                            id = q_list_obj.getString("id");
                            emp_id = q_list_obj.getString("emp_id");
                            phone = q_list_obj.getString("empphone");
                            latitude = q_list_obj.getString("latitude");
                            longitude = q_list_obj.getString("longitude");
                            created = q_list_obj.getString("created");


                            LocationList l_list = new LocationList(id, emp_id, phone, latitude, longitude, created);
                            locationlist.add(l_list);
                        }
                    }
                    // int status = res.optInt("login_status");
                    //  message = res.optString("message");
                }
                return null;


            } catch (SocketTimeoutException exception) {
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch (ConnectException exception) {
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch (MalformedURLException exception) {
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch (IOException exception) {
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch (Exception exception) {
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            if(server_status==1) {
                Collections.reverse(locationlist);
                locadapter = new LocationListingAdapter(LocationListingByUser.this, locationlist);
                locListView.setAdapter(locadapter);
            }
            else{
                locListView.setVisibility(View.GONE);
                tvNoRecordFoundText.setVisibility(View.VISIBLE);
            }
            progressDialog.dismiss();

        }
    }
}
