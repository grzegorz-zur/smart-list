/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.MediumTest;

import org.apache.commons.collections.primitives.LongList;

import static com.gzapps.shopping.TimeValues.ONE_DAY_AGO;
import static com.gzapps.shopping.TimeValues.ONE_YEAR_AGO;
import static com.gzapps.shopping.TimeValues.TODAY;
import static com.gzapps.shopping.core.Time.HOUR;

@MediumTest
public class AnalysisTruncateBuysTest extends AnalysisTest {

    private Product product;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        product = shopping.create("product1");
    }

    public void test0() {
        // when
        analysis.truncateBuys(product);
        // then
        LongList buys = product.buys;
        assertEquals(0, buys.size());
    }

    public void test1() {
        // given
        product.buy(TODAY);
        // when
        analysis.truncateBuys(product);
        // then
        assertEquals(1, product.buys.size());
    }

    public void test2() {
        // given
        product.buy(ONE_DAY_AGO);
        product.buy(TODAY);
        // when
        analysis.truncateBuys(product);
        // then
        assertEquals(2, product.buys.size());
    }

    public void test3() {
        // given
        product.buy(ONE_YEAR_AGO);
        product.buy(TODAY);
        // when
        analysis.truncateBuys(product);
        // then
        assertEquals(2, product.buys.size());
    }

    public void test4() {
        // given
        product.buy(ONE_YEAR_AGO - HOUR);
        product.buy(TODAY);
        // when
        analysis.truncateBuys(product);
        // then
        assertEquals(1, product.buys.size());
    }

    public void test5() {
        // given
        product.buy(ONE_YEAR_AGO - HOUR);
        // when
        analysis.truncateBuys(product);
        // then
        assertEquals(0, product.buys.size());
    }
}
