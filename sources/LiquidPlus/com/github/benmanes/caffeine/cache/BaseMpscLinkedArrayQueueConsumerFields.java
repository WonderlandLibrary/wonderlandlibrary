/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.BaseMpscLinkedArrayQueuePad2;

abstract class BaseMpscLinkedArrayQueueConsumerFields<E>
extends BaseMpscLinkedArrayQueuePad2<E> {
    protected long consumerMask;
    protected E[] consumerBuffer;
    protected long consumerIndex;

    BaseMpscLinkedArrayQueueConsumerFields() {
    }
}

