/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import android.test.suitebuilder.annotation.SmallTest;

import org.apache.commons.collections.primitives.ArrayLongList;
import org.apache.commons.collections.primitives.LongList;

@SmallTest
public class AnalysisMergeDatesTest extends AnalysisTest {

    private LongList source;

    private LongList target;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        source = new ArrayLongList();
        target = new ArrayLongList();
    }

    public void test0() {
        // when
        Analysis.mergeDates(source, target);
        // then
        assertEquals(0, target.size());
    }

    public void test1() {
        // given
        source.add(1);
        // when
        Analysis.mergeDates(source, target);
        // then
        assertEquals(1, target.size());
    }

    public void test2() {
        // given
        source.add(1);
        target.add(1);
        // when
        Analysis.mergeDates(source, target);
        // then
        assertEquals(1, target.size());
    }

    public void test3() {
        // given
        source.add(1);
        target.add(2);
        // when
        Analysis.mergeDates(source, target);
        // then
        assertEquals(2, target.size());
        assertEquals(1, target.get(0));
        assertEquals(2, target.get(1));
    }

    public void test4() {
        // given
        target.add(2);
        source.add(1);
        source.add(3);
        // when
        Analysis.mergeDates(source, target);
        // then
        assertEquals(3, target.size());
        assertEquals(1, target.get(0));
        assertEquals(2, target.get(1));
        assertEquals(3, target.get(2));
    }

    public void test5() {
        // given
        target.add(1);
        target.add(3);
        target.add(5);
        target.add(7);
        source.add(2);
        source.add(4);
        source.add(6);
        source.add(8);
        // when
        Analysis.mergeDates(source, target);
        // then
        assertEquals(8, target.size());
        assertEquals(1, target.get(0));
        assertEquals(2, target.get(1));
        assertEquals(3, target.get(2));
        assertEquals(4, target.get(3));
        assertEquals(5, target.get(4));
        assertEquals(6, target.get(5));
        assertEquals(7, target.get(6));
        assertEquals(8, target.get(7));
    }
}
