package com.iiit.amaresh.demotrack.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Adapter.AllUserAdapter;
import com.iiit.amaresh.demotrack.Database.DBHelper;
import com.iiit.amaresh.demotrack.Extra.BaseActivity;
import com.iiit.amaresh.demotrack.Pojo.Constants;
import com.iiit.amaresh.demotrack.Pojo.UserListing;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mobileapplication on 11/3/17.
 */

public class AlluserList extends BaseActivity {

    String id, emp_id, emp_name, emp_add, emp_mail, emp_phone,
            emp_address, emp_pass, emp_imei, empl_type, emp_state, emp_dist, emp_block,
            emp_desig, usertype = null, user_status;
    List<UserListing> userlist;
    List<UserListing> userlist_search;
    AllUserAdapter qadapter;
    ListView mListView = null;
    private SearchView search;
    TextView tvEmptyView;
    private int index = 0, top = 0;
    int g_position = 0, USER_id;
    String pagename, userType, districtid, distric_id, block_id;
    DBHelper db = new DBHelper(this);
    Button bt_ok, bt_cancel, SEND_ok;
    LinearLayout message_send, btn_layout;
    TextView rcpt_name;
    String name, em_id, indexx = null;
    EditText msgbody;
    String sender_name,state_id,bloc_id;
    String server_response;
    int server_status;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            toolbar.setTitle(getResources().getString(R.string.userlist));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(AlluserList.this);
                }
            });
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pagename = extras.getString("page");
            distric_id = extras.getString("DISTRICTID");
            block_id = extras.getString("BLOCKID");
            // and get whatever type user account id is
        }

        userType = this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_TYPE, null);
        districtid = this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_DISTRICT_ID, null);
        USER_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.SP_USER_ID, 0);


        Date todaysdate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(todaysdate);

        search = (SearchView) findViewById(R.id.searchView1);
        mListView = (ListView) findViewById(R.id.listView_user);
        SEND_ok = (Button) findViewById(R.id.SEND_ok);
        msgbody = (EditText) findViewById(R.id.msgbody);
        message_send = (LinearLayout) findViewById(R.id.message_send);
        rcpt_name = (TextView) findViewById(R.id.rcpt_name);
        sender_name = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_NAME, null);
        state_id = this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_STATE_ID, null);
        distric_id = this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_DISTRICT_ID, null);
        bloc_id = this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_BLOCK_ID, null);
        tvEmptyView = (TextView) findViewById(R.id.tvNoRecordFoundText);
        getContact();
        search.setQueryHint("SearchView");

        //*** setOnQueryTextFocusChangeListener ***
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        //*** setOnQueryTextListener ***
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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserListing users = (UserListing) parent.getItemAtPosition(position);
                //  Toast.makeText(AdminUserList.this,users.getUser_name(),Toast.LENGTH_LONG).show();
                name = users.getU_emp_name();
                em_id = users.getU_id();
               /* index = mListView.getFirstVisiblePosition();
                View v = mListView.getChildAt(0);
                top = (v == null) ? 0 : (v.getTop() - mListView.getPaddingTop());
                String value = users.getU_emp_phone();
                if(pagename.contains("movement")){
                    GetMovement.phone.setText(value);
                }*/
                if (pagename.contains("SPD")) {
                    indexx = users.getU_emp_id();
                    gotomessage();
                   /* listview.setVisibility(View.GONE);
                    searchView1.setVisibility(View.GONE);
                    btn_layout.setVisibility(View.GONE);
                    message_send.setVisibility(View.VISIBLE);
                    block_name=sb1.toString().trim().substring(0, sb1.length() - 1);
                    rcpt_name.setText("To"+" "+":"+" "+block_name);*/

                } else if (pagename.contains("SPB")) {
                    indexx = users.getU_emp_id();
                    gotomessage();
                   /* listview.setVisibility(View.GONE);
                    searchView1.setVisibility(View.GONE);
                    btn_layout.setVisibility(View.GONE);
                    message_send.setVisibility(View.VISIBLE);
                    block_name=sb1.toString().trim().substring(0, sb1.length() - 1);
                    rcpt_name.setText("To"+" "+":"+" "+block_name);*/

                }
            }
        });

        SEND_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=msgbody.getText().toString();
                String name=rcpt_name.getText().toString();
                if (msg.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Kindly Enter Message", Toast.LENGTH_LONG).show();
                } else {
                    sendMessage();
                }

            }
        });
    }

    private void sendMessage() {
        if(Util.getNetworkConnectivityStatus(AlluserList.this)) {
            send_message regtask = new send_message();
            String msg=msgbody.getText().toString();
            regtask.execute(sender_name,msg,indexx);
        }
        else{
            Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_LONG).show();

        }
    }

    private void gotomessage() {
        mListView.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
        message_send.setVisibility(View.VISIBLE);
        //block_name=sb1.toString().trim().substring(0, sb1.length() - 1);
        rcpt_name.setText("To" + " " + ":" + " " + name);
    }

    private void setQuestionList(String filterText) {

        userlist_search = new ArrayList<UserListing>();
        if (filterText != null && filterText.trim().length() > 0) {
            for (int i = 0; i < userlist .size(); i++) {
                String q_title = userlist.get(i).getU_emp_name();
                if (q_title != null && filterText != null &&
                        q_title.toLowerCase().contains(filterText.toLowerCase())) {
                    userlist_search.add(userlist.get(i));
                }
            }
        }else{
            userlist_search.addAll(userlist);
        }

        // create an Object for Adapter
        qadapter = new AllUserAdapter(AlluserList.this,userlist_search);
        mListView.setAdapter(qadapter);
        //  mAdapter.notifyDataSetChanged();


        if (userlist.isEmpty()) {
            mListView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }

        qadapter.notifyDataSetChanged();
    }


    private void getContact() {
        if (Util.getNetworkConnectivityStatus(this)) {
            GetUserDetail asyncTask = new GetUserDetail();
            String emp_type=null;
            String districid=null;
            String blockid=null;

            if(userType.contains("1")){
                emp_type="2";
                districid=distric_id;
            }
            else if(userType.contains("2")){
                emp_type="2";
                districid=distric_id;

            }
            else if(userType.contains("0")){
                emp_type="0";
                 blockid=String.valueOf(USER_id);

            }
            asyncTask.execute(emp_type,districid,blockid);

        }
    }

    private class GetUserDetail  extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;
        int Status;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(AlluserList.this, "Please Wait",
                    "Loading UserList...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String _emptype = params[0];
                String _distrc = params[1];
                String _block = params[2];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINE_URL + Constants.USER_LIST;
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

                Uri.Builder builder = new Uri.Builder();
                if(userType.contentEquals("1")){
                    builder = new Uri.Builder()
                            .appendQueryParameter("user_type", _emptype)
                            .appendQueryParameter("district_id", _distrc);

                }
                else if(pagename.contains("SPD")){
                    builder = new Uri.Builder()
                            .appendQueryParameter("user_type", _emptype)
                            .appendQueryParameter("district_id", _distrc);
                }
                else if(pagename.contains("SPB")){
                    builder = new Uri.Builder()
                            .appendQueryParameter("user_type", _emptype)
                            .appendQueryParameter("id", _block);
                }
                else {
                    builder = new Uri.Builder()
                            .appendQueryParameter("user_type", _emptype)
                            .appendQueryParameter("district_id", _distrc);
                }
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

                    JSONObject res = new JSONObject(response);
                    JSONArray user_list = res.getJSONArray("emp");

                    Status =res.getInt("status");
                    userlist = new ArrayList<UserListing>();

                    //db=new DBHelper(QAAnsweredListActivity.this);
                    if(Status==1){
                    for (int i = 0; i < user_list.length(); i++) {

                        JSONObject q_list_obj = user_list.getJSONObject(i);

                        id = q_list_obj.getString("id");
                        emp_id = q_list_obj.getString("id");
                        emp_name = q_list_obj.getString("empname");
                        emp_add = q_list_obj.getString("empadd");
                        emp_mail = q_list_obj.getString("empmail");
                        emp_phone = q_list_obj.getString("empphone");
                        emp_address = q_list_obj.getString("empaddress");
                        emp_pass = q_list_obj.getString("emppass");
                        emp_imei = q_list_obj.getString("imei");
                        empl_type = q_list_obj.getString("emptype");
                        emp_state = q_list_obj.getString("state");
                        emp_dist = q_list_obj.getString("district");
                        emp_block = q_list_obj.getString("block");
                        emp_desig = q_list_obj.getString("empdesg");
                        usertype = q_list_obj.getString("usertype");
                        user_status = q_list_obj.getString("user_status");
                        if (usertype == "1") {
                            usertype = "Admin";
                        } else {
                            usertype = "User";
                        }

                        UserListing u_list = new UserListing(id, emp_id, emp_name, emp_add, emp_mail, emp_phone, emp_address, emp_pass, emp_imei, empl_type, emp_state, emp_dist, emp_block, emp_desig, usertype, user_status);
                        userlist.add(u_list);
                    }
                        // db.addAnsweredQuestionList(new AnswerDetails(qid,uid,q_title,qdesc,q_admin_desc,q_isanswer,q_ispublish,q_fullname,q_postdate,q_created));

                      /*String q_isanswer = q_list_obj.getString("is_answer");
                      String q_ispublish = q_list_obj.getString("is_publish");
                      String q_postdate = q_list_obj.getString("post_date");
                      String q_created = q_list_obj.getString("full_name");
                      String q_modified = q_list_obj.getString("modified");*/
                    }
                    else{

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
            if(Status==1) {
                qadapter = new AllUserAdapter(AlluserList.this, userlist);
                mListView.setAdapter(qadapter);
                mListView.setSelectionFromTop(index, top);
            }
            else{
                Toast.makeText(getApplicationContext(),"Connectivity Issue,Check your Internet",Toast.LENGTH_LONG).show();
            }
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
                progressDialog = ProgressDialog.show(AlluserList.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String _sendby = params[0];
                String _msg_body = params[1];
                String _emp_id = params[2];


                InputStream in = null;
                int resCode = -1;

                String link = Constants.ONLINE_URL + Constants.MEG_SEND;
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
                .appendQueryParameter("emp_id",_emp_id)
                        .appendQueryParameter("message",_msg_body)
                        .appendQueryParameter("send_by",_sendby);
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
                Intent intent=new Intent(AlluserList.this,SelectedUser.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
            else if(server_status==0){
                Toast.makeText(getBaseContext(),"No User To Accept The Message",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(AlluserList.this,SelectedUser.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
            progressDialog.cancel();
        }
    }
}
