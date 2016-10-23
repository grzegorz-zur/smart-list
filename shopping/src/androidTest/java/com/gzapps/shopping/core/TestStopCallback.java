/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

class TestStopCallback extends StopCallback {

    private final boolean stop;

    TestStopCallback(boolean stop) {
        this.stop = stop;
    }

    @Override
    public void stop() throws StopException {
        if (stop) {
            throw new StopException();
        }
    }
}
