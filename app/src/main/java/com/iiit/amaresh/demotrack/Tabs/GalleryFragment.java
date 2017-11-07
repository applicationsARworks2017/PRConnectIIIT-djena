package com.iiit.amaresh.demotrack.Tabs;

import android.app.ActionBar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.iiit.amaresh.demotrack.R;

/**
 * Created by RN on 11/6/2017.
 */

public class GalleryFragment extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public GalleryFragment(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                OnlineAssetGalleryFragment tab1 = new OnlineAssetGalleryFragment();
                return tab1;
            case 1:
                OflineAssetGalleryFragment tab2 = new OflineAssetGalleryFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}