package impl.function;

import api.IMathGroup;

import java.math.BigDecimal;

public class MathFunctionSin implements IMathFunction {

    private final BigDecimal value;

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
        return this.getClass().getSimpleName() + "(" + value + ")";
    }

}
