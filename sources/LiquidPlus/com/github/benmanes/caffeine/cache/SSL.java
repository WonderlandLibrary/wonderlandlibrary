/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.github.benmanes.caffeine.cache.SS;

class SSL<K, V>
extends SS<K, V> {
    final RemovalListener<K, V> removalListener;

    SSL(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
        super(builder, cacheLoader, async);
        this.removalListener = builder.getRemovalListener(async);
    }

    @Override
    public final RemovalListener<K, V> removalListener() {
        return this.removalListener;
    }

    @Override
    public final boolean hasRemovalListener() {
        return true;
    }
}

