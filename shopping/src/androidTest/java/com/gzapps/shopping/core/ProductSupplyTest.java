/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import static com.gzapps.shopping.TimeValues.FIVE_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.FOUR_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.ONE_DAY_AGO;
import static com.gzapps.shopping.TimeValues.THREE_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.TODAY;
import static com.gzapps.shopping.TimeValues.TWO_DAYS_AGO;
import static com.gzapps.shopping.core.Time.DAY;

@SmallTest
public class ProductSupplyTest extends ProductTest {

    private Product product;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        product = shopping.create("product");
    }

    public void test0() {
        // when
        float supply = product.supply(TODAY);
        // then
        assertEquals(0, supply, DELTA);
    }

    public void test1() {
        // given
        product.buy(TWO_DAYS_AGO);
        // when
        float supply = product.supply(TODAY);
        // then
        assertEquals(Float.MAX_VALUE, supply, DELTA);
    }

    public void test2() {
        // given
        product.buy(TWO_DAYS_AGO);
        product.buy(ONE_DAY_AGO);
        product.distance = DAY;
        // when
        float supply = product.supply(TODAY);
        // then
        assertEquals(0, supply, DELTA);
    }

    public void test3() {
        // given
        product.buy(THREE_DAYS_AGO);
        product.buy(ONE_DAY_AGO);
        product.distance = 2 * DAY;
        // when
        float supply = product.supply(TODAY);
        // then
        assertEquals(0.5, supply, DELTA);
    }

    public void test4() {
        // given
        product.buy(ONE_DAY_AGO);
        product.buy(TODAY);
        product.distance = DAY;
        // when
        float supply = product.supply(TODAY);
        // then
        assertEquals(1, supply, DELTA);
    }

    public void test5() {
        // given
        product.buy(FIVE_DAYS_AGO);
        product.buy(TWO_DAYS_AGO);
        product.distance = 3 * DAY;
        // when
        float supply = product.supply(TODAY);
        // then
        assertEquals(0.333333, supply, DELTA);
    }

    public void test6() {
        // given
        product.buy(FOUR_DAYS_AGO);
        product.buy(ONE_DAY_AGO);
        product.distance = 3 * DAY;
        // when
        float supply = product.supply(TODAY);
        // then
        assertEquals(0.666666, supply, DELTA);
    }

    public void test7() {
        // given
        product.buy(FOUR_DAYS_AGO);
        product.buy(THREE_DAYS_AGO);
        product.distance = DAY;
        // when
        float supply = product.supply(TODAY);
        // then
        assertEquals(-2, supply, DELTA);
    }

    public void test8() {
        // given
        product.buy(FOUR_DAYS_AGO);
        product.buy(TWO_DAYS_AGO);
        product.distance = 2 * DAY;
        // when
        float supply = product.supply(TODAY);
        // then
        assertEquals(0, supply, DELTA);
    }
}
