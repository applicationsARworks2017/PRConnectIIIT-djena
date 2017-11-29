package com.iiit.amaresh.demotrack.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Extra.BaseActivity;
import com.iiit.amaresh.demotrack.Pojo.Constants;
import com.iiit.amaresh.demotrack.Pojo.Util;
import com.iiit.amaresh.demotrack.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SubOrdinateHistory extends BaseActivity {
    private DatePicker datePicker;
    private Calendar calendar;
    public static EditText dateView,phoneview;
    Button cntne,set;
    String phone_number,name;
    int user_id;
    ProgressDialog progressDialog;
    int server_status;
    String server_message;
    Calendar myCalendar;
    ImageView add_phone_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.iiit.amaresh.demotrack.R.layout.activity_sub_ordinate_history);

        if(null!=toolbar){
            toolbar.setNavigationIcon(com.iiit.amaresh.demotrack.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            toolbar.setTitle(getResources().getString(com.iiit.amaresh.demotrack.R.string.Check_history));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(SubOrdinateHistory.this);
                }
            });
        }
        phone_number = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_PHONE, null);
        name = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_NAME, null);
        user_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.SP_USER_ID, 0);
        add_phone_name=(ImageView) findViewById(R.id.add_phone_name);
        add_phone_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SubOrdinateHistory.this,ViewAllUsers.class);
                i.putExtra("page","history");
                startActivity(i);
            }
        });

        //// for calander
        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        dateView=(EditText)findViewById(com.iiit.amaresh.demotrack.R.id.sub_date);
        phoneview=(EditText)findViewById(com.iiit.amaresh.demotrack.R.id.sub_pnone);
        phoneview.setKeyListener(null);
        phoneview.setText(phone_number);
        set=(Button)findViewById(com.iiit.amaresh.demotrack.R.id.sub_btndset);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateView.setText("");
                new DatePickerDialog(SubOrdinateHistory.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        cntne=(Button)findViewById(com.iiit.amaresh.demotrack.R.id.sub_cont);

        cntne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value_phone=phoneview.getText().toString();
                if(value_phone.length()==0){
                    Toast.makeText(SubOrdinateHistory.this, "Enter Phone", Toast.LENGTH_LONG).show();

                }
                else {

                    if (Util.getNetworkConnectivityStatus(SubOrdinateHistory.this)) {
                        //selftrack();
                        String date = dateView.getText().toString();
                        Intent intent = new Intent(SubOrdinateHistory.this, LocationListingByUser.class);
                        intent.putExtra("phone_number", value_phone);
                        intent.putExtra("date", date);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SubOrdinateHistory.this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });



    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateView.setText(sdf.format(myCalendar.getTime()));
    }
}
