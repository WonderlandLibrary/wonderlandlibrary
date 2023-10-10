/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.lang3.concurrent;

public interface CircuitBreaker<T> {
    public boolean isOpen();

    public boolean isClosed();

    public boolean checkState();

    public void close();

    public void open();

    public boolean incrementAndCheckState(T var1);
}

