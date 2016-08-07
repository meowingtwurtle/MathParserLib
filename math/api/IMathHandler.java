package com.meowingtwurtle.math.api;

import com.meowingtwurtle.math.impl.MathHandlerImpl;

public interface IMathHandler {
    public IMathGroup parse(String exp);
    
    public static IMathHandler getMathHandler() {
        return MathHandlerImpl.INSTANCE;
    }
}
