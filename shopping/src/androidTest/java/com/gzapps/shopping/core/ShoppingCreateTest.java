/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import java.util.List;

@SmallTest
public class ShoppingCreateTest extends ShoppingTest {

    public void testValid() {
        // when
        Product product = shopping.create("product");
        // then
        assertEquals("product", product.name());
    }

    public void testProducts() {
        // when
        Product product = shopping.create("product");
        // then
        List<Product> products = shopping.products();
        assertEquals(1, products.size());
        Product product1 = products.get(0);
        assertEquals(product, product1);
    }

    public void testInvalid() {
        // when
        try {
            shopping.create(null);
            fail();
        } catch (IllegalArgumentException e) {
            // then
        }
    }

    public void testExists() {
        // given
        shopping.create("product");
        // when
        try {
            shopping.create("product");
            fail();
        } catch (IllegalArgumentException e) {
            // then
        }
    }
}
