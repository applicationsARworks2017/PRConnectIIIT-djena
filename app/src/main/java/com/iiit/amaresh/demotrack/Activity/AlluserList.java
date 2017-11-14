package com.iiit.amaresh.demotrack.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.iiit.amaresh.demotrack.Adapter.AllUserAdapter;
import com.iiit.amaresh.demotrack.Database.DBHelper;
import com.iiit.amaresh.demotrack.Extra.BaseActivity;
import com.iiit.amaresh.demotrack.Pojo.Constants;
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

/**
 * Created by mobileapplication on 11/3/17.
 */

public class AlluserList extends BaseActivity {

    String id,emp_id,emp_name,emp_add,emp_mail,emp_phone,
            emp_address,emp_pass,emp_imei,empl_type,emp_state,emp_dist,emp_block,
            emp_desig,usertype=null,user_status;
    List<UserListing> userlist;
    List<UserListing> userlist_search;
    AllUserAdapter qadapter;
    ListView mListView = null;
    private SearchView search;
    TextView tvEmptyView;
    private int index = 0, top = 0;
    int g_position=0;
    String pagename,servermessage;
    DBHelper db=new DBHelper(this);
    int user_id,user_type,block_id,district_id,state_id,status;

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
        user_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.SP_USER_ID, 0);
        state_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.SP_STATE_ID, 0);
        block_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.SP_BLOCK_ID, 0);
        district_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.SP_DISTRICT_ID, 0);
        user_type = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.SP_USER_TYPE, 0);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pagename = extras.getString("page");
            // and get whatever type user account id is
        }

        search = (SearchView)findViewById(R.id.searchView1);
        mListView = (ListView) findViewById(R.id.listView_user);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserListing users= (UserListing) parent.getItemAtPosition(position);
                //  Toast.makeText(AdminUserList.this,users.getUser_name(),Toast.LENGTH_LONG).show();
               index = mListView.getFirstVisiblePosition();
                View v = mListView.getChildAt(0);
                top = (v == null) ? 0 : (v.getTop() - mListView.getPaddingTop());
                String value = users.getU_emp_phone();
                if(pagename.contains("movement")){
                    GetMovement.phone.setText(value);
                }
                else{
                    SubOrdinateHistory.phoneview.setText(value);
                }
                finish();
            }
        });
        tvEmptyView = (TextView)findViewById(R.id.tvNoRecordFoundText);
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
            if(user_type==0){
                 emp_type="block";
            }
            else if(user_type==1){
                emp_type="state";
            }
            else{
                emp_type="district";
            }

            asyncTask.execute(emp_type,String.valueOf(user_id),String.valueOf(block_id),String.valueOf(state_id),String.valueOf(district_id));

        }
    }

    private class GetUserDetail  extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(AlluserList.this, "Please Wait",
                    "Loading UserList...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String _emptype = params[0];
                String _userid = params[1];
                String _blockid = params[2];
                String _stateid = params[3];
                String _district = params[4];
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

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("usertype", _emptype)
                        .appendQueryParameter("block_id", _blockid)
                        .appendQueryParameter("state_id", _stateid)
                        .appendQueryParameter("district_id", _district);

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
                    status=res.getInt("Status");
                    if(status==1){
                        servermessage="Got userlist";
                    }
                    else{
                        servermessage="userlist not found";

                    }
                    JSONArray user_list = res.getJSONArray("emp");

                    userlist = new ArrayList<UserListing>();

                    //db=new DBHelper(QAAnsweredListActivity.this);

                    for (int i = 0; i < user_list.length(); i++) {

                        JSONObject q_list_obj = user_list.getJSONObject(i);

                        id = q_list_obj.getString("id");
                        emp_id = q_list_obj.getString("empid");
                        emp_name = q_list_obj.getString("empname");
                        emp_add = q_list_obj.getString("empadd");
                        emp_mail  = q_list_obj.getString("empmail");
                        emp_phone = q_list_obj.getString("empphone");
                        emp_address = q_list_obj.getString("empaddress");
                        emp_pass = q_list_obj.getString("emppass");
                        emp_imei = q_list_obj.getString("imei");
                        empl_type= q_list_obj.getString("emptype");
                        emp_state= q_list_obj.getString("state");
                        emp_dist= q_list_obj.getString("district");
                        emp_block= q_list_obj.getString("block");
                        emp_desig= q_list_obj.getString("empdesg");
                        usertype= q_list_obj.getString("usertype");
                        user_status= q_list_obj.getString("user_status");
                        if(usertype =="1"){
                            usertype ="Admin";
                        }
                        else {
                            usertype="User";
                        }

                        UserListing u_list=new UserListing(id,emp_id,emp_name,emp_add,emp_mail,emp_phone,emp_address,emp_pass,emp_imei,empl_type,emp_state,emp_dist,emp_block,emp_desig,usertype,user_status);
                        userlist.add(u_list);

                        // db.addAnsweredQuestionList(new AnswerDetails(qid,uid,q_title,qdesc,q_admin_desc,q_isanswer,q_ispublish,q_fullname,q_postdate,q_created));

                      /*String q_isanswer = q_list_obj.getString("is_answer");
                      String q_ispublish = q_list_obj.getString("is_publish");
                      String q_postdate = q_list_obj.getString("post_date");
                      String q_created = q_list_obj.getString("full_name");
                      String q_modified = q_list_obj.getString("modified");*/
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
            if(status==1){
                qadapter = new AllUserAdapter(AlluserList.this,userlist);
                mListView.setAdapter(qadapter);
                mListView.setSelectionFromTop(index, top);
            }
            else{
                mListView.setVisibility(View.GONE);
                TextView tvNoRecordFoundText=(TextView)findViewById(R.id.tvNoRecordFoundText);
                tvNoRecordFoundText.setVisibility(View.VISIBLE);

            }

            progress.dismiss();
        }
    }
}
