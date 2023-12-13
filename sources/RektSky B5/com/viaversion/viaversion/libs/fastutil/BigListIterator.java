/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil;

import com.viaversion.viaversion.libs.fastutil.BidirectionalIterator;

public interface BigListIterator<K>
extends BidirectionalIterator<K> {
    public long nextIndex();

    public long previousIndex();

    default public void set(K e2) {
        throw new UnsupportedOperationException();
    }

    default public void add(K e2) {
        throw new UnsupportedOperationException();
    }
}

