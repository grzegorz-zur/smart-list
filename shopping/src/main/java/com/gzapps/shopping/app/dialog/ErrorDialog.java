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

abstract class ErrorDialog extends DialogFragment implements
        Dialog.OnClickListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(getMessageId());
        builder.setNeutralButton(R.string.dialog_close, this);
        return builder.create();
    }

    abstract int getMessageId();

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
}
