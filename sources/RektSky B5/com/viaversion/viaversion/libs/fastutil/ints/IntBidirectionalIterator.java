/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectBidirectionalIterator;

public interface IntBidirectionalIterator
extends IntIterator,
ObjectBidirectionalIterator<Integer> {
    public int previousInt();

    @Override
    @Deprecated
    default public Integer previous() {
        return this.previousInt();
    }

    @Override
    default public int back(int n2) {
        int i2 = n2;
        while (i2-- != 0 && this.hasPrevious()) {
            this.previousInt();
        }
        return n2 - i2 - 1;
    }

    @Override
    default public int skip(int n2) {
        return IntIterator.super.skip(n2);
    }
}

