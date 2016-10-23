/*
 * Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app.fragment;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gzapps.shopping.R;
import com.gzapps.shopping.app.Loaders;
import com.gzapps.shopping.app.Logs;
import com.gzapps.shopping.app.ShoppingActivity;
import com.gzapps.shopping.app.ShoppingLoader;
import com.gzapps.shopping.app.dialog.QuantityDialog;
import com.gzapps.shopping.app.dialog.RenameDialog;
import com.gzapps.shopping.core.Product;
import com.gzapps.shopping.core.Shopping;

import java.util.List;

public class ProductsFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Shopping>,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
        ActionMode.Callback {

    ProductsAdapter adapter;
    ActionMode mode;

    public ProductsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView listView = getListView();
        listView.setItemsCanFocus(false);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        FragmentActivity activity = getActivity();
        LoaderManager loaderManager = activity.getSupportLoaderManager();
        loaderManager.initLoader(loaderId(), null, this);
    }

    @Override
    public void onDestroy() {
        ShoppingActivity activity = (ShoppingActivity) getActivity();
        unregister(activity);
        setListAdapter(null);
        adapter = null;
        super.onDestroy();
    }

    @Override
    public Loader<Shopping> onCreateLoader(int i, Bundle bundle) {
        FragmentActivity activity = getActivity();
        return new ShoppingLoader(activity);
    }

    @Override
    public void onLoadFinished(Loader<Shopping> shoppingLoader,
                               Shopping shopping) {
        adapter = createAdapter(shopping);
        adapter.init();
        setListAdapter(adapter);

        ShoppingActivity activity = (ShoppingActivity) getActivity();
        register(activity);
    }

    @Override
    public void onLoaderReset(Loader<Shopping> shoppingLoader) {
    }

    protected int loaderId() {
        return Loaders.PRODUCTS;
    }

    protected ProductsAdapter createAdapter(Shopping shopping) {
        return new ProductsAdapter(shopping, this);
    }

    protected void register(ShoppingActivity activity) {
        activity.products = this;
    }

    protected void unregister(ShoppingActivity activity) {
        activity.products = null;
    }

    public boolean search(String text) {
        int position = adapter.search(text);
        if (position < 0) {
            return false;
        }
        highlight(position);
        return true;
    }

    public void search(Product product) {
        int position = adapter.search(product);
        if (position < 0) {
            return;
        }
        highlight(position);
    }

    private void highlight(int position) {
        adapter.highlight(position);
        ListView listView = getListView();
        listView.invalidateViews();
        listView.setSelection(position);
    }

    public void refresh() {
        adapter.refresh();
        refreshSelection();
    }

    void refreshActivity() {
        ShoppingActivity activity = (ShoppingActivity) getActivity();
        activity.refresh();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view,
                            int position,
                            long id) {
        if (mode != null) {
            toggle(position);
        } else {
            Item item = adapter.getItem(position);
            Product product = item.product;
            if (product != null) {
                productClick(product);
            }
        }
    }

    void productClick(Product product) {
        if (product.enlisted()) {
            product.delist();
        } else {
            product.enlist();
        }
        refreshActivity();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        if (mode == null) {
            ActionBarActivity activity = (ActionBarActivity) getActivity();
            mode = activity.startSupportActionMode(this);
        }
        toggle(position);
        return true;
    }

    private void toggle(int position) {
        adapter.toggle(position);
        refreshSelection();
    }

    public void cancel() {
        if (mode != null) {
            mode.finish();
        }
    }

    private void quantitize(List<Product> products) {
        if (!products.isEmpty()) {
            FragmentManager fragmentManager = getFragmentManager();
            QuantityDialog dialog = new QuantityDialog(products);
            dialog.show(fragmentManager, null);
        }
    }

    private void dequantitize(List<Product> products) {
        for (Product product : products) {
            product.dequantitize();
        }
        refreshActivity();
    }

    private void enlist(List<Product> products) {
        for (Product product : products) {
            product.enlist();
        }
        refreshActivity();
    }

    private void delist(List<Product> products) {
        for (Product product : products) {
            product.delist();
        }
        refreshActivity();
    }

    private void buy(List<Product> products) {
        long time = System.currentTimeMillis();
        for (Product product : products) {
            product.buy(time);
        }
        refreshActivity();
    }

    private void rename(List<Product> products) {
        if (!products.isEmpty()) {
            FragmentManager fragmentManager = getFragmentManager();
            RenameDialog dialog = new RenameDialog(products);
            dialog.show(fragmentManager, null);
        }
    }

    private void remove(List<Product> products) {
        for (Product product : products) {
            product.remove();
        }
        refreshActivity();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = mode.getMenuInflater();
        menuInflater.inflate(R.menu.products, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        List<Product> products = adapter.checked();

        switch (item.getItemId()) {
            case R.id.menu_quantitize:
                quantitize(products);
                break;
            case R.id.menu_dequantitize:
                dequantitize(products);
                break;
            case R.id.menu_enlist:
                enlist(products);
                break;
            case R.id.menu_delist:
                delist(products);
                break;
            case R.id.menu_buy:
                buy(products);
                break;
            case R.id.menu_rename:
                rename(products);
                break;
            case R.id.menu_remove:
                remove(products);
                break;
        }

        mode.finish();

        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        if (adapter != null) {
            adapter.clearChecked();
        }
        this.mode = null;
    }

    private void refreshSelection() {
        if (mode == null) {
            return;
        }

        int count = adapter.checkedCount();
        Resources resources = getResources();
        String quantity =
                resources.getQuantityString(R.plurals.selected, count, count);
        mode.setTitle(quantity);
        ListView listView = getListView();
        listView.invalidateViews();
    }

    float limit(String preferenceKey, int defaultId) {
        Resources resources = getResources();
        String limitDefault = resources.getString(defaultId);

        FragmentActivity activity = getActivity();
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(activity);
        String limitString = preferences.getString(preferenceKey,
                limitDefault);

        try {
            return Float.parseFloat(limitString);
        } catch (NumberFormatException e) {
            Log.e(Logs.TAG, "cannot parse limit value " + limitString);
            float limit = Float.parseFloat(limitDefault);
            preferences.edit().putFloat(preferenceKey, limit).commit();
            return limit;
        }
    }
}
