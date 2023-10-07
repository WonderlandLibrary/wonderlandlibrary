package cc.novoline.modules.exceptions;

import java.io.IOException;

/**
 * @author xDelsy
 */
public final class ReadConfigException extends IOException {

    public ReadConfigException() {
    }

    public ReadConfigException(String message) {
        super(message);
    }

    public ReadConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadConfigException(Throwable cause) {
        super(cause);
    }

}
