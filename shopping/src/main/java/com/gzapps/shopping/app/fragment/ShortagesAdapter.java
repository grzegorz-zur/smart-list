/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.app.fragment;

import com.gzapps.shopping.app.ShoppingApplication;
import com.gzapps.shopping.core.Product;
import com.gzapps.shopping.core.Shopping;

import java.util.List;

public class ShortagesAdapter extends ProductsAdapter {

    private float limit;

    ShortagesAdapter(Shopping shopping, float limit,
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
        return shopping.shortages(time, ShoppingApplication.SHORTAGES_LOWER_BOUND, limit);
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
