package cc.novoline.modules.exceptions;

/**
 * @author xDelsy
 */
public final class UnregisteredModuleException extends RuntimeException {

    public UnregisteredModuleException() {
    }

    public UnregisteredModuleException(String message) {
        super(message);
    }

    public UnregisteredModuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnregisteredModuleException(Throwable cause) {
        super(cause);
    }

}
