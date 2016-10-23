/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

@SmallTest
public class ShoppingValidTest extends ShoppingTest {

    public void testValid() {
        assertTrue(Shopping.valid("name"));
    }

    public void testInvalid0() {
        assertFalse(Shopping.valid(""));
    }

    public void testInvalid1() {
        assertFalse(Shopping.valid(" "));
    }

    public void testInvalid2() {
        assertFalse(Shopping.valid(" \t\n\r"));
    }

    public void testNull() {
        assertFalse(Shopping.valid(null));
    }
}
