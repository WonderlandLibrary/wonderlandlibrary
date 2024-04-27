/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Node;
import com.github.benmanes.caffeine.cache.SSA;
import com.github.benmanes.caffeine.cache.WriteOrderDeque;

class SSAW<K, V>
extends SSA<K, V> {
    final WriteOrderDeque<Node<K, V>> writeOrderDeque = new WriteOrderDeque();
    volatile long expiresAfterWriteNanos;

    SSAW(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
        super(builder, cacheLoader, async);
        this.expiresAfterWriteNanos = builder.getExpiresAfterWriteNanos();
    }

    @Override
    protected final WriteOrderDeque<Node<K, V>> writeOrderDeque() {
        return this.writeOrderDeque;
    }

    @Override
    protected final boolean expiresAfterWrite() {
        return true;
    }

    @Override
    protected final long expiresAfterWriteNanos() {
        return this.expiresAfterWriteNanos;
    }

    @Override
    protected final void setExpiresAfterWriteNanos(long expiresAfterWriteNanos) {
        this.expiresAfterWriteNanos = expiresAfterWriteNanos;
    }
}

