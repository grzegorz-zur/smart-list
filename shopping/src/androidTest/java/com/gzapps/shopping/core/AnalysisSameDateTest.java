/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.MediumTest;

import java.util.Calendar;

import static com.gzapps.shopping.TimeValues.ONE_DAY_AGO;
import static com.gzapps.shopping.TimeValues.TODAY;
import static com.gzapps.shopping.TimeValues.TWO_DAYS_AGO;
import static com.gzapps.shopping.core.Time.HOUR;
import static com.gzapps.shopping.core.Time.MINUTE;

@MediumTest
public class AnalysisSameDateTest extends AnalysisTest {

    public void test0() {
        // when
        boolean same = analysis.sameDate(TWO_DAYS_AGO, TODAY);
        // then
        assertFalse(same);
    }

    public void test1() {
        // when
        boolean same = analysis.sameDate(ONE_DAY_AGO, TODAY);
        // then
        assertFalse(same);
    }

    public void test2() {
        // when
        boolean same = analysis.sameDate(TODAY, TODAY);
        // then
        assertTrue(same);
    }

    public void test3() {
        // when
        boolean same = analysis.sameDate(TODAY - MINUTE, TODAY);
        // then
        assertTrue(same);
    }

    public void test4() {
        // when
        boolean same = analysis.sameDate(TODAY - HOUR, TODAY);
        // then
        assertTrue(same);
    }

    public void test5() {
        // given
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long first = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long last = calendar.getTimeInMillis();

        // when
        boolean same = analysis.sameDate(first, last);

        // then
        assertTrue(same);
    }
}
