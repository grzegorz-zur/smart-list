/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.app;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import com.gzapps.shopping.R;
import com.gzapps.shopping.core.Analysis;
import com.gzapps.shopping.core.Shopping;
import com.gzapps.shopping.core.StopCallback;

import java.io.IOException;

public class ShoppingService extends IntentService {

    private static final String NAME = "ShoppingService";
    private static final int PURGE_DEFAULT_ID = R.bool.products_purge_default;
    private static final String PURGE_PREFERENCE = "products_purge";

    public ShoppingService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(Logs.TAG, "service start");
        ShoppingApplication application =
                (ShoppingApplication) getApplication();
        application.acquire();
        try {
            long time = System.currentTimeMillis();
            Shopping shopping = application.shopping();
            if (shopping == null) {
                application.load();
            }
            shopping = application.shopping();
            if (shopping != null) {
                StopCallback callback = new ReleaseCallback(application);
                boolean purge = shouldPurge();
                Analysis analysis = new Analysis(shopping, time, purge, callback);
                analysis.analyze();
                application.save();
            }
        } catch (IOException e) {
            Log.e(Logs.TAG, "failed analysis", e);
        } finally {
            application.release();
        }
        Log.i(Logs.TAG, "service end");
    }

    private boolean shouldPurge() {
        Resources resources = getResources();
        boolean purgeDefault = resources.getBoolean(PURGE_DEFAULT_ID);
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean(PURGE_PREFERENCE, purgeDefault);
    }
}
