/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import java.util.List;

@SmallTest
public class ShoppingClearTest extends ShoppingTest {

    public void testClearList() {
        // given
        Product product = shopping.create("product");
        shopping.enlist(product);
        // when
        shopping.clearList();
        // then
        List<Product> list = shopping.list();
        assertEquals(0, list.size());
    }

    public void testClearProducts() {
        // given
        shopping.create("product");
        // when
        shopping.clearProducts();
        // then
        List<Product> products = shopping.products();
        assertEquals(0, products.size());
    }
}
