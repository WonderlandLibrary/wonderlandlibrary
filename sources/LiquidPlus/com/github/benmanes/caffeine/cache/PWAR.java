/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Node;
import com.github.benmanes.caffeine.cache.PWA;
import com.github.benmanes.caffeine.cache.UnsafeAccess;
import java.lang.ref.ReferenceQueue;

class PWAR<K, V>
extends PWA<K, V> {
    protected static final long WRITE_TIME_OFFSET = UnsafeAccess.objectFieldOffset(PWAR.class, "writeTime");
    volatile long writeTime;

    PWAR() {
    }

    PWAR(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        super(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
        UnsafeAccess.UNSAFE.putLong((Object)this, WRITE_TIME_OFFSET, now);
    }

    PWAR(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        super(keyReference, value, valueReferenceQueue, weight, now);
        UnsafeAccess.UNSAFE.putLong((Object)this, WRITE_TIME_OFFSET, now);
    }

    @Override
    public final long getWriteTime() {
        return UnsafeAccess.UNSAFE.getLong((Object)this, WRITE_TIME_OFFSET);
    }

    @Override
    public final void setWriteTime(long writeTime) {
        UnsafeAccess.UNSAFE.putLong((Object)this, WRITE_TIME_OFFSET, writeTime);
    }

    @Override
    public final boolean casWriteTime(long expect, long update) {
        return this.writeTime == expect && UnsafeAccess.UNSAFE.compareAndSwapLong(this, WRITE_TIME_OFFSET, expect, update);
    }

    @Override
    public Node<K, V> newNode(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        return new PWAR<K, V>(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
    }

    @Override
    public Node<K, V> newNode(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        return new PWAR<K, V>(keyReference, value, valueReferenceQueue, weight, now);
    }
}

