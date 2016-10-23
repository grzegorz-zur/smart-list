/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import java.util.List;

@SmallTest
public class ShoppingSwapTest extends ShoppingTest {

    public void testSwap() {
        // given
        Product product1 = shopping.create("product1");
        Product product2 = shopping.create("product2");
        product1.enlist();
        product2.enlist();
        // when
        shopping.swap(product1, product2);
        // then
        List<Product> list = shopping.list();
        assertEquals(product2, list.get(0));
        assertEquals(product1, list.get(1));
    }

    public void testSwapEmptyList() {
        // given
        Product product1 = shopping.create("product1");
        Product product2 = shopping.create("product2");
        // when
        shopping.swap(product1, product2);
        // then
        List<Product> list = shopping.list();
        assertEquals(0, list.size());
    }

    public void testSwapOneOnList() {
        // given
        Product product1 = shopping.create("product1");
        Product product2 = shopping.create("product2");
        product1.enlist();
        // when
        shopping.swap(product1, product2);
        // then
        List<Product> list = shopping.list();
        assertEquals(1, list.size());
    }
}
