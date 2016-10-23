/*
 * Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.gzapps.shopping.app.ShoppingActivity;
import com.gzapps.shopping.app.ShoppingLoader;
import com.gzapps.shopping.core.Product;
import com.gzapps.shopping.core.Shopping;

import java.util.ArrayList;
import java.util.List;

public abstract class ProductsDialog extends DialogFragment implements
        TextView.OnEditorActionListener, View.OnClickListener,
        LoaderManager.LoaderCallbacks<Shopping> {

    private static final String IDS = "ids";
    Shopping shopping;
    List<Product> products;
    Product product;

    protected ProductsDialog() {
    }

    protected ProductsDialog(List<Product> products) {
        Bundle arguments = new Bundle();
        short[] ids = new short[products.size()];
        int i = 0;
        for (Product product : products) {
            ids[i] = product.id();
            i += 1;
        }
        arguments.putShortArray(IDS, ids);
        setArguments(arguments);
    }

    public void deserialize() {
        Bundle arguments = getArguments();
        short[] ids = arguments.getShortArray(IDS);
        products = new ArrayList<Product>();
        for (short id : ids) {
            Product product = shopping.find(id);
            products.add(product);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ShoppingActivity activity = (ShoppingActivity) getActivity();
        LoaderManager loaderManager = activity.getSupportLoaderManager();
        loaderManager.initLoader(loaderId(), null, this);
    }

    protected abstract int loaderId();

    @Override
    public ShoppingLoader onCreateLoader(int i, Bundle bundle) {
        Activity activity = getActivity();
        return new ShoppingLoader(activity);
    }

    @Override
    public void onLoadFinished(Loader<Shopping> shoppingLoader,
                               Shopping shopping) {
        this.shopping = shopping;
        deserialize();
        if (products.isEmpty()) {
            dismiss();
        } else {
            init();
        }
    }

    @Override
    public void onLoaderReset(Loader<Shopping> shoppingLoader) {
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId,
                                  KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            confirm();
            return true;
        }
        return false;
    }

    protected void init() {
        product = products.remove(0);
        Dialog dialog = getDialog();
        dialog.setTitle(product.name());
    }

    protected abstract void confirm();

    protected void next() {
        if (products.isEmpty()) {
            dismiss();
        } else {
            init();
        }
    }
}
