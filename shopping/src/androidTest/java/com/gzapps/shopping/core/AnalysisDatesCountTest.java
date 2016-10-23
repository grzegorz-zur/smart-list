/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import static com.gzapps.shopping.TimeValues.ONE_DAY_AGO;
import static com.gzapps.shopping.TimeValues.TODAY;
import static com.gzapps.shopping.TimeValues.TWO_DAYS_AGO;
import static com.gzapps.shopping.core.Time.HOUR;

@SmallTest
public class AnalysisDatesCountTest extends AnalysisTest {

    private Product product1;

    private Product product2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        product1 = shopping.create("product1");
        product2 = shopping.create("product2");
    }

    public void test0() throws StopException {
        // when
        analysis.analyzeProducts();
        int count = analysis.datesCount();
        // then
        assertEquals(0, count);
    }

    public void test1() throws StopException {
        // given
        product1.buy(TODAY);
        analysis.analyzeProducts();
        // when
        int count = analysis.datesCount();
        // then
        assertEquals(1, count);
    }

    public void test2() throws StopException {
        // given
        product1.buy(TODAY - HOUR);
        product2.buy(TODAY + HOUR);
        analysis.analyzeProducts();
        // when
        int count = analysis.datesCount();
        // then
        assertEquals(1, count);
    }

    public void test3() throws StopException {
        // given
        product1.buy(TODAY - HOUR);
        product2.buy(TODAY + HOUR);
        analysis.analyzeProducts();
        // when
        int count = analysis.datesCount();
        // then
        assertEquals(1, count);
    }

    public void test4() throws StopException {
        // given
        product1.buy(ONE_DAY_AGO);
        product2.buy(TODAY);
        analysis.analyzeProducts();
        // when
        int count = analysis.datesCount();
        // then
        assertEquals(2, count);
    }

    public void test5() throws StopException {
        // given
        product1.buy(TWO_DAYS_AGO);
        product1.buy(ONE_DAY_AGO);
        product2.buy(ONE_DAY_AGO + HOUR);
        product2.buy(TODAY - HOUR);
        product1.buy(TODAY);
        analysis.analyzeProducts();
        // when
        int count = analysis.datesCount();
        // then
        assertEquals(3, count);
    }
}
