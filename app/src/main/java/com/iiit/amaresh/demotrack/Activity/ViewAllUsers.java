package com.iiit.amaresh.demotrack.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Adapter.UserListingAdapter;
import com.iiit.amaresh.demotrack.Extra.BaseActivity;
import com.iiit.amaresh.demotrack.Util.Constants;
import com.iiit.amaresh.demotrack.Pojo.UserListing;
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

public class ViewAllUsers extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "AllUsers";
    private ListView mListView = null;
    List<UserListing> userlist;
    List<UserListing> userlist_search;
    UserListingAdapter qadapter;
    private SearchView search;
    ListView lv;
    String data="view",userType,districtid,userid,pagename;
    TextView tvEmptyView;
    private int index = 0, top = 0;

    // private QaListAdapter mAdapter = null;
    private Menu menu;
    String id,emp_id,emp_name,emp_add,emp_mail,emp_phone,
            emp_address,emp_pass,emp_imei,empl_type,emp_state,emp_dist,emp_block,
            emp_desig,usertype=null,user_status;
    int USER_id;
    String name,em_id,indexx = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_users);
        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

            toolbar.setTitle(getResources().getString(R.string.all_user));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(ViewAllUsers.this);
                }
            });

        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pagename = extras.getString("page");
            // and get whatever type user account id is
        }
        userType = this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_TYPE, null);
        districtid = this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_DISTRICT_ID, null);
        USER_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.SP_USER_ID, 0);

        search = (SearchView)findViewById(R.id.searchView1);
        mListView = (ListView) findViewById(R.id.listView);
        tvEmptyView = (TextView)findViewById(R.id.tvNoRecordFoundText);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserListing users= (UserListing) parent.getItemAtPosition(position);
                //  Toast.makeText(AdminUserList.this,users.getUser_name(),Toast.LENGTH_LONG).show();
                name = users.getU_emp_name();
                em_id = users.getU_emp_id();
                index = mListView.getFirstVisiblePosition();
                View v = mListView.getChildAt(0);
                top = (v == null) ? 0 : (v.getTop() - mListView.getPaddingTop());
                String value = users.getU_emp_phone();
                if(pagename.contains("movement")){
                    GetMovement.phone.setText(value);
                    finish();
                }
                else if(pagename.contentEquals("history")){
                    SubOrdinateHistory.phoneview.setText(value);
                    finish();

                }
            }
        });

        if (Util.getNetworkConnectivityStatus(ViewAllUsers.this)) {
            Userlist();
        }
        else{
            Toast.makeText(this,"offline mode",Toast.LENGTH_LONG).show();

        }

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
        qadapter = new UserListingAdapter(ViewAllUsers.this,userlist_search,pagename);
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


    private void Userlist() {
        if (Util.getNetworkConnectivityStatus(ViewAllUsers.this)) {
            SyncDetails asyncTask = new SyncDetails();
            String emp_type=null;
            String districid=null;

            if(userType.contains("1")){
                emp_type="1";
            }
            else if(userType.contains("2")){
                emp_type="2";
                districid=districtid;

            } else if(userType.contains("0")){
                emp_type="0";
                userid=String.valueOf(USER_id);

            }
            asyncTask.execute(emp_type,districid,userid);

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    ///SyncDetail starts


    private class SyncDetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;
        int Status;
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ViewAllUsers.this, "Please Wait",
                    "Loading UserList...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String _emptype = params[0];
                String _distric = params[1];
                String _userid = params[2];
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

                Uri.Builder builder;
                if(userType.contentEquals("1")){
                    builder = new Uri.Builder()
                            .appendQueryParameter("usertype", _emptype);
                }
                else if(userType.contains("0")){
                    builder = new Uri.Builder()
                    .appendQueryParameter("usertype", _emptype)
                    .appendQueryParameter("id", _userid);

                }
                else {
                    builder = new Uri.Builder()
                            .appendQueryParameter("usertype", _emptype)
                            .appendQueryParameter("district_id", _distric);
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
                        emp_id = q_list_obj.getString("empid");
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
                qadapter = new UserListingAdapter(ViewAllUsers.this, userlist,pagename);
                mListView.setAdapter(qadapter);
            }
            else{
                Toast.makeText(getApplicationContext(),"Data not found",Toast.LENGTH_LONG).show();
            }
            progress.dismiss();

        }
    }

}
