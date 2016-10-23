/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import java.util.List;

@SmallTest
public class ShoppingProductsTest extends ShoppingTest {

    public void testEmptyProducts() {
        // when
        List<Product> products = shopping.products();
        // then
        assertEquals(0, products.size());
    }

    public void testAllProducts() {
        // given
        Product product = shopping.create("product");
        // when
        List<Product> products = shopping.products();
        // then
        assertEquals(1, products.size());
        Product list1 = products.get(0);
        assertEquals(product, list1);
    }
}
