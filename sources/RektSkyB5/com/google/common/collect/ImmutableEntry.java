/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.AbstractMapEntry;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true)
class ImmutableEntry<K, V>
extends AbstractMapEntry<K, V>
implements Serializable {
    final K key;
    final V value;
    private static final long serialVersionUID = 0L;

    ImmutableEntry(@Nullable K key, @Nullable V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    @Nullable
    public final K getKey() {
        return this.key;
    }

    @Override
    @Nullable
    public final V getValue() {
        return this.value;
    }

    @Override
    public final V setValue(V value) {
        throw new UnsupportedOperationException();
    }
}

