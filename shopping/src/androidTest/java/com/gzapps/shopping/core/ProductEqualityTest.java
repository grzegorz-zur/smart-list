/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

@SmallTest
public class ProductEqualityTest extends ProductTest {

    private Product product1;

    private Product product2;

    private Product product1SameId;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        product1 = new Product(shopping, (short) 1, "product1");
        product2 = new Product(shopping, (short) 2, "product2");
        product1SameId = new Product(shopping, (short) 1, "product1SameId");
    }

    @SuppressWarnings("ObjectEqualsNull")
    public void testNotEqualsNull() {
        assertFalse(product1.equals(null));
    }

    @SuppressWarnings(
            {"EqualsBetweenInconvertibleTypes", "LiteralAsArgToStringEquals"})
    public void testNotEqualsObject() {
        assertFalse(product1.equals(""));
    }

    public void testNotEqualsDifferent() {
        assertFalse(product1.equals(product2));
    }

    public void testEqualsSame() {
        assertTrue(product1.equals(product1));
    }

    public void testEqualsById() {
        assertTrue(product1.equals(product1SameId));
    }

    public void testHashCode0() {
        assertEquals(1, product1.hashCode());
    }

    public void testHashCode1() {
        assertEquals(2, product2.hashCode());
    }
}
