package com.meowingtwurtle.math.impl.function;

import com.meowingtwurtle.math.api.IMathGroup;

import java.math.BigDecimal;

public class MathFunctionCos implements IMathFunction {

    private final BigDecimal value;

    public MathFunctionCos(BigDecimal value) {
        this.value = value;
    }

    public MathFunctionCos(IMathGroup value) {
        this.value = value.eval();
    }

    @Override
    public BigDecimal eval() {
        return IMathFunction.evalTrigFunction(Math::cos, value);
    }

    public String toString() {
        return this.getClass().getSimpleName() + "(" + value + ")";
    }

}
