/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.ints.IntBidirectionalIterator;
import java.util.ListIterator;

public interface IntListIterator
extends IntBidirectionalIterator,
ListIterator<Integer> {
    @Override
    default public void set(int k2) {
        throw new UnsupportedOperationException();
    }

    @Override
    default public void add(int k2) {
        throw new UnsupportedOperationException();
    }

    @Override
    default public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default public void set(Integer k2) {
        this.set((int)k2);
    }

    @Override
    @Deprecated
    default public void add(Integer k2) {
        this.add((int)k2);
    }

    @Override
    @Deprecated
    default public Integer next() {
        return IntBidirectionalIterator.super.next();
    }

    @Override
    @Deprecated
    default public Integer previous() {
        return IntBidirectionalIterator.super.previous();
    }
}

