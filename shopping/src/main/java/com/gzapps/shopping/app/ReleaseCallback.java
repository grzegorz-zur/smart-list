/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app;

import com.gzapps.shopping.core.StopCallback;
import com.gzapps.shopping.core.StopException;

class ReleaseCallback extends StopCallback {

    private final ShoppingApplication application;

    ReleaseCallback(ShoppingApplication application) {
        this.application = application;
    }

    @Override
    public void stop() throws StopException {
        if (application.shouldRelease()) {
            throw new StopException();
        }
    }
}
