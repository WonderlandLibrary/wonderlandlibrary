/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.BaseMpscLinkedArrayQueue;
import com.github.benmanes.caffeine.cache.Caffeine;

abstract class MpscChunkedArrayQueueColdProducerFields<E>
extends BaseMpscLinkedArrayQueue<E> {
    protected final long maxQueueCapacity;

    MpscChunkedArrayQueueColdProducerFields(int initialCapacity, int maxCapacity) {
        super(initialCapacity);
        if (maxCapacity < 4) {
            throw new IllegalArgumentException("Max capacity must be 4 or more");
        }
        if (Caffeine.ceilingPowerOfTwo(initialCapacity) >= Caffeine.ceilingPowerOfTwo(maxCapacity)) {
            throw new IllegalArgumentException("Initial capacity cannot exceed maximum capacity(both rounded up to a power of 2)");
        }
        this.maxQueueCapacity = (long)Caffeine.ceilingPowerOfTwo(maxCapacity) << 1;
    }
}

