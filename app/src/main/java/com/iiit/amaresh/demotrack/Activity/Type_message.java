package com.iiit.amaresh.demotrack.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Extra.BaseActivity;
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
import java.util.ArrayList;
import java.util.List;

public class Type_message extends BaseActivity {
    private String UserName,message_body,id;
    TextView recipnt_name;
    EditText Msg_body;
    Button send;
    ProgressDialog progressDialog;
    int server_status;
    String server_response;
    int sender_id;
    String sender_name;
    List<CharSequence> list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.iiit.amaresh.demotrack.R.layout.activity_type_message);
        if (null != toolbar) {
            toolbar.setNavigationIcon(com.iiit.amaresh.demotrack.R.drawable.ic_arrow_back_white_24dp);

            toolbar.setTitle(getResources().getString(com.iiit.amaresh.demotrack.R.string.msg_page));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(Type_message.this);
                }
            });

        }
        list= new ArrayList<CharSequence>();
         sender_name = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_NAME, null);
       //  sender_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.SP_USER_ID, 0);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            UserName = extras.getString("name");
            id = extras.getString("id");
        }

        recipnt_name=(TextView)findViewById(com.iiit.amaresh.demotrack.R.id.rcpt_name);
        recipnt_name.setText("Name : "+UserName);
        Msg_body=(EditText)findViewById(com.iiit.amaresh.demotrack.R.id.msgbody);
        send=(Button)findViewById(com.iiit.amaresh.demotrack.R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message_body = Msg_body.getText().toString();
                if (Msg_body.length() == 0) {
                    Toast.makeText(Type_message.this, "Kindly Enter Message", Toast.LENGTH_LONG).show();
                } else {
                    sendMessage();
                }
            }
        });
    }

    private void sendMessage() {
        if(Util.getNetworkConnectivityStatus(Type_message.this)) {
            send_message regtask = new send_message();
           // String s_sender_id=String.valueOf(sender_id);
            regtask.execute(id,message_body,sender_name);
        }
        else{
            Toast.makeText(Type_message.this, "Check Your Internet Connection", Toast.LENGTH_LONG).show();

        }
    }
    private class send_message extends AsyncTask<String, Void, Void> {
        private static final String TAG = "register_user";
        //private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(Type_message.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                String _emp_id = params[0];
                String _msg_body = params[1];
                String _sendby = params[2];

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
            //postregistration();
            progressDialog.cancel();
            finish();
        }
    }
    public void postregistration(){
        Toast.makeText(Type_message.this,server_response,Toast.LENGTH_LONG).show();
        Intent i=new Intent(Type_message.this,ViewAllUsers.class);
        startActivity(i);
    }

}
