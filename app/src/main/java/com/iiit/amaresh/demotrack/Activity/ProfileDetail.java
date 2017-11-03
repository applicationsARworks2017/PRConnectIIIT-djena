package com.iiit.amaresh.demotrack.Activity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.iiit.amaresh.demotrack.Extra.BaseActivity;
import com.iiit.amaresh.demotrack.Pojo.Constants;
import com.iiit.amaresh.demotrack.R;

/**
 * Created by RN on 11/2/2017.
 */

public class ProfileDetail extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        TextView tv_phone,tv_name,tv_designation;
        String phone_number,name,designation;


        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

            toolbar.setTitle(getResources().getString(R.string.profile));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(ProfileDetail.this);
                }
            });

        }
        phone_number = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_PHONE, null);
        name = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_NAME, null);
        designation = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_DESG, null);
        tv_phone=(TextView)findViewById(R.id.tv_phone);
        tv_name=(TextView)findViewById(R.id.tv_name);
        tv_designation=(TextView)findViewById(R.id.tv_designation);
        tv_phone.setText(phone_number);
        tv_name.setText(name);
        tv_designation.setText(designation);
    }
}
