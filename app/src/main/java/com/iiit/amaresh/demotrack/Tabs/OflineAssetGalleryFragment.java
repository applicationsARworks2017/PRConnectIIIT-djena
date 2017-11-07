package com.iiit.amaresh.demotrack.Tabs;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.android.volley.toolbox.NetworkImageView;
import com.iiit.amaresh.demotrack.Adapter.OflineAdaper;
import com.iiit.amaresh.demotrack.Database.DBHelper;
import com.iiit.amaresh.demotrack.Pojo.Oflinedata;
import com.iiit.amaresh.demotrack.Pojo.Util;
import com.iiit.amaresh.demotrack.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RN on 11/5/2017.
 */

public class OflineAssetGalleryFragment extends Fragment {

    ListView listView;
    ArrayList<Oflinedata> oflineList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmenttab1, container, false);
        listView = (ListView)rootView.findViewById(R.id.listView_ofline);
        oflineList=new ArrayList<>();
            getalloflinedata();
        return rootView;
    }

    private void getalloflinedata() {
        DBHelper db=new DBHelper(getActivity());
        List<Oflinedata> list=db.getOflinedata();
        db.close();
        if (list != null
                && list.size() > 0) {
            oflineList.clear();
            oflineList.addAll(list);
        }
        OflineAdaper adapter=new OflineAdaper(getActivity(),oflineList);
        listView.setAdapter(adapter);
    }

}
