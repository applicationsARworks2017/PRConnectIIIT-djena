package com.iiit.amaresh.demotrack.Util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.iiit.amaresh.demotrack.AutosendData.ScheduledJobService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by ISEA on 11-01-2017.
 */

public class Constants {

    //public static final String ONLINE_URL="http://legalsocial.in/web/";
    public static final String ONLINE_URL="http://14.139.198.169/web/";
    public static final String PHONE_VERIFY="getemp.php";
    public static final String USER_REGISTER="setemp.php";
    public static final String USER_ONLINE="setemp.php";
    public static final String USER_LOGIN="login.php";
    public static final String UPLOAD_IMAGE="setfile.php";
    public static final String UPLOAD_POS="setlocation.php";
    public static final String USER_LIST="getemplist.php";
    public static final String LOC_LIST="getlocationlist.php";
    public static final String MEG_SEND="setmessage.php";
    public static final String MSG_LIST="getmessagelist.php";
    public static final String LATEST_LOC_DETAILS="getlatestlocationlist.php";
    public static final String IMAGE_LIST="getfilelist.php";
    public static final String DOWNLOAD_URL="http://14.139.198.169/web/files/";
    public static final String SEND_ALL="setbulkmessage.php";
    public static final String SEND_DISTRICT_W_B_USER="getdistrict.php";
    public static final String BLOCK_USER="getblock.php";
    public static final String PROJECT_LIST="getprojectlist.php";
    public static final String FCM_ID="fcmid";





    public static final String SHAREDPREFERENCE_KEY = "odisha_PR_connect";
    public static final String SHAREDPREFERENCE_KEY_FCM = "firebaseid";
    public static final String SHAREDPREFERENCE_KEY1 = "odisha_PR_connect_IMEI";
    public static final String IMEI = "phone_imei";
    public static final String SP_LOGIN_STATUS = "login_status";
    public static final String SP_USER_ID = "user_id";
    public static final String SP_USER_API_KEY = "user_api_key";
    public static final String SP_USER_DESG = "user_desg";
    public static final String SP_USER_TYPE = "user_type";
    public static final String SP_USER_NAME = "user_name";
    public static final String SP_USER_EMAIL = "user_email";
    public static final String SP_USER_PHONE = "user_phone";
    public static final String SP_STATE_ID = "state_id";
    public static final String SP_BLOCK_ID = "block_id";
    public static final String SP_DISTRICT_ID = "district_id";


    public static String getAddress(Double lat, Double lng, Context context) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        String Address = null;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            Address = address + city ;
            // loc.setText(address+","+city+","+state);
            //et_latlong.setText(sign_lat+","+sign_long);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Address;

    }


    public static Job createJob(FirebaseJobDispatcher dispatcher){
        Job job = dispatcher.newJobBuilder()
                // persist the task across boots
                .setLifetime(Lifetime.FOREVER)
                // Call this service when the criteria are met.
                .setService(ScheduledJobService.class)
                // unique id of the task
                .setTag("LocationJob")
                // We are mentioning that the job is not periodic.
                .setRecurring(true)
                // Run between 30 - 60 seconds from now.
                .setTrigger(Trigger.executionWindow(10,20))
                //Run this job only when the network is avaiable.
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();
        return job;
    }

}

