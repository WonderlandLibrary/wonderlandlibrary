/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.RemovalCause;
import org.checkerframework.checker.nullness.qual.Nullable;

enum DisabledWriter implements CacheWriter<Object, Object>
{
    INSTANCE;


    @Override
    public void write(Object key, Object value) {
    }

    @Override
    public void delete(Object key, @Nullable Object value, RemovalCause cause) {
    }
}

