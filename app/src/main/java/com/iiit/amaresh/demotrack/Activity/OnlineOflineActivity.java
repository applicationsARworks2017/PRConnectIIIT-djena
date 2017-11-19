package com.iiit.amaresh.demotrack.Activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.iiit.amaresh.demotrack.Extra.BaseActivity;
import com.iiit.amaresh.demotrack.R;
import com.iiit.amaresh.demotrack.Tabs.GalleryFragment;
import com.iiit.amaresh.demotrack.Tabs.OflineAssetGalleryFragment;
import com.iiit.amaresh.demotrack.Tabs.OnlineAssetGalleryFragment;

import static com.iiit.amaresh.demotrack.R.id.toolbar;

/**
 * Created by RN on 11/5/2017.
 */

public class OnlineOflineActivity extends AppCompatActivity {
    ActionBar.Tab Tab1, Tab2;
    OnlineAssetGalleryFragment onlinegallery=new OnlineAssetGalleryFragment();
    OflineAssetGalleryFragment oflinegallery=new OflineAssetGalleryFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_ofline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("ONLINE GALLERY"));
        tabLayout.addTab(tabLayout.newTab().setText("OFLINE GALLERY"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final GalleryFragment adapter = new GalleryFragment
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}