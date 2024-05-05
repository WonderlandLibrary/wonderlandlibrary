/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.arrays.ScriptObjectIterator;

final class ReverseScriptObjectIterator
extends ScriptObjectIterator {
    ReverseScriptObjectIterator(ScriptObject obj, boolean includeUndefined) {
        super(obj, includeUndefined);
        this.index = JSType.toUint32(obj.getLength()) - 1L;
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

