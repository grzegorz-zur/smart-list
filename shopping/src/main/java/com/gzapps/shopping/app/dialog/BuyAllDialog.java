/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.gzapps.shopping.R;
import com.gzapps.shopping.app.ShoppingActivity;
import com.gzapps.shopping.core.Shopping;

public class BuyAllDialog extends DialogFragment implements
        Dialog.OnClickListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.dialog_buy_all_text);
        builder.setPositiveButton(R.string.dialog_buy_all_ok, this);
        builder.setNegativeButton(R.string.dialog_cancel, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        ShoppingActivity activity = (ShoppingActivity) getActivity();
        Shopping shopping = activity.shopping;
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                long time = System.currentTimeMillis();
                shopping.buyAll(time);
                activity.refresh();
                dialog.dismiss();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
            default:
                dialog.cancel();
                break;
        }
    }
}
