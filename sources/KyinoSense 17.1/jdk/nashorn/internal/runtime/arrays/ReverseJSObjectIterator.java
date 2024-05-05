/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.arrays.JSObjectIterator;

final class ReverseJSObjectIterator
extends JSObjectIterator {
    ReverseJSObjectIterator(JSObject obj, boolean includeUndefined) {
        super(obj, includeUndefined);
        this.index = JSType.toUint32(obj.hasMember("length") ? obj.getMember("length") : Integer.valueOf(0)) - 1L;
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

