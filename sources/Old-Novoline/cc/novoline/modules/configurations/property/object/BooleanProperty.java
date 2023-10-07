package cc.novoline.modules.configurations.property.object;

import cc.novoline.modules.configurations.property.AbstractProperty;
import cc.novoline.utils.java.Lazy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author xDelsy
 */
public class BooleanProperty extends AbstractProperty<Boolean> {

    /* fields */
    private static final Lazy<BooleanProperty> TRUE = Lazy.createThreadSafe(() -> new ImmutableBooleanProperty(true));
    private static final Lazy<BooleanProperty> FALSE = Lazy.createThreadSafe(() -> new ImmutableBooleanProperty(false));

    /* constructors */
    public BooleanProperty(@Nullable Boolean value) {
        super(value != null ? value : false);
    }

    public BooleanProperty() {
        this(false);
    }

    public static @NotNull BooleanProperty of(@Nullable Boolean value) {
        return new BooleanProperty(value);
    }

    public static @NotNull BooleanProperty create() {
        return new BooleanProperty();
    }

    /* methods */
    public static BooleanProperty alwaysTrue() {
        return TRUE.get();
    }

    public static BooleanProperty alwaysFalse() {
        return FALSE.get();
    }

    public void setTrue() {
        set(true);
    }

    public void setFalse() {
        set(false);
    }

    public void invert() {
        this.value = value == null || !value;
    }

    /* inner classes */
    private static final class ImmutableBooleanProperty extends BooleanProperty {

        private ImmutableBooleanProperty(@Nullable Boolean value) {
            super(value);
        }

        @Override
        public void set(@Nullable Boolean value) {
            throw new UnsupportedOperationException("Tried to change immutable property");
        }
    }
}
