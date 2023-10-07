package cc.novoline.modules.configurations.property.object;

import cc.novoline.modules.configurations.property.AbstractProperty;
import cc.novoline.modules.configurations.property.exception.UnacceptableValueException;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StringProperty extends AbstractProperty<String> {

    /* fields */
    private List<String> acceptableValues;

    /* constructors */
    public StringProperty(@Nullable String value) {
        super(value);
    }

    public StringProperty() {
        this("");
    }

    public static @NotNull StringProperty of(@Nullable String value) {
        return new StringProperty(value);
    }

    public static @NotNull StringProperty create() {
        return new StringProperty();
    }

    /* methods */
    @Override
    public void set(@Nullable String value) {
        if(value == null || inLimits(value)) {
            super.set(value);
        } else {
            throw new UnacceptableValueException("Unable to set " + value + " as it's unacceptable value: " + value, this);
        }
    }

    private boolean inLimits(@NotNull String value) {
        if(acceptableValues == null || acceptableValues.isEmpty()) return true;
        return acceptableValues.contains(value);
    }

    public boolean equals(@Nullable String s) {
        if(s == null) return value == null;
        return s.equals(value);
    }

    public boolean equalsIgnoreCase(@Nullable String s) {
        if(s == null) return value == null;
        return s.equalsIgnoreCase(value);
    }

    public StringProperty append(@Nullable Object o) {
        set(get() + o);
        return this;
    }

    public StringProperty acceptableValues(@NotNull Collection<String> values) {
        this.acceptableValues = new ObjectArrayList<>(values);
        return this;
    }

    public final StringProperty acceptableValues(@NotNull String... values) {
        this.acceptableValues = new ObjectArrayList<>(values);
        return this;
    }

    //region Lombok
    public List<String> getAcceptableValues() {
        return acceptableValues;
    }
    //endregion
}
