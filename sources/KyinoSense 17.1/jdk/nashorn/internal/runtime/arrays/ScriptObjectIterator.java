/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import java.util.NoSuchElementException;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

class ScriptObjectIterator
extends ArrayLikeIterator<Object> {
    protected final ScriptObject obj;
    private final long length;

    ScriptObjectIterator(ScriptObject obj, boolean includeUndefined) {
        super(includeUndefined);
        this.obj = obj;
        this.length = JSType.toUint32(obj.getLength());
        this.index = 0L;
    }

    protected boolean indexInArray() {
        return this.index < this.length;
    }

    @Override
    public long getLength() {
        return this.length;
    }

    @Override
    public boolean hasNext() {
        if (this.length == 0L) {
            return false;
        }
        while (this.indexInArray() && !this.obj.has(this.index) && !this.includeUndefined) {
            this.bumpIndex();
        }
        return this.indexInArray();
    }

    @Override
    public Object next() {
        if (this.indexInArray()) {
            return this.obj.get(this.bumpIndex());
        }
        throw new NoSuchElementException();
    }
}

