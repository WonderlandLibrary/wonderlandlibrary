/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.AccessOrderDeque;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.MpscGrowableArrayQueue;
import com.github.benmanes.caffeine.cache.Node;
import com.github.benmanes.caffeine.cache.Pacer;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.github.benmanes.caffeine.cache.Ticker;
import com.github.benmanes.caffeine.cache.TimerWheel;
import com.github.benmanes.caffeine.cache.WIL;

class WILA<K, V>
extends WIL<K, V> {
    final Ticker ticker;
    final AccessOrderDeque<Node<K, V>> accessOrderWindowDeque;
    final Expiry<K, V> expiry;
    final TimerWheel<K, V> timerWheel;
    volatile long expiresAfterAccessNanos;
    final MpscGrowableArrayQueue<Runnable> writeBuffer;
    final Pacer pacer;

    WILA(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
        super(builder, cacheLoader, async);
        this.ticker = builder.getTicker();
        this.accessOrderWindowDeque = builder.evicts() || builder.expiresAfterAccess() ? new AccessOrderDeque() : null;
        this.expiry = builder.getExpiry(this.isAsync);
        this.timerWheel = builder.expiresVariable() ? new TimerWheel(this) : null;
        this.expiresAfterAccessNanos = builder.getExpiresAfterAccessNanos();
        this.writeBuffer = new MpscGrowableArrayQueue(4, WRITE_BUFFER_MAX);
        this.pacer = builder.getScheduler() == Scheduler.disabledScheduler() ? null : new Pacer(builder.getScheduler());
    }

    @Override
    public final Ticker expirationTicker() {
        return this.ticker;
    }

    @Override
    protected final AccessOrderDeque<Node<K, V>> accessOrderWindowDeque() {
        return this.accessOrderWindowDeque;
    }

    @Override
    protected final boolean expiresVariable() {
        return this.timerWheel != null;
    }

    @Override
    protected final Expiry<K, V> expiry() {
        return this.expiry;
    }

    @Override
    protected final TimerWheel<K, V> timerWheel() {
        return this.timerWheel;
    }

    @Override
    protected final boolean expiresAfterAccess() {
        return this.timerWheel == null;
    }

    @Override
    protected final long expiresAfterAccessNanos() {
        return this.expiresAfterAccessNanos;
    }

    @Override
    protected final void setExpiresAfterAccessNanos(long expiresAfterAccessNanos) {
        this.expiresAfterAccessNanos = expiresAfterAccessNanos;
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

