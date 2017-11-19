package com.iiit.amaresh.demotrack.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RN on 11/18/2017.
 */

public class SelectedUser extends AppCompatActivity {

    TextView tv_all_state,tv_state,tv_district,tv_block,send_msgbody;
    LinearLayout message_body;
    Button ok;
    String messagebody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_user);

        send_msgbody=(TextView)findViewById(R.id.send_msgbody);
        tv_all_state=(TextView)findViewById(R.id.tv_ststuser);
        tv_state=(TextView)findViewById(R.id.tv_state);
        tv_district=(TextView)findViewById(R.id.tv_district);
        tv_block=(TextView)findViewById(R.id.tv_block);
        message_body=(LinearLayout)findViewById(R.id.message_body);
        ok=(Button)findViewById(R.id.ok);

        tv_all_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message_body.setVisibility(View.VISIBLE);
                /*Intent i=new Intent(SelectedUser.this,MessageToSelectedUser.class);
                startActivity(i);*/
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messagebody = send_msgbody.getText().toString();
                if (send_msgbody.length() == 0) {
                    Toast.makeText(SelectedUser.this, "Kindly Enter Message", Toast.LENGTH_LONG).show();
                } else {
                   // sendMessage();
                }
            }
        });



    }
}
