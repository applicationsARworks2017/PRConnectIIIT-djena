package com.iiit.amaresh.demotrack.Tabs;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.Fragment;


import com.iiit.amaresh.demotrack.Adapter.UnreadMessageAdaper;
import com.iiit.amaresh.demotrack.Pojo.Constants;
import com.iiit.amaresh.demotrack.Pojo.MessageUnread;
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
 */

public class UnreadMessages extends Fragment {

    private static final String TAG = "Unread Message";
    private ListView mListView = null;
    private List<MessageUnread> allmessagelist;
    private UnreadMessageAdaper qadapter;
    String par_id,par_status;
    String id,empid,message,created_date,send_by,msg_status;
    ListView lv;
    String phone_number,name;
    int user_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.unreadmessage, container, false);
        lv=(ListView)view.findViewById(R.id.listView_unreadmessage);
        phone_number =getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_PHONE, null);
        name = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_NAME, null);
        user_id = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.SP_USER_ID, 0);
        if (Util.getNetworkConnectivityStatus(getContext())) {
            UnReadMessageList();
        }
        else{
            Toast.makeText(getContext(),"No Internet",Toast.LENGTH_LONG).show();

        }

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return view;
    }

    private void UnReadMessageList() {
        SynchUnreadMessage asyncTask = new SynchUnreadMessage();
        String status="N";
        String sid=String.valueOf(user_id);
        asyncTask.execute(sid,status);

    }
    private class SynchUnreadMessage extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;
        int server_status;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getContext(), "Please Wait",
                    "Loading MessgeList...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String _empid = params[0];
                String _empstatus = params[1];
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
                        .appendQueryParameter("emp_id", _empid)
                        .appendQueryParameter("status", _empstatus);

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

                    allmessagelist = new ArrayList<MessageUnread>();

                    //db=new DBHelper(QAAnsweredListActivity.this);

                    for (int i = 0; i < user_list.length(); i++) {

                        JSONObject q_list_obj = user_list.getJSONObject(i);

                        id = q_list_obj.getString("id");
                        empid = q_list_obj.getString("emp_id");
                        message = q_list_obj.getString("message");
                        message = q_list_obj.getString("message");
                        created_date = q_list_obj.getString("created");
                        send_by = q_list_obj.getString("send_by");
                        msg_status = q_list_obj.getString("status");

                        MessageUnread urm_list = new MessageUnread(id, empid, message, send_by, created_date, msg_status);
                        allmessagelist.add(urm_list);

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
                qadapter = new UnreadMessageAdaper(getContext(), allmessagelist);
                lv.setAdapter(qadapter);
            }
            progress.dismiss();

        }
    }
}
