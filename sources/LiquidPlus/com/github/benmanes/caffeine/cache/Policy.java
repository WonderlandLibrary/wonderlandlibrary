/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.CompatibleWith
 *  org.checkerframework.checker.index.qual.NonNegative
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.google.errorprone.annotations.CompatibleWith;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface Policy<K, V> {
    public boolean isRecordingStats();

    default public @Nullable V getIfPresentQuietly(@CompatibleWith(value="K") @NonNull Object key) {
        throw new UnsupportedOperationException();
    }

    public @NonNull Optional<Eviction<K, V>> eviction();

    public @NonNull Optional<Expiration<K, V>> expireAfterAccess();

    public @NonNull Optional<Expiration<K, V>> expireAfterWrite();

    default public @NonNull Optional<VarExpiration<K, V>> expireVariably() {
        return Optional.empty();
    }

    public @NonNull Optional<Expiration<K, V>> refreshAfterWrite();

    public static interface VarExpiration<K, V> {
        public @NonNull OptionalLong getExpiresAfter(@NonNull K var1, @NonNull TimeUnit var2);

        default public @NonNull Optional<Duration> getExpiresAfter(@NonNull K key) {
            OptionalLong duration = this.getExpiresAfter(key, TimeUnit.NANOSECONDS);
            return duration.isPresent() ? Optional.of(Duration.ofNanos(duration.getAsLong())) : Optional.empty();
        }

        public void setExpiresAfter(@NonNull K var1, @NonNegative long var2, @NonNull TimeUnit var4);

        default public void setExpiresAfter(@NonNull K key, @NonNull Duration duration) {
            this.setExpiresAfter(key, duration.toNanos(), TimeUnit.NANOSECONDS);
        }

        default public boolean putIfAbsent(@NonNull K key, @NonNull V value, @NonNegative long duration, @NonNull TimeUnit unit) {
            throw new UnsupportedOperationException();
        }

        default public boolean putIfAbsent(@NonNull K key, @NonNull V value, @NonNull Duration duration) {
            return this.putIfAbsent(key, value, duration.toNanos(), TimeUnit.NANOSECONDS);
        }

        default public void put(@NonNull K key, @NonNull V value, @NonNegative long duration, @NonNull TimeUnit unit) {
            throw new UnsupportedOperationException();
        }

        default public void put(@NonNull K key, @NonNull V value, @NonNull Duration duration) {
            this.put(key, value, duration.toNanos(), TimeUnit.NANOSECONDS);
        }

        public @NonNull Map<@NonNull K, @NonNull V> oldest(@NonNegative int var1);

        public @NonNull Map<@NonNull K, @NonNull V> youngest(@NonNegative int var1);
    }

    public static interface Expiration<K, V> {
        public @NonNull OptionalLong ageOf(@NonNull K var1, @NonNull TimeUnit var2);

        default public @NonNull Optional<Duration> ageOf(@NonNull K key) {
            OptionalLong duration = this.ageOf(key, TimeUnit.NANOSECONDS);
            return duration.isPresent() ? Optional.of(Duration.ofNanos(duration.getAsLong())) : Optional.empty();
        }

        public @NonNegative long getExpiresAfter(@NonNull TimeUnit var1);

        default public @NonNull Duration getExpiresAfter() {
            return Duration.ofNanos(this.getExpiresAfter(TimeUnit.NANOSECONDS));
        }

        public void setExpiresAfter(@NonNegative long var1, @NonNull TimeUnit var3);

        default public void setExpiresAfter(@NonNull Duration duration) {
            this.setExpiresAfter(duration.toNanos(), TimeUnit.NANOSECONDS);
        }

        public @NonNull Map<@NonNull K, @NonNull V> oldest(@NonNegative int var1);

        public @NonNull Map<@NonNull K, @NonNull V> youngest(@NonNegative int var1);
    }

    public static interface Eviction<K, V> {
        public boolean isWeighted();

        default public @NonNull OptionalInt weightOf(@NonNull K key) {
            return OptionalInt.empty();
        }

        public @NonNull OptionalLong weightedSize();

        public @NonNegative long getMaximum();

        public void setMaximum(@NonNegative long var1);

        public @NonNull Map<@NonNull K, @NonNull V> coldest(@NonNegative int var1);

        public @NonNull Map<@NonNull K, @NonNull V> hottest(@NonNegative int var1);
    }
}

