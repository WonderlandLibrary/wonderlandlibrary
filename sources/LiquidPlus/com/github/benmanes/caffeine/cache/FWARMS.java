/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.FWAR;
import com.github.benmanes.caffeine.cache.Node;
import java.lang.ref.ReferenceQueue;

final class FWARMS<K, V>
extends FWAR<K, V> {
    int queueType;

    FWARMS() {
    }

    FWARMS(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        super(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
    }

    FWARMS(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        super(keyReference, value, valueReferenceQueue, weight, now);
    }

    @Override
    public int getQueueType() {
        return this.queueType;
    }

    @Override
    public void setQueueType(int queueType) {
        this.queueType = queueType;
    }

    @Override
    public Node<K, V> newNode(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        return new FWARMS<K, V>(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
    }

    @Override
    public Node<K, V> newNode(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        return new FWARMS<K, V>(keyReference, value, valueReferenceQueue, weight, now);
    }
}

