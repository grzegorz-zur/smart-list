/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import junit.framework.TestCase;

public abstract class StatisticsTest extends TestCase {

    protected static final float DELTA = 0.0001F;

    protected Shopping shopping;

    protected Statistics statistics;

    protected Product product1;

    protected Product product2;

    protected Product product3;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        shopping = new Shopping();
        statistics = new Statistics();
        product1 = shopping.create("product1");
        product2 = shopping.create("product2");
        product3 = shopping.create("product3");
    }
}
