package com.iiit.amaresh.demotrack.Tabs;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;

import com.iiit.amaresh.demotrack.R;

/**
 * Created by RN on 11/6/2017.
 */

public class GalleryFragment implements ActionBar.TabListener {
    Fragment fragment;

    public GalleryFragment(OflineAssetGalleryFragment oflinegallery) {
        this.fragment = oflinegallery;
    }

    public GalleryFragment(OnlineAssetGalleryFragment onlinegallery) {
        this.fragment = onlinegallery;

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.replace(R.id.fragment_container, fragment);


    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.remove(fragment);

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
