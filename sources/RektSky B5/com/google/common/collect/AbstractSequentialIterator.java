/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.UnmodifiableIterator;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class AbstractSequentialIterator<T>
extends UnmodifiableIterator<T> {
    private T nextOrNull;

    protected AbstractSequentialIterator(@Nullable T firstOrNull) {
        this.nextOrNull = firstOrNull;
    }

    protected abstract T computeNext(T var1);

    @Override
    public final boolean hasNext() {
        return this.nextOrNull != null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public final T next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            T t2 = this.nextOrNull;
            return t2;
        }
        finally {
            this.nextOrNull = this.computeNext(this.nextOrNull);
        }
    }
}

