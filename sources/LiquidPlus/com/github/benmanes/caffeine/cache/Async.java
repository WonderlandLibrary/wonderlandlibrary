/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.github.benmanes.caffeine.cache.Weigher;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import org.checkerframework.checker.nullness.qual.Nullable;

final class Async {
    static final long ASYNC_EXPIRY = 0x5FFFFFFFFFFFFFFEL;

    private Async() {
    }

    static boolean isReady(@Nullable CompletableFuture<?> future) {
        return future != null && future.isDone() && !future.isCompletedExceptionally() && future.join() != null;
    }

    static <V> @Nullable V getIfReady(@Nullable CompletableFuture<V> future) {
        return Async.isReady(future) ? (V)future.join() : null;
    }

    static <V> @Nullable V getWhenSuccessful(@Nullable CompletableFuture<V> future) {
        try {
            return future == null ? null : (V)future.join();
        }
        catch (CancellationException | CompletionException e) {
            return null;
        }
    }

    static final class AsyncExpiry<K, V>
    implements Expiry<K, CompletableFuture<V>>,
    Serializable {
        private static final long serialVersionUID = 1L;
        final Expiry<K, V> delegate;

        AsyncExpiry(Expiry<K, V> delegate) {
            this.delegate = Objects.requireNonNull(delegate);
        }

        @Override
        public long expireAfterCreate(K key, CompletableFuture<V> future, long currentTime) {
            if (Async.isReady(future)) {
                long duration = this.delegate.expireAfterCreate(key, future.join(), currentTime);
                return Math.min(duration, 0x3FFFFFFFFFFFFFFFL);
            }
            return 0x5FFFFFFFFFFFFFFEL;
        }

        @Override
        public long expireAfterUpdate(K key, CompletableFuture<V> future, long currentTime, long currentDuration) {
            if (Async.isReady(future)) {
                long duration = currentDuration > 0x3FFFFFFFFFFFFFFFL ? this.delegate.expireAfterCreate(key, future.join(), currentTime) : this.delegate.expireAfterUpdate(key, future.join(), currentTime, currentDuration);
                return Math.min(duration, 0x3FFFFFFFFFFFFFFFL);
            }
            return 0x5FFFFFFFFFFFFFFEL;
        }

        @Override
        public long expireAfterRead(K key, CompletableFuture<V> future, long currentTime, long currentDuration) {
            if (Async.isReady(future)) {
                long duration = this.delegate.expireAfterRead(key, future.join(), currentTime, currentDuration);
                return Math.min(duration, 0x3FFFFFFFFFFFFFFFL);
            }
            return 0x5FFFFFFFFFFFFFFEL;
        }

        Object writeReplace() {
            return this.delegate;
        }
    }

    static final class AsyncWeigher<K, V>
    implements Weigher<K, CompletableFuture<V>>,
    Serializable {
        private static final long serialVersionUID = 1L;
        final Weigher<K, V> delegate;

        AsyncWeigher(Weigher<K, V> delegate) {
            this.delegate = Objects.requireNonNull(delegate);
        }

        @Override
        public int weigh(K key, CompletableFuture<V> future) {
            return Async.isReady(future) ? this.delegate.weigh(key, future.join()) : 0;
        }

        Object writeReplace() {
            return this.delegate;
        }
    }

    static final class AsyncRemovalListener<K, V>
    implements RemovalListener<K, CompletableFuture<V>>,
    Serializable {
        private static final long serialVersionUID = 1L;
        final RemovalListener<K, V> delegate;
        final Executor executor;

        AsyncRemovalListener(RemovalListener<K, V> delegate, Executor executor) {
            this.delegate = Objects.requireNonNull(delegate);
            this.executor = Objects.requireNonNull(executor);
        }

        @Override
        public void onRemoval(@Nullable K key, @Nullable CompletableFuture<V> future, RemovalCause cause) {
            if (future != null) {
                future.thenAcceptAsync(value -> {
                    if (value != null) {
                        this.delegate.onRemoval(key, value, cause);
                    }
                }, this.executor);
            }
        }

        Object writeReplace() {
            return this.delegate;
        }
    }
}

