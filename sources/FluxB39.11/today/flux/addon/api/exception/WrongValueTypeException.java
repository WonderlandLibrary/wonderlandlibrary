package today.flux.addon.api.exception;

public class WrongValueTypeException extends APIException {

    public WrongValueTypeException(String reason) {
        super(reason);
    }

}
