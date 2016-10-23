/*
 * Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.TextView;

import com.gzapps.shopping.R;
import com.gzapps.shopping.core.Product;
import com.gzapps.shopping.core.Shopping;

import java.util.List;

public class ShoppingListAdapter extends ProductsAdapter {

    private boolean dragging;

    private long dragId;

    ShoppingListAdapter(Shopping shopping, ProductsFragment fragment) {
        super(shopping, fragment);
    }

    @Override
    List<Product> list() {
        return shopping.list();
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
    public int getItemViewType(int position) {
        return TYPE_PRODUCT;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public View getProductView(Item item, View view, ViewGroup parent) {
        Product product = item.product;
        ListItemHolder holder;

        if (view == null) {
            LayoutInflater layoutInflater = fragment.getLayoutInflater(null);
            view = layoutInflater.inflate(R.layout.item_list, parent, false);

            holder = new ListItemHolder();
            view.setTag(holder);

            holder.name = (TextView) view.findViewById(R.id.list_item_name);
            holder.quantity =
                    (TextView) view.findViewById(R.id.list_item_quantity);
            holder.unit = (TextView) view.findViewById(R.id.list_item_unit);
        } else {
            holder = (ListItemHolder) view.getTag();
        }

        Checkable checkable = (Checkable) view;
        if (item.checked || dragging && dragId == product.id())
            checkable.setChecked(true);
        else
            checkable.setChecked(false);

        holder.name.setText(product.name());

        if (product.quantitized()) {
            holder.quantity.setVisibility(View.VISIBLE);
            holder.quantity.setTag(product);
            holder.quantity.setText(product.quantity().toPlainString());

            holder.unit.setVisibility(View.VISIBLE);
            holder.unit.setTag(product);
            holder.unit.setText(product.unit());
        } else {
            holder.quantity.setVisibility(View.GONE);
            holder.unit.setVisibility(View.GONE);
        }

        return view;
    }

    public void dragStart(long dragId) {
        dragging = true;
        this.dragId = dragId;
        refresh();
    }

    public void dragFinish() {
        dragging = false;
        refresh();
    }

    public void swap(long dragId, long dropId) {
        if (dragId > Short.MAX_VALUE || dragId < Short.MIN_VALUE)
            return;
        if (dropId > Short.MAX_VALUE || dropId < Short.MIN_VALUE)
            return;

        Product dragProduct = shopping.find((short) dragId);
        if (dragProduct == null)
            return;

        Product dropProduct = shopping.find((short) dropId);
        if (dropProduct == null)
            return;

        shopping.swap(dragProduct, dropProduct);

        refresh();
    }

    static class ListItemHolder {

        TextView name;
        TextView quantity;
        TextView unit;
    }
}
