package com.iiit.amaresh.demotrack.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Adapter.DistrictAdapter;
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
 * Created by RN on 11/20/2017.
 */

public class DistrictUser extends AppCompatActivity {

    String id,emp_id,emp_name,emp_add,emp_mail,emp_phone,
            emp_address,emp_pass,emp_imei,empl_type,emp_state,emp_dist,emp_block,
            emp_desig,usertype=null,user_status;
    List<UserListing> userlist;

    TextView tv_all_state, tv_state, tv_district, tv_block, send_msgbody;
    LinearLayout message_body;
    Button ok;
    String messagebody;
    RelativeLayout a_d_user, spcfc_d_user, d_w_user;
    DistrictAdapter adapter;
    ListView listview;
    ArrayList<UserListing> district_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.district_list);

        listview = (ListView) findViewById(R.id.district_listView);
        getDistrictuser();

    }

    private void getDistrictuser() {
        if (Util.getNetworkConnectivityStatus(this)) {
            String emp_type="";
            GetUserDetail asyncTask = new GetUserDetail();
            asyncTask.execute(emp_type);
        } else {
            Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }

    private class GetUserDetail extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(DistrictUser.this, "Please Wait",
                    "Loading UserList...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String _emptype = params[0];
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
                        .appendQueryParameter("emptype", _emptype);

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

                    district_list= new ArrayList<UserListing>();

                    //db=new DBHelper(QAAnsweredListActivity.this);

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
                        district_list.add(u_list);

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
            adapter = new DistrictAdapter(DistrictUser.this, district_list);
            listview.setAdapter(adapter);
            // mListView.setSelectionFromTop(index, top);
            progress.dismiss();
        }
    }
}
