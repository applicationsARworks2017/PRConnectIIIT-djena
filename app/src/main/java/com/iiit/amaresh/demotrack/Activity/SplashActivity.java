package com.iiit.amaresh.demotrack.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.iiit.amaresh.demotrack.Pojo.Constants;

public class SplashActivity extends AppCompatActivity {
    private static final int PERMISSION_ACCESS_COARSE_LOCATION =100;
    SharedPreferences sharedPreferences;
    private static final int SPLASH_INTERVAL_TIME = 1000;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.iiit.amaresh.demotrack.R.layout.layout_splash);

        final String phone_number = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_PHONE, null);
         //final String phone_number = "";


        if (phone_number==null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_INTERVAL_TIME);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_INTERVAL_TIME);
        }

      /*  Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 3 seconds
                    sleep(5*1000);

                    if(phone_number.length()==0){
                        Intent i=new Intent(getBaseContext(),MainActivity.class);
                        startActivity(i);

                    }
                    else{
                        Intent i=new Intent(getBaseContext(),Home.class);
                        startActivity(i);

                    }
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();*/
    }
}
