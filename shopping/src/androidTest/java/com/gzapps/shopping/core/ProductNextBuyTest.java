/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import static com.gzapps.shopping.TimeValues.ONE_DAY_AGO;
import static com.gzapps.shopping.TimeValues.ONE_DAY_AHEAD;
import static com.gzapps.shopping.TimeValues.ONE_WEEK_AGO;
import static com.gzapps.shopping.TimeValues.THREE_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.THREE_DAYS_AHEAD;
import static com.gzapps.shopping.TimeValues.TODAY;
import static com.gzapps.shopping.core.Time.DAY;

@SmallTest
public class ProductNextBuyTest extends ProductTest {

    private Product product;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        product = shopping.create("product");
    }

    public void test0() {
        // when
        long nextBuy = product.nextBuy();
        // then
        assertEquals(nextBuy, 0);
    }

    public void test1() {
        // when
        product.buy(ONE_DAY_AGO);
        long nextBuy = product.nextBuy();
        // then
        assertEquals(nextBuy, Long.MAX_VALUE);
    }

    public void test2() {
        // given
        product.distance = -1;
        product.buy(ONE_WEEK_AGO);
        product.buy(ONE_DAY_AGO);
        // when
        long nextBuy = product.nextBuy();
        // then
        assertEquals(nextBuy, Long.MAX_VALUE);
    }

    public void test3() {
        // given
        product.distance = DAY;
        product.buy(ONE_DAY_AGO);
        product.buy(TODAY);
        // when
        long nextBuy = product.nextBuy();
        // then
        assertEquals(ONE_DAY_AHEAD, nextBuy);
    }

    public void test4() {
        // given
        product.distance = 3 * DAY;
        product.buy(THREE_DAYS_AGO);
        product.buy(TODAY);
        // when
        long nextBuy = product.nextBuy();
        // then
        assertEquals(THREE_DAYS_AHEAD, nextBuy);
    }
}
