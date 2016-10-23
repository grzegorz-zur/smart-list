/*
 * Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private boolean checked;

    public CheckableLinearLayout(Context context) {
        super(context);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(11)
    public CheckableLinearLayout(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        refreshDrawableState();
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        checked = !checked;
        refreshDrawableState();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] states = super.onCreateDrawableState(extraSpace + 1);
        if (checked) {
            mergeDrawableStates(states, CHECKED_STATE_SET);
        }
        return states;
    }
}
