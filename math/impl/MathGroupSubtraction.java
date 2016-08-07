package com.meowingtwurtle.math.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import com.meowingtwurtle.math.api.IMathGroup;

public class MathGroupSubtraction  implements IMathGroup{
    
    IMathGroup[] components;

    public MathGroupSubtraction(IMathGroup... exps) {
        components = exps;
    }
    
    public MathGroupSubtraction(Collection<IMathGroup> exps) {
        this(exps.toArray(new IMathGroup[0]));
    }
    
    public BigDecimal eval() {
        BigDecimal ret = components[0].eval();
                
        for (int x = 1; x < components.length; x++) {
            ret = ret.subtract(components[x].eval());
        }
        
        return ret;
    }
    
    public String toString() {
        return this.getClass().getSimpleName() + Arrays.deepToString(components);
    }

}
