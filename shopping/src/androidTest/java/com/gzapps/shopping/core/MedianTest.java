/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.core;

import junit.framework.TestCase;

public class MedianTest extends TestCase {

    private Median median;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        median = new Median(20);
    }

    public void test0() {
        try {
            // when
            median.calculate();
            fail();
            // then
        } catch (IllegalStateException e) {
        }
    }

    public void test1() {
        // given
        median.add(1);
        // when
        long value = median.calculate();
        // then
        assertEquals(1, value);
    }

    public void test2() {
        // given
        median.add(new long[]{1, 1});
        // when
        long value = median.calculate();
        // then
        assertEquals(1, value);
    }

    public void test3() {
        // given
        median.add(new long[]{2, 3, 2, 1});
        // when
        long value = median.calculate();
        // then
        assertEquals(2, value);
    }

    public void test4() {
        // given
        median.add(new long[]{2, 1, 3});
        // when
        long value = median.calculate();
        // then
        assertEquals(2, value);
    }

    public void test5() {
        // given
        median.add(new long[]{2, 1, 3, 4, 7, 3});
        // when
        long value = median.calculate();
        // then
        assertEquals(3, value);
    }

    public void test6() {
        // given
        median.add(new long[]{2, 1, 2, 4, 2, 8, 9, 3, 5, 4, 1, 2, 9, 7, 6});
        // when
        long value = median.calculate();
        // then
        assertEquals(4, value);
    }

    public void test7() {
        // given
        median.add(new long[]{9, 8, 7, 6, 5, 4, 3, 2, 1});
        // when
        long value = median.calculate();
        // then
        assertEquals(5, value);
    }

    public void test8() {
        // given                                          s
        median.add(new long[]{9, 8, 7, 6, 5, 1, 2, 3, 4, 5});
        // when
        long value = median.calculate();
        // then
        assertEquals(5, value);
    }

    public void test9() {
        // given                                          s
        median.add(new long[]{7, 2, 3, 1, 1});
        // when
        long value = median.calculate();
        // then
        assertEquals(2, value);
    }
}
