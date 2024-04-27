/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Node;
import com.github.benmanes.caffeine.cache.PS;
import com.github.benmanes.caffeine.cache.UnsafeAccess;
import java.lang.ref.ReferenceQueue;

class PSA<K, V>
extends PS<K, V> {
    protected static final long ACCESS_TIME_OFFSET = UnsafeAccess.objectFieldOffset(PSA.class, "accessTime");
    volatile long accessTime;
    Node<K, V> previousInAccessOrder;
    Node<K, V> nextInAccessOrder;

    PSA() {
    }

    PSA(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        super(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
        UnsafeAccess.UNSAFE.putLong((Object)this, ACCESS_TIME_OFFSET, now);
    }

    PSA(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        super(keyReference, value, valueReferenceQueue, weight, now);
        UnsafeAccess.UNSAFE.putLong((Object)this, ACCESS_TIME_OFFSET, now);
    }

    @Override
    public Node<K, V> getPreviousInVariableOrder() {
        return this.previousInAccessOrder;
    }

    @Override
    public void setPreviousInVariableOrder(Node<K, V> previousInAccessOrder) {
        this.previousInAccessOrder = previousInAccessOrder;
    }

    @Override
    public Node<K, V> getNextInVariableOrder() {
        return this.nextInAccessOrder;
    }

    @Override
    public void setNextInVariableOrder(Node<K, V> nextInAccessOrder) {
        this.nextInAccessOrder = nextInAccessOrder;
    }

    @Override
    public long getVariableTime() {
        return UnsafeAccess.UNSAFE.getLong((Object)this, ACCESS_TIME_OFFSET);
    }

    @Override
    public void setVariableTime(long accessTime) {
        UnsafeAccess.UNSAFE.putLong((Object)this, ACCESS_TIME_OFFSET, accessTime);
    }

    @Override
    public boolean casVariableTime(long expect, long update) {
        return this.accessTime == expect && UnsafeAccess.UNSAFE.compareAndSwapLong(this, ACCESS_TIME_OFFSET, expect, update);
    }

    @Override
    public final long getAccessTime() {
        return UnsafeAccess.UNSAFE.getLong((Object)this, ACCESS_TIME_OFFSET);
    }

    @Override
    public final void setAccessTime(long accessTime) {
        UnsafeAccess.UNSAFE.putLong((Object)this, ACCESS_TIME_OFFSET, accessTime);
    }

    @Override
    public final Node<K, V> getPreviousInAccessOrder() {
        return this.previousInAccessOrder;
    }

    @Override
    public final void setPreviousInAccessOrder(Node<K, V> previousInAccessOrder) {
        this.previousInAccessOrder = previousInAccessOrder;
    }

    @Override
    public final Node<K, V> getNextInAccessOrder() {
        return this.nextInAccessOrder;
    }

    @Override
    public final void setNextInAccessOrder(Node<K, V> nextInAccessOrder) {
        this.nextInAccessOrder = nextInAccessOrder;
    }

    @Override
    public Node<K, V> newNode(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        return new PSA<K, V>(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
    }

    @Override
    public Node<K, V> newNode(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        return new PSA<K, V>(keyReference, value, valueReferenceQueue, weight, now);
    }
}

