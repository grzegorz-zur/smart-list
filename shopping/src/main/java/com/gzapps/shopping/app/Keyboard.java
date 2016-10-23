/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Keyboard {

    public static void show(View view) {
        view.requestFocus();

        Context context = view.getContext();
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager
                .showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hide(View view) {
        view.clearFocus();

        Context context = view.getContext();
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
