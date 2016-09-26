package com.meowingtwurtle.math.impl;

import com.meowingtwurtle.math.api.IMathGroup;

import java.math.BigDecimal;

public class MathGroupNegation implements IMathGroup {

    private final BigDecimal value;

    public MathGroupNegation(BigDecimal value) {
        this.value = value.negate();
    }

    public MathGroupNegation(IMathGroup value) {
        this.value = value.eval().negate();
    }

    @Override
    public BigDecimal eval() {
        return value;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "(" + value + ")";
    }

}
