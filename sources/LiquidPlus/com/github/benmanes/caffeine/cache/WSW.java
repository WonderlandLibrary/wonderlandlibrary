/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.MpscGrowableArrayQueue;
import com.github.benmanes.caffeine.cache.Node;
import com.github.benmanes.caffeine.cache.Pacer;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.github.benmanes.caffeine.cache.Ticker;
import com.github.benmanes.caffeine.cache.WS;
import com.github.benmanes.caffeine.cache.WriteOrderDeque;

class WSW<K, V>
extends WS<K, V> {
    final Ticker ticker;
    final WriteOrderDeque<Node<K, V>> writeOrderDeque;
    volatile long expiresAfterWriteNanos;
    final MpscGrowableArrayQueue<Runnable> writeBuffer;
    final Pacer pacer;

    WSW(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
        super(builder, cacheLoader, async);
        this.ticker = builder.getTicker();
        this.writeOrderDeque = new WriteOrderDeque();
        this.expiresAfterWriteNanos = builder.getExpiresAfterWriteNanos();
        this.writeBuffer = new MpscGrowableArrayQueue(4, WRITE_BUFFER_MAX);
        this.pacer = builder.getScheduler() == Scheduler.disabledScheduler() ? null : new Pacer(builder.getScheduler());
    }

    @Override
    public final Ticker expirationTicker() {
        return this.ticker;
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

    @Override
    protected final MpscGrowableArrayQueue<Runnable> writeBuffer() {
        return this.writeBuffer;
    }

    @Override
    protected final boolean buffersWrites() {
        return true;
    }

    @Override
    public final Pacer pacer() {
        return this.pacer;
    }
}

