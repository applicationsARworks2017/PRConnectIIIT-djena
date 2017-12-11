package com.iiit.amaresh.demotrack.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iiit.amaresh.demotrack.Activity.MessageDetailsActivity;
import com.iiit.amaresh.demotrack.Util.Constants;
import com.iiit.amaresh.demotrack.Pojo.MessageAll;
import com.iiit.amaresh.demotrack.Pojo.Util;
import com.iiit.amaresh.demotrack.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by LIPL on 24/01/17.
 */

public class AllMessageAdaper extends BaseAdapter{
    Context context;
    ProgressDialog progress;
    List<MessageAll> allmsg_lis;
    Holder holder;
    String page="answer";
    String server_message;
    Integer server_status;
    String message_status;
    String message_id;
   // RelativeLayout layout_allmsg;

    public AllMessageAdaper(Context allMessageAdaper, List<MessageAll> allmessagelist) {
        this.context=allMessageAdaper;
        this.allmsg_lis=allmessagelist;

    }

    @Override
    public int getCount() {
        if(allmsg_lis.size()<=0){
            return 0;
        }
        else {
            return allmsg_lis.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder {
        private TextView msghead;
        private TextView datetime;
        private TextView msgbdy;
        private RelativeLayout layout_allmsg;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MessageAll msg_pos=allmsg_lis.get(position);

        holder = new Holder();

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.allmessage_adapter_layout, parent, false);

            holder.msghead = (TextView) convertView.findViewById(R.id.head_message);
            holder.datetime = (TextView) convertView.findViewById(R.id.datetime);
            holder.msgbdy = (TextView) convertView.findViewById(R.id.msgbdy);
            holder.layout_allmsg=(RelativeLayout)convertView.findViewById(R.id.rel_allmsg);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.msghead.setTag(position);
        holder.datetime.setTag(position);
        holder.msgbdy.setTag(position);
        holder.layout_allmsg.setTag(position);

        holder.msghead.setText(msg_pos.getSend_by());
        holder.datetime.setText(msg_pos.getCreated_date());
        holder.msgbdy.setText(msg_pos.getMessage());
        message_status=msg_pos.getMsg_status();

        holder.layout_allmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    message_id=msg_pos.getId();
                String sendby=msg_pos.getSend_by();
                String mesbdy=msg_pos.getMessage();
                String date=msg_pos.getCreated_date();
                String status="Y";
                if (Util.getNetworkConnectivityStatus(context)) {
                    SetReadMessage asyncTask = new SetReadMessage();
                    asyncTask.execute(message_id,status);
                    Intent intent=new Intent(context, MessageDetailsActivity.class);
                    intent.putExtra("SENDBY",sendby);
                    intent.putExtra("BODY",mesbdy);
                    intent.putExtra("DATE",date);
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(context,"No Internet",Toast.LENGTH_LONG).show();

                }

            }
        });
        return convertView;
    }
    private class SetReadMessage extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
      //  ProgressDialog progress;

        @Override
        protected void onPreExecute() {
           /* progress = ProgressDialog.show(context, "Please Wait",
                    "Loading MessgeList...", true);*/

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String _msg_id = params[0];
                String _msg_status = params[1];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.ONLINE_URL + Constants.MEG_SEND;
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setAllowUserInteraction(false);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id", _msg_id)
                        .appendQueryParameter("status", _msg_status);

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = conn.getInputStream();
                }
                if (in == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "", data = "";

                while ((data = reader.readLine()) != null) {
                    response += data + "\n";
                }

                Log.i(TAG, "Response : " + response);


                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response.trim());
                     server_status = res.getInt("status");
                    if (server_status==1){
                       server_message="Read the message";
                    }
                    else {
                        server_message="Error Message";
                    }
                }

                return null;


            } catch (Exception exception) {
                server_message="Network Error";
                Log.e(TAG, "LoginAsync : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
          if (server_status!=1){
            Toast.makeText(context,server_message,Toast.LENGTH_LONG).show();
          }

        }
    }
}
