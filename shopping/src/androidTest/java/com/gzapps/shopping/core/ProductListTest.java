/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

@SmallTest
public class ProductListTest extends ProductTest {

    private Product product;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        product = shopping.create("product");
    }

    public void testEnlist() {
        // when
        product.enlist();
        // then
        assertTrue(shopping.list.contains(product));
    }

    public void testDelist() {
        // when
        product.delist();
        // then
        assertFalse(shopping.list.contains(product));
    }

    public void testNotEnlisted() {
        // when
        boolean enlisted = product.enlisted();
        // then
        assertFalse(enlisted);
    }

    public void testEnlisted() {
        // given
        product.enlist();
        // when
        boolean enlisted = product.enlisted();
        // then
        assertTrue(enlisted);
    }
}
