package cc.novoline.modules.configurations.property.object;

import cc.novoline.modules.configurations.property.AbstractNumberProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FloatProperty extends AbstractNumberProperty<FloatProperty, Float> {

    /* constructors */
    public FloatProperty(@Nullable Float value) {
        super(value);
    }

    public FloatProperty() {
        this(0.0F);
    }

    public static @NotNull FloatProperty of(@Nullable Float value) {
        return new FloatProperty(value);
    }

    public static @NotNull FloatProperty create() {
        return new FloatProperty();
    }

    /* methods */
    @Override
    protected void add(@Nullable Float number) {
        this.value += number;
    }

    @Override
    protected void subtract(@Nullable Float number) {
        this.value -= number;
    }

    @Override
    protected boolean greaterThan(@Nullable Float number) {
        if(number == null) {
            return value != null;
        } else if(value == null) {
            return true;
        } else {
            return value > number;
        }
    }

    @Override
    protected boolean lessThan(@Nullable Float number) {
        if(number == null) {
            return false;
        } else if(value == null) {
            return true;
        } else {
            return value < number;
        }
    }

    @Override
    protected boolean inLimits(@NotNull Float number) {
        return (minimum == null || number >= minimum) && (maximum == null || number <= maximum);
    }

    @Override
    protected FloatProperty self() {
        return this;
    }

}
