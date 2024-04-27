/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Node;
import com.github.benmanes.caffeine.cache.NodeFactory;
import com.github.benmanes.caffeine.cache.UnsafeAccess;
import java.lang.ref.ReferenceQueue;
import java.util.Objects;

class PS<K, V>
extends Node<K, V>
implements NodeFactory<K, V> {
    protected static final long KEY_OFFSET = UnsafeAccess.objectFieldOffset(PS.class, "key");
    protected static final long VALUE_OFFSET = UnsafeAccess.objectFieldOffset(PS.class, "value");
    volatile K key;
    volatile V value;

    PS() {
    }

    PS(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        this(key, value, valueReferenceQueue, weight, now);
    }

    PS(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        UnsafeAccess.UNSAFE.putObject((Object)this, KEY_OFFSET, keyReference);
        UnsafeAccess.UNSAFE.putObject((Object)this, VALUE_OFFSET, value);
    }

    @Override
    public final K getKey() {
        return (K)UnsafeAccess.UNSAFE.getObject((Object)this, KEY_OFFSET);
    }

    @Override
    public final Object getKeyReference() {
        return UnsafeAccess.UNSAFE.getObject((Object)this, KEY_OFFSET);
    }

    @Override
    public final V getValue() {
        return (V)UnsafeAccess.UNSAFE.getObject((Object)this, VALUE_OFFSET);
    }

    @Override
    public final Object getValueReference() {
        return UnsafeAccess.UNSAFE.getObject((Object)this, VALUE_OFFSET);
    }

    @Override
    public final void setValue(V value, ReferenceQueue<V> referenceQueue) {
        UnsafeAccess.UNSAFE.putObject((Object)this, VALUE_OFFSET, value);
    }

    @Override
    public final boolean containsValue(Object value) {
        return Objects.equals(value, this.getValue());
    }

    @Override
    public Node<K, V> newNode(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        return new PS<K, V>(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
    }

    @Override
    public Node<K, V> newNode(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        return new PS<K, V>(keyReference, value, valueReferenceQueue, weight, now);
    }

    @Override
    public final boolean isAlive() {
        Object key = this.getKeyReference();
        return key != RETIRED_STRONG_KEY && key != DEAD_STRONG_KEY;
    }

    @Override
    public final boolean isRetired() {
        return this.getKeyReference() == RETIRED_STRONG_KEY;
    }

    @Override
    public final void retire() {
        UnsafeAccess.UNSAFE.putObject((Object)this, KEY_OFFSET, RETIRED_STRONG_KEY);
    }

    @Override
    public final boolean isDead() {
        return this.getKeyReference() == DEAD_STRONG_KEY;
    }

    @Override
    public final void die() {
        UnsafeAccess.UNSAFE.putObject((Object)this, VALUE_OFFSET, null);
        UnsafeAccess.UNSAFE.putObject((Object)this, KEY_OFFSET, DEAD_STRONG_KEY);
    }
}

