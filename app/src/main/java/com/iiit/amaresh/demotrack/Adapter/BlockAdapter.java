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

import com.iiit.amaresh.demotrack.Pojo.BlockList;
import com.iiit.amaresh.demotrack.R;

import java.util.ArrayList;

/**
 * Created by mobileapplication on 11/21/17.
 */

public class BlockAdapter  extends BaseAdapter {
    Context context;
    ArrayList<BlockList> dlist;
    Holder holder;


    public BlockAdapter(Context contex, ArrayList<BlockList> message_list) {
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
        BlockList user_pos=dlist.get(position);
        holder = new Holder();

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.block_list, parent, false);

            holder.name=(TextView) convertView.findViewById(R.id.tv_list);
            holder.checkBox=(CheckBox) convertView.findViewById(R.id.checkBox1);

            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }
        holder.name.setTag(position);
        holder.checkBox.setTag(position);
        holder.name.setText(user_pos.getB_title());

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
