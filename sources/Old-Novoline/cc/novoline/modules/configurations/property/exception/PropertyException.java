package cc.novoline.modules.configurations.property.exception;

import cc.novoline.modules.configurations.property.Property;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author xDelsy
 */
public class PropertyException extends RuntimeException {

    /* fields */
    @NonNull
    private final Property<?> property;

    /* constructors */
    public PropertyException(@NonNull Property<?> property) {
        this.property = property;
    }

    public PropertyException(String message, @NonNull Property<?> property) {
        super(message);
        this.property = property;
    }

    public PropertyException(String message, Throwable cause, @NonNull Property<?> property) {
        super(message, cause);
        this.property = property;
    }

    public PropertyException(Throwable cause, @NonNull Property<?> property) {
        super(cause);
        this.property = property;
    }

    /* methods */
    //region Lombok
    @NonNull
    public Property<?> getProperty() {
        return this.property;
    }
    //endregion

}
