package com.meowingtwurtle.math.impl;

import com.meowingtwurtle.math.api.IMathGroup;

import java.math.BigDecimal;

public class MathGroupBasic implements IMathGroup {

    private final BigDecimal value;

    public MathGroupBasic(BigDecimal value) {
        this.value = value;
    }

    public MathGroupBasic(IMathGroup value) {
        this.value = value.eval();
    }

    @Override
    public BigDecimal eval() {
        return value;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "(" + value + ")";
    }

}
