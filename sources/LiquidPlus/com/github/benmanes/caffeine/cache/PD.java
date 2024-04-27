/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Node;
import com.github.benmanes.caffeine.cache.NodeFactory;
import com.github.benmanes.caffeine.cache.References;
import com.github.benmanes.caffeine.cache.UnsafeAccess;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

class PD<K, V>
extends Node<K, V>
implements NodeFactory<K, V> {
    protected static final long KEY_OFFSET = UnsafeAccess.objectFieldOffset(PD.class, "key");
    protected static final long VALUE_OFFSET = UnsafeAccess.objectFieldOffset(PD.class, "value");
    volatile K key;
    volatile References.SoftValueReference<V> value;

    PD() {
    }

    PD(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        this(key, value, valueReferenceQueue, weight, now);
    }

    PD(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        UnsafeAccess.UNSAFE.putObject((Object)this, KEY_OFFSET, keyReference);
        UnsafeAccess.UNSAFE.putObject((Object)this, VALUE_OFFSET, new References.SoftValueReference<V>(keyReference, value, valueReferenceQueue));
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
        return (V)((Reference)UnsafeAccess.UNSAFE.getObject((Object)this, VALUE_OFFSET)).get();
    }

    @Override
    public final Object getValueReference() {
        return UnsafeAccess.UNSAFE.getObject((Object)this, VALUE_OFFSET);
    }

    @Override
    public final void setValue(V value, ReferenceQueue<V> referenceQueue) {
        ((Reference)this.getValueReference()).clear();
        UnsafeAccess.UNSAFE.putObject((Object)this, VALUE_OFFSET, new References.SoftValueReference<V>(this.getKeyReference(), value, referenceQueue));
    }

    @Override
    public final boolean containsValue(Object value) {
        return this.getValue() == value;
    }

    @Override
    public Node<K, V> newNode(K key, ReferenceQueue<K> keyReferenceQueue, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        return new PD<K, V>(key, keyReferenceQueue, value, valueReferenceQueue, weight, now);
    }

    @Override
    public Node<K, V> newNode(Object keyReference, V value, ReferenceQueue<V> valueReferenceQueue, int weight, long now) {
        return new PD<K, V>(keyReference, value, valueReferenceQueue, weight, now);
    }

    @Override
    public boolean softValues() {
        return true;
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
        ((Reference)this.getValueReference()).clear();
        UnsafeAccess.UNSAFE.putObject((Object)this, KEY_OFFSET, RETIRED_STRONG_KEY);
    }

    @Override
    public final boolean isDead() {
        return this.getKeyReference() == DEAD_STRONG_KEY;
    }

    @Override
    public final void die() {
        ((Reference)this.getValueReference()).clear();
        UnsafeAccess.UNSAFE.putObject((Object)this, KEY_OFFSET, DEAD_STRONG_KEY);
    }
}

