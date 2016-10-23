/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

@SmallTest
public class ShoppingPositionTest extends ShoppingTest {

    private Statistics statistics;
    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;
    private Product product5;
    private Product product6;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        statistics = shopping.statistics;
        product1 = shopping.create("product1");
        product2 = shopping.create("product2");
        product3 = shopping.create("product3");
        product4 = shopping.create("product4");
        product5 = shopping.create("product5");
        product6 = shopping.create("product6");
    }

    public void test00() {
        // when
        int position = shopping.position(product1);
        // then
        assertEquals(0, position);
    }

    public void test01() {
        // given
        statistics.distance(product1, product2, 1);
        product1.enlist();
        // when
        int position = shopping.position(product2);
        // then
        assertEquals(1, position);
    }

    public void test02() {
        // given
        statistics.distance(product1, product3, 1);
        statistics.distance(product2, product3, 10);
        product1.enlist();
        product2.enlist();
        // when
        int position = shopping.position(product3);
        // then
        assertEquals(1, position);
    }

    public void test03() {
        // given
        statistics.distance(product2, product3, 10);
        product1.enlist();
        product2.enlist();
        // when
        int position = shopping.position(product3);
        // then
        assertEquals(2, position);
    }

    public void test04() {
        // given
        statistics.distance(product1, product3, 1);
        product1.enlist();
        product2.enlist();
        // when
        int position = shopping.position(product3);
        // then
        assertEquals(0, position);
    }

    public void test05() {
        // given
        statistics.distance(product1, product4, 1);
        statistics.distance(product2, product4, 10);
        statistics.distance(product3, product4, 100);
        product1.enlist();
        product2.enlist();
        product3.enlist();
        // when
        int position = shopping.position(product4);
        // then
        assertEquals(1, position);
    }

    public void test06() {
        // given
        statistics.distance(product1, product4, 1);
        statistics.distance(product2, product4, 10);
        statistics.distance(product3, product4, 100);
        statistics.distance(product1, product2, 5);
        statistics.distance(product2, product3, 5);
        product1.enlist();
        product2.enlist();
        product3.enlist();
        // when
        int position = shopping.position(product4);
        // then
        assertEquals(0, position);
    }

    public void test07() {
        // given
        statistics.distance(product1, product4, 1);
        statistics.distance(product2, product4, 10);
        statistics.distance(product3, product4, 100);
        statistics.distance(product1, product2, 50);
        statistics.distance(product2, product3, 50);
        product1.enlist();
        product2.enlist();
        product3.enlist();
        // when
        int position = shopping.position(product4);
        // then
        assertEquals(1, position);
    }

    public void test08() {
        // given
        statistics.distance(product1, product4, 10);
        statistics.distance(product2, product4, 1);
        statistics.distance(product3, product4, 100);
        product1.enlist();
        product2.enlist();
        product3.enlist();
        // when
        int position = shopping.position(product4);
        // then
        assertEquals(1, position);
    }

    public void test09() {
        // given
        statistics.distance(product1, product4, 100);
        statistics.distance(product2, product4, 1);
        statistics.distance(product3, product4, 10);
        product1.enlist();
        product2.enlist();
        product3.enlist();
        // when
        int position = shopping.position(product4);
        // then
        assertEquals(2, position);
    }

    public void test10() {
        // given
        statistics.distance(product1, product6, 100);
        statistics.distance(product2, product6, 20);
        statistics.distance(product3, product6, 10);
        statistics.distance(product4, product6, 30);
        statistics.distance(product5, product6, 100);
        product1.enlist();
        product2.enlist();
        product3.enlist();
        product4.enlist();
        product5.enlist();
        // when
        int position = shopping.position(product6);
        // then
        assertEquals(2, position);
    }
}
