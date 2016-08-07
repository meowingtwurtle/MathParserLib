package com.meowingtwurtle.math.impl.function;

import java.math.BigDecimal;

import com.meowingtwurtle.math.api.IMathGroup;

public class MathFunctionSin implements IMathFunction {
    
    private BigDecimal value;

    public MathFunctionSin(BigDecimal value) {
        this.value = value;
    }
    
    public MathFunctionSin(IMathGroup value) {
        this.value = value.eval();
    }

    @Override
    public BigDecimal eval() {
        return IMathFunction.evalTrigFunction(Math::sin, value);
    }
    
    public String toString() {
        return this.getClass().getSimpleName() +  "(" + value + ")";
    }

}
