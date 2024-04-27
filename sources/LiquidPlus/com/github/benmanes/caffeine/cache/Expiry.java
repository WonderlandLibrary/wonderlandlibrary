/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.index.qual.NonNegative
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package com.github.benmanes.caffeine.cache;

import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Expiry<K, V> {
    public long expireAfterCreate(@NonNull K var1, @NonNull V var2, long var3);

    public long expireAfterUpdate(@NonNull K var1, @NonNull V var2, long var3, @NonNegative long var5);

    public long expireAfterRead(@NonNull K var1, @NonNull V var2, long var3, @NonNegative long var5);
}

