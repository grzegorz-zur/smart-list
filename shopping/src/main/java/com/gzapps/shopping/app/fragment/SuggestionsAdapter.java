/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app.fragment;

import com.gzapps.shopping.core.Product;
import com.gzapps.shopping.core.Shopping;

import java.util.List;

import static com.gzapps.shopping.app.ShoppingApplication.CORRELATION;

public class SuggestionsAdapter extends ProductsAdapter {

    private float limit;

    SuggestionsAdapter(Shopping shopping, float limit,
                       ProductsFragment fragment) {
        super(shopping, fragment);
        this.limit = limit;
    }

    public void limit(float limit) {
        this.limit = limit;
        refresh();
    }

    @Override
    List<Product> list() {
        long time = System.currentTimeMillis();
        return shopping.suggestions(time, CORRELATION, limit);
    }

    @Override
    boolean separate() {
        return false;
    }

    @Override
    boolean tick() {
        return false;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }
}
