package com.iiit.amaresh.demotrack.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.iiit.amaresh.demotrack.Adapter.DistrictAdapter;
import com.iiit.amaresh.demotrack.Extra.BaseActivity;
import com.iiit.amaresh.demotrack.Pojo.Constants;
import com.iiit.amaresh.demotrack.Pojo.UserListing;
import com.iiit.amaresh.demotrack.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RN on 11/18/2017.
 */

public class SelectedUser extends BaseActivity {

    String id,emp_id,emp_name,emp_add,emp_mail,emp_phone,
            emp_address,emp_pass,emp_imei,empl_type,emp_state,emp_dist,emp_block,
            emp_desig,usertype=null,user_status;
    List<UserListing> userlist;

    TextView tv_all_state, tv_state, tv_district, tv_block, send_msgbody;
    LinearLayout message_body;
    Button ok;
    String messagebody;
    RelativeLayout a_d_user, spcfc_d_user, d_w_user,s_all_user,spcfc_b_user,district_List,block_list,
    specific_block_user,specific_d_user,specific_state_user,spcfc_state_user_district,specific_district_user,
            specic_block_user_district,all_d_w_b_user,spcfc_block_user;
    DistrictAdapter adapter;
    ListView listview;
    ArrayList<UserListing> district_list;
    String tab=null;
    String userType,districtid;
    ScrollView scrollview;
    public static String selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_user);

        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setTitle(getResources().getString(R.string.userlist));
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(SelectedUser.this);
                }
            });
        }

        userType = this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_TYPE, null);
        districtid = this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_DISTRICT_ID, null);
    //for layout
        district_List = (RelativeLayout) findViewById(R.id.district_list);
        block_list = (RelativeLayout) findViewById(R.id.block_list);

        //for block
        specific_state_user = (RelativeLayout) findViewById(R.id.specific_state_user);
        specific_d_user = (RelativeLayout) findViewById(R.id.specific_d_user);
        specific_block_user = (RelativeLayout) findViewById(R.id.specific_block_user);

        //for district
        spcfc_state_user_district = (RelativeLayout) findViewById(R.id.spcfc_state_user_district);
        specific_district_user = (RelativeLayout) findViewById(R.id.specific_district_user);
        specic_block_user_district = (RelativeLayout) findViewById(R.id.specic_block_user_district);
        all_d_w_b_user = (RelativeLayout) findViewById(R.id.all_d_w_b_user);

        //for state
        s_all_user = (RelativeLayout) findViewById(R.id.all);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        d_w_user = (RelativeLayout) findViewById(R.id.d_w_user);
        a_d_user = (RelativeLayout) findViewById(R.id.a_d_user);
        spcfc_d_user = (RelativeLayout) findViewById(R.id.spcfc_d_user);
        spcfc_block_user = (RelativeLayout) findViewById(R.id.spcfc_block_user);

        // usertype differentiate
        if(userType.contains("2")){
            scrollview.setVisibility(View.GONE);
            district_List.setVisibility(View.VISIBLE);
        }
       else if(userType.contains("0")){
            scrollview.setVisibility(View.GONE);
            block_list.setVisibility(View.VISIBLE);

        }
        else{
            scrollview.setVisibility(View.VISIBLE);
        }

        // For State unitialization
        d_w_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection="dwbu";
                Intent i=new Intent(SelectedUser.this,DistrictUser.class);
                //i.putExtra("TAB","dwbu");
                startActivity(i);
               // Specificlist();
            }
        });
        spcfc_d_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection="sdu";
                Intent i=new Intent(SelectedUser.this,DistrictUser.class);
               // i.putExtra("TAB","sdu");
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
        s_all_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab="all";
                Intent i=new Intent(SelectedUser.this,MessageToSelectedUser.class);
                i.putExtra("TAB",tab);
                startActivity(i);

            }
        });
        spcfc_block_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selection="SBU";
                    Intent i=new Intent(SelectedUser.this,DistrictUser.class);
                    i.putExtra("TAB",tab);
                    startActivity(i);

                }
            });

        //for district initialization

        all_d_w_b_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection="dwbu";
                Intent i=new Intent(SelectedUser.this,DistrictUser.class);
               // i.putExtra("TAB","dwbu");
                startActivity(i);
                // Specificlist();
            }
        });
        specic_block_user_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SelectedUser.this,BlockUser.class);
                i.putExtra("page","SBU");
                startActivity(i);
                // Specificlist();
            }
        });
        specific_district_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection="sdu";
                Intent i=new Intent(SelectedUser.this,DistrictUser.class);
               // i.putExtra("TAB","sdu");
                startActivity(i);
                // Specificlist();
            }
        });
        spcfc_state_user_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab="State";
                Intent i=new Intent(SelectedUser.this,AlluserList.class);
                i.putExtra("page",tab);
                startActivity(i);

            }
        });

        //for block

        specific_state_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab = "State";
                Intent i = new Intent(SelectedUser.this, AlluserList.class);
                i.putExtra("page", tab);
                startActivity(i);
            }
        });
        specific_d_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection="sdu";
                Intent i=new Intent(SelectedUser.this,DistrictUser.class);
               // i.putExtra("TAB","sdu");
                startActivity(i);
                // Specificlist();
            }
        });
        specific_block_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SelectedUser.this,BlockUser.class);
                i.putExtra("page","SBU");
                startActivity(i);
                // Specificlist();
            }
        });
    }

}
