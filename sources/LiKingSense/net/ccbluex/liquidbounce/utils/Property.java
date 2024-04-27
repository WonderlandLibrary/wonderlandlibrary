/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.ccbluex.liquidbounce.utils.ValueChangeListener;

public class Property<T> {
    protected final Class owner;
    protected final String label;
    protected final Supplier<Boolean> dependency;
    private final List<ValueChangeListener<T>> valueChangeListeners = new ArrayList<ValueChangeListener<T>>();
    protected T value;

    public Property(String label, T value, Supplier<Boolean> dependency) {
        this.label = label;
        this.dependency = dependency;
        this.value = value;
        this.owner = this.getClass();
    }

    public Property(String label, T value) {
        this(label, value, () -> true);
    }

    public boolean available() {
        return this.dependency.get();
    }

    public void setValue(T value) {
        T oldValue = this.value;
        this.value = value;
        if (oldValue != value) {
            for (ValueChangeListener<T> valueChangeListener : this.valueChangeListeners) {
                valueChangeListener.onValueChange(oldValue, value);
            }
        }
    }

    public void callOnce() {
        for (ValueChangeListener<T> valueChangeListener : this.valueChangeListeners) {
            valueChangeListener.onValueChange(this.value, this.value);
        }
    }

    public Class<?> type() {
        return this.value.getClass();
    }

    public String getLabel() {
        return this.label;
    }

    public Supplier<Boolean> getDependancy() {
        return this.dependency;
    }

    public T getValue() {
        return this.value;
    }

    public Object owner() {
        return this.owner;
    }

    public List<ValueChangeListener<T>> getValueChangeListeners() {
        return this.valueChangeListeners;
    }

    public static enum Representation {
        INT,
        DOUBLE,
        PERCENTAGE,
        MILLISECONDS,
        DISTANCE;

    }
}

