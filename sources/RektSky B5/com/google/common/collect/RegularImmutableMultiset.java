/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true)
class RegularImmutableMultiset<E>
extends ImmutableMultiset<E> {
    private final transient ImmutableMap<E, Integer> map;
    private final transient int size;

    RegularImmutableMultiset(ImmutableMap<E, Integer> map, int size) {
        this.map = map;
        this.size = size;
    }

    @Override
    boolean isPartialView() {
        return this.map.isPartialView();
    }

    @Override
    public int count(@Nullable Object element) {
        Integer value = this.map.get(element);
        return value == null ? 0 : value;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(@Nullable Object element) {
        return this.map.containsKey(element);
    }

    @Override
    public ImmutableSet<E> elementSet() {
        return this.map.keySet();
    }

    @Override
    Multiset.Entry<E> getEntry(int index) {
        Map.Entry mapEntry = (Map.Entry)((ImmutableCollection)((Object)this.map.entrySet())).asList().get(index);
        return Multisets.immutableEntry(mapEntry.getKey(), (Integer)mapEntry.getValue());
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }
}

