package io.github.nevalackin.client.property;

import io.github.nevalackin.client.property.Property;

import java.util.Arrays;
import java.util.List;

public final class MultiSelectionEnumProperty<T extends Enum<T>> extends Property<List<T>> {

    private final T[] values;

    @SuppressWarnings("unchecked")
    public MultiSelectionEnumProperty(String name, List<T> selected, T[] values, Dependency dependency) {
        super(name, selected, dependency);

        this.values = ((Class<T>) values[0].getClass()).getEnumConstants();
    }

    public MultiSelectionEnumProperty(String name, List<T> selected, T[] values) {
        this(name, selected, values, null);
    }

    public void select(int idx) {
        this.getValue().add(this.values[this.clamp(idx)]);
    }

    public void unselect(int idx) {
        this.getValue().remove(this.clamp(idx));
    }

    public boolean isSelected(final T value) {
        return this.getValue().contains(value);
    }

    public boolean hasSelections() {
        return !this.getValue().isEmpty();
    }

    private int clamp(int idx) {
        return Math.max(0, Math.min(this.values.length - 1, idx));
    }

    public String[] getValueNames() {
        return Arrays.stream(this.values)
            .map(Enum::toString)
            .toArray(String[]::new);
    }

    public int[] getValueIndices() {
        return this.getValue().stream()
            .mapToInt(Enum::ordinal)
            .toArray();
    }

    public T[] getValues() {
        return values;
    }
}
