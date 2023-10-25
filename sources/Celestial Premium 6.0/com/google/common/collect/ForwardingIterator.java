/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.CanIgnoreReturnValue
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ForwardingObject;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;

@GwtCompatible
public abstract class ForwardingIterator<T>
extends ForwardingObject
implements Iterator<T> {
    protected ForwardingIterator() {
    }

    @Override
    protected abstract Iterator<T> delegate();

    @Override
    public boolean hasNext() {
        return this.delegate().hasNext();
    }

    @Override
    @CanIgnoreReturnValue
    public T next() {
        return (T)this.delegate().next();
    }

    @Override
    public void remove() {
        this.delegate().remove();
    }
}

