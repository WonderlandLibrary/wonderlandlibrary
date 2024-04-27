/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.DisabledWriter;
import com.github.benmanes.caffeine.cache.RemovalCause;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@Deprecated
public interface CacheWriter<K, V> {
    public void write(@NonNull K var1, @NonNull V var2);

    public void delete(@NonNull K var1, @Nullable V var2, @NonNull RemovalCause var3);

    public static <K, V> @NonNull CacheWriter<K, V> disabledWriter() {
        DisabledWriter writer = DisabledWriter.INSTANCE;
        return writer;
    }
}

