package com.iiit.amaresh.demotrack.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Adapter.ProjectAdapter;
import com.iiit.amaresh.demotrack.Pojo.ProjectListing;
import com.iiit.amaresh.demotrack.Pojo.Util;
import com.iiit.amaresh.demotrack.R;
import com.iiit.amaresh.demotrack.Util.Constants;

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

/**
 * Created by mobileapplication on 11/29/17.
 */

public class StaticUserLlist extends AppCompatActivity {

    ListView static_listView;
    SearchView search;
    ProjectAdapter adapter;
    ArrayList<ProjectListing> projectlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.iiit.amaresh.demotrack.R.layout.activity_static_userlist);

        static_listView = (ListView) findViewById(R.id.static_listView);
        search = (SearchView) findViewById(R.id.searchView1);
        search.setQueryHint("SearchView");
        GetProjectList();
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
        static_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProjectListing users = (ProjectListing) parent.getItemAtPosition(position);
                //  Toast.makeText(AdminUserList.this,users.getUser_name(),Toast.LENGTH_LONG).show();
                UploadAssets.et_projct_title.setText(users.getTitle());
                finish();
            }
        });

    }

    private void setQuestionList(String filterText) {

       ArrayList<ProjectListing> project_search = new ArrayList<ProjectListing>();
        if (filterText != null && filterText.trim().length() > 0) {
            for (int i = 0; i < projectlist .size(); i++) {
                String q_title = projectlist.get(i).getTitle();
                if (q_title != null && filterText != null &&
                        q_title.toLowerCase().contains(filterText.toLowerCase())) {
                    project_search.add(projectlist.get(i));
                }
            }
        }else{
            project_search.addAll(projectlist);
        }

        // create an Object for Adapter
        adapter = new ProjectAdapter(StaticUserLlist.this,project_search);
        static_listView.setAdapter(adapter);
        //  mAdapter.notifyDataSetChanged();


        if (projectlist.isEmpty()) {
            static_listView.setVisibility(View.GONE);
            //tvEmptyView.setVisibility(View.VISIBLE);
        } else {
            static_listView.setVisibility(View.VISIBLE);
            //tvEmptyView.setVisibility(View.GONE);
        }

        adapter.notifyDataSetChanged();
    }


    private void GetProjectList() {
        if(Util.getNetworkConnectivityStatus(StaticUserLlist.this)) {
            GetProjectData regtask = new GetProjectData();
            regtask.execute("");
        }
        else{
            Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_LONG).show();

        }
    }

    private class GetProjectData extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;
        int Status;
        String id,title,created,modified;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(StaticUserLlist.this, "Please Wait",
                    "Loading UserList...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String _emptype = params[0];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINE_URL + Constants.PROJECT_LIST;
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
                    builder = new Uri.Builder()
                            .appendQueryParameter("user_type", _emptype);
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
                /*{
            "projects": [
                {
                    "id": "1",
                    "title": "gfdgdfgdfgdfg",
                    "created": "30-11--0001 12:00 AM",
                    "modified": "30-11--0001 12:00 AM"
                }
            ],
            "status": 1,
            "message": "Records available"
        }*/


                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    JSONArray user_list = res.getJSONArray("projects");

                    Status = res.getInt("status");
                    projectlist = new ArrayList<ProjectListing>();

                    //db=new DBHelper(QAAnsweredListActivity.this);
                    if (Status == 1) {
                        for (int i = 0; i < user_list.length(); i++) {

                            JSONObject q_list_obj = user_list.getJSONObject(i);

                            id = q_list_obj.getString("id");
                            title = q_list_obj.getString("title");
                            created = q_list_obj.getString("created");
                            modified = q_list_obj.getString("modified");


                            ProjectListing p_list = new ProjectListing(id,title,created,modified);
                            projectlist.add(p_list);
                        }

                    } else {

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
            if (Status == 1) {
                adapter = new ProjectAdapter(StaticUserLlist.this, projectlist);
                static_listView.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "Connectivity Issue,Check your Internet", Toast.LENGTH_LONG).show();
            }
            progress.dismiss();
        }
    }
}
