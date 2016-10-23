/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.MediumTest;

import org.apache.commons.collections.primitives.ArrayLongList;
import org.apache.commons.collections.primitives.LongList;

import static com.gzapps.shopping.TimeValues.ONE_DAY_AGO;
import static com.gzapps.shopping.TimeValues.THREE_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.TODAY;
import static com.gzapps.shopping.TimeValues.TWO_DAYS_AGO;

@MediumTest
public class AnalysisCountCorrelatedTest extends AnalysisTest {

    private LongList dates1;

    private LongList dates2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dates1 = new ArrayLongList();
        dates2 = new ArrayLongList();
    }

    public void test0() {
        // when
        int count = Analysis.countCorrelated(dates1, dates2);
        // then
        assertEquals(0, count);
    }

    public void test1() {
        // given
        dates1.add(TODAY);
        // when
        int count = Analysis.countCorrelated(dates1, dates2);
        // then
        assertEquals(0, count);
    }

    public void test2() {
        // given
        dates2.add(TODAY);
        // when
        int count = Analysis.countCorrelated(dates1, dates2);
        // then
        assertEquals(0, count);
    }

    public void test3() {
        // given
        dates1.add(TODAY);
        dates2.add(TODAY);
        // when
        int count = Analysis.countCorrelated(dates1, dates2);
        // then
        assertEquals(1, count);
    }

    public void test4() {
        // given
        dates1.add(ONE_DAY_AGO);
        dates2.add(TODAY);
        // when
        int count = Analysis.countCorrelated(dates1, dates2);
        // then
        assertEquals(0, count);
    }

    public void test5() {
        // given
        dates1.add(ONE_DAY_AGO);

        dates2.add(ONE_DAY_AGO);
        dates2.add(TODAY);
        // when
        int count = Analysis.countCorrelated(dates1, dates2);
        // then
        assertEquals(1, count);
    }

    public void test6() {
        // given
        dates1.add(THREE_DAYS_AGO);
        dates1.add(TWO_DAYS_AGO);
        dates1.add(ONE_DAY_AGO);
        dates1.add(TODAY);

        dates2.add(TWO_DAYS_AGO);
        dates2.add(TODAY);
        // when
        int count = Analysis.countCorrelated(dates1, dates2);
        // then
        assertEquals(2, count);
    }

    public void test7() {
        // given
        dates1.add(TWO_DAYS_AGO);
        dates1.add(TODAY);

        dates2.add(THREE_DAYS_AGO);
        dates2.add(TWO_DAYS_AGO);
        dates2.add(ONE_DAY_AGO);
        dates2.add(TODAY);
        // when
        int count = Analysis.countCorrelated(dates1, dates2);
        // then
        assertEquals(2, count);
    }

    public void test8() {
        // given
        dates2.add(TODAY);
        // when
        int count = Analysis.countCorrelated(dates1, dates2);
        // then
        assertEquals(0, count);
    }

    public void test9() {
        // given
        dates2.add(TODAY);
        // when
        int count = Analysis.countCorrelated(dates1, dates2);
        // then
        assertEquals(0, count);
    }
}
