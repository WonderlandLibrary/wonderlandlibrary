package today.flux.addon.api.exception;

public class NoSuchValueException extends APIException {
    public NoSuchValueException(String reason) {
        super(reason);
    }
}
