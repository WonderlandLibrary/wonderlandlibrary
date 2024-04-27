/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.BaseMpscLinkedArrayQueuePad3;

abstract class BaseMpscLinkedArrayQueueColdProducerFields<E>
extends BaseMpscLinkedArrayQueuePad3<E> {
    protected volatile long producerLimit;
    protected long producerMask;
    protected E[] producerBuffer;

    BaseMpscLinkedArrayQueueColdProducerFields() {
    }
}

