/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.lang3.concurrent;

import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.ConcurrentInitializer;

public abstract class LazyInitializer<T>
implements ConcurrentInitializer<T> {
    private volatile T object;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public T get() throws ConcurrentException {
        T result = this.object;
        if (result == null) {
            LazyInitializer lazyInitializer = this;
            synchronized (lazyInitializer) {
                result = this.object;
                if (result == null) {
                    this.object = result = this.initialize();
                }
            }
        }
        return result;
    }

    protected abstract T initialize() throws ConcurrentException;
}

