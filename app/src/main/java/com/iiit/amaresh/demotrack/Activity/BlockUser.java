package com.iiit.amaresh.demotrack.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Adapter.BlockAdapter;
import com.iiit.amaresh.demotrack.Pojo.BlockList;
import com.iiit.amaresh.demotrack.Pojo.Constants;
import com.iiit.amaresh.demotrack.Pojo.DistrictUserList;
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
 * Created by mobileapplication on 11/21/17.
 */

public class BlockUser extends AppCompatActivity {

    String id, emp_id, emp_name, emp_add, emp_mail, emp_phone,
            emp_address, emp_pass, emp_imei, empl_type, emp_state, emp_dist, emp_block,
            emp_desig, usertype = null, user_status;
    List<UserListing> userlist;
    SearchView searchView;
    TextView tv_all_state, tv_state, tv_district, tv_block, send_msgbody, tvNoRecordFound;
    LinearLayout message_body;
    Button ok;
    String selected_district_id;
    BlockAdapter b_adapter;
    ListView listview;
    ArrayList<DistrictUserList> district_list;
    ArrayList<BlockList> blocklist;
    String state_id;
    String data;
    Button bt_ok, bt_cancel;
    SearchView searchView1;
    public static EditText flatName;
    private static int counter = 0;
    String block_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block_user_list);

        state_id = this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_STATE_ID, null);
        listview = (ListView) findViewById(R.id.district_listView);
        tvNoRecordFound = (TextView) findViewById(R.id.blank_text);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        searchView1 = (SearchView) findViewById(R.id.searchView1);
        searchView1.setQueryHint("Search");
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            selected_district_id = extras.getString("DISTRICTID");
        }
        GetBlockUserList();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox chk = (CheckBox) view.findViewById(R.id.checkbox);

                BlockList bean = blocklist.get(position);

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
                StringBuffer sb = new StringBuffer();

                for (BlockList bean : blocklist) {

                    if (bean.isSelected()) {
                        if (sb.toString().trim().contains(bean.getB_id())) {

                        } else {
                            sb.append(bean.getB_id());
                            sb.append(",");
                        }
                    }
                    if (sb.length() <= 0) {
                        block_id = " ";
                        // flatName.setText("");
                        //DistrictUser.this.finish();
                    } else {
                        block_id=sb.toString();
                       // GetBlockUserList();
                        //flatName.setText(sb.toString().trim().substring(0, sb.length() - 1));
                        //DistrictUser.this.finish();
                    }

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


    private void setQuestionList(String filterText) {

        final ArrayList<BlockList> visitors_search = new ArrayList<>();
        if (filterText != null && filterText.trim().length() > 0) {
            for (int i = 0; i < blocklist .size(); i++) {
                String q_title = blocklist.get(i).getB_title();
                if (q_title != null && filterText != null &&
                        q_title.toLowerCase().contains(filterText.toLowerCase())) {
                    visitors_search.add(blocklist.get(i));
                }
            }
        }else{
            visitors_search.addAll(blocklist);
        }
        // create an Object for Adapter
        b_adapter = new BlockAdapter(this, visitors_search);
        listview.setAdapter(b_adapter);
        //  mAdapter.notifyDataSetChanged();


        if (visitors_search.isEmpty()) {
            listview.setVisibility(View.GONE);
            tvNoRecordFound.setVisibility(View.VISIBLE);
        } else {
            listview.setVisibility(View.VISIBLE);
            tvNoRecordFound.setVisibility(View.GONE);
        }

        b_adapter.notifyDataSetChanged();
    }

    private void GetBlockUserList() {
        if (Util.getNetworkConnectivityStatus(this)) {
            GetBlock asyncTask = new GetBlock();
            asyncTask.execute(selected_district_id);
        } else {
            Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }


    private class GetBlock extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;
        String b_id,distric_id,b_title;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(BlockUser.this, "Please Wait",
                    "Loading UserList...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String districtid = params[0];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINE_URL + Constants.BLOCK_USER;
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
                        .appendQueryParameter("district_id", districtid);

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
                 *{
                 "blocks": [
                 {
                 "id": "11",
                 "district_id": "2",
                 "title": "ANGUL"
                 },
                 {
                 "id": "12",
                 "district_id": "2",
                 "title": "ATHMALLIK"
                 },
                 * */

                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    JSONArray user_list = res.getJSONArray("blocks");

                    blocklist= new ArrayList<BlockList>();

                    //db=new DBHelper(QAAnsweredListActivity.this);

                    for (int i = 0; i < user_list.length(); i++) {

                        JSONObject q_list_obj = user_list.getJSONObject(i);

                        b_id = q_list_obj.getString("id");
                        distric_id = q_list_obj.getString("district_id");
                        b_title = q_list_obj.getString("title");

                        BlockList b_list = new BlockList(b_id, distric_id, b_title);
                        blocklist.add(b_list);

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
            b_adapter = new BlockAdapter(BlockUser.this, blocklist);
            listview.setAdapter(b_adapter);
            // mListView.setSelectionFromTop(index, top);
            progress.dismiss();
        }
    }
}
