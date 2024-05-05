/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import java.lang.reflect.Array;
import jdk.nashorn.internal.runtime.arrays.JavaArrayIterator;

final class ReverseJavaArrayIterator
extends JavaArrayIterator {
    public ReverseJavaArrayIterator(Object array, boolean includeUndefined) {
        super(array, includeUndefined);
        this.index = Array.getLength(array) - 1;
    }

    @Override
    public boolean isReverse() {
        return true;
    }

    @Override
    protected boolean indexInArray() {
        return this.index >= 0L;
    }

    @Override
    protected long bumpIndex() {
        return this.index--;
    }
}

