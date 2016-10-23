/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import junit.framework.TestCase;

abstract class ShoppingTest extends TestCase {

    protected Shopping shopping;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        shopping = new Shopping();
    }
}
