package com.meowingtwurtle.math.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import com.meowingtwurtle.math.api.IMathGroup;

public class MathGroupMultiplication  implements IMathGroup{
    
    IMathGroup[] components;

    public MathGroupMultiplication(IMathGroup... exps) {
        components = exps;
    }
    
    public MathGroupMultiplication(Collection<IMathGroup> exps) {
        this(exps.toArray(new IMathGroup[0]));
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
