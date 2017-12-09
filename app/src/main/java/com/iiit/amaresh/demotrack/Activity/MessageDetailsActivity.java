package com.iiit.amaresh.demotrack.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iiit.amaresh.demotrack.Extra.BaseActivity;
import com.iiit.amaresh.demotrack.R;

public class MessageDetailsActivity extends BaseActivity {
    String sendby,date,msgbody;
    Button okay;
    TextView tv_sendername,tv_msgbody,tv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        if (null != toolbar) {
          toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

            toolbar.setTitle(getResources().getString(R.string.msg_page));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(MessageDetailsActivity.this);
                }
            });

        }
        tv_sendername=(TextView)findViewById(R.id.sender);
        tv_msgbody=(TextView)findViewById(R.id.send_msgbody);
        okay=(Button)findViewById(R.id.ok);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sendby = extras.getString("SENDBY");
            msgbody = extras.getString("BODY");
            date = extras.getString("DATE");
        }

        tv_sendername.setText("From : "+sendby);
        tv_msgbody.setText(msgbody);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MessageDetailsActivity.this,PrimaryNotices.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }
}
