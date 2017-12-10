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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Extra.BaseActivity;
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

public class MessageToSelectedUser extends BaseActivity{

    String data,BLOCKID,BLOCKNAME;
    TextView rcpt_name;
    EditText msgbody;
    Button ok;
    String message_body;
    ProgressDialog progressDialog;
    int server_status;
    String server_response,sender_name,distric_id,state_id,bloc_id,USERID,USERNAME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectd_user_messag);

        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setTitle(getResources().getString(R.string.writemessage));
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(MessageToSelectedUser.this);
                }
            });

        }

        Intent intent=getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            data = extras.getString("TAB");
            BLOCKID = extras.getString("BLOCKID");
            BLOCKNAME = extras.getString("BLOCKNAME");
            USERNAME = extras.getString("USERNAME");
            USERID = extras.getString("USERID");
        }

        sender_name = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_NAME, null);
        //state_id = this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_STATE_ID, null);
        distric_id = this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_DISTRICT_ID, null);
        bloc_id = this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_BLOCK_ID, null);

        rcpt_name=(TextView)findViewById(R.id.rcpt_name);
        msgbody=(EditText)findViewById(R.id.msgbody);
        ok=(Button) findViewById(R.id.ok);

        if(data.contains("all")){
          rcpt_name.setText("To"+" "+":"+" "+"All");
        }
        else if(data.contains("adu")){

            rcpt_name.setText("To"+" "+":"+" "+"ALL DISTRICT USER");

        }
        else if(data.contains("SBU")){

            rcpt_name.setText("To"+" "+":"+" "+USERNAME);

        }  else if(data.contains("State")){

            rcpt_name.setText("To"+" "+":"+" "+USERNAME);

        } else if(data.contains("dwbu")){

            rcpt_name.setText("To"+" "+":"+" "+BLOCKNAME);

        }
      ok.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              message_body = msgbody.getText().toString();
              if (msgbody.length() == 0) {
                  Toast.makeText(MessageToSelectedUser.this, "Kindly Enter Message", Toast.LENGTH_LONG).show();
              } else {
                  sendMessage();
              }
          }
      });


    }

    private void sendMessage() {
        if(Util.getNetworkConnectivityStatus(MessageToSelectedUser.this)) {
            send_message regtask = new send_message();
            regtask.execute(sender_name,message_body,distric_id,bloc_id,state_id,BLOCKID,USERID);
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
                String _msg = params[1];
                String _distric = params[2];
                String _block = params[3];
                String _state = params[4];
                String _blockid = params[5];
                String _userid = params[6];

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
                Uri.Builder builder=null;
                if(data.contentEquals("adu")){
                    builder = new Uri.Builder()
                            .appendQueryParameter("send_by",_sendby)
                            .appendQueryParameter("message",_msg)
                            .appendQueryParameter("usertype","2");
                }else if(data.contentEquals("SBU")){
                    builder = new Uri.Builder()
                            .appendQueryParameter("send_by",_sendby)
                            .appendQueryParameter("message",_msg);

                }else if(data.contentEquals("SPB")){
                    builder = new Uri.Builder()
                            .appendQueryParameter("send_by",_sendby)
                            .appendQueryParameter("message",_msg);

                }else if(data.contentEquals("SPD")){
                    builder = new Uri.Builder()
                            .appendQueryParameter("send_by",_sendby)
                            .appendQueryParameter("message",_msg);
                }
                else if(data.contentEquals("dwbu")){
                    builder = new Uri.Builder()
                            .appendQueryParameter("send_by",_sendby)
                            .appendQueryParameter("message",_msg)
                            .appendQueryParameter("block_id",_blockid)
                            .appendQueryParameter("usertype","0");
                }
                else {
                    builder = new Uri.Builder()
                            .appendQueryParameter("send_by", _sendby)
                            .appendQueryParameter("message", _msg);
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
