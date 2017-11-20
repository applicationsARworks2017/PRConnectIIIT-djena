package com.iiit.amaresh.demotrack.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iiit.amaresh.demotrack.Pojo.UserListing;
import com.iiit.amaresh.demotrack.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RN on 11/20/2017.
 */

public class DistrictAdapter extends BaseAdapter {
    Context context;
    List<UserListing> mesglist;
    Holder holder;


    public DistrictAdapter(Context contex, ArrayList<UserListing> message_list) {
        this.context=contex;
        this.mesglist=message_list;

    }

    @Override
    public int getCount() {
        //return 0;
        return mesglist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class Holder {
        private TextView name;
        CheckBox checkBox;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UserListing user_pos=mesglist.get(position);
        //String  user_pos=values.toString();
        holder = new Holder();

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.district_list, parent, false);

            holder.name=(TextView) convertView.findViewById(R.id.tv_list);
            holder.checkBox=(CheckBox) convertView.findViewById(R.id.checkBox1);

            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }
        holder.name.setTag(position);
        holder.checkBox.setTag(holder);
        holder.name.setText(user_pos.getU_emp_name());

        return convertView;
    }
}
