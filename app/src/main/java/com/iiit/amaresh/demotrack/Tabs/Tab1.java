package com.iiit.amaresh.demotrack.Tabs;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Activity.Home;
import com.iiit.amaresh.demotrack.Activity.MainActivity;
import com.iiit.amaresh.demotrack.Pojo.Constants;
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

/**
 */

//Our class extending fragment
public class Tab1 extends Fragment {
    Button login;
    EditText phoneemail,password;
    String emailphone,pass;
    ProgressDialog progressDialog;
    String server_response;
    String emp_name,emp_phone,emp_mail,emp_desg;
    int server_status,user_id;
    String state_id,district_id,block_id;
    SharedPreferences sharedPreferences;
    private TelephonyManager mTelephonyManager;
    String deviceid,user_type;
    String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE};



    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view=inflater.inflate(com.iiit.amaresh.demotrack.R.layout.tab1, container, false);
        login=(Button)view.findViewById(com.iiit.amaresh.demotrack.R.id.btnLogin);
        phoneemail=(EditText)view.findViewById(com.iiit.amaresh.demotrack.R.id.email);
        password=(EditText)view.findViewById(com.iiit.amaresh.demotrack.R.id.password);
        getDeviceImei();
         login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailphone=phoneemail.getText().toString();
                pass=password.getText().toString();
                if(emailphone.length()==0){
                    Toast.makeText(getContext(),"Enter Phone Number", Toast.LENGTH_LONG).show();
                }
                else if(pass.length()==0){
                    Toast.makeText(getContext(),"Enter Password", Toast.LENGTH_LONG).show();
                }
                else{
                    userLogin();
                }
               /* Intent i=new Intent(getContext(),Home.class);
                startActivity(i);*/
            }
        });

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return view;
    }

    private void getDeviceImei() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(getActivity(), PERMISSIONS)) {
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, 1);
            } else {

                mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
                deviceid = mTelephonyManager.getDeviceId();

            }
        } else {
            mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            deviceid = mTelephonyManager.getDeviceId();

        }
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    private void userLogin(){
        if(Util.getNetworkConnectivityStatus(getContext())) {
            LoginAsync regtask = new LoginAsync();
            regtask.execute(emailphone,pass,deviceid);

        }
    }
    private class LoginAsync extends AsyncTask<String, Void, Void> {
        private static final String TAG = "register_user";
        //private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(getContext(), "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                String _emp_pass = params[1];
                String _emp_mail = params[0];
                String _device_imei = params[2];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.ONLINE_URL+Constants.USER_LOGIN;
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
                        .appendQueryParameter("empmail", _emp_mail)
                        .appendQueryParameter("imei", _device_imei)
                        .appendQueryParameter("emppass", _emp_pass);
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
                 *
                 {
                 "emp": {
                 "id": "546",
                 "empname": "New user",
                 "empphone": "7026405550",
                 "empmail": null,
                 "empdesg": "",
                 "usertype": "1",
                 "state_id": "1",
                 "district_id": null,
                 "block_id": null
                 },
                 * */

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status == 1){
                        JSONObject userObj = res.optJSONObject("emp");
                        user_id=userObj.optInt("id");
                        emp_name = userObj.optString("empname");
                        emp_phone = userObj.optString("empphone");
                        emp_mail = userObj.optString("empmail");
                        emp_desg = userObj.optString("empdesg");
                        user_type = userObj.optString("usertype");
                        state_id = userObj.getString("state_id");
                        district_id = userObj.getString("district_id");
                        block_id = userObj.getString("block_id");
                        server_response="Successfully LoggedIn.";
                    }
                    else{
                        server_response="Invalid Credentials";

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
            postLogin();
            progressDialog.cancel();

        }
    }
    public void postLogin(){
        if(server_status==1) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.SP_USER_TYPE,user_type);
            editor.putString(Constants.SP_USER_NAME,emp_name);
            editor.putString(Constants.SP_USER_PHONE,emp_phone);
            editor.putString(Constants.SP_USER_EMAIL,emp_mail);
            editor.putString(Constants.SP_USER_DESG,emp_desg);
            editor.putInt(Constants.SP_USER_ID,user_id);
            editor.putString(Constants.SP_STATE_ID,state_id);
            editor.putString(Constants.SP_BLOCK_ID,block_id);
            editor.putString(Constants.SP_DISTRICT_ID,district_id);
            editor.commit();

            Toast.makeText(getContext(), server_response, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        else {
            Toast.makeText(getContext(), server_response, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }


}

