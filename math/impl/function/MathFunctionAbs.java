package com.meowingtwurtle.math.impl.function;

import java.math.BigDecimal;

import com.meowingtwurtle.math.api.IMathGroup;

public class MathFunctionAbs implements IMathFunction {
    
    private BigDecimal value;

    public MathFunctionAbs(BigDecimal value) {
        this.value = value;
    }
    
    public MathFunctionAbs(IMathGroup value) {
        this.value = value.eval();
    }

    @Override
    public BigDecimal eval() {
        return value.abs();
    }
    
    public String toString() {
        return this.getClass().getSimpleName() +  "(" + value + ")";
    }

}
