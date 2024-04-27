/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.BaseMpscLinkedArrayQueuePad1;

abstract class BaseMpscLinkedArrayQueueProducerFields<E>
extends BaseMpscLinkedArrayQueuePad1<E> {
    protected long producerIndex;

    BaseMpscLinkedArrayQueueProducerFields() {
    }
}

