package cc.novoline.modules.configurations.property.object;

import cc.novoline.modules.configurations.property.AbstractNumberProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LongProperty extends AbstractNumberProperty<LongProperty, Long> {

    /* constructors */
    public LongProperty(@Nullable Long value) {
        super(value);
    }

    public LongProperty() {
        this(0L);
    }

    public static @NotNull LongProperty of(@Nullable Long value) {
        return new LongProperty(value);
    }

    public static @NotNull LongProperty create() {
        return new LongProperty();
    }

    /* methods */
    @Override
    protected void add(@Nullable Long number) {
        this.value += number;
    }

    @Override
    protected void subtract(@Nullable Long number) {
        this.value -= number;
    }

    @Override
    protected boolean greaterThan(@Nullable Long number) {
        if (number == null) {
            return value != null;
        } else if (value == null) {
            return true;
        } else {
            return value > number;
        }
    }

    @Override
    protected boolean lessThan(@Nullable Long number) {
        if (number == null) {
            return false;
        } else if (value == null) {
            return true;
        } else {
            return value < number;
        }
    }

    @Override
    protected boolean inLimits(@NotNull Long number) {
        return (minimum == null || number >= minimum) && (maximum == null || number <= maximum);
    }

    @Override
    protected LongProperty self() {
        return this;
    }
}
