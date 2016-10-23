/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import java.util.List;

@SmallTest
public class ShoppingUnBuyTest extends ShoppingTest {

    public void test0() {
        // when
        shopping.unBuy();
        // then
        List<Product> list = shopping.list();
        assertEquals(true, list.isEmpty());
        assertEquals(false, shopping.canUnBuy());
    }

    public void test1() {
        // given
        Product product = shopping.create("product");
        product.enlist();
        long time = System.currentTimeMillis();
        product.buy(time);
        // when
        shopping.unBuy();
        // then
        assertEquals(true, product.enlisted());
        assertEquals(Long.MIN_VALUE, product.lastBuy());
        assertEquals(false, shopping.canUnBuy());
    }

    public void test2() {
        // given
        Product product1 = shopping.create("product1");
        Product product2 = shopping.create("product2");
        product1.enlist();
        product2.enlist();
        long time = System.currentTimeMillis();
        product1.buy(time);
        product2.buy(time);

        // when
        shopping.unBuy();

        // then
        assertEquals(false, product1.enlisted());
        assertEquals(time, product1.lastBuy());
        assertEquals(true, product2.enlisted());
        assertEquals(Long.MIN_VALUE, product2.lastBuy());
        assertEquals(true, shopping.canUnBuy());
    }

    public void test3() {
        // given
        Product product1 = shopping.create("product1");
        Product product2 = shopping.create("product2");
        product1.enlist();
        product2.enlist();
        long time = System.currentTimeMillis();
        product1.buy(time);
        product2.buy(time);

        // when
        shopping.unBuy();
        shopping.unBuy();

        // then
        assertEquals(true, product1.enlisted());
        assertEquals(true, product2.enlisted());
        assertEquals(false, shopping.canUnBuy());
    }
}
