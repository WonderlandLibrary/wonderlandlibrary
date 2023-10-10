/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.ImmutableAsList;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
final class ImmutableMapValues<K, V>
extends ImmutableCollection<V> {
    private final ImmutableMap<K, V> map;

    ImmutableMapValues(ImmutableMap<K, V> map) {
        this.map = map;
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public UnmodifiableIterator<V> iterator() {
        return Maps.valueIterator(((ImmutableSet)this.map.entrySet()).iterator());
    }

    @Override
    public boolean contains(@Nullable Object object) {
        return object != null && Iterators.contains(this.iterator(), object);
    }

    @Override
    boolean isPartialView() {
        return true;
    }

    @Override
    ImmutableList<V> createAsList() {
        final ImmutableList entryList = ((ImmutableCollection)((Object)this.map.entrySet())).asList();
        return new ImmutableAsList<V>(){

            @Override
            public V get(int index) {
                return ((Map.Entry)entryList.get(index)).getValue();
            }

            @Override
            ImmutableCollection<V> delegateCollection() {
                return ImmutableMapValues.this;
            }
        };
    }

    @Override
    @GwtIncompatible(value="serialization")
    Object writeReplace() {
        return new SerializedForm<V>(this.map);
    }

    @GwtIncompatible(value="serialization")
    private static class SerializedForm<V>
    implements Serializable {
        final ImmutableMap<?, V> map;
        private static final long serialVersionUID = 0L;

        SerializedForm(ImmutableMap<?, V> map) {
            this.map = map;
        }

        Object readResolve() {
            return this.map.values();
        }
    }
}

