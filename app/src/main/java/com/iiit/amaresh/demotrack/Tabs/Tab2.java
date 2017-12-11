package com.iiit.amaresh.demotrack.Tabs;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Util.Constants;
import com.iiit.amaresh.demotrack.Activity.UserDetails;
import com.iiit.amaresh.demotrack.Pojo.Util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 */

public class Tab2 extends Fragment  {
    Button cont;
    EditText et_phone;
    RadioGroup rg;
    int pos,server_status,user_id;
    int pos1;
    String level="";
    String phone_num,server_message;
    ProgressDialog progressDialog;
    String emp_name,emp_phone,emp_mail,emp_desg,s_area,d_area,b_area;
    private TelephonyManager mTelephonyManager;
    String deviceid;
    String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view=inflater.inflate(com.iiit.amaresh.demotrack.R.layout.tab2, container, false);
        et_phone=(EditText)view.findViewById(com.iiit.amaresh.demotrack.R.id.phnum);
        cont=(Button)view.findViewById(com.iiit.amaresh.demotrack.R.id.cont);
        rg = (RadioGroup) view.findViewById(com.iiit.amaresh.demotrack.R.id.usertype);
        getDeviceImei();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                pos=rg.indexOfChild(view.findViewById(checkedId));
                /*Toast.makeText(getContext(), "Method 1 ID = "+String.valueOf(pos),
                        Toast.LENGTH_SHORT).show();*/
                //Method 2 For Getting Index of RadioButton
               /* pos1=rg.indexOfChild(view.findViewById(rg.getCheckedRadioButtonId()));

                Toast.makeText(getContext(), "Method 2 ID = "+String.valueOf(pos1),
                        Toast.LENGTH_SHORT).show();
*/
                switch (pos)
                {
                    case 0 :
                        level="state";
                        break;
                    case 1 :
                        level="district";
                        break;
                    case 2 :
                        level="block";
                        break;
                    default :
                        level=" ";

                }
            }
        });
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_num=et_phone.getText().toString();
                if(phone_num .length()<=0){
                    Toast.makeText(getContext(),"Kindly Enter the Phone Number",Toast.LENGTH_LONG).show();
                }
                else if(phone_num.length()<10){
                    Toast.makeText(getContext(),"Kindly Enter 10 digit Phone Num",Toast.LENGTH_LONG).show();
                }
               else if(level==""){
                   Toast.makeText(getContext(),"Kindly select User Type",Toast.LENGTH_LONG).show();
               }
                else {
                  valid_mobie();
               }

            }
        });

        return view;


    }

    private void getDeviceImei() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(getActivity(), PERMISSIONS)) {
                ActivityCompat.requestPermissions(getActivity(),
                        PERMISSIONS,
                        1);
            }
            else {

                mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
                deviceid = mTelephonyManager.getDeviceId();

            }
        } else {
            mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            deviceid = mTelephonyManager.getDeviceId();

        }
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void valid_mobie(){
        if (Util.getNetworkConnectivityStatus(getContext())) {
            send_mob_num();
        }else {
            Toast.makeText(getContext(), "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }

    }
    private void send_mob_num(){
        SynchMobnum asyncTask = new SynchMobnum();
        asyncTask.execute(level,phone_num,deviceid);
    }

    /**
     * Async task to get sync camp table from server
     * */
    private class SynchMobnum extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        //private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(getContext(), "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _userphone = params[1];
                String _usertype = params[0];
                String _device_imei = params[2];

                InputStream in = null;
                int resCode = -1;

               String link = Constants.ONLINE_URL+Constants.PHONE_VERIFY;
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
                        .appendQueryParameter("empphone", _userphone)
                        .appendQueryParameter("imei", _device_imei)
                        .appendQueryParameter("emptype", _usertype);
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
                if(in == null){
                    return null;
                }
                BufferedReader reader =new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "",data="";

                while ((data = reader.readLine()) != null){
                    response += data + "\n";
                }

                Log.i(TAG, "Response : "+response);

                /**
                 * {
                 "emp": {
                 "id": "1",
                 "empname": "Sri Deoranjan Kumar Singh",
                 "empphone": "8280405001",
                 "empmail": null,
                 "empdesg": "Commissioner-cum-Secretary",
                 "state": ""
                 },
                 "status": 1,
                 "message": "Records available"
                 }

                 wrong
                 {
                 "status": 0,
                 "message": "No records available"
                 }
                 * */

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                     server_status = res.optInt("status");
                    if(server_status==1) {
                        JSONObject userObj = res.optJSONObject("emp");
                        emp_name = userObj.optString("empname");
                        emp_phone = userObj.optString("empphone");
                        emp_mail = userObj.optString("empmail");
                        emp_desg = userObj.optString("empdesg");
                        s_area = userObj.optString("state");
                        d_area = userObj.optString("district");
                        b_area = userObj.optString("block");
                        user_id=userObj.optInt("id");
                        server_message="Fetched the Data";
                    }
                    else{
                        server_message="Invalid Credentials";
                    }


                   // int status = res.optInt("login_status");
                    //  message = res.optString("message");
                }

/*                    if(status == 1){
                        JSONObject userObj = res.optJSONObject("User");
                        String user_id = userObj.optString("user_id");
                        login_userid=user_id;
                        String full_name = userObj.optString("full_name");
                        String username = userObj.optString("username");
                        String password = userObj.optString("password");
                        String api_key = userObj.optString("api_key");
                        String m_user_type_id = userObj.optString("m_user_type_id");
                        login_usertypeid=m_user_type_id;
                        String m_desg_id = userObj.optString("m_desg_id");
                        String m_state_id = userObj.optString("m_state_id");
                        String m_district_id = userObj.optString("m_district_id");
                        String m_block_id = userObj.optString("m_block_id");
                        String m_organization_id = userObj.optString("m_organization_id");
                        String mobile_no = userObj.optString("mobile_no");
                        String email_id = userObj.optString("email_id");
                        String is_active = userObj.optString("is_active");
                        String created = userObj.optString("created");
                        String modified = userObj.optString("modified");

                        User user = null;
                        if(user_id != null && TextUtils.isDigitsOnly(user_id)) {
                            user = new User(Parcel.obtain());
                            user.setUser_id(Integer.parseInt(user_id));
                            user.setFull_name(full_name);
                            user.setApi_key(api_key);
                            user.setUsername(username);
                            user.setPassword(password);
                            user.setM_user_type_id(m_user_type_id);
                            user.setM_desg_id(m_desg_id);
                            user.setM_state_id(m_state_id);
                            user.setM_district_id(m_district_id);
                            user.setM_block_id(m_block_id);
                            user.setM_organization_id(m_organization_id);
                            user.setMobile_no(mobile_no);
                            user.setEmail_id(email_id);
                            user.setIs_active(is_active);
                            user.setCreated(created);
                            user.setModified(modified);
                            //dbHelper.insertUser(user);
                        }

                        if(user != null) {
                            getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 2).edit().putInt(Constants.SP_LOGIN_STATUS, 1).commit();
                            getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 2).edit().putInt(Constants.SP_USER_ID, user.getUser_id()).commit();
                            getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 2).edit().putString(Constants.SP_USER_API_KEY, user.getApi_key()).commit();
                            getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 2).edit().putString(Constants.SP_USER_DESG_ID, user.getM_desg_id()).commit();
                            getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 2).edit().putString(Constants.SP_USER_NAME, user.getFull_name()).commit();
                            getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 2).edit().putString(Constants.SP_USER_EMAIL, user.getEmail_id()).commit();
                        }

                        if(Util.getNetworkConnectivityStatus(LoginActivity.this)) {
                            UserUtil.registerPushBots(LoginActivity.this, false);
                        }*/

                //dbHelper.close();
                return null;


            } catch(SocketTimeoutException exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch(ConnectException exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch(MalformedURLException exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch (IOException exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch(Exception exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            calltopostExecute();
            progressDialog.cancel();
        }
    }
    private void calltopostExecute(){
        if(server_status==1) {
            Intent i = new Intent(getContext(), UserDetails.class);
            i.putExtra("emp_name", emp_name);
            i.putExtra("emp_desg", emp_desg);
            i.putExtra("emp_phone", emp_phone);
            i.putExtra("s_area", s_area);
            i.putExtra("d_area", d_area);
            i.putExtra("b_area", b_area);
            i.putExtra("emp_mail", emp_mail);
            i.putExtra("user_id", user_id);

            startActivity(i);
        }
        else{
            Toast.makeText(getContext(),server_message,Toast.LENGTH_LONG).show();
        }
    }
}


// onPostExecuteTask();

           /* if(user != null) {
                getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 2).edit().putInt(Constants.SP_LOGIN_STATUS, 1).commit();
                getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 2).edit().putInt(Constants.SP_USER_ID, user.getUser_id()).commit();
                getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 2).edit().putString(Constants.SP_USER_API_KEY, user.getApi_key()).commit();
                getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 2).edit().putString(Constants.SP_USER_DESG_ID, user.getM_desg_id()).commit();
                getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 2).edit().putString(Constants.SP_USER_NAME, user.getFull_name()).commit();
                getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 2).edit().putString(Constants.SP_USER_EMAIL, user.getEmail_id()).commit();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        int userType = Integer.parseInt(user.getM_user_type_id());
                        if (userType == 2) {
                            getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 2).edit().putInt(Constants.SP_USER_TYPE, 2).commit();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else if (userType == 1) {
                            getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 2).edit().putInt(Constants.SP_USER_TYPE, 1).commit();
                            syncDistrictList();
                            //getNodalofficers();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }


                        //getNotification();

                        //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        //finish();
                    }
                }, 0);
            } else{

                String _snack_bar_msg = "";
                if(message != null && message.length() > 0){
                    _snack_bar_msg = message;
                } else {
                    _snack_bar_msg = "Some Problem occured.";
                }

                Snackbar snackbar = Snackbar.make(findViewById(R.id.layoutParent), _snack_bar_msg, Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                View snackbarView = snackbar.getView();
                TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                TextView tvAction = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_action);
                tvAction.setTextColor(Color.CYAN);
                snackbar.show();
            }*/

            // fpr loading the count of dash board. It'll move to Dashboard fragmant
            //dashboardCount();

