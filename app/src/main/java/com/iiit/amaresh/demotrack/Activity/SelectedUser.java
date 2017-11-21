package com.iiit.amaresh.demotrack.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iiit.amaresh.demotrack.Adapter.DistrictAdapter;
import com.iiit.amaresh.demotrack.Pojo.UserListing;
import com.iiit.amaresh.demotrack.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RN on 11/18/2017.
 */

public class SelectedUser extends AppCompatActivity {

    String id,emp_id,emp_name,emp_add,emp_mail,emp_phone,
            emp_address,emp_pass,emp_imei,empl_type,emp_state,emp_dist,emp_block,
            emp_desig,usertype=null,user_status;
    List<UserListing> userlist;

    TextView tv_all_state, tv_state, tv_district, tv_block, send_msgbody;
    LinearLayout message_body;
    Button ok;
    String messagebody;
    RelativeLayout a_d_user, spcfc_d_user, d_w_user,all_user;
    DistrictAdapter adapter;
    ListView listview;
    ArrayList<UserListing> district_list;
    String tab=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_user);

        all_user = (RelativeLayout) findViewById(R.id.all);
        d_w_user = (RelativeLayout) findViewById(R.id.d_w_user);
        a_d_user = (RelativeLayout) findViewById(R.id.a_d_user);
        spcfc_d_user = (RelativeLayout) findViewById(R.id.spcfc_d_user);

        d_w_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SelectedUser.this,DistrictUser.class);
                i.putExtra("TAB","dwbu");
                startActivity(i);
               // Specificlist();
            }
        });
        spcfc_d_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SelectedUser.this,DistrictUser.class);
                i.putExtra("TAB","sdu");
                startActivity(i);
               // Specificlist();
            }
        });
        a_d_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab="adu";
                Intent i=new Intent(SelectedUser.this,MessageToSelectedUser.class);
                i.putExtra("TAB",tab);
                startActivity(i);
               // Specificlist();
            }
        });
        all_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab="all";
                Intent i=new Intent(SelectedUser.this,MessageToSelectedUser.class);
                i.putExtra("TAB",tab);
                startActivity(i);

            }
        });
    }

}
