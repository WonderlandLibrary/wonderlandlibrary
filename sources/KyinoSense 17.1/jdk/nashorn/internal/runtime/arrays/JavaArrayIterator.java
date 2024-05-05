/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import java.lang.reflect.Array;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

class JavaArrayIterator
extends ArrayLikeIterator<Object> {
    protected final Object array;
    protected final long length;

    protected JavaArrayIterator(Object array, boolean includeUndefined) {
        super(includeUndefined);
        assert (array.getClass().isArray()) : "expecting Java array object";
        this.array = array;
        this.length = Array.getLength(array);
    }

    protected boolean indexInArray() {
        return this.index < this.length;
    }

    @Override
    public Object next() {
        return Array.get(this.array, (int)this.bumpIndex());
    }

    @Override
    public long getLength() {
        return this.length;
    }

    @Override
    public boolean hasNext() {
        return this.indexInArray();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }
}

