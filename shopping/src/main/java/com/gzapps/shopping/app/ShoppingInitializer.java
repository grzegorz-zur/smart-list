/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.gzapps.shopping.core.Shopping;

import java.io.IOException;

public class ShoppingInitializer extends AsyncTaskLoader<Void> {

    private volatile boolean created;
    private volatile boolean restored;
    private volatile boolean error;
    private volatile boolean consumed;

    public ShoppingInitializer(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        ShoppingApplication application = application();
        Shopping shopping = application.shopping();

        if (shopping == null) {
            forceLoad();
        } else {
            deliverResult(null);
        }
    }

    @Override
    public Void loadInBackground() {
        ShoppingApplication application = application();
        Shopping shopping = null;

        try {
            application.load();
            shopping = application.shopping();
            if (shopping != null) {
                Log.i(Logs.TAG, "loaded");
            } else {
                Log.i(Logs.TAG, "nothing loaded");
            }
        } catch (IOException e) {
            error = true;
            if (application.backupAvailable()) {
                try {
                    application.restore();
                    application.load();
                    shopping = application.shopping();
                    Log.i(Logs.TAG, "restored");
                    restored = true;
                } catch (IOException ex) {
                    Log.e(Logs.TAG, "restore error");
                }
            }
        }

        if (shopping == null) {
            application.create();
            Log.i(Logs.TAG, "created");
            created = true;
        }

        return null;
    }

    private ShoppingApplication application() {
        Context context = getContext();
        return (ShoppingApplication) context.getApplicationContext();
    }

    public void consume() {
        consumed = true;
    }

    public boolean isCreated() {
        return created;
    }

    public boolean isRestored() {
        return restored;
    }

    public boolean isError() {
        return error;
    }

    public boolean isConsumed() {
        return consumed;
    }
}
