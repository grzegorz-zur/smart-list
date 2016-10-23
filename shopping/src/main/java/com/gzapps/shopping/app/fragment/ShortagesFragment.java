/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.gzapps.shopping.R;
import com.gzapps.shopping.app.Loaders;
import com.gzapps.shopping.app.ShoppingActivity;
import com.gzapps.shopping.core.Product;
import com.gzapps.shopping.core.Shopping;

public class ShortagesFragment extends ProductsFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final int LIMIT_DEFAULT_ID = R.string
            .shortages_limit_default;
    private static final String LIMIT_PREFERENCE = "shortages_limit";
    private ShortagesAdapter shortagesAdapter;

    public ShortagesFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Activity activity = getActivity();
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(activity);
        preferences.registerOnSharedPreferenceChangeListener(this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        Activity activity = getActivity();
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(activity);
        preferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    protected int loaderId() {
        return Loaders.SHORTAGES;
    }

    @Override
    protected ProductsAdapter createAdapter(Shopping shopping) {
        float limit = limit(LIMIT_PREFERENCE, LIMIT_DEFAULT_ID);
        return shortagesAdapter = new ShortagesAdapter(shopping, limit, this);
    }

    @Override
    protected void register(ShoppingActivity activity) {
        activity.shortages = this;
    }

    @Override
    protected void unregister(ShoppingActivity activity) {
        activity.shortages = null;
    }

    @Override
    void productClick(Product product) {
        product.enlist();
        refreshActivity();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences,
                                          String key) {
        if (shortagesAdapter != null && LIMIT_PREFERENCE.equals(key)) {
            float limit = limit(LIMIT_PREFERENCE, LIMIT_DEFAULT_ID);
            shortagesAdapter.limit(limit);
        }
    }
}
