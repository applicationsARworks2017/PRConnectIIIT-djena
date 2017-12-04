package com.iiit.amaresh.demotrack.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.iiit.amaresh.demotrack.Activity.Type_message;
import com.iiit.amaresh.demotrack.Pojo.UserListing;
import com.iiit.amaresh.demotrack.R;

import java.util.List;

/**
 * Created by LIPL on 17/01/17.
 */
public class UserListingAdapter extends BaseAdapter {
    Context context;
    ProgressDialog progress;
    List<UserListing> user_lis;
    Holder holder;
    Holder holder1;
    String page;
    String server_message;
    Integer server_status;
    public UserListingAdapter(Context userListActivity, List<UserListing> data, String pagename) {
        this.context=userListActivity;
        this.user_lis=data;
        this.page=pagename;
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
        private TextView User_email;
        private TextView User_designation;
        private TextView User_dist;
        private TextView User_block;
        private TextView User_state;
        private ImageView iv_letterview;

        private ImageView im_message;
        private ImageView im_location;
        private ImageView im_on_status,im_of_status;
        RelativeLayout user_detaill_list;
        RelativeLayout user_list;
         int count=0;
        //private ImageView imgSend;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UserListing user_pos=user_lis.get(position);

        holder = new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item2, parent, false);

            holder.User_name=(TextView)convertView.findViewById(R.id.tvUsername);
            holder.User_phone=(TextView)convertView.findViewById(R.id.tvUserPhone);
            holder.User_email=(TextView)convertView.findViewById(R.id.tvemail);
            holder.User_designation=(TextView)convertView.findViewById(R.id.tvdesig);
            holder.User_dist=(TextView)convertView.findViewById(R.id.tvDist);
            holder.User_block=(TextView)convertView.findViewById(R.id.tvblock);
            holder.User_state=(TextView)convertView.findViewById(R.id.tvstate);
            holder.im_message=(ImageView)convertView.findViewById(R.id.msgicon);
            holder.im_on_status=(ImageView)convertView.findViewById(R.id.statusonline_image);
            holder.im_of_status=(ImageView)convertView.findViewById(R.id.statusoffline_image);
            holder.iv_letterview=(ImageView)convertView.findViewById(R.id.iv_letterView);
            holder.user_detaill_list=(RelativeLayout)convertView.findViewById(R.id.user_detaill_list);
            holder.user_list=(RelativeLayout)convertView.findViewById(R.id.user_list);
         //   holder.im_location=(ImageView)convertView.findViewById(R.id.gpsicon);
            convertView.setTag(holder);

        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.User_name.setTag(position);
        holder.User_phone.setTag(position);
        holder.User_email.setTag(position);
        holder.User_designation.setTag(position);
        holder.User_dist.setTag(position);
        holder.User_block.setTag(position);
        holder.User_state.setTag(position);
        holder.im_message.setTag(holder);
        holder.im_on_status.setTag(position);
        holder.im_of_status.setTag(position);
        holder.user_detaill_list.setTag(holder);
        holder.iv_letterview.setTag(holder);
        holder.user_list.setTag(holder);
       // holder.im_location.setTag(position);
        String firstLetter = user_pos.getU_emp_name().substring(0, 1).toUpperCase();
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(user_pos.getU_emp_name());
        //int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color); // radius in px
        holder.iv_letterview.setImageDrawable(drawable);
        holder.User_name.setText(user_pos.getU_emp_name());
        holder.User_phone.setText("Mob: "+user_pos.getU_emp_phone());
        holder.User_email.setText("Mail: "+user_pos.getU_emp_mail());
        holder.User_designation.setText("Designation: "+user_pos.getU_emp_desig());
        holder.User_dist.setText("District: "+user_pos.getU_emp_dist());
        holder.User_block.setText("Block: "+user_pos.getU_emp_block());
        holder.User_state.setText("State: "+user_pos.getU_emp_state());
        String user_status=user_pos.getU_user_status();
        if(user_status.contentEquals("Online")){
            holder.im_of_status.setVisibility(View.INVISIBLE);
            holder.im_on_status.setVisibility(View.VISIBLE);
        }
        else{
            holder.im_of_status.setVisibility(View.VISIBLE);
            holder.im_on_status.setVisibility(View.INVISIBLE);
        }
        holder.iv_letterview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page.contentEquals("home")) {
                    holder1 = (Holder) v.getTag();
                    holder.count++;
                    if (holder.count == 1) {
                        holder1.user_detaill_list.setVisibility(View.VISIBLE);
                    }
                    //double click
                    if (holder.count == 2) {
                        holder1.user_detaill_list.setVisibility(View.GONE);
                        holder.count = 0;
                        //double function()
                        // holder.count==0;
                    }
                }
                }
            });


        holder.im_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=user_pos.getU_emp_name();
                String id=user_pos.getU_id();

                Intent intent=new Intent(context, Type_message.class);
                intent.putExtra("name",username);
                intent.putExtra("id",id);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
