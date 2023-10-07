/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.options;

public class Option<T> {
    protected T value;

    Option(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    public String toString() {
        return this.getValue() + " [" + this.getValue().getClass() + "]";
    }
}

