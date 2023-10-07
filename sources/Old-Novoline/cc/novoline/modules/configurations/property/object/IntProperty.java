package cc.novoline.modules.configurations.property.object;

import cc.novoline.modules.configurations.property.AbstractNumberProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IntProperty extends AbstractNumberProperty<IntProperty, Integer> {

    /* constructors */
    public IntProperty(@Nullable Integer value) {
        super(value);
    }

    public IntProperty() {
        this(0);
    }

    public static @NotNull IntProperty of(@Nullable Integer value) {
        return new IntProperty(value);
    }

    public static @NotNull IntProperty create() {
        return new IntProperty();
    }

    /* methods */
    @Override
    protected boolean inLimits(@NotNull Integer number) {
        return (minimum == null || number >= minimum) && (maximum == null || number <= maximum);
    }

    @Override
    public void add(@Nullable Integer number) {
        if (value == null) {
            this.value = number;
        } else if (number != null) {
            set(value + number);
        }
    }

    @Override
    public void subtract(@Nullable Integer number) {
        if (value == null) {
            this.value = number;
        } else if (number != null) {
            set(value - number);
        }
    }

    @Override
    public boolean greaterThan(@Nullable Integer number) {
        if (number == null) {
            return value != null;
        } else if (value == null) {
            return true;
        } else {
            return value > number;
        }
    }

    @Override
    public boolean lessThan(@Nullable Integer number) {
        if (number == null) {
            return false;
        } else if (value == null) {
            return true;
        } else {
            return value < number;
        }
    }

    @Override
    protected IntProperty self() {
        return this;
    }

}
