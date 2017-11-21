package com.iiit.amaresh.demotrack.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.iiit.amaresh.demotrack.Pojo.DistrictUserList;
import com.iiit.amaresh.demotrack.R;

import java.util.ArrayList;

/**
 * Created by RN on 11/20/2017.
 */

public class DistrictAdapter extends BaseAdapter {
    Context context;
    ArrayList<DistrictUserList> dlist;
    Holder holder;


    public DistrictAdapter(Context contex, ArrayList<DistrictUserList> message_list) {
        this.context=contex;
        this.dlist=message_list;

    }

    @Override
    public int getCount() {
        //return 0;
        return dlist.size();
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
         DistrictUserList user_pos=dlist.get(position);
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
        holder.checkBox.setTag(position);
        holder.name.setText(user_pos.getTitle());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();
                dlist.get(getPosition).setSelected(buttonView.isChecked());
              /* if(context instanceof SelectPreferedLocationReg) {
                   ((SelectPreferedLocationReg) context).onItemClickOfListView(getPosition, buttonView.isChecked());
               }*/
            }
        });

        holder.checkBox.setChecked(dlist.get(position).isSelected());

        return convertView;
    }
}
