/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import java.util.List;

@SmallTest
public class ShoppingRemoveTest extends ShoppingTest {

    public void testRemove0() {
        // given
        Product product = shopping.create("product");
        // when
        shopping.remove(product);
        // then
        List<Product> products = shopping.products();
        assertEquals(0, products.size());
    }

    public void testRemove1() {
        // given
        Product product = shopping.create("product");
        product.enlist();
        // when
        shopping.remove(product);
        // then
        List<Product> list = shopping.products();
        assertEquals(0, list.size());
    }
}
