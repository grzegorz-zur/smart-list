/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import java.util.List;

import static com.gzapps.shopping.TimeValues.TODAY;

@SmallTest
public class ShoppingBuyTest extends ShoppingTest {

    public void testList() {
        // given
        Product product = shopping.create("product");
        shopping.enlist(product);
        // when
        shopping.buy(product);
        // then
        List<Product> list = shopping.list();
        assertEquals(0, list.size());
    }

    public void testEnlisted() {
        // given
        Product product = shopping.create("product");
        shopping.enlist(product);
        // when
        shopping.buy(product);
        // then
        assertEquals(false, shopping.onList(product));
    }

    public void testBuys() {
        // given
        Product product = shopping.create("product");
        shopping.enlist(product);
        // when
        shopping.buy(product);
        // then
        assertEquals(1, shopping.buys.size());
    }

    public void testAnalyze() {
        // given
        Product product = shopping.create("product");
        shopping.enlist(product);
        // when
        shopping.buy(product);
        // then
        assertEquals(true, shopping.analyze);
    }

    public void testBuyAll() {
        // given
        Product product1 = shopping.create("product1");
        Product product2 = shopping.create("product2");
        shopping.enlist(product1);
        shopping.enlist(product2);
        // when
        shopping.buyAll(TODAY);
        // then
        List<Product> list = shopping.list();
        assertEquals(0, list.size());
    }
}
