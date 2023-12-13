package io.github.nevalackin.client.property;

import java.util.ArrayList;
import java.util.List;

public abstract class Property<T> {

    private final String name;
    private T value;
    private final List<ValueChangeListener<T>> valueChangeListeners;
    private final Dependency dependency;

    private List<Property<?>> attachedProperties;

    public Property(final String name, final T value, final Dependency dependency) {
        this.name = name;
        this.valueChangeListeners = new ArrayList<>();
        this.dependency = dependency;

        this.setValue(value);
    }

    public Property(final String name, final T value) {
        this(name, value, null);
    }

    public void addChangeListener(final ValueChangeListener<T> changeListener) {
        this.valueChangeListeners.add(changeListener);
    }

    public T getValue() {
        return value;
    }

    public boolean check() {
        return this.dependency == null || this.dependency.test();
    }

    public void setValue(final T value) {
        for (final ValueChangeListener<T> changeListener : this.valueChangeListeners) {
            changeListener.onChange(value);
        }

        this.value = value;
    }

    public List<Property<?>> getAttachedProperties() {
        return attachedProperties;
    }

    public void attachProperty(final Property<?> property) {
        if (this.attachedProperties == null) this.attachedProperties = new ArrayList<>();
        this.attachedProperties.add(property);
    }

    public String getName() {
        return name;
    }

    @FunctionalInterface
    public interface Dependency {
        boolean test();
    }
}
