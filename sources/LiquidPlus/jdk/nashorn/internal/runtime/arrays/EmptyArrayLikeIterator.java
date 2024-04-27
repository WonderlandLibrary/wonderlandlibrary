/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import java.util.NoSuchElementException;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

final class EmptyArrayLikeIterator
extends ArrayLikeIterator<Object> {
    EmptyArrayLikeIterator() {
        super(false);
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object next() {
        throw new NoSuchElementException();
    }

    @Override
    public long getLength() {
        return 0L;
    }
}

