/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.MediumTest;

import java.util.Calendar;

@MediumTest
public class AnalysisDateTest extends AnalysisTest {

    public void test0() {
        // given
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();
        // when
        long date = analysis.date(time);
        // then
        calendar.setTimeInMillis(date);
        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }

    public void test1() {
        // given
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long time = calendar.getTimeInMillis();
        // when
        long date = analysis.date(time);
        // then
        assertEquals(time, date);
    }
}
