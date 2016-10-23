/*
 * Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app.fragment;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.gzapps.shopping.app.Loaders;
import com.gzapps.shopping.app.ShoppingActivity;
import com.gzapps.shopping.core.Product;
import com.gzapps.shopping.core.Shopping;

public class ShoppingListFragment extends ProductsFragment implements View.OnTouchListener {

    private static final int DRAG_AREA = 48;

    private boolean dragging;

    private long dragId;

    public ShoppingListFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView listView = getListView();
        listView.setOnTouchListener(this);
    }

    @Override
    protected int loaderId() {
        return Loaders.LIST;
    }

    @Override
    protected ProductsAdapter createAdapter(Shopping shopping) {
        return new ShoppingListAdapter(shopping, this);
    }

    @Override
    protected void register(ShoppingActivity activity) {
        activity.list = this;
    }

    @Override
    protected void unregister(ShoppingActivity activity) {
        activity.list = null;
    }

    @Override
    void productClick(Product product) {
        long time = System.currentTimeMillis();
        product.buy(time);
        refreshActivity();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        ListView listView = getListView();
        ShoppingListAdapter listAdapter = (ShoppingListAdapter) getListAdapter();

        if (action == MotionEvent.ACTION_DOWN && x < DRAG_AREA)
            dragging = true;

        if (!dragging)
            return false;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                dragId = listView.pointToRowId(x, y);
                listAdapter.dragStart(dragId);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                dragging = false;
            case MotionEvent.ACTION_MOVE:
                long dropId = listView.pointToRowId(x, y);
                listAdapter.swap(dragId, dropId);
                if (!dragging)
                    listAdapter.dragFinish();
                break;
        }

        return true;
    }
}
