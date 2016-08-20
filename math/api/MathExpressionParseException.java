package com.meowingtwurtle.math.api;

/**
 * Created by jason on 8/20/16.
 */
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
