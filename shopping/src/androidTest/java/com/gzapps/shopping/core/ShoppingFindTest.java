/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

@SmallTest
public class ShoppingFindTest extends ShoppingTest {

    public void testFind0() {
        // given
        Product product = shopping.create("product");
        // when
        Product foundProduct = shopping.find(product.id());
        // then
        assertEquals(product, foundProduct);
    }

    public void testFind1() {
        // given
        Product product = shopping.create("product");
        short id = (short) (product.id() + 1);
        // when
        Product foundProduct = shopping.find(id);
        // then
        assertNull(foundProduct);
    }
}
