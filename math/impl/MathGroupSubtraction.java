package impl;

import api.IMathGroup;

import java.math.BigDecimal;
import java.util.Arrays;

public class MathGroupSubtraction implements IMathGroup {

    private final IMathGroup[] components;

    public MathGroupSubtraction(IMathGroup... exps) {
        components = exps;
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
