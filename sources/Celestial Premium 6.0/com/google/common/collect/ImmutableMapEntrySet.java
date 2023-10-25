/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.j2objc.annotations.Weak
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.RegularImmutableAsList;
import com.google.common.collect.UnmodifiableIterator;
import com.google.j2objc.annotations.Weak;
import java.io.Serializable;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
abstract class ImmutableMapEntrySet<K, V>
extends ImmutableSet<Map.Entry<K, V>> {
    ImmutableMapEntrySet() {
    }

    abstract ImmutableMap<K, V> map();

    @Override
    public int size() {
        return this.map().size();
    }

    @Override
    public boolean contains(@Nullable Object object) {
        if (object instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry)object;
            V value = this.map().get(entry.getKey());
            return value != null && value.equals(entry.getValue());
        }
        return false;
    }

    @Override
    boolean isPartialView() {
        return this.map().isPartialView();
    }

    @Override
    @GwtIncompatible
    boolean isHashCodeFast() {
        return this.map().isHashCodeFast();
    }

    @Override
    public int hashCode() {
        return this.map().hashCode();
    }

    @Override
    @GwtIncompatible
    Object writeReplace() {
        return new EntrySetSerializedForm<K, V>(this.map());
    }

    @GwtIncompatible
    private static class EntrySetSerializedForm<K, V>
    implements Serializable {
        final ImmutableMap<K, V> map;
        private static final long serialVersionUID = 0L;

        EntrySetSerializedForm(ImmutableMap<K, V> map) {
            this.map = map;
        }

        Object readResolve() {
            return this.map.entrySet();
        }
    }

    static final class RegularEntrySet<K, V>
    extends ImmutableMapEntrySet<K, V> {
        @Weak
        private final transient ImmutableMap<K, V> map;
        private final transient Map.Entry<K, V>[] entries;

        RegularEntrySet(ImmutableMap<K, V> map, Map.Entry<K, V>[] entries) {
            this.map = map;
            this.entries = entries;
        }

        @Override
        ImmutableMap<K, V> map() {
            return this.map;
        }

        @Override
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return Iterators.forArray(this.entries);
        }

        @Override
        public Spliterator<Map.Entry<K, V>> spliterator() {
            return Spliterators.spliterator(this.entries, 1297);
        }

        @Override
        public void forEach(Consumer<? super Map.Entry<K, V>> action) {
            Preconditions.checkNotNull(action);
            for (Map.Entry<K, V> entry : this.entries) {
                action.accept(entry);
            }
        }

        @Override
        ImmutableList<Map.Entry<K, V>> createAsList() {
            return new RegularImmutableAsList<Map.Entry<K, V>>(this, this.entries);
        }
    }
}

