/*
 * Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;

import com.gzapps.shopping.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class PageChangeListener extends ViewPager.SimpleOnPageChangeListener {

    private final ShoppingActivity activity;

    PageChangeListener(ShoppingActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onPageSelected(int position) {
        activity.cancel();
        super.onPageSelected(position);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setSelectedNavigationItem(position);
        selectInSpinnerIfPresent(position);
    }

    private void selectInSpinnerIfPresent(int position) {
        try {
            View actionBarView = activity.findViewById(R.id.action_bar);
            if (actionBarView == null) {
                int id = activity.getResources()
                        .getIdentifier("action_bar", "id", "android");
                actionBarView = activity.findViewById(id);
            }

            Class<?> actionBarViewClass = actionBarView.getClass();
            Field mTabScrollViewField =
                    actionBarViewClass.getDeclaredField("mTabScrollView");
            mTabScrollViewField.setAccessible(true);

            Object mTabScrollView = mTabScrollViewField.get(actionBarView);
            if (mTabScrollView == null) {
                return;
            }

            Field mTabSpinnerField =
                    mTabScrollView.getClass().getDeclaredField("mTabSpinner");
            mTabSpinnerField.setAccessible(true);

            Object mTabSpinner = mTabSpinnerField.get(mTabScrollView);
            if (mTabSpinner == null) {
                return;
            }

            Method setSelectionMethod = mTabSpinner.getClass().getSuperclass()
                    .getDeclaredMethod("setSelection", Integer.TYPE,
                            Boolean.TYPE);
            setSelectionMethod.invoke(mTabSpinner, position, true);

            Method requestLayoutMethod = mTabSpinner.getClass().getSuperclass()
                    .getDeclaredMethod("requestLayout");
            requestLayoutMethod.invoke(mTabSpinner);

        } catch (IllegalArgumentException e) {
            Log.e(Logs.TAG, "action bar tab fix problem", e);
        } catch (IllegalAccessException e) {
            Log.e(Logs.TAG, "action bar tab fix problem", e);
        } catch (NoSuchFieldException e) {
            Log.e(Logs.TAG, "action bar tab fix problem", e);
        } catch (NoSuchMethodException e) {
            Log.e(Logs.TAG, "action bar tab fix problem", e);
        } catch (InvocationTargetException e) {
            Log.e(Logs.TAG, "action bar tab fix problem", e);
        }
    }
}
