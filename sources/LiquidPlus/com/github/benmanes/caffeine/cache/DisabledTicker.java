/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Ticker;

enum DisabledTicker implements Ticker
{
    INSTANCE;


    @Override
    public long read() {
        return 0L;
    }
}

