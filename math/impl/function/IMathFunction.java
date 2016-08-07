package com.meowingtwurtle.math.impl.function;

import java.math.BigDecimal;
import java.util.function.Function;

import com.meowingtwurtle.math.api.IMathGroup;

public interface IMathFunction extends IMathGroup {
    public static BigDecimal evalTrigFunction(Function<Double, Double> func, BigDecimal input) {
        return BigDecimal.valueOf(func.apply(input.remainder(BigDecimal.valueOf(2 * Math.PI)).doubleValue()));
    }
}
