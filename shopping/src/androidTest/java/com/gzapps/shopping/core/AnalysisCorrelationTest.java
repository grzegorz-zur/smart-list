/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

@SmallTest
public class AnalysisCorrelationTest extends AnalysisTest {

    private static final float DELTA = 0.0001F;

    public void test0() {
        assertEquals(0, Analysis.correlation(0, 0, 0, 0), DELTA);
    }

    public void test2() {
        assertEquals(0, Analysis.correlation(1, 1, 1, 1), DELTA);
    }

    public void test3() {
        assertEquals(1, Analysis.correlation(1, 1, 1, 2), DELTA);
    }

    public void test4() {
        assertEquals(0, Analysis.correlation(0, 1, 0, 1), DELTA);
    }

    public void test5() {
        assertEquals(0.5, Analysis.correlation(1, 2, 1, 3), DELTA);
    }

    public void test6() {
        assertEquals(-1, Analysis.correlation(1, 1, 0, 2), DELTA);
    }

    public void test7() {
        assertEquals(0.5, Analysis.correlation(20, 20, 15, 40), DELTA);
    }

    public void test8() {
        assertEquals(-0.5, Analysis.correlation(20, 20, 5, 40), DELTA);
    }

    public void test9() {
        assertEquals(0.5, Analysis.correlation(4, 4, 3, 8), DELTA);
    }
}
