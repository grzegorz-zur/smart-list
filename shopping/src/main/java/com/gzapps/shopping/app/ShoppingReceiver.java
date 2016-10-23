/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ShoppingReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(Logs.TAG, "receiver received");

        Intent serviceIntent = new Intent(context, ShoppingService.class);
        context.startService(serviceIntent);

        Log.i(Logs.TAG, "receiver starting service");
    }
}
