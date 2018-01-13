package com.iiit.amaresh.demotrack.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Util.Constants;
import com.iiit.amaresh.demotrack.Pojo.Util;

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

public class UserDetails extends AppCompatActivity {
    Button cont;
    ProgressDialog progressDialog;
    EditText name,desg,pass,email,area,phone;
    String emp_name,emp_phone,empl_mail,emp_desg,emp_sarea,emp_darea,emp_barea,st_usr_id;
    String server_response,password;
    int server_status,user_id;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION =100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.iiit.amaresh.demotrack.R.layout.layout_user_details);
        name=(EditText)findViewById(com.iiit.amaresh.demotrack.R.id.f_name) ;
        desg=(EditText)findViewById(com.iiit.amaresh.demotrack.R.id.id_desig) ;
        pass=(EditText)findViewById(com.iiit.amaresh.demotrack.R.id.text_inputpassword) ;
        email=(EditText)findViewById(com.iiit.amaresh.demotrack.R.id.text_inputmail) ;
        area=(EditText)findViewById(com.iiit.amaresh.demotrack.R.id.area) ;
        phone=(EditText)findViewById(com.iiit.amaresh.demotrack.R.id.text_phones) ;


        //getting the value from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            emp_name = extras.getString("emp_name");
            emp_desg = extras.getString("emp_desg");
            emp_phone = extras.getString("emp_phone");
            user_id = extras.getInt("user_id");
            st_usr_id=String.valueOf(user_id);
            empl_mail = extras.getString("emp_mail");
            emp_sarea = extras.getString("s_area");
            emp_darea = extras.getString("d_area");
            emp_barea = extras.getString("b_area");

            // and get whatever type user account id is
        }
        name.setText(emp_name);
        desg.setText(emp_desg);
        email.setText(empl_mail);
        phone.setText(emp_phone);
        area.setText(emp_barea+","+emp_darea+","+emp_sarea);
        cont=(Button)findViewById(com.iiit.amaresh.demotrack.R.id.continu);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password=pass.getText().toString();

                if (password.length()==0){
                    Toast.makeText(UserDetails.this, "Kindly Enter Password", Toast.LENGTH_LONG).show();
                }
                else {
                    register();
                }
           /* Intent i=new Intent(UserDetails.this,Home.class);
            startActivity(i);*/
            }
        });

    }
    private void register(){
        //Toast.makeText(UserDetails.this,password, Toast.LENGTH_LONG).show();

        if(Util.getNetworkConnectivityStatus(UserDetails.this)) {
            register_user regtask = new register_user();
            regtask.execute(emp_name, empl_mail, emp_phone,password, emp_desg, emp_sarea, emp_darea, emp_barea,st_usr_id);
        }

    }

    private class register_user extends AsyncTask<String, Void, Void> {
        private static final String TAG = "register_user";
        //private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(UserDetails.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                String _emp_name = params[0];
                String _emp_mail = params[1];
                String _emp_phone = params[2];
                String _emp_pass = params[3];
                String _emp_desg = params[4];
                String _emp_sarea = params[5];
                String _emp_darea = params[6];
                String _emp_barea = params[7];
                String _user_id = params[8];

                InputStream in = null;
                int resCode = -1;

                String link = Constants.ONLINE_URL + Constants.USER_REGISTER;
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
                        .appendQueryParameter("empname", _emp_name)
                        .appendQueryParameter("empmail", _emp_mail)
                        .appendQueryParameter("empphone", _emp_phone)
                        .appendQueryParameter("empdesg", _emp_desg)
                        .appendQueryParameter("state", _emp_sarea)
                        .appendQueryParameter("district", _emp_darea)
                        .appendQueryParameter("block", _emp_barea)
                        .appendQueryParameter("emppass",_emp_pass)
                        .appendQueryParameter("id",_user_id);
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
                        server_response="You have successfully registered.";

                    }
                    else{
                        server_response="Registration Field.";

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
            postregistration();
            progressDialog.cancel();
        }
    }
    public void postregistration(){
        Toast.makeText(UserDetails.this,server_response,Toast.LENGTH_LONG).show();
        Intent i=new Intent(UserDetails.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }
}
