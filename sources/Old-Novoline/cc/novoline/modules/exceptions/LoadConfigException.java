package cc.novoline.modules.exceptions;

/**
 * @author xDelsy
 */
public final class LoadConfigException extends RuntimeException {

    public LoadConfigException() {
    }

    public LoadConfigException(String message) {
        super(message);
    }

    public LoadConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadConfigException(Throwable cause) {
        super(cause);
    }

}
