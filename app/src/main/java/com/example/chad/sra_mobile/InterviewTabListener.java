package com.example.chad.sra_mobile;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * Created by jordanreed on 10/7/14.
 */
public class InterviewTabListener implements ActionBar.TabListener {
    Fragment fragment;

    public InterviewTabListener(Fragment fragment) {
        this.fragment = fragment;
    }

    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        ft.replace(R.id.fragment_container, fragment);
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        ft.remove(fragment);
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // nothing done here
    }
}