/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.index.qual.NonNegative
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.BoundedWeigher;
import com.github.benmanes.caffeine.cache.SingletonWeigher;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface Weigher<K, V> {
    public @NonNegative int weigh(@NonNull K var1, @NonNull V var2);

    public static <K, V> @NonNull Weigher<K, V> singletonWeigher() {
        SingletonWeigher self = SingletonWeigher.INSTANCE;
        return self;
    }

    public static <K, V> @NonNull Weigher<K, V> boundedWeigher(@NonNull Weigher<K, V> delegate) {
        return new BoundedWeigher<K, V>(delegate);
    }
}

