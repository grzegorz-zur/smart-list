/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.MediumTest;

import static com.gzapps.shopping.TimeValues.TODAY;
import static com.gzapps.shopping.core.Statistics.DISTANCE_DEFAULT;
import static com.gzapps.shopping.core.Time.MINUTE;

@MediumTest
public class AnalysisAnalyzeDistanceTest extends AnalysisTest {

    private Product product1;

    private Product product2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        product1 = shopping.create("product 1");
        product2 = shopping.create("product 2");
    }

    public void test0() {
        // when
        analysis.analyzeDistance(product1, product2);
        // then
        assertEquals(DISTANCE_DEFAULT,
                analysis.statistics.distance(product1, product2));
    }

    public void test1() {
        // given
        product1.buy(TODAY);
        product2.buy(TODAY);
        // when
        analysis.analyzeDistance(product1, product2);
        // then
        assertEquals(0, analysis.statistics.distance(product1, product2));
    }

    public void test2() {
        // given
        product1.buy(TODAY - MINUTE);
        product2.buy(TODAY);
        // when
        analysis.analyzeDistance(product1, product2);
        // then
        assertEquals(60, analysis.statistics.distance(product1, product2));
    }
}
