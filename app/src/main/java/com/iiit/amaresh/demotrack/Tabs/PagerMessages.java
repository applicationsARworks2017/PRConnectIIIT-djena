package com.iiit.amaresh.demotrack.Tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by LIPL on 24/01/17.
 */

public class PagerMessages extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public PagerMessages(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                AllMessage sms = new AllMessage();
                return sms;
            case 1:
                ReadMessages rsms = new ReadMessages();
                return rsms;
            case 2:
                UnreadMessages ursms = new UnreadMessages();
                return ursms;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
