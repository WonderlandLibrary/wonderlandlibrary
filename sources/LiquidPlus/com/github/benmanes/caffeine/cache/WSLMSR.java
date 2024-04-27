/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import com.github.benmanes.caffeine.cache.WSLMS;

final class WSLMSR<K, V>
extends WSLMS<K, V> {
    final Ticker ticker;
    volatile long refreshAfterWriteNanos;

    WSLMSR(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
        super(builder, cacheLoader, async);
        this.ticker = builder.getTicker();
        this.refreshAfterWriteNanos = builder.getRefreshAfterWriteNanos();
    }

    @Override
    public Ticker expirationTicker() {
        return this.ticker;
    }

    @Override
    protected boolean refreshAfterWrite() {
        return true;
    }

    @Override
    protected long refreshAfterWriteNanos() {
        return this.refreshAfterWriteNanos;
    }

    @Override
    protected void setRefreshAfterWriteNanos(long refreshAfterWriteNanos) {
        this.refreshAfterWriteNanos = refreshAfterWriteNanos;
    }
}

