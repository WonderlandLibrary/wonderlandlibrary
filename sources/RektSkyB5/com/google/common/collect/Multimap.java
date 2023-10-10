/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Multiset;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public interface Multimap<K, V> {
    public int size();

    public boolean isEmpty();

    public boolean containsKey(@Nullable Object var1);

    public boolean containsValue(@Nullable Object var1);

    public boolean containsEntry(@Nullable Object var1, @Nullable Object var2);

    public boolean put(@Nullable K var1, @Nullable V var2);

    public boolean remove(@Nullable Object var1, @Nullable Object var2);

    public boolean putAll(@Nullable K var1, Iterable<? extends V> var2);

    public boolean putAll(Multimap<? extends K, ? extends V> var1);

    public Collection<V> replaceValues(@Nullable K var1, Iterable<? extends V> var2);

    public Collection<V> removeAll(@Nullable Object var1);

    public void clear();

    public Collection<V> get(@Nullable K var1);

    public Set<K> keySet();

    public Multiset<K> keys();

    public Collection<V> values();

    public Collection<Map.Entry<K, V>> entries();

    public Map<K, Collection<V>> asMap();

    public boolean equals(@Nullable Object var1);

    public int hashCode();
}

