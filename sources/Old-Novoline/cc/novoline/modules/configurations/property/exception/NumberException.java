package cc.novoline.modules.configurations.property.exception;

import cc.novoline.modules.configurations.property.AbstractNumberProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author xDelsy
 */
public class NumberException extends PropertyException {

    /* constructors */
    public NumberException(@NonNull AbstractNumberProperty<?, ?> property) {
        super(property);
    }

    public NumberException(String message, @NonNull AbstractNumberProperty<?, ?> property) {
        super(message, property);
    }

    public NumberException(String message, Throwable cause, @NonNull AbstractNumberProperty<?, ?> property) {
        super(message, cause, property);
    }

    public NumberException(Throwable cause, @NonNull AbstractNumberProperty<?, ?> property) {
        super(cause, property);
    }

}
