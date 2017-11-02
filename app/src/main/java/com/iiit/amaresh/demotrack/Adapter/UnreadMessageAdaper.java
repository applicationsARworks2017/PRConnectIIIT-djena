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

import com.iiit.amaresh.demotrack.Pojo.MessageUnread;
import com.iiit.amaresh.demotrack.R;

import java.util.List;

/**
 * Created by LIPL on 25/01/17.
 */
public class UnreadMessageAdaper extends BaseAdapter {
    Context context;
    ProgressDialog progress;
    List<MessageUnread> unreadmsg_lis;
    Holder holder;
    String page="answer";
    String server_message;
    Integer server_status;
    String message_status;
    // RelativeLayout layout_allmsg;

    public UnreadMessageAdaper(Context unreadAdapter, List<MessageUnread> unreadmessagelist) {
        this.context=unreadAdapter;
        this.unreadmsg_lis=unreadmessagelist;

    }

    @Override
    public int getCount() {
        if(unreadmsg_lis.size()<=0){
            return 0;
        }
        else {
            return unreadmsg_lis.size();
        }

        //  return unreadmsg_lis.size();
       //return 0;
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
        final MessageUnread msg_pos=unreadmsg_lis.get(position);

        holder = new UnreadMessageAdaper.Holder();

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.unreadmessage_adapter_layout, parent, false);

            holder.msghead = (TextView) convertView.findViewById(R.id.head_message_ur);
            holder.datetime = (TextView) convertView.findViewById(R.id.datetime_ur);
            holder.msgbdy = (TextView) convertView.findViewById(R.id.msgbdy_ur);
            holder.layout_allmsg=(RelativeLayout)convertView.findViewById(R.id.rel_urmsg);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.msghead.setTag(position);
        holder.datetime.setTag(position);
        holder.msgbdy.setTag(position);

        holder.msghead.setText(msg_pos.geturSend_by());
        holder.datetime.setText(msg_pos.geturCreated_date());
        holder.msgbdy.setText(msg_pos.geturMessage());
        message_status=msg_pos.geturMsg_status();




        return convertView;
    }
}
