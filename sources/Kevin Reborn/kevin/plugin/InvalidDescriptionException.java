package kevin.plugin;

public class InvalidDescriptionException extends Exception {
    InvalidDescriptionException(String reason) {
        super(reason);
    }
}
