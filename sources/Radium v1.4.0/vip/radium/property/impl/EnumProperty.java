package vip.radium.property.impl;

import vip.radium.property.Property;

import java.util.function.Supplier;

public class EnumProperty<T extends Enum<T>> extends Property<T> {

    private final T[] values;

    public EnumProperty(String label, T value, Supplier<Boolean> dependency) {
        super(label, value, dependency);

        this.values = getEnumConstants();
    }

    public EnumProperty(String label, T value) {
        this(label, value, () -> true);
    }

    @SuppressWarnings("unchecked")
    private T[] getEnumConstants() {
        return (T[]) value.getClass().getEnumConstants();
    }

    public T[] getValues() {
        return values;
    }

    public void setValue(int index) {
        setValue(values[index]);
    }
}
