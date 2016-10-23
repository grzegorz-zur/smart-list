/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import static com.gzapps.shopping.TimeValues.ONE_DAY_AGO;
import static com.gzapps.shopping.TimeValues.TODAY;

@SmallTest
public class AnalysisAnalyzeCorrelationTest extends AnalysisTest {

    private static final float DELTA = 0.0001F;

    private Product product1;

    private Product product2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        product1 = shopping.create("product1");
        product2 = shopping.create("product2");
    }

    public void test0() throws StopException {
        // given
        product1.buy(TODAY);
        product2.buy(TODAY);
        analysis.analyzeProducts();
        // when
        analysis.analyzeCorrelation(product1, product2, 3);
        // then
        float correlation = analysis.statistics.correlation(product1, product2);
        assertEquals(1, correlation, DELTA);
    }

    public void test1() throws StopException {
        // given
        product1.buy(ONE_DAY_AGO);
        product2.buy(TODAY);
        analysis.analyzeProducts();
        // when
        analysis.analyzeCorrelation(product1, product2, 3);
        // then
        float correlation = analysis.statistics.correlation(product1, product2);
        assertEquals(0, correlation, DELTA);
    }

    public void test2() throws StopException {
        // given
        product1.buy(ONE_DAY_AGO);
        product2.buy(TODAY);
        analysis.analyzeProducts();
        // when
        analysis.analyzeCorrelation(product1, product2, 0);
        // then
        float correlation = analysis.statistics.correlation(product1, product2);
        assertEquals(0, correlation, DELTA);
    }

    public void test3() throws StopException {
        // given
        product2.buy(TODAY);
        analysis.analyzeProducts();
        // when
        analysis.analyzeCorrelation(product1, product2, 0);
        // then
        float correlation = analysis.statistics.correlation(product1, product2);
        assertEquals(0, correlation, DELTA);
    }

    public void test4() throws StopException {
        // given
        product1.buy(ONE_DAY_AGO);
        analysis.analyzeProducts();
        // when
        analysis.analyzeCorrelation(product1, product2, 0);
        // then
        float correlation = analysis.statistics.correlation(product1, product2);
        assertEquals(0, correlation, DELTA);
    }
}
