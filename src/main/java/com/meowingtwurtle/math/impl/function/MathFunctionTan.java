package com.meowingtwurtle.math.impl.function;

import com.meowingtwurtle.math.api.IMathGroup;

import java.math.BigDecimal;

public class MathFunctionTan implements IMathFunction {

    private final BigDecimal value;

    public MathFunctionTan(BigDecimal value) {
        this.value = value;
    }

    public MathFunctionTan(IMathGroup value) {
        this.value = value.eval();
    }

    @Override
    public BigDecimal eval() {
        return IMathFunction.evalTrigFunction(Math::tan, value);
    }

    public String toString() {
        return this.getClass().getSimpleName() + "(" + value + ")";
    }

}
