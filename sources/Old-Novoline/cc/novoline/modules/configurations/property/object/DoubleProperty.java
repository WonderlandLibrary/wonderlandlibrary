package cc.novoline.modules.configurations.property.object;

import cc.novoline.modules.configurations.property.AbstractNumberProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author xDelsy
 */
public final class DoubleProperty extends AbstractNumberProperty<DoubleProperty, Double> {

    /* constructors */
    public DoubleProperty(@Nullable Double value) {
        super(value);
    }

    public DoubleProperty() {
        super(0.0D);
    }

    public static @NotNull DoubleProperty of(@Nullable Double value) {
        return new DoubleProperty(value);
    }

    public static @NotNull DoubleProperty create() {
        return new DoubleProperty();
    }

    /* methods */
    @Override
    protected void add(@Nullable Double number) {
        this.value += number;
    }

    @Override
    protected void subtract(@Nullable Double number) {
        this.value -= number;
    }

    @Override
    protected boolean greaterThan(@Nullable Double number) {
        if (number == null) {
            return value != null;
        } else if (value == null) {
            return true;
        } else {
            return value > number;
        }
    }

    @Override
    protected boolean lessThan(@Nullable Double number) {
        if (number == null) {
            return false;
        } else if (value == null) {
            return true;
        } else {
            return value < number;
        }
    }

    @Override
    protected boolean inLimits(@NotNull Double number) {
        return (minimum == null || number >= minimum) && (maximum == null || number <= maximum);
    }

    @Override
    protected DoubleProperty self() {
        return this;
    }

}
