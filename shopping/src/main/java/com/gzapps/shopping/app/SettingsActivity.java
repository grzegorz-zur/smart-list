/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.gzapps.shopping.R;

public class SettingsActivity extends PreferenceActivity {

    public SettingsActivity() {
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

}
