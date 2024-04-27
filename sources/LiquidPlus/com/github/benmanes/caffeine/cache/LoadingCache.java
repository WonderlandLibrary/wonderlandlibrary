/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Cache;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface LoadingCache<K, V>
extends Cache<K, V> {
    public @Nullable V get(@NonNull K var1);

    public @NonNull Map<@NonNull K, @NonNull V> getAll(@NonNull Iterable<? extends @NonNull K> var1);

    public void refresh(@NonNull K var1);
}

