/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzapps.shopping.R;
import com.gzapps.shopping.app.Logs;

public class AboutDialog extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        dialog.requestWindowFeature(STYLE_NO_TITLE);

        View view = inflater.inflate(R.layout.dialog_about, container, false);
        TextView versionTextView =
                (TextView) view.findViewById(R.id.about_version);
        try {
            Activity activity = getActivity();
            PackageManager packageManager = activity.getPackageManager();
            String packageName = activity.getPackageName();
            PackageInfo packageInfo =
                    packageManager.getPackageInfo(packageName, 0);
            String versionName = packageInfo.versionName;
            String versionString =
                    getString(R.string.dialog_about_version, versionName);
            versionTextView.setText(versionString);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(Logs.TAG, "activity cannot get package name", e);
        }
        TextView librariesTextView =
                (TextView) view.findViewById(R.id.about_libraries);
        librariesTextView.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }
}
