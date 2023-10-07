package cc.novoline.modules.exceptions;

/**
 * @author xDelsy
 */
public final class OutdatedConfigException extends IllegalArgumentException {

    public OutdatedConfigException() {
    }

    public OutdatedConfigException(String s) {
        super(s);
    }

    public OutdatedConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutdatedConfigException(Throwable cause) {
        super(cause);
    }

}
