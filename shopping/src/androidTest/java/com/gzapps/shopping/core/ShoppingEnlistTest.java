/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import java.util.List;

@SmallTest
public class ShoppingEnlistTest extends ShoppingTest {

    private Statistics statistics;

    private Product product1;

    private Product product2;

    private Product product3;

    private Product product4;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        statistics = shopping.statistics;
        product1 = shopping.create("product1");
        product2 = shopping.create("product2");
        product3 = shopping.create("product3");
        product4 = shopping.create("product4");
    }

    public void testEnlist0() {
        // then
        List<Product> list = shopping.list();
        assertEquals(0, list.size());
    }

    public void testEnlist1() {
        // when
        shopping.enlist(product1);
        // then
        assertEquals(1, shopping.list.size());
        assertEquals(product1, shopping.list.get(0));
    }

    public void testEnlist2() {
        // when
        shopping.enlist(product1);
        shopping.enlist(product1);
        // then
        assertEquals(1, shopping.list.size());
        assertEquals(product1, shopping.list.get(0));
    }

    public void testEnlist3() {
        // when
        shopping.enlist(product1);
        shopping.enlist(product2);
        // then
        assertEquals(2, shopping.list.size());
        assertEquals(product1, shopping.list.get(0));
        assertEquals(product2, shopping.list.get(1));
    }

    public void testEnlist4() {
        // given
        statistics.distance(product1, product4, 1);
        statistics.distance(product2, product4, 10);
        statistics.distance(product3, product4, 100);
        // when
        shopping.enlist(product1);
        shopping.enlist(product2);
        shopping.enlist(product3);
        shopping.enlist(product4);
        // then
        assertEquals(4, shopping.list.size());
        assertEquals(product1, shopping.list.get(0));
        assertEquals(product4, shopping.list.get(1));
        assertEquals(product2, shopping.list.get(2));
        assertEquals(product3, shopping.list.get(3));
    }
}
