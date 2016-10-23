/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

class TabListener implements ActionBar.TabListener {

    private final ViewPager pager;

    TabListener(ViewPager pager) {
        this.pager = pager;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
                              FragmentTransaction transaction) {
        int position = tab.getPosition();
        pager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
                                FragmentTransaction transaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
                                FragmentTransaction transaction) {
    }
}
