package com.iiit.amaresh.demotrack.Tabs;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iiit.amaresh.demotrack.R;

/**
 * Created by RN on 11/5/2017.
 */

public class OflineAssetGalleryFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmenttab1, container, false);
        return rootView;
    }
}
