/*
 Copyright (c) 2011-2013 GZapps Grzegorz Żur
 */

package com.gzapps.shopping.app;

import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
public class StepValue {

    public BigDecimal value;

    protected BigDecimal step;

    StepValue() {
        changeValue(BigDecimal.ZERO);
    }

    StepValue(String value) {
        changeValue(value);
    }

    StepValue(BigDecimal value) {
        changeValue(value);
    }

    public void changeValue(String value) {
        changeValue(new BigDecimal(value));
    }

    public void changeValue(BigDecimal value) {
        this.value = value;
        calculateStep();
    }

    public BigDecimal value() {
        return value;
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }

    public void increase() {
        step(step);
    }

    public void decrease() {
        step(step.negate());
    }

    protected void step(BigDecimal step) {
        value = value.add(step);
    }

    protected void calculateStep() {
        step = new BigDecimal(BigInteger.ONE, value.scale());
        BigInteger value = this.value.unscaledValue();

        while (value.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divideAndReminder =
                    value.divideAndRemainder(BigInteger.TEN);
            BigInteger divide = divideAndReminder[0];
            BigInteger reminder = divideAndReminder[1];

            if (reminder.compareTo(BigInteger.ZERO) == 0) {
                step = step.multiply(BigDecimal.TEN);
            } else {
                break;
            }

            value = divide;
        }
    }
}
