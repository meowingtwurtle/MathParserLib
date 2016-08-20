package com.meowingtwurtle.math.api;

import com.meowingtwurtle.math.impl.MathHandlerImpl;

public interface IMathHandler {
    IMathGroup parse(String exp);
    
    static IMathHandler getMathHandler() {
        return MathHandlerImpl.INSTANCE;
    }
}
