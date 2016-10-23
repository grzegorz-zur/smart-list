/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

@SmallTest
public class ProductRenameTest extends ProductTest {

    private Product product;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        product = shopping.create("product");
    }

    public void testValid() {
        // when
        product.rename("rename");
        // then
        assertEquals("rename", product.name());
    }

    public void testInvalid() {
        // when
        try {
            product.rename("  ");
            fail();
        } catch (IllegalArgumentException e) {
            // then
        }
    }

    public void testExists() {
        // given
        shopping.create("rename");
        // when
        try {
            product.rename("rename");
            fail();
        } catch (IllegalArgumentException e) {
            // then
        }
    }
}
