package impl.function;

import api.IMathGroup;

import java.math.BigDecimal;

public class MathFunctionAbs implements IMathFunction {

    private final BigDecimal value;

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
        return this.getClass().getSimpleName() + "(" + value + ")";
    }

}
