/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.BidirectionalIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;

public interface ObjectBidirectionalIterator<K>
extends ObjectIterator<K>,
BidirectionalIterator<K> {
    default public int back(int n2) {
        int i2 = n2;
        while (i2-- != 0 && this.hasPrevious()) {
            this.previous();
        }
        return n2 - i2 - 1;
    }

    @Override
    default public int skip(int n2) {
        return ObjectIterator.super.skip(n2);
    }
}

