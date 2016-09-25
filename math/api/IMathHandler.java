package api;

import impl.MathHandlerImpl;

public interface IMathHandler {
    IMathGroup parse(String exp);

    static IMathHandler getMathHandler() {
        return MathHandlerImpl.INSTANCE;
    }
}
