/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import static com.gzapps.shopping.TimeValues.ONE_DAY_AGO;
import static com.gzapps.shopping.TimeValues.ONE_YEAR_AGO;
import static com.gzapps.shopping.TimeValues.TODAY;
import static com.gzapps.shopping.core.Time.DAY;
import static com.gzapps.shopping.core.Time.MINUTE;

@SmallTest
public class AnalysisAnalyzeTest extends AnalysisTest {

    private static final float DELTA = 0.0001F;

    private Product product1;

    private Product product2;

    private Product product3;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        product1 = shopping.create("product1");
        product2 = shopping.create("product2");
        product3 = shopping.create("product3");
    }

    public void testCorrelation() {
        // given
        product1.buy(TODAY);
        product2.buy(TODAY + MINUTE);
        product3.buy(ONE_DAY_AGO);
        // when
        analysis.analyze();
        // then
        float correlation = shopping.statistics.correlation(product1, product2);
        assertEquals(1, correlation, DELTA);
    }

    public void testDistance() {
        // given
        product1.buy(TODAY);
        product2.buy(TODAY + MINUTE);
        product3.buy(ONE_DAY_AGO);
        // when
        analysis.analyze();
        // then
        int distance = shopping.statistics.distance(product1, product2);
        assertEquals(60, distance);
    }

    public void testTruncate() {
        // given
        product1.buy(ONE_YEAR_AGO - DAY);
        product1.buy(TODAY);
        // when
        analysis.analyze();
        // then
        assertEquals(1, product1.buys.size());
    }

    public void testPurge() {
        // given
        product1.buy(ONE_YEAR_AGO - DAY);
        // when
        analysis.analyze();
        // then
        assertEquals(0, shopping.products.size());
    }
}
