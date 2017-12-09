
package com.iiit.amaresh.demotrack.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Extra.BaseActivity;
import com.iiit.amaresh.demotrack.Pojo.Constants;
import com.iiit.amaresh.demotrack.R;

public class GetMovement extends BaseActivity {

    public static EditText phone;
    Button cont;
     String phone_number;
    ImageView add_phone_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_movement);
        if(null!=toolbar){
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setTitle(getResources().getString(R.string.Live_Track));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(GetMovement.this);
                }
            });
        }
        phone_number = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_PHONE, null);

        phone=(EditText)findViewById(R.id.mov_pnone);
        add_phone_name=(ImageView) findViewById(R.id.add_phone_name);
        add_phone_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(GetMovement.this,ViewAllUsers.class);
                i.putExtra("page","movement");
                startActivity(i);
            }
        });
        phone.setKeyListener(null);
        phone.setText(phone_number);
        cont=(Button)findViewById(R.id.mov_cont);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value_phone=phone.getText().toString();
                if(value_phone.length()==0){
                    Toast.makeText(GetMovement.this, "Enter Phone", Toast.LENGTH_LONG).show();

                }
                else{
                    Intent intent=new Intent(GetMovement.this,MovementMap.class);
                    intent.putExtra("phone_number",value_phone);
                    startActivity(intent);
                }
            }
        });
    }
}

