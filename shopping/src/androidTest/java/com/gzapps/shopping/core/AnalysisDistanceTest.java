/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.MediumTest;

import org.apache.commons.collections.primitives.ArrayLongList;
import org.apache.commons.collections.primitives.LongList;

import static com.gzapps.shopping.TimeValues.FIVE_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.FOUR_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.ONE_DAY_AGO;
import static com.gzapps.shopping.TimeValues.SEVEN_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.SIX_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.THREE_DAYS_AGO;
import static com.gzapps.shopping.TimeValues.TODAY;
import static com.gzapps.shopping.TimeValues.TWO_DAYS_AGO;
import static com.gzapps.shopping.core.Time.HOUR;
import static com.gzapps.shopping.core.Time.MINUTE;

@MediumTest
public class AnalysisDistanceTest extends AnalysisTest {

    private LongList times1;

    private LongList times2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        times1 = new ArrayLongList();
        times2 = new ArrayLongList();
    }

    public void test0() {
        // when
        long distance = analysis.distance(times1, times2);
        // then
        assertEquals(-1, distance);
    }

    public void test1() {
        // given
        times1.add(TODAY);
        // when
        long distance = analysis.distance(times1, times2);
        // then
        assertEquals(-1, distance);
    }

    public void test2() {
        // given
        times1.add(TODAY);
        times2.add(TODAY);
        // when
        long distance = analysis.distance(times1, times2);
        // then
        assertEquals(0, distance);
    }

    public void test3() {
        // given
        times1.add(ONE_DAY_AGO);
        times2.add(TODAY);
        // when
        long distance = analysis.distance(times1, times2);
        // then
        assertEquals(-1, distance);
    }

    public void test4() {
        // given
        times1.add(TODAY - HOUR);
        times2.add(TODAY + HOUR);
        // when
        long distance = analysis.distance(times1, times2);
        // then
        assertEquals(2 * HOUR, distance);
    }

    public void test5() {
        // given
        times1.add(TWO_DAYS_AGO - 2 * HOUR);
        times2.add(TWO_DAYS_AGO + 2 * HOUR);
        times1.add(ONE_DAY_AGO - HOUR);
        times2.add(ONE_DAY_AGO + HOUR);
        times1.add(TODAY - MINUTE);
        times2.add(TODAY + MINUTE);
        // when
        long distance = analysis.distance(times1, times2);
        // then
        assertEquals(2 * HOUR, distance);
    }

    public void test6() {
        // given
        times1.add(SEVEN_DAYS_AGO);
        times2.add(SIX_DAYS_AGO);
        times1.add(FIVE_DAYS_AGO - 2 * HOUR);
        times2.add(FIVE_DAYS_AGO + 2 * HOUR);
        times1.add(FOUR_DAYS_AGO);
        times2.add(THREE_DAYS_AGO);
        times1.add(ONE_DAY_AGO - HOUR);
        times2.add(ONE_DAY_AGO + HOUR);
        times1.add(TODAY - 2 * MINUTE);
        times2.add(TODAY - MINUTE);
        // when
        long distance = analysis.distance(times1, times2);
        // then
        assertEquals(2 * HOUR, distance);
    }

    public void test7() {
        // given
        times1.add(THREE_DAYS_AGO);
        times1.add(TWO_DAYS_AGO);
        times2.add(TWO_DAYS_AGO);
        times1.add(ONE_DAY_AGO);
        times1.add(TODAY);
        times2.add(TODAY);
        // when
        int count = Analysis.countCorrelated(times1, times2);
        // then
        assertEquals(2, count);
    }
}
