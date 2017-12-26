package com.iiit.amaresh.demotrack.Tabs;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Adapter.AllMessageAdaper;
import com.iiit.amaresh.demotrack.Database.DBHelper;
import com.iiit.amaresh.demotrack.Util.Constants;
import com.iiit.amaresh.demotrack.Pojo.MessageAll;
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
import java.util.Collections;
import java.util.List;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class AllMessage extends Fragment {
    private static final String TAG = "All Message";
    private ListView mListView = null;
    List<MessageAll> allmessagelist;
    List<MessageAll> allmessagelist_search;
    AllMessageAdaper qadapter;
    String par_id,par_status;
    String id,empid,message,created_date,send_by,msg_status;
    ListView lv;
    String phone_number,name;
    int user_id,server_status;
    private SearchView search;
    TextView tvEmptyView;



    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.allmessage, container, false);

        lv = (ListView) view.findViewById(R.id.listView_allmessage);
        search = (SearchView) view.findViewById(R.id.searchView_allmessgae);
        tvEmptyView = (TextView) view.findViewById(R.id.tvNoRecordFoundText_allmessage);
        phone_number = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_PHONE, null);
        name = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_NAME, null);
        user_id = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.SP_USER_ID, 0);
        if (Util.getNetworkConnectivityStatus(getContext())) {
            MessageList();
        } else {
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_LONG).show();

        }
        return view;

    }
    /*    search.setQueryHint("SearchView");

        /*//*** setOnQueryTextFocusChangeListener ***
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        /*//*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

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


        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return view;
    }
    private void setQuestionList(String filterText) {

        allmessagelist_search = new ArrayList<MessageAll>();
        if (filterText != null && filterText.trim().length() > 0) {
            for (int i = 0; i < allmessagelist .size(); i++) {
                String q_title = allmessagelist.get(i).getSend_by();
                if (q_title != null && filterText != null &&
                        q_title.toLowerCase().contains(filterText.toLowerCase())) {
                    allmessagelist_search.add(allmessagelist.get(i));
                }
            }
        }else{
            allmessagelist_search.addAll(allmessagelist);
        }

        // create an Object for Adapter
        qadapter = new AllMessageAdaper(getContext(),allmessagelist_search);
        mListView.setAdapter(qadapter);
        //  mAdapter.notifyDataSetChanged();


        if (allmessagelist.isEmpty()) {
            mListView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }

        qadapter.notifyDataSetChanged();
    }
*/
    private void MessageList() {
        SynchMessage asyncTask = new SynchMessage();
        String status=" ";
        String sid=String.valueOf(user_id);
        asyncTask.execute(sid);
    }
    private class SynchMessage extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getContext(), "Please Wait",
                    "Loading MessgeList...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String _empid = params[0];
              //  String _empstatus = params[1];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINE_URL + Constants.MSG_LIST;
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
                      //  .appendQueryParameter("status", _empstatus);

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


                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.getInt("status");
                    if (server_status==1){
                    JSONArray user_list = res.getJSONArray("empmessage");

                    allmessagelist = new ArrayList<MessageAll>();

                   // db=new DBHelper(QAAnsweredListActivity.this);

                    for (int i = 0; i < user_list.length(); i++) {

                        JSONObject q_list_obj = user_list.getJSONObject(i);

                        id = q_list_obj.getString("id");
                        empid = q_list_obj.getString("emp_id");
                        message = q_list_obj.getString("message");
                        message = q_list_obj.getString("message");
                        created_date = q_list_obj.getString("created");
                        send_by = q_list_obj.getString("send_by");
                        msg_status = q_list_obj.getString("status");

                        MessageAll am_list = new MessageAll(id, empid, message, send_by, created_date, msg_status);
                        allmessagelist.add(am_list);
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
                Collections.reverse(allmessagelist);
                qadapter = new AllMessageAdaper(getActivity(), allmessagelist);
                lv.setAdapter(qadapter);
            }
            progress.dismiss();

        }
    }
}

