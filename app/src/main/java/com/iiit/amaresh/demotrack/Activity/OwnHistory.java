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
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Extra.BaseActivity;
import com.iiit.amaresh.demotrack.Util.Constants;
import com.iiit.amaresh.demotrack.Pojo.Util;
import com.iiit.amaresh.demotrack.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OwnHistory extends BaseActivity {

    private DatePicker datePicker;
    private Calendar calendar;
    private EditText dateView;
    Button cntne,set;
    String phone_number,name;
    int user_id;
    ProgressDialog progressDialog;
    int server_status;
    String server_message;
     Calendar myCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.iiit.amaresh.demotrack.R.layout.activity_own_history);

        if(null!=toolbar){
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setTitle(getResources().getString(com.iiit.amaresh.demotrack.R.string.Check_history));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(OwnHistory.this);
                }
            });
        }
        phone_number = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_PHONE, null);
        name = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SP_USER_NAME, null);
        user_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.SP_USER_ID, 0);

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


        dateView=(EditText)findViewById(com.iiit.amaresh.demotrack.R.id.date);
        set=(Button)findViewById(com.iiit.amaresh.demotrack.R.id.btndset);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateView.setText("");
                new DatePickerDialog(OwnHistory.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        cntne=(Button)findViewById(com.iiit.amaresh.demotrack.R.id.cont);

        cntne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.getNetworkConnectivityStatus(OwnHistory.this)) {
                    //selftrack();
                    String date=dateView.getText().toString();
                    Intent intent=new Intent(OwnHistory.this,LocationListingByUser.class);
                    intent.putExtra("phone_number",phone_number);
                    intent.putExtra("date",date);
                    startActivity(intent);
                }else {
                    Toast.makeText(OwnHistory.this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
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
