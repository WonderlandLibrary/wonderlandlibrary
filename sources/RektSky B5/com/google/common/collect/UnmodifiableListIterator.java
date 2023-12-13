/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ListIterator;

@GwtCompatible
public abstract class UnmodifiableListIterator<E>
extends UnmodifiableIterator<E>
implements ListIterator<E> {
    protected UnmodifiableListIterator() {
    }

    @Override
    @Deprecated
    public final void add(E e2) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void set(E e2) {
        throw new UnsupportedOperationException();
    }
}

