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
import android.widget.TextView;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Pojo.Constants;
import com.iiit.amaresh.demotrack.Pojo.Util;
import com.iiit.amaresh.demotrack.R;

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

/**
 * Created by RN on 11/19/2017.
 */

public class MessageToSelectedUser extends AppCompatActivity{

    String data;
    TextView rcpt_name;
    EditText msgbody;
    Button ok;
    String message_body;
    ProgressDialog progressDialog;
    int server_status;
    String server_response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectd_user_messag);


        Intent intent=getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            data = extras.getString("TAB");
        }


        rcpt_name=(TextView)findViewById(R.id.rcpt_name);
        msgbody=(EditText)findViewById(R.id.msgbody);
        ok=(Button) findViewById(R.id.ok);

        if(data.contains("all")){
          rcpt_name.setText("To"+" "+":"+" "+"All");
        }
        else if(data.contains("adu")){

            rcpt_name.setText("To"+" "+":"+" "+"ALL DISTRICT USER");

        }
      ok.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              message_body = msgbody.getText().toString();
              if (msgbody.length() == 0) {
                  Toast.makeText(MessageToSelectedUser.this, "Kindly Enter Message", Toast.LENGTH_LONG).show();
              } else {
                  //sendMessage();
              }
          }
      });


    }

    private void sendMessage() {
        if(Util.getNetworkConnectivityStatus(MessageToSelectedUser.this)) {
            send_message regtask = new send_message();
            String sender_name="";
            regtask.execute(sender_name);
        }
        else{
            Toast.makeText(MessageToSelectedUser.this, "Check Your Internet Connection", Toast.LENGTH_LONG).show();

        }
    }

    private class send_message extends AsyncTask<String, Void, Void> {
        private static final String TAG = "register_user";
        //private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(MessageToSelectedUser.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String _sendby = params[0];

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
                Intent intent=new Intent(MessageToSelectedUser.this,SelectedUser.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
            progressDialog.cancel();
        }
    }
}
