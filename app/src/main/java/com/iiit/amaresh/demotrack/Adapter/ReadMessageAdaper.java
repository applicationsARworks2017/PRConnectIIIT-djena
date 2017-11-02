package com.iiit.amaresh.demotrack.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iiit.amaresh.demotrack.Pojo.MessageRead;

import java.util.List;

/**
 * Created by LIPL on 25/01/17.
 */
public class ReadMessageAdaper extends BaseAdapter{
    Context context;
    ProgressDialog progress;
    List<MessageRead> rreadmsg_lis;
    Holder holder;
    String page="answer";
    String server_message;
    Integer server_status;
    String message_status;
    public ReadMessageAdaper(Context context, List<MessageRead> readmessagelist) {
        this.context=context;
        this.rreadmsg_lis=readmessagelist;


    }

    @Override
    public int getCount() {
        if(rreadmsg_lis.size()==0){
            return 0;
        }
        else {
            return rreadmsg_lis.size();
        }

        //return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class Holder {
        private TextView msghead;
        private TextView datetime;
        private TextView msgbdy;
        private RelativeLayout layout_allmsg;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MessageRead msg_pos=rreadmsg_lis.get(position);

        holder = new Holder();

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(com.iiit.amaresh.demotrack.R.layout.readmessage_adapter_layout, parent, false);

            holder.msghead = (TextView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.head_message_r);
            holder.datetime = (TextView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.datetime_r);
            holder.msgbdy = (TextView) convertView.findViewById(com.iiit.amaresh.demotrack.R.id.msgbdy_r);
            holder.layout_allmsg=(RelativeLayout)convertView.findViewById(com.iiit.amaresh.demotrack.R.id.rel_rmsg);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.msghead.setTag(position);
        holder.datetime.setTag(position);
        holder.msgbdy.setTag(position);

        holder.msghead.setText(msg_pos.getSend_by());
        holder.datetime.setText(msg_pos.getCreated_date());
        holder.msgbdy.setText(msg_pos.getMessage());
        message_status=msg_pos.getMsg_status();
        return convertView;
    }
}
