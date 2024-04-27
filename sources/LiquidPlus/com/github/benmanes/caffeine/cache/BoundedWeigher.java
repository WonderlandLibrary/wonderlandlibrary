/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Weigher;
import java.io.Serializable;
import java.util.Objects;

final class BoundedWeigher<K, V>
implements Weigher<K, V>,
Serializable {
    static final long serialVersionUID = 1L;
    final Weigher<? super K, ? super V> delegate;

    BoundedWeigher(Weigher<? super K, ? super V> delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public int weigh(K key, V value) {
        int weight = this.delegate.weigh(key, value);
        Caffeine.requireArgument(weight >= 0);
        return weight;
    }

    Object writeReplace() {
        return this.delegate;
    }
}

