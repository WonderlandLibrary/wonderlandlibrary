/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

public abstract class LazyLoadBase<T> {
    private T value;
    private boolean isLoaded = false;

    protected abstract T load();

    public T getValue() {
        if (!this.isLoaded) {
            this.isLoaded = true;
            this.value = this.load();
        }
        return this.value;
    }
}

