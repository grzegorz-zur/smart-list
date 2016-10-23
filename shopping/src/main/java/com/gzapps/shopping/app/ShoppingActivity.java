/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.gzapps.shopping.R;
import com.gzapps.shopping.app.action.SearchCreateAction;
import com.gzapps.shopping.app.dialog.AboutDialog;
import com.gzapps.shopping.app.dialog.BuyAllDialog;
import com.gzapps.shopping.app.dialog.ClearListDialog;
import com.gzapps.shopping.app.dialog.ClearProductsDialog;
import com.gzapps.shopping.app.dialog.LoadCreatedErrorDialog;
import com.gzapps.shopping.app.dialog.LoadRestoredErrorDialog;
import com.gzapps.shopping.app.dialog.SaveErrorDialog;
import com.gzapps.shopping.app.fragment.ProductsFragment;
import com.gzapps.shopping.app.fragment.ShoppingListFragment;
import com.gzapps.shopping.app.fragment.ShortagesFragment;
import com.gzapps.shopping.app.fragment.SuggestionsFragment;
import com.gzapps.shopping.core.Product;
import com.gzapps.shopping.core.Shopping;

import java.io.IOException;

public class ShoppingActivity extends ActionBarActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int KEEP_SCREEN_ON_DEFAULT_ID = R.bool.keep_screen_on_default;
    private static final String KEEP_SCREEN_ON_PREFERENCE = "keep_screen_on";
    public Shopping shopping;
    public ShoppingListFragment list;
    public SuggestionsFragment suggestions;
    public ShortagesFragment shortages;
    public ProductsFragment products;
    private ShoppingApplication application;
    private ViewPager pager;

    public ShoppingActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        application = (ShoppingApplication) getApplication();
        application.acquire();

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminate(true);
        setProgressBarIndeterminateVisibility(true);

        setContentView(R.layout.activity_shopping);

        pager = (ViewPager) findViewById(R.id.pager);

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        updateKeepScreenOn(preferences);
        preferences.registerOnSharedPreferenceChangeListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        PagerAdapter adapter = new PagerAdapter(fragmentManager);
        pager.setAdapter(adapter);

        PageChangeListener pageChangeListener = new PageChangeListener(this);
        pager.setOnPageChangeListener(pageChangeListener);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        TabListener tabListener = new TabListener(pager);

        ActionBar.Tab listTab = actionBar.newTab();
        listTab.setText(R.string.tab_list);
        listTab.setTabListener(tabListener);
        actionBar.addTab(listTab);

        ActionBar.Tab suggestionTab = actionBar.newTab();
        suggestionTab.setText(R.string.tab_suggestions);
        suggestionTab.setTabListener(tabListener);
        actionBar.addTab(suggestionTab);

        ActionBar.Tab shortagesTab = actionBar.newTab();
        shortagesTab.setText(R.string.tab_shortages);
        shortagesTab.setTabListener(tabListener);
        actionBar.addTab(shortagesTab);

        ActionBar.Tab productsTab = actionBar.newTab();
        productsTab.setText(R.string.tab_products);
        productsTab.setTabListener(tabListener);
        actionBar.addTab(productsTab);

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(Loaders.INIT, null, new ShoppingInitialized());
        loaderManager.initLoader(Loaders.ACTIVITY, null, new ShoppingLoaded());
    }

    private void onInit(ShoppingInitializer initializer) {
        if (initializer.isError()) {
            if (initializer.isRestored()) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                LoadRestoredErrorDialog dialog = new LoadRestoredErrorDialog();
                dialog.show(fragmentManager, null);
            }
            if (initializer.isCreated()) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                LoadCreatedErrorDialog dialog = new LoadCreatedErrorDialog();
                dialog.show(fragmentManager, null);
            }
        } else if (initializer.isCreated()) {
            Toast.makeText(this, R.string.toast_created, Toast.LENGTH_LONG)
                    .show();
        }
        initializer.consume();
    }

    private void onLoad(Shopping shopping) {
        this.shopping = shopping;
        setProgressBarIndeterminate(false);
        setProgressBarIndeterminateVisibility(false);
        supportInvalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        try {
            if (shopping != null) {
                application.save();
            }
        } catch (IOException e) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            SaveErrorDialog dialog = new SaveErrorDialog();
            dialog.show(fragmentManager, null);
        }

        if (shopping != null) {
            if (shopping.analyze()) {
                application.schedule();
            }
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        application.release();
        super.onDestroy();
    }

    @Override
    public boolean onSearchRequested() {
        if (shopping == null) {
            return true;
        }
        SearchCreateAction searchCreateAction = new SearchCreateAction(shopping, this);
        startSupportActionMode(searchCreateAction);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (shopping == null) {
            return true;
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actvity, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (shopping == null) {
            return true;
        }

        MenuItem unBuy = menu.findItem(R.id.menu_un_buy);
        if (unBuy != null) {
            unBuy.setEnabled(shopping.canUnBuy());
        }

        MenuItem buyAll = menu.findItem(R.id.menu_buy_all);
        if (buyAll != null) {
            buyAll.setEnabled(!shopping.isListEmpty());
        }

        MenuItem clearList = menu.findItem(R.id.menu_clear_list);
        if (clearList != null) {
            clearList.setEnabled(!shopping.isListEmpty());
        }

        MenuItem clearProducts = menu.findItem(R.id.menu_clear_products);
        if (clearProducts != null) {
            clearProducts.setEnabled(!shopping.isEmpty());
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (item.getItemId()) {
            case R.id.menu_edit:
                startSupportActionMode(new SearchCreateAction(shopping, this));
                return true;
            case R.id.menu_un_buy:
                unBuy();
                return true;
            case R.id.menu_buy_all:
                new BuyAllDialog().show(fragmentManager, null);
                return true;
            case R.id.menu_clear_list:
                new ClearListDialog().show(fragmentManager, null);
                return true;
            case R.id.menu_clear_products:
                new ClearProductsDialog().show(fragmentManager, null);
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menu_about:
                new AboutDialog().show(fragmentManager, null);
                return true;
            default:
                return false;
        }
    }

    private void unBuy() {
        shopping.unBuy();
        refresh();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean search(String text) {
        int position = pager.getCurrentItem();
        switch (position) {
            case 0:
                return list.search(text);
            case 1:
                return suggestions.search(text);
            case 2:
                return shortages.search(text);
            case 3:
                return products.search(text);
            default:
                return false;
        }
    }

    public void search(Product product) {
        int position = pager.getCurrentItem();
        switch (position) {
            case 0:
                list.search(product);
                break;
            case 1:
                suggestions.search(product);
                break;
            case 2:
                shortages.search(product);
                break;
            case 3:
                products.search(product);
                break;
        }
    }

    public void cancel() {
        if (list != null) {
            list.cancel();
        }
        if (suggestions != null) {
            suggestions.cancel();
        }
        if (shortages != null) {
            shortages.cancel();
        }
        if (products != null) {
            products.cancel();
        }
    }

    public void refresh() {
        if (list != null) {
            list.refresh();
        }
        if (suggestions != null) {
            suggestions.refresh();
        }
        if (shortages != null) {
            shortages.refresh();
        }
        if (products != null) {
            products.refresh();
        }

        supportInvalidateOptionsMenu();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        if (KEEP_SCREEN_ON_PREFERENCE.equals(key)) {
            updateKeepScreenOn(preferences);
        }
    }

    private void updateKeepScreenOn(SharedPreferences preferences) {
        if (pager == null) {
            return;
        }
        Resources resources = getResources();
        boolean keepScreenOnDefault = resources.getBoolean(KEEP_SCREEN_ON_DEFAULT_ID);
        boolean keepScreenOn = preferences.getBoolean(KEEP_SCREEN_ON_PREFERENCE, keepScreenOnDefault);
        pager.setKeepScreenOn(keepScreenOn);
    }

    private class ShoppingInitialized implements
            LoaderManager.LoaderCallbacks<Void> {

        @Override
        public Loader<Void> onCreateLoader(int id, Bundle bundle) {
            return new ShoppingInitializer(ShoppingActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<Void> loader, Void nothing) {
            ShoppingInitializer initializer = (ShoppingInitializer) loader;
            onInit(initializer);
        }

        @Override
        public void onLoaderReset(Loader<Void> loader) {
        }
    }

    private class ShoppingLoaded implements
            LoaderManager.LoaderCallbacks<Shopping> {

        @Override
        public Loader<Shopping> onCreateLoader(int id, Bundle bundle) {
            return new ShoppingLoader(ShoppingActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<Shopping> loader,
                                   Shopping shopping) {
            onLoad(shopping);
        }

        @Override
        public void onLoaderReset(Loader<Shopping> loader) {
        }
    }
}
