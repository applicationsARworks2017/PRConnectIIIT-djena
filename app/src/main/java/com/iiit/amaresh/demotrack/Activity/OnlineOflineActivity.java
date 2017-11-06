package com.iiit.amaresh.demotrack.Activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;

import com.iiit.amaresh.demotrack.Extra.BaseActivity;
import com.iiit.amaresh.demotrack.R;
import com.iiit.amaresh.demotrack.Tabs.GalleryFragment;
import com.iiit.amaresh.demotrack.Tabs.OflineAssetGalleryFragment;
import com.iiit.amaresh.demotrack.Tabs.OnlineAssetGalleryFragment;

/**
 * Created by RN on 11/5/2017.
 */

public class OnlineOflineActivity extends BaseActivity {
    ActionBar.Tab Tab1, Tab2;
    OnlineAssetGalleryFragment onlinegallery=new OnlineAssetGalleryFragment();
    OflineAssetGalleryFragment oflinegallery=new OflineAssetGalleryFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_ofline);
        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

            toolbar.setTitle(getResources().getString(R.string.Galary_ASSET));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(OnlineOflineActivity.this);
                }
            });

        }
        ActionBar actionBar = getActionBar();

        // Hide Actionbar Icon
//        actionBar.setDisplayShowHomeEnabled(false);

        // Hide Actionbar Title
        //actionBar.setDisplayShowTitleEnabled(false);

        // Create Actionbar Tabs
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set Tab Icon and Titles
        Tab1 = actionBar.newTab().setText("ONLINE GALLERY");
        Tab2 = actionBar.newTab().setText("OFLINE GALLERY");

        // Set Tab Listeners
        Tab1.setTabListener(new GalleryFragment(onlinegallery));
        Tab2.setTabListener(new GalleryFragment(oflinegallery));

        // Add tabs to actionbar
        actionBar.addTab(Tab1);
        actionBar.addTab(Tab2);
    }
}
