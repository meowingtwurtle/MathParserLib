package com.meowingtwurtle.math.impl;

import com.meowingtwurtle.math.api.IMathGroup;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class MathGroupDivision implements IMathGroup {

    private final IMathGroup[] components;

    public MathGroupDivision(IMathGroup[] exps) {
        components = exps;
    }

    public BigDecimal eval() {
        BigDecimal ret = components[0].eval();

        for (int x = 1; x < components.length; x++) {
            ret = ret.divide(components[x].eval(), 8, RoundingMode.HALF_EVEN);
        }

        return ret;
    }

    public String toString() {
        return this.getClass().getSimpleName() + Arrays.deepToString(components);
    }

}
