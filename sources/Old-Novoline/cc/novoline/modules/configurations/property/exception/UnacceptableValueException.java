package cc.novoline.modules.configurations.property.exception;

import cc.novoline.modules.configurations.property.Property;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author xDelsy
 */
public final class UnacceptableValueException extends PropertyException {

    public UnacceptableValueException(@NonNull Property<?> property) {
        super(property);
    }

    public UnacceptableValueException(String message, @NonNull Property<?> property) {
        super(message, property);
    }

    public UnacceptableValueException(String message, Throwable cause, @NonNull Property<?> property) {
        super(message, cause, property);
    }

    public UnacceptableValueException(Throwable cause, @NonNull Property<?> property) {
        super(cause, property);
    }

}
