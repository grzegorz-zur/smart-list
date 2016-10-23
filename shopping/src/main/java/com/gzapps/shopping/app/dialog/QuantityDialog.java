/*
 * Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app.dialog;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.gzapps.shopping.R;
import com.gzapps.shopping.app.Loaders;
import com.gzapps.shopping.app.PositiveStepValue;
import com.gzapps.shopping.app.ShoppingActivity;
import com.gzapps.shopping.app.StepValue;
import com.gzapps.shopping.core.Product;

import java.math.BigDecimal;
import java.util.List;

public class QuantityDialog extends ProductsDialog implements TextWatcher {

    private StepValue quantity;
    private int quantityLength;
    private boolean buttonChange;
    private EditText quantityEditText;
    private EditText unitEditText;

    public QuantityDialog() {
    }

    public QuantityDialog(List<Product> products) {
        super(products);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_quantity, null);

        ImageButton quantityIncrease =
                (ImageButton) view.findViewById(R.id.quantity_increase);
        quantityIncrease.setOnClickListener(this);

        ImageButton quantityDecrease =
                (ImageButton) view.findViewById(R.id.quantity_decrease);
        quantityDecrease.setOnClickListener(this);

        Button quantityCancel =
                (Button) view.findViewById(R.id.quantity_cancel);
        quantityCancel.setOnClickListener(this);

        Button quantityOk = (Button) view.findViewById(R.id.quantity_set);
        quantityOk.setOnClickListener(this);

        quantityEditText = (EditText) view.findViewById(R.id.quantity_quantity);
        unitEditText = (EditText) view.findViewById(R.id.quantity_unit);

        return view;
    }

    @Override
    protected int loaderId() {
        return Loaders.QUANTITY;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        Resources resources = activity.getResources();
        quantityLength = resources.getInteger(R.integer.length_quantity);
    }

    @Override
    protected void init() {
        super.init();

        quantity = new PositiveStepValue();
        BigDecimal quantityValue = product.quantity();
        if (quantityValue != null) {
            quantity.changeValue(quantityValue);
            quantityEditText.setText(quantity.toString());
        }

        String unitValue = product.unit();
        if (unitValue != null) {
            unitEditText.setText(unitValue);
        }

        quantityEditText.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.quantity_increase:
                increase();
                break;
            case R.id.quantity_decrease:
                decrease();
                break;
            case R.id.quantity_set:
                confirm();
                break;
            case R.id.quantity_cancel:
                next();
                break;
        }
    }

    void increase() {
        buttonChange = true;

        if (quantity == null) {
            return;
        }
        quantity.increase();

        String quantityText = quantity.toString();
        if (quantityText.length() > quantityLength) {
            quantity.decrease();
            return;
        }

        quantityEditText.setText(quantityText);
    }

    void decrease() {
        buttonChange = true;

        if (quantity == null) {
            return;
        }
        quantity.decrease();

        String quantityText = quantity.toString();
        if (quantityText.length() > quantityLength) {
            quantity.increase();
            return;
        }

        quantityEditText.setText(quantityText);
    }

    @Override
    protected void confirm() {
        String unit = unitEditText.getText().toString();
        product.quantitize(quantity.value, unit);

        ShoppingActivity activity = (ShoppingActivity) getActivity();
        activity.refresh();

        next();
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before,
                              int count) {
    }

    @Override
    public void beforeTextChanged(CharSequence text, int start, int count,
                                  int after) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        quantityEditText.setError(null);

        if (buttonChange) {
            buttonChange = false;
            return;
        }

        String text = editable.toString();
        if (text.trim().length() == 0) {
            quantity.changeValue(BigDecimal.ZERO);
        } else {
            try {
                quantity.changeValue(text);
            } catch (NumberFormatException e) {
                Activity activity = getActivity();
                CharSequence message =
                        activity.getText(R.string.error_invalid_number);
                quantityEditText.setError(message);
            }
        }
    }
}
