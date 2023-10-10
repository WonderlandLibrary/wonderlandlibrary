/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMapEntrySet;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true, emulated=true)
final class ImmutableEnumMap<K extends Enum<K>, V>
extends ImmutableMap<K, V> {
    private final transient EnumMap<K, V> delegate;

    static <K extends Enum<K>, V> ImmutableMap<K, V> asImmutable(EnumMap<K, V> map) {
        switch (map.size()) {
            case 0: {
                return ImmutableMap.of();
            }
            case 1: {
                Map.Entry<K, V> entry = Iterables.getOnlyElement(map.entrySet());
                return ImmutableMap.of(entry.getKey(), entry.getValue());
            }
        }
        return new ImmutableEnumMap<K, V>(map);
    }

    private ImmutableEnumMap(EnumMap<K, V> delegate) {
        this.delegate = delegate;
        Preconditions.checkArgument(!delegate.isEmpty());
    }

    @Override
    ImmutableSet<K> createKeySet() {
        return new ImmutableSet<K>(){

            @Override
            public boolean contains(Object object) {
                return ImmutableEnumMap.this.delegate.containsKey(object);
            }

            @Override
            public int size() {
                return ImmutableEnumMap.this.size();
            }

            @Override
            public UnmodifiableIterator<K> iterator() {
                return Iterators.unmodifiableIterator(ImmutableEnumMap.this.delegate.keySet().iterator());
            }

            @Override
            boolean isPartialView() {
                return true;
            }
        };
    }

    @Override
    public int size() {
        return this.delegate.size();
    }

    @Override
    public boolean containsKey(@Nullable Object key) {
        return this.delegate.containsKey(key);
    }

    @Override
    public V get(Object key) {
        return this.delegate.get(key);
    }

    @Override
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return new ImmutableMapEntrySet<K, V>(){

            @Override
            ImmutableMap<K, V> map() {
                return ImmutableEnumMap.this;
            }

            @Override
            public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
                return new UnmodifiableIterator<Map.Entry<K, V>>(){
                    private final Iterator<Map.Entry<K, V>> backingIterator;
                    {
                        this.backingIterator = ImmutableEnumMap.this.delegate.entrySet().iterator();
                    }

                    @Override
                    public boolean hasNext() {
                        return this.backingIterator.hasNext();
                    }

                    @Override
                    public Map.Entry<K, V> next() {
                        Map.Entry entry = this.backingIterator.next();
                        return Maps.immutableEntry(entry.getKey(), entry.getValue());
                    }
                };
            }
        };
    }

    @Override
    boolean isPartialView() {
        return false;
    }

    @Override
    Object writeReplace() {
        return new EnumSerializedForm<K, V>(this.delegate);
    }

    private static class EnumSerializedForm<K extends Enum<K>, V>
    implements Serializable {
        final EnumMap<K, V> delegate;
        private static final long serialVersionUID = 0L;

        EnumSerializedForm(EnumMap<K, V> delegate) {
            this.delegate = delegate;
        }

        Object readResolve() {
            return new ImmutableEnumMap(this.delegate);
        }
    }
}

