/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.app.action;

import android.support.v7.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gzapps.shopping.R;
import com.gzapps.shopping.app.Keyboard;
import com.gzapps.shopping.app.ShoppingActivity;
import com.gzapps.shopping.core.Product;
import com.gzapps.shopping.core.Shopping;

import static com.gzapps.shopping.app.ShoppingApplication.SIZE;

public class SearchCreateAction implements ActionMode.Callback, TextView.OnEditorActionListener {

    final Shopping shopping;
    final ShoppingActivity activity;
    EditText nameEditText;

    public SearchCreateAction(Shopping shopping, ShoppingActivity activity) {
        this.shopping = shopping;
        this.activity = activity;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = mode.getMenuInflater();
        menuInflater.inflate(R.menu.action_create, menu);
        menuInflater.inflate(R.menu.action_search, menu);

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        nameEditText = (EditText) layoutInflater
                .inflate(R.layout.action_search_create, null);
        nameEditText.setOnEditorActionListener(this);
        nameEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mode.setCustomView(nameEditText);

        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_create:
                create();
                return true;
            case R.id.menu_action_search:
                search();
                return true;
            default:
                return true;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId,
                                  KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_DONE:
                create();
                return true;
            case EditorInfo.IME_ACTION_SEARCH:
                search();
                return true;
        }
        return false;
    }

    private void create() {
        String name = nameEditText.getText().toString().trim();

        if (shopping.productsCount() >= SIZE) {
            String message = activity.getString(R.string.error_exhausted, SIZE);
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Shopping.valid(name)) {
            String message = activity.getString(R.string.error_not_valid);
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            return;
        }

        Product product;
        String message;
        if (shopping.exists(name)) {
            product = shopping.find(name);
            if (product.enlisted()) {
                message = activity.getString(R.string.info_on_list);
            } else {
                product.enlist();
                message = activity.getString(R.string.info_enlisted);
            }
        } else {
            product = shopping.create(name);
            product.enlist();
            message = activity.getString(R.string.info_created_enlisted);
        }
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        activity.refresh();
        activity.search(product);
        nameEditText.setText("");
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        Keyboard.show(nameEditText);
        return true;
    }

    void search() {
        String text = nameEditText.getText().toString().trim();
        if (text.length() == 0) {
            return;
        }
        if (!activity.search(text)) {
            String message = activity.getString(R.string.toast_not_found, text);
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        Keyboard.hide(nameEditText);
    }
}
