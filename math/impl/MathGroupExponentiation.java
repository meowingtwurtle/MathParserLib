package com.meowingtwurtle.math.impl;

import com.meowingtwurtle.math.api.IMathGroup;

import java.math.BigDecimal;
import java.util.Arrays;

public class MathGroupExponentiation implements IMathGroup {

    private final IMathGroup[] components;

    public MathGroupExponentiation(IMathGroup[] exps) {
        components = exps;
    }

    public BigDecimal eval() {
        BigDecimal ret = components[components.length - 1].eval();
                
        for (int x = components.length - 2; x >= 0; x--) {
//            ret = components[x].eval().pow(ret.intValueExact());
            ret = BigDecimal.valueOf(Math.pow(components[x].eval().doubleValue(), ret.doubleValue()));
        }
        
        return ret;
    }
    
    public String toString() {        
        return this.getClass().getSimpleName() + Arrays.deepToString(components);
    }

}
