/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.MediumTest;

import static com.gzapps.shopping.TimeValues.FIVE_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.ONE_DAY_AGO;
import static com.gzapps.shopping.TimeValues.SEVEN_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.TODAY;
import static com.gzapps.shopping.TimeValues.TWO_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.TWO_WEEKS_AGO;
import static com.gzapps.shopping.core.Time.DAY;

@MediumTest
public class AnalysisCalculateDistanceTest extends AnalysisTest {

    private Product product;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        product = shopping.create("product1");
    }

    public void test0() {
        // when
        analysis.calculateDistance(product);
        // then
        assertEquals(-1, product.distance);
    }

    public void test1() {
        // given
        product.buy(TODAY);
        // when
        analysis.calculateDistance(product);
        // then
        assertEquals(-1, product.distance);
    }

    public void test2() {
        // given
        product.buy(ONE_DAY_AGO);
        product.buy(TODAY);
        // when
        analysis.calculateDistance(product);
        // then
        assertEquals(DAY, product.distance);
    }

    public void test3() {
        // given
        product.buy(SEVEN_DAYS_AGO);
        product.buy(FIVE_DAYS_AGO);
        product.buy(ONE_DAY_AGO);
        product.buy(TODAY);
        // when
        analysis.calculateDistance(product);
        // then
        assertEquals(2 * DAY, product.distance);
    }

    public void test4() {
        // given
        product.buy(TWO_WEEKS_AGO);
        product.buy(SEVEN_DAYS_AGO);
        product.buy(FIVE_DAYS_AGO);
        product.buy(TWO_DAYS_AGO);
        product.buy(ONE_DAY_AGO);
        product.buy(TODAY);
        // when
        analysis.calculateDistance(product);
        // then
        assertEquals(2 * DAY, product.distance);
    }
}
