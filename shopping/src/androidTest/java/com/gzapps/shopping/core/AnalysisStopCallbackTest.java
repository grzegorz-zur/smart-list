/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.MediumTest;

import java.util.Random;

import static com.gzapps.shopping.TimeValues.TODAY;
import static com.gzapps.shopping.core.Time.DAY;

@MediumTest
public class AnalysisStopCallbackTest extends AnalysisTest {

    private static final int PRODUCTS = 100;

    private static final int BUYS = 90;

    public void testAnalysisRunning() {
        // given
        populate();
        Statistics statistics = shopping.statistics;
        analysis = new Analysis(shopping, TODAY, false, new TestStopCallback(false));
        // when
        analysis.analyze();
        // then
        assertNotSame(statistics, shopping.statistics);
    }

    public void testAnalysisStopped() {
        // given
        populate();
        Statistics statistics = shopping.statistics;
        analysis = new Analysis(shopping, TODAY, false, new TestStopCallback(true));
        // when
        analysis.analyze();
        // then
        assertSame(statistics, shopping.statistics);
    }

    private void populate() {
        Random random = new Random();
        for (int i = 0; i < PRODUCTS; ++i) {
            Product product = shopping.create(String.valueOf(i));
            for (int j = 0; j < BUYS; ++j) {
                if (random.nextBoolean()) {
                    product.buy(TODAY - j * DAY);
                }
            }
        }
    }
}
