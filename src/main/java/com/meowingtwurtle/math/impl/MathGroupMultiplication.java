package com.meowingtwurtle.math.impl;

import com.meowingtwurtle.math.api.IMathGroup;

import java.math.BigDecimal;
import java.util.Arrays;

public class MathGroupMultiplication implements IMathGroup {

    private final IMathGroup[] components;

    public MathGroupMultiplication(IMathGroup... exps) {
        components = exps;
    }

    public BigDecimal eval() {
        BigDecimal ret = BigDecimal.ONE;

        for (IMathGroup x : components) {
            ret = ret.multiply(x.eval());
        }

        return ret;
    }

    public String toString() {
        return this.getClass().getSimpleName() + Arrays.deepToString(components);
    }

}
