/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import org.apache.commons.collections.primitives.LongList;

import static com.gzapps.shopping.TimeValues.ONE_DAY_AGO;
import static com.gzapps.shopping.TimeValues.ONE_DAY_AHEAD;
import static com.gzapps.shopping.TimeValues.TODAY;

@SmallTest
public class ProductBuyTest extends ProductTest {

    private Product product;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        product = shopping.create("product");
    }

    public void testDelegate() {
        // when
        product.buy(TODAY);
        // then
        assertFalse(shopping.list.contains(product));
    }

    public void testFirst() {
        // when
        product.buy(TODAY);
        // then
        LongList buys = product.buys();
        assertEquals(1, buys.size());
        assertEquals(TODAY, buys.get(0));
    }

    public void testSecond() {
        // when
        product.buy(ONE_DAY_AGO);
        product.buy(TODAY);
        // then
        LongList buys = product.buys();
        assertEquals(2, buys.size());
        assertEquals(ONE_DAY_AGO, buys.get(0));
        assertEquals(TODAY, buys.get(1));
    }

    public void testWrong() {
        // when
        try {
            product.buy(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // then
        }
    }

    public void testOutOfOrder0() {
        // when
        product.buy(TODAY);
        product.buy(ONE_DAY_AGO);
        // then
        LongList buys = product.buys();
        assertEquals(2, buys.size());
        assertEquals(ONE_DAY_AGO, buys.get(0));
        assertEquals(TODAY, buys.get(1));
    }

    public void testOutOfOrder1() {
        // when
        product.buy(TODAY);
        product.buy(ONE_DAY_AHEAD);
        product.buy(ONE_DAY_AGO);
        // then
        LongList buys = product.buys();
        assertEquals(ONE_DAY_AGO, buys.get(0));
        assertEquals(TODAY, buys.get(1));
        assertEquals(ONE_DAY_AHEAD, buys.get(2));
    }
}
