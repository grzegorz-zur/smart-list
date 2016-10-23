/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

@SmallTest
public class ImportanceComparatorTest extends TestCase {

    private Map<Product, Float> importances;

    private Product product1;

    private Product product2;

    private ImportanceComparator comparator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Shopping shopping = new Shopping();
        product1 = shopping.create("product1");
        product2 = shopping.create("product2");

        importances = new HashMap<Product, Float>();
        comparator = new ImportanceComparator(importances);
    }

    public void testCompareSame() {
        // when
        int comparison = comparator.compare(product1, product1);
        // then
        assertEquals(0, comparison);
    }

    public void testCompare0() {
        // given
        importances.put(product1, 1F);
        importances.put(product2, 1F);
        // when
        int comparison = comparator.compare(product1, product2);
        // then
        assertEquals(0, comparison);
    }

    public void testCompare1() {
        // given
        importances.put(product1, 1F);
        importances.put(product2, 2F);
        // when
        int comparison = comparator.compare(product1, product2);
        // then
        assertEquals(1, comparison);
    }

    public void testCompare2() {
        // given
        importances.put(product1, 2F);
        importances.put(product2, 1F);
        // when
        int comparison = comparator.compare(product1, product2);
        // then
        assertEquals(-1, comparison);
    }
}
