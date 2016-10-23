/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import java.util.List;

@SmallTest
public class ShoppingDelistTest extends ShoppingTest {

    public void testDelist0() {
        // given
        Product product = shopping.create("product");
        shopping.enlist(product);
        // when
        shopping.delist(product);
        // then
        List<Product> list = shopping.list();
        assertEquals(0, list.size());
    }

    public void testDelist1() {
        // given
        Product product = shopping.create("product");
        // when
        shopping.delist(product);
        // then
        List<Product> list = shopping.list();
        assertEquals(0, list.size());
    }
}
