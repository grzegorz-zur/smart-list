/*
 * Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gzapps.shopping.R;
import com.gzapps.shopping.app.Loaders;
import com.gzapps.shopping.app.ShoppingActivity;
import com.gzapps.shopping.core.Product;
import com.gzapps.shopping.core.Shopping;

import java.util.List;

public class RenameDialog extends ProductsDialog {

    private EditText nameEditText;

    public RenameDialog() {
    }

    public RenameDialog(List<Product> products) {
        super(products);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_rename, null);

        nameEditText = (EditText) view.findViewById(R.id.rename_name);
        nameEditText.setOnEditorActionListener(this);

        Button renameCancel = (Button) view.findViewById(R.id.rename_cancel);
        renameCancel.setOnClickListener(this);

        Button renameOk = (Button) view.findViewById(R.id.rename_ok);
        renameOk.setOnClickListener(this);

        return view;
    }

    @Override
    protected int loaderId() {
        return Loaders.RENAME;
    }

    @Override
    protected void init() {
        super.init();
        nameEditText.setText(product.name());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rename_ok:
                confirm();
                break;
            case R.id.rename_cancel:
                next();
                break;
        }
    }

    @Override
    protected void confirm() {
        ShoppingActivity activity = (ShoppingActivity) getActivity();
        Dialog dialog = getDialog();
        String name = nameEditText.getText().toString().trim();

        if (product.name().equals(name)) {
            dialog.dismiss();
            return;
        }

        if (!Shopping.valid(name)) {
            Toast.makeText(activity, R.string.error_not_valid,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (shopping.exists(name)) {
            Toast.makeText(activity, R.string.error_exists, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        product.rename(name);
        activity.refresh();
        activity.search(product);
        next();
    }
}
