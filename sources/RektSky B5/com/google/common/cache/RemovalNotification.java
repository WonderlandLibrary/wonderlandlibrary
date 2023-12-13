/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.cache.RemovalCause;
import java.util.Map;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
public final class RemovalNotification<K, V>
implements Map.Entry<K, V> {
    @Nullable
    private final K key;
    @Nullable
    private final V value;
    private final RemovalCause cause;
    private static final long serialVersionUID = 0L;

    RemovalNotification(@Nullable K key, @Nullable V value, RemovalCause cause) {
        this.key = key;
        this.value = value;
        this.cause = Preconditions.checkNotNull(cause);
    }

    public RemovalCause getCause() {
        return this.cause;
    }

    public boolean wasEvicted() {
        return this.cause.wasEvicted();
    }

    @Override
    @Nullable
    public K getKey() {
        return this.key;
    }

    @Override
    @Nullable
    public V getValue() {
        return this.value;
    }

    @Override
    public final V setValue(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object instanceof Map.Entry) {
            Map.Entry that = (Map.Entry)object;
            return Objects.equal(this.getKey(), that.getKey()) && Objects.equal(this.getValue(), that.getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        K k2 = this.getKey();
        V v2 = this.getValue();
        return (k2 == null ? 0 : k2.hashCode()) ^ (v2 == null ? 0 : v2.hashCode());
    }

    public String toString() {
        return this.getKey() + "=" + this.getValue();
    }
}

