/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.gzapps.shopping.core.Shopping;

public class ShoppingLoader extends AsyncTaskLoader<Shopping> {

    public ShoppingLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        ShoppingApplication application = application();
        Shopping shopping = application.shopping();

        if (shopping == null) {
            forceLoad();
        } else {
            deliverResult(shopping);
        }
    }

    @Override
    public Shopping loadInBackground() {
        ShoppingApplication application =
                (ShoppingApplication) getContext().getApplicationContext();

        Shopping shopping = application.shopping();
        while (shopping == null) {
            Thread.yield();
            shopping = application.shopping();
        }

        return shopping;
    }

    private ShoppingApplication application() {
        Context context = getContext();
        return (ShoppingApplication) context.getApplicationContext();
    }
}
