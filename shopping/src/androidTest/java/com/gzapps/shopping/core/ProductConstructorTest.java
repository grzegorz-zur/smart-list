/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

@SmallTest
public class ProductConstructorTest extends ProductTest {

    private short id;

    private String name;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        id = 0;
        name = "name";
    }

    public void testConstructor() {
        // when
        Product product = new Product(shopping, id, name);
        // then
        assertEquals(id, product.id());
        assertEquals(name, product.name());
    }
}
