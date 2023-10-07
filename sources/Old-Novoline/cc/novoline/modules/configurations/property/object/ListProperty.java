package cc.novoline.modules.configurations.property.object;

import cc.novoline.modules.configurations.property.AbstractProperty;
import cc.novoline.modules.configurations.property.exception.UnacceptableValueException;
import com.google.common.collect.Collections2;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * @author xDelsy
 */
public final class ListProperty<Type> extends AbstractProperty<List<Type>> {

    /* fields */
    protected List<Type> acceptableValues;

    /* constructors */
    public ListProperty(@NotNull List<Type> values) {
        super(values);
    }

    public ListProperty(@NotNull Collection<Type> values) {
        this(new ObjectArrayList<>(values));
    }

    @SafeVarargs
    public ListProperty(@NotNull Type... values) {
        this(new ObjectArrayList<>(values));
    }

    public ListProperty(@Nullable Type value) {
        this(new ObjectArrayList<>(Collections.singletonList(value)));
    }

    public ListProperty(@Nullable Type value1, @Nullable Type value2) {
        this(new ObjectArrayList<>(asList(value1, value2)));
    }

    public ListProperty(@Nullable Type value1, @Nullable Type value2, @Nullable Type value3) {
        this(new ObjectArrayList<>(asList(value1, value2, value3)));
    }

    public ListProperty() {
        this(new ObjectArrayList<>());
    }

    public static @NotNull <Type> ListProperty<Type> of(@NotNull List<Type> values) {
        return new ListProperty<>(values);
    }

    public static @NotNull <Type> ListProperty<Type> of(@NotNull Collection<Type> value) {
        return new ListProperty<>(new ObjectArrayList<>(value));
    }

    @SafeVarargs
    public static @NotNull <Type> ListProperty<Type> of(@NotNull Type... values) {
        return new ListProperty<>(values);
    }

    public static @NotNull <Type> ListProperty<Type> of(@Nullable Type value) {
        return new ListProperty<>(value);
    }

    public static @NotNull <Type> ListProperty<Type> of(@Nullable Type value1, @Nullable Type value2) {
        return new ListProperty<>(value1, value2);
    }

    public static @NotNull <Type> ListProperty<Type> of(@Nullable Type value1, @Nullable Type value2,
                                                        @Nullable Type value3) {
        return new ListProperty<>(value1, value2, value3);
    }

    public static @NotNull <Type> ListProperty<Type> empty() {
        return new ListProperty<>();
    }

    /* methods */
    @Override
    public void set(@Nullable List<Type> value) {
        if (value == null || inLimits(value)) {
            super.set(value);
        } else {
            throw new UnacceptableValueException(
                    "Unable to set " + value + " as it contains unacceptable value(s): " + Collections2
                            .filter(value, input -> !acceptableValues.contains(input)), this);
        }
    }

    private boolean inLimits(@NotNull List<Type> value) {
        if (acceptableValues == null || value.isEmpty() || acceptableValues.isEmpty()) return true;

        for (Type srcElement : value) {
            if (!acceptableValues.contains(srcElement)) return false;
        }

        return true;
    }

    public ListProperty<Type> acceptableValues(@NotNull Collection<Type> values) {
        this.acceptableValues = new ObjectArrayList<>(values);
        return this;
    }

    @SafeVarargs
    public final ListProperty<Type> acceptableValues(@NotNull Type... values) {
        this.acceptableValues = new ObjectArrayList<>(values);
        return this;
    }

    public Type get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return value != null ? value.get(index) : null;
    }

    public boolean contains(Type element) {
        return value != null && value.contains(element);
    }

    public int size() {
        return value != null ? value.size() : 0;
    }

    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }

    public boolean add(@Nullable Type element) {
        if (acceptableValues != null && !acceptableValues.contains(element)) return false;
        if (value == null) set(new ObjectArrayList<>());

        return value.add(element);
    }

    public boolean remove(@Nullable Type element) {
        return value != null && value.remove(element);
    }

    //region Lombok
    public List<Type> getAcceptableValues() {
        return acceptableValues;
    }
    //endregion

	/*@SafeVarargs
	public final void addAll(@NotNull Type... values) {
		if(value == null) set(new ObjectArrayList<>());
		Collections.addAll(value, values);
	}

	public void addAll(@NotNull Collection<Type> values) {
		if(value == null) set(new ObjectArrayList<>());
		value.addAll(values);
	}*/
}
