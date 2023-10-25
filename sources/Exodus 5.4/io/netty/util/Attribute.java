/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util;

import io.netty.util.AttributeKey;

public interface Attribute<T> {
    public boolean compareAndSet(T var1, T var2);

    public void set(T var1);

    public void remove();

    public T getAndSet(T var1);

    public T setIfAbsent(T var1);

    public T get();

    public AttributeKey<T> key();

    public T getAndRemove();
}

