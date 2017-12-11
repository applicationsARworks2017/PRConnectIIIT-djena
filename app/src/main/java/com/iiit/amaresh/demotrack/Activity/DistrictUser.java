package com.iiit.amaresh.demotrack.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Adapter.BlockAdapter;
import com.iiit.amaresh.demotrack.Adapter.DistrictAdapter;
import com.iiit.amaresh.demotrack.Extra.BaseActivity;
import com.iiit.amaresh.demotrack.Pojo.BlockList;
import com.iiit.amaresh.demotrack.Util.Constants;
import com.iiit.amaresh.demotrack.Pojo.DistrictUserList;
import com.iiit.amaresh.demotrack.Pojo.Util;
import com.iiit.amaresh.demotrack.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by RN on 11/20/2017.
 */

public class DistrictUser extends BaseActivity {

    String server_response;
    int server_status;
    SearchView searchView;
    TextView tv_all_state, tv_state, tv_district, tv_block, send_msgbody, tvNoRecordFound;
    LinearLayout message_body;
    Button ok;
    String messagebody;
    RelativeLayout a_d_user, spcfc_d_user, d_w_user;
    DistrictAdapter adapter;
    BlockAdapter b_adapter;
    ListView listview;
    LinearLayout message_send;
    ArrayList<DistrictUserList> district_list;
    ArrayList<BlockList> blocklist;
    String state_id;
    String data;
    Button bt_ok, bt_cancel,SEND_ok;
    SearchView searchView1;
    public static EditText flatName;
    private static int counter = 0;
    String district_id,district_name,distric_id,sender_name,bloc_id;
    TextView rcpt_name;
    ProgressDialog progressDialog;
    LinearLayout btn_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.district_user_list);

        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setTitle(getResources().getString(R.string.districtuser));
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(DistrictUser.this);
                }
            });
        }
        data=SelectedUser.selection;
        /*Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            data = extras.getString("TAB");
        }*/


        sender_name = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_NAME, null);
        state_id = this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_STATE_ID, null);
        distric_id = this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_DISTRICT_ID, null);
        bloc_id = this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_BLOCK_ID, null);


        listview = (ListView) findViewById(R.id.district_listView);
        tvNoRecordFound = (TextView) findViewById(R.id.blank_text);
        rcpt_name = (TextView) findViewById(R.id.rcpt_name);
        send_msgbody = (EditText) findViewById(R.id.msgbody);
        btn_layout=(LinearLayout)findViewById(R.id.btn_layout);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        SEND_ok = (Button) findViewById(R.id.SEND_ok);
        message_send=(LinearLayout)findViewById(R.id.message_send);
        searchView1 = (SearchView) findViewById(R.id.searchView1);
        searchView1.setQueryHint("Search");
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(data!=null && data!="" && !data.isEmpty()) {
            if (data.contains("dwbu")) {
                getDistrictuser();
            }
            else if(data.contains("SBU")){
                getDistrictuser();

            }
            else
             {
                getDistrictuser();

            }
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox chk = (CheckBox) view.findViewById(R.id.checkbox);

                DistrictUserList bean = district_list.get(position);

                if (bean.isSelected()) {
                    bean.setSelected(false);
                    chk.setChecked(false);
                } else {
                    bean.setSelected(true);
                    chk.setChecked(true);
                }


            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for id
                StringBuffer sb = new StringBuffer();
                StringBuffer sb1 = new StringBuffer();

                for (DistrictUserList bean : district_list) {
                        /*if (counter<5) {
                            counter++;
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Only five please", Toast.LENGTH_SHORT).show();
                        }*/
                    if (bean.isSelected()) {
                        if (sb.toString().trim().contains(bean.getD_id())) {

                        } else {
                            sb.append(bean.getD_id());
                            sb.append(",");
                            sb1.append(bean.getTitle());
                            sb1.append(",");

                        }
                    }
                    if (sb.length() <= 0) {
                        district_id = " ";
                        // flatName.setText("");
                        //DistrictUser.this.finish();
                    } else  {
                        if (data.contains("dwbu")) {
                            district_name=sb1.toString().trim().substring(0, sb1.length() - 1);
                            rcpt_name.setText("To"+" "+":"+" "+district_name);
                            district_id=sb.toString().trim().substring(0, sb.length() - 1);
                            Intent i=new Intent(DistrictUser.this,BlockUser.class);
                            i.putExtra("DISTRICTID",district_id);
                            i.putExtra("page",data);
                            startActivity(i);
                        }else if (data.contains("SBU")) {
                            district_name=sb1.toString().trim().substring(0, sb1.length() - 1);
                           // rcpt_name.setText("To"+" "+":"+" "+district_name);
                            district_id=sb.toString().trim().substring(0, sb.length() - 1);
                            Intent i=new Intent(DistrictUser.this,BlockUser.class);
                            i.putExtra("DISTRICTID",district_id);
                            i.putExtra("page",data);
                            startActivity(i);
                        }
                        else{
                            district_id=sb.toString().trim().substring(0, sb.length() - 1);
                            Intent i=new Intent(DistrictUser.this,AlluserList.class);
                            i.putExtra("DISTRICTID",district_id);
                            i.putExtra("page","SPD");
                            startActivity(i);
                           /* listview.setVisibility(View.GONE);
                            searchView1.setVisibility(View.GONE);
                            btn_layout.setVisibility(View.GONE);
                            message_send.setVisibility(View.VISIBLE);*/
                            //DistrictUser.this.finish();


                        }

                    }
                }
                //for name

            }
        });
        SEND_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=send_msgbody.getText().toString();
                String name=rcpt_name.getText().toString();
                if (msg.length() == 0) {
                    Toast.makeText(DistrictUser.this, "Kindly Enter Message", Toast.LENGTH_LONG).show();
                } else {
                    sendMessage();
                }

            }
        });
        searchView1.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        //*** setOnQueryTextListener ***
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

    private void sendMessage() {
        if(Util.getNetworkConnectivityStatus(DistrictUser.this)) {
            send_message regtask = new send_message();
            String msg=send_msgbody.getText().toString();

            regtask.execute(sender_name,msg,district_id,bloc_id,state_id);
        }
        else{
            Toast.makeText(DistrictUser.this, "Check Your Internet Connection", Toast.LENGTH_LONG).show();

        }
    }


    private void setQuestionList(String filterText) {

        final ArrayList<DistrictUserList> visitors_search = new ArrayList<>();
        if (filterText != null && filterText.trim().length() > 0) {
            for (int i = 0; i < district_list .size(); i++) {
                String q_title = district_list.get(i).getTitle();
                if (q_title != null && filterText != null &&
                        q_title.toLowerCase().contains(filterText.toLowerCase())) {
                    visitors_search.add(district_list.get(i));
                }
            }
        }else{
            visitors_search.addAll(district_list);
        }
        // create an Object for Adapter
        adapter = new DistrictAdapter(this, visitors_search);
        listview.setAdapter(adapter);
        //  mAdapter.notifyDataSetChanged();


        if (visitors_search.isEmpty()) {
            listview.setVisibility(View.GONE);
            tvNoRecordFound.setVisibility(View.VISIBLE);
        } else {
            listview.setVisibility(View.VISIBLE);
            tvNoRecordFound.setVisibility(View.GONE);
        }

        adapter.notifyDataSetChanged();
    }

    private void getDistrictuser() {
        if (Util.getNetworkConnectivityStatus(this)) {
            GetAllDistrict asyncTask = new GetAllDistrict();
            asyncTask.execute(state_id);
        } else {
            Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }

    private class GetAllDistrict extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;
        String d_id,stat_id,title;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(DistrictUser.this, "Please Wait",
                    "Loading UserList...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String stateid = params[0];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINE_URL + Constants.SEND_DISTRICT_W_B_USER;
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
                        .appendQueryParameter("state_id", stateid);

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

                /**
                 *
                 {
                 "districts": [
                 {
                 "id": "1",
                 "state_id": "1",
                 "title": "KHORDHA"
                 },
                 {
                 "id": "2",
                 "state_id": "1",
                 "title": "ANUGUL"
                 },
                 * */

                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    JSONArray user_list = res.getJSONArray("districts");

                    district_list= new ArrayList<DistrictUserList>();
                    for (int i = 0; i < user_list.length(); i++) {

                        JSONObject q_list_obj = user_list.getJSONObject(i);

                        d_id = q_list_obj.getString("id");
                        stat_id = q_list_obj.getString("state_id");
                        title = q_list_obj.getString("title");

                        DistrictUserList d_list = new DistrictUserList(d_id, stat_id, title);
                        district_list.add(d_list);
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
            adapter = new DistrictAdapter(DistrictUser.this, district_list);
            listview.setAdapter(adapter);
            // mListView.setSelectionFromTop(index, top);
            progress.dismiss();
        }
    }

    private class send_message extends AsyncTask<String, Void, Void> {
        private static final String TAG = "register_user";
        //private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(DistrictUser.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String _sendby = params[0];
                String _msg = params[1];
                String _distric = params[2];
                String _block = params[3];
                String _state = params[4];

                InputStream in = null;
                int resCode = -1;

                String link = Constants.ONLINE_URL + Constants.SEND_ALL;
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
                        .appendQueryParameter("send_by",_sendby)
                        .appendQueryParameter("message",_msg)
                        .appendQueryParameter("district_id",_distric)
                         .appendQueryParameter("usertype","2");
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

                /**
                 * {
                 "status": 1,
                 "message": "Details inserted successfully"
                 }
                 * */

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status == 1){
                        server_response="Message Sent";
                    }
                    else{
                        server_response="Error";

                    }
                    // server_response = res.optString("message");


                    // int status = res.optInt("login_status");
                    //  message = res.optString("message");
                }

                return null;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            if(server_status==1){
                Intent intent=new Intent(DistrictUser.this,SelectedUser.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
            else if(server_status==0){
                Toast.makeText(getBaseContext(),"No User To Accept The Message",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(DistrictUser.this,SelectedUser.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
            progressDialog.cancel();
        }
    }
}
