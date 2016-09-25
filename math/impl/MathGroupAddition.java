package impl;

import api.IMathGroup;

import java.math.BigDecimal;
import java.util.Arrays;

public class MathGroupAddition implements IMathGroup {

    private final IMathGroup[] components;

    public MathGroupAddition(IMathGroup... exps) {
        components = exps;
    }

    public BigDecimal eval() {
        BigDecimal ret = BigDecimal.ZERO;

        for (IMathGroup x : components) {
            ret = ret.add(x.eval());
        }

        return ret;
    }

    public String toString() {
        return this.getClass().getSimpleName() + Arrays.deepToString(components);
    }

}
