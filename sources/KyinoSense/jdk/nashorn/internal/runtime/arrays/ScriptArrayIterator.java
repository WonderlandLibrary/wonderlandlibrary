/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

class ScriptArrayIterator
extends ArrayLikeIterator<Object> {
    protected final ScriptObject array;
    protected final long length;

    protected ScriptArrayIterator(ScriptObject array, boolean includeUndefined) {
        super(includeUndefined);
        this.array = array;
        this.length = array.getArray().length();
    }

    protected boolean indexInArray() {
        return this.index < this.length;
    }

    @Override
    public Object next() {
        return this.array.get(this.bumpIndex());
    }

    @Override
    public long getLength() {
        return this.length;
    }

    @Override
    public boolean hasNext() {
        if (!this.includeUndefined) {
            while (this.indexInArray() && !this.array.has(this.index)) {
                this.bumpIndex();
            }
        }
        return this.indexInArray();
    }

    @Override
    public void remove() {
        this.array.delete(this.index, false);
    }
}

