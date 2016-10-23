/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app;

public class PositiveStepValue extends StepValue {

    @Override
    public void decrease() {
        if (value.compareTo(step) >= 0) {
            super.decrease();
        }
    }
}
