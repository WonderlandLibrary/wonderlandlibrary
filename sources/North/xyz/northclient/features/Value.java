package xyz.northclient.features;

import java.util.function.Supplier;

public abstract class Value<T> {
    public abstract T get();
    public abstract void set(T value);
    public abstract String getName();

    public abstract VisibleSupplier isVisible();
}
