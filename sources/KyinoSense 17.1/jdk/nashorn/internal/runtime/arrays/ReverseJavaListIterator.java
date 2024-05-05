/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import java.util.List;
import jdk.nashorn.internal.runtime.arrays.JavaListIterator;

final class ReverseJavaListIterator
extends JavaListIterator {
    public ReverseJavaListIterator(List<?> list, boolean includeUndefined) {
        super(list, includeUndefined);
        this.index = list.size() - 1;
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

