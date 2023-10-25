/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.CanIgnoreReturnValue
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public interface BiMap<K, V>
extends Map<K, V> {
    @Override
    @Nullable
    @CanIgnoreReturnValue
    public V put(@Nullable K var1, @Nullable V var2);

    @Nullable
    @CanIgnoreReturnValue
    public V forcePut(@Nullable K var1, @Nullable V var2);

    @Override
    public void putAll(Map<? extends K, ? extends V> var1);

    @Override
    public Set<V> values();

    public BiMap<V, K> inverse();
}

