package com.meowingtwurtle.math.impl.function;

import com.meowingtwurtle.math.api.IMathGroup;

import java.math.BigDecimal;
import java.util.function.Function;

public interface IMathFunction extends IMathGroup {
    static BigDecimal evalTrigFunction(Function<Double, Double> func, BigDecimal input) {
        return BigDecimal.valueOf(func.apply(input.remainder(BigDecimal.valueOf(2 * Math.PI)).doubleValue()));
    }
}
