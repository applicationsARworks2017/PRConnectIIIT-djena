package com.iiit.amaresh.demotrack.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.iiit.amaresh.demotrack.Activity.AlluserList;
import com.iiit.amaresh.demotrack.Pojo.UserListing;
import com.iiit.amaresh.demotrack.R;

import java.util.List;

/**
 * Created by mobileapplication on 11/3/17.
 */

public class AllUserAdapter extends BaseAdapter {
    Context context;
    ProgressDialog progress;
    List<UserListing> user_lis;
    Holder holder;
    String page="answer";
    String server_message;
    Integer server_status;

    public AllUserAdapter(AlluserList alluserList, List<UserListing> userlist){
        this.context=alluserList;
        this.user_lis=userlist;
    }

    @Override
    public int getCount() {
        return  user_lis.size();
    }

    @Override
    public Object getItem(int position) {
        return user_lis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder{
        private TextView User_name;
        private TextView User_phone;
        private ImageView iv_letterview;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UserListing user_pos=user_lis.get(position);

        holder = new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.all_list_item, parent, false);

            holder.User_name=(TextView)convertView.findViewById(R.id.tvUsername);
            holder.User_phone=(TextView)convertView.findViewById(R.id.tvUserPhone);
            holder.iv_letterview=(ImageView)convertView.findViewById(R.id.iv_letterView);
            convertView.setTag(holder);

        }
        else{
            holder = (Holder) convertView.getTag();
        }
        String firstLetter = user_pos.getU_emp_name().substring(0, 1).toUpperCase();
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(user_pos.getU_emp_name());
        //int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color); // radius in px
        holder.iv_letterview.setImageDrawable(drawable);
        holder.User_name.setTag(position);
        holder.User_phone.setTag(position);
        holder.User_name.setText("Name: "+user_pos.getU_emp_name());
        holder.User_phone.setText("Mob: "+user_pos.getU_emp_phone()+","+" "+user_pos.getU_emp_desig());
        return convertView;
    }
}
