/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import java.util.List;

@SmallTest
public class ShoppingListTest extends ShoppingTest {

    public void testListEmpty() {
        // when
        List<Product> list = shopping.list();
        // then
        assertEquals(0, list.size());
    }

    public void testListOne() {
        // given
        Product product = shopping.create("product");
        shopping.enlist(product);
        // when
        List<Product> list = shopping.list();
        // then
        assertEquals(1, list.size());
        Product list1 = list.get(0);
        assertEquals(product, list1);
    }

    public void testListMany() {
        Product product1 = shopping.create("product1");
        Product product2 = shopping.create("product2");
        shopping.enlist(product1);
        shopping.enlist(product2);
        // when
        List<Product> list = shopping.list();
        // then
        assertEquals(2, list.size());
        Product list1 = list.get(0);
        assertEquals(product1, list1);
        Product list2 = list.get(1);
        assertEquals(product2, list2);
    }
}
