/*
 * Copyright (c) 2011-2014 Grzegorz Żur
 */

package com.gzapps.shopping.app;

import junit.framework.TestCase;

import java.math.BigDecimal;

public class StepValueTest extends TestCase {

    public void test00() {
        // given
        StepValue value = new StepValue("0");
        // when
        value.increase();
        // then
        assertEquals(BigDecimal.ONE, value.value());
    }

    public void test01() {
        // given
        StepValue value = new StepValue("0.0");
        // when
        value.increase();
        // then
        assertEquals(new BigDecimal("0.1"), value.value());
    }

    public void test02() {
        // given
        StepValue value = new StepValue("0.1");
        // when
        value.increase();
        // then
        assertEquals(new BigDecimal("0.2"), value.value());
    }

    public void test03() {
        // given
        StepValue value = new StepValue("0.001");
        // when
        value.increase();
        // then
        assertEquals(new BigDecimal("0.002"), value.value());
    }

    public void test04() {
        // given
        StepValue value = new StepValue("1");
        // when
        value.increase();
        // then
        assertEquals(new BigDecimal("2"), value.value());
    }

    public void test05() {
        // given
        StepValue value = new StepValue("100");
        // when
        value.increase();
        // then
        assertEquals(new BigDecimal("200"), value.value());
    }

    public void test06() {
        // given
        StepValue value = new StepValue("1010");
        // when
        value.increase();
        // then
        assertEquals(new BigDecimal("1020"), value.value());
    }

    public void test07() {
        // given
        StepValue value = new StepValue("0.010");
        // when
        value.increase();
        // then
        assertEquals(new BigDecimal("0.020"), value.value());
    }

    public void test08() {
        // given
        StepValue value = new StepValue("0.030");
        // when
        value.decrease();
        // then
        assertEquals(new BigDecimal("0.020"), value.value());
    }

    public void test09() {
        // given
        StepValue value = new StepValue("9");
        // when
        value.increase();
        value.increase();
        // then
        assertEquals(new BigDecimal("11"), value.value());
    }

    public void test10() {
        // given
        StepValue value = new StepValue("0.090");
        // when
        value.increase();
        value.increase();
        // then
        assertEquals(new BigDecimal("0.110"), value.value());
    }
}
