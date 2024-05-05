/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import java.util.List;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

class JavaListIterator
extends ArrayLikeIterator<Object> {
    protected final List<?> list;
    protected final long length;

    protected JavaListIterator(List<?> list, boolean includeUndefined) {
        super(includeUndefined);
        this.list = list;
        this.length = list.size();
    }

    protected boolean indexInArray() {
        return this.index < this.length;
    }

    @Override
    public Object next() {
        return this.list.get((int)this.bumpIndex());
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
        this.list.remove(this.index);
    }
}

