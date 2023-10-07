package cc.novoline.modules.configurations.property.exception;

import cc.novoline.modules.configurations.property.AbstractNumberProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author xDelsy
 */
public final class NumberLimitsException extends NumberException {

    public NumberLimitsException(@NonNull AbstractNumberProperty<?, ?> property) {
        super(property);
    }

    public NumberLimitsException(String message, @NonNull AbstractNumberProperty<?, ?> property) {
        super(message, property);
    }

    public NumberLimitsException(String message, Throwable cause, @NonNull AbstractNumberProperty<?, ?> property) {
        super(message, cause, property);
    }

    public NumberLimitsException(Throwable cause, @NonNull AbstractNumberProperty<?, ?> property) {
        super(cause, property);
    }

}
