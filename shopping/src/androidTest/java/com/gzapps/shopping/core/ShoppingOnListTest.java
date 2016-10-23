/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

@SmallTest
public class ShoppingOnListTest extends ShoppingTest {

    public void testOnList0() {
        // given
        Product product = shopping.create("product");
        // when
        boolean onList = shopping.onList(product);
        // then
        assertFalse(onList);
    }

    public void testOnList1() {
        // given
        Product product = shopping.create("product");
        shopping.enlist(product);
        // when
        boolean onList = shopping.onList(product);
        // then
        assertTrue(onList);
    }
}
