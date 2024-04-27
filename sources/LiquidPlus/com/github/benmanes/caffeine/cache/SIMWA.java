/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.Pacer;
import com.github.benmanes.caffeine.cache.SIMW;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.github.benmanes.caffeine.cache.Ticker;
import com.github.benmanes.caffeine.cache.TimerWheel;

class SIMWA<K, V>
extends SIMW<K, V> {
    final Ticker ticker;
    final Expiry<K, V> expiry;
    final TimerWheel<K, V> timerWheel;
    volatile long expiresAfterAccessNanos;
    final Pacer pacer;

    SIMWA(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
        super(builder, cacheLoader, async);
        this.ticker = builder.getTicker();
        this.expiry = builder.getExpiry(this.isAsync);
        this.timerWheel = builder.expiresVariable() ? new TimerWheel(this) : null;
        this.expiresAfterAccessNanos = builder.getExpiresAfterAccessNanos();
        this.pacer = builder.getScheduler() == Scheduler.disabledScheduler() ? null : new Pacer(builder.getScheduler());
    }

    @Override
    public final Ticker expirationTicker() {
        return this.ticker;
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
    public final Pacer pacer() {
        return this.pacer;
    }
}

