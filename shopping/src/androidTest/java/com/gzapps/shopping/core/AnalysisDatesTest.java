/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.MediumTest;

import org.apache.commons.collections.primitives.ArrayLongList;
import org.apache.commons.collections.primitives.LongList;

import static com.gzapps.shopping.TimeValues.ONE_DAY_AGO;
import static com.gzapps.shopping.TimeValues.TODAY;
import static com.gzapps.shopping.TimeValues.TWO_DAYS_AGO;
import static com.gzapps.shopping.core.Time.HOUR;
import static com.gzapps.shopping.core.Time.MINUTE;

@MediumTest
public class AnalysisDatesTest extends AnalysisTest {

    private LongList times;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        times = new ArrayLongList();
    }

    public void test0() {
        // when
        LongList dates = analysis.dates(times);
        // then
        assertEquals(0, dates.size());
    }

    public void test1() {
        // given
        times.add(TODAY);
        // when
        LongList dates = analysis.dates(times);
        // then
        assertEquals(1, dates.size());
    }

    public void test2() {
        // given
        times.add(TODAY - HOUR);
        times.add(TODAY + MINUTE);
        // when
        LongList dates = analysis.dates(times);
        // then
        assertEquals(1, dates.size());
    }

    public void test3() {
        // given
        times.add(TWO_DAYS_AGO + MINUTE);
        times.add(TWO_DAYS_AGO);
        times.add(ONE_DAY_AGO);
        times.add(TODAY);
        times.add(TODAY);
        // when
        LongList dates = analysis.dates(times);
        // then
        assertEquals(3, dates.size());
    }
}
