/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

@SmallTest
public class ShoppingNextIdTest extends ShoppingTest {

    public void testNextId0() {
        assertEquals(0, shopping.nextId());
    }

    public void testNextId1() {
        assertEquals(0, shopping.nextId());
        assertEquals(0, shopping.nextId());
    }

    public void testNextId2() {
        // when
        shopping.create("product1");
        // then
        assertEquals(1, shopping.nextId());
    }

    public void testNextId3() {
        // when
        shopping.create("product1");
        shopping.create("product2");
        // then
        assertEquals(2, shopping.nextId());
    }
}
