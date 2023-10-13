/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.serialization;

import io.netty.handler.codec.serialization.ReferenceMap;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;

final class WeakReferenceMap<K, V>
extends ReferenceMap<K, V> {
    public WeakReferenceMap(Map<K, Reference<V>> delegate) {
        super(delegate);
    }

    @Override
    Reference<V> fold(V value) {
        return new WeakReference<V>(value);
    }
}

