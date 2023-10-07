package cc.novoline.modules.configurations.property;

import cc.novoline.modules.configurations.property.exception.NumberLimitsException;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author xDelsy
 */
public abstract class AbstractNumberProperty<Self extends AbstractNumberProperty<Self, Type>, Type extends Number> extends
        AbstractProperty<Type> {

    /* fields */
    protected Type minimum, maximum;

    /* constructors */
    protected AbstractNumberProperty(Type value) {
        super(value);
    }

    protected AbstractNumberProperty() {
    }

    /* methods */
    @Override
    public void set(@Nullable Type value) {
        if(value == null || inLimits(value)) {
            super.set(value);
        } else {
            throw new NumberLimitsException("Unable to set " + value + " (min:" + minimum + ", max:" + maximum + ")", this);
        }
    }

    protected abstract void add(Type number);

    protected abstract void subtract(Type number);

    /**
     * @return {@code false} if {@code (number == null && value == null) || value <= number}
     */
    protected abstract boolean greaterThan(Type number);

    /**
     * @return {@code false} if {@code number == null || value >= number}
     */
    protected abstract boolean lessThan(Type number);

    protected abstract boolean inLimits(@NotNull Type number);

    protected boolean greaterOrEquals(Type number) {
        if (greaterThan(number)) {
            return true;
        } else if (number == null && value == null) {
            return false;
        } else {
            return Objects.equals(number, value);
        }
    }

    protected boolean lessOrEquals(Type number) {
        if (lessThan(number)) {
            return true;
        } else if (number == null) {
            return false;
        } else {
            return Objects.equals(number, value);
        }
    }

    public Self minimum(@Nullable Type minimum) {
        if (value != null && lessThan(minimum)) throw new IllegalArgumentException(
                "minimum is greater than current value: " + value + ", min: " + minimum);

        this.minimum = minimum;
        return self();
    }

    public Type getMinimum() {
        return minimum;
    }

    public Self maximum(@Nullable Type maximum) {
        if (value != null && greaterThan(maximum)) throw new IllegalArgumentException(
                "current value is greater than maximum: " + value + ", max: " + maximum);

        this.maximum = maximum;
        return self();
    }

    public Type getMaximum() {
        return maximum;
    }

    protected abstract Self self();

}
