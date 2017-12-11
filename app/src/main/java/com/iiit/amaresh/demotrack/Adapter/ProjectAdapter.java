package com.iiit.amaresh.demotrack.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.iiit.amaresh.demotrack.Activity.StaticUserLlist;
import com.iiit.amaresh.demotrack.Pojo.ProjectListing;
import com.iiit.amaresh.demotrack.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mobileapplication on 12/11/17.
 */

public class ProjectAdapter extends BaseAdapter{
    Context context;
    List<ProjectListing> project_list;
    Holder holder;


    public ProjectAdapter(StaticUserLlist staticUserLlist, ArrayList<ProjectListing> projectlist) {
        this.context=staticUserLlist;
        this.project_list=projectlist;
    }

    @Override
    public int getCount() {
        return  project_list.size();
    }

    @Override
    public Object getItem(int position) {
        return project_list.get(position);
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
        final ProjectListing user_pos=project_list.get(position);

        holder = new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.pro_list_item, parent, false);

            holder.User_name=(TextView)convertView.findViewById(R.id.tvUsername);
            convertView.setTag(holder);

        }
        else{
            holder = (Holder) convertView.getTag();
        }
        String firstLetter = user_pos.getTitle().substring(0, 1).toUpperCase();
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(user_pos.getTitle());
        //int color = generator.getRandomColor();
       // TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color); // radius in px
      //  holder.iv_letterview.setImageDrawable(drawable);
        holder.User_name.setTag(position);
        holder.User_name.setText(user_pos.getTitle());

        return convertView;
    }
}
