package com.meowingtwurtle.math.api;

public class MathExpressionParseException extends RuntimeException {
    public MathExpressionParseException() {
        super();
    }
    public MathExpressionParseException(String reason) {
        super(reason);
    }
    public MathExpressionParseException(Throwable cause) {
        super(cause);
    }
}
