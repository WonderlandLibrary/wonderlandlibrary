/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Spliterator;

public interface ObjectSpliterator<K>
extends Spliterator<K> {
    default public long skip(long n2) {
        if (n2 < 0L) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
        }
        long i2 = n2;
        while (i2-- != 0L && this.tryAdvance(unused -> {})) {
        }
        return n2 - i2 - 1L;
    }

    @Override
    public ObjectSpliterator<K> trySplit();
}

