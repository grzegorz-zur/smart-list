/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gzapps.shopping.app.fragment.ProductsFragment;
import com.gzapps.shopping.app.fragment.ShoppingListFragment;
import com.gzapps.shopping.app.fragment.ShortagesFragment;
import com.gzapps.shopping.app.fragment.SuggestionsFragment;

class PagerAdapter extends FragmentPagerAdapter {

    PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public ProductsFragment getItem(int position) {
        switch (position) {
            case 0:
                return new ShoppingListFragment();
            case 1:
                return new SuggestionsFragment();
            case 2:
                return new ShortagesFragment();
            case 3:
                return new ProductsFragment();
            default:
                return null;
        }
    }
}
