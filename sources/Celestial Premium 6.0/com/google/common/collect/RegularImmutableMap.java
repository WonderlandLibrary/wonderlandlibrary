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
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Hashing;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMapEntry;
import com.google.common.collect.ImmutableMapEntrySet;
import com.google.common.collect.ImmutableSet;
import com.google.j2objc.annotations.Weak;
import java.io.Serializable;
import java.util.Map;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true, emulated=true)
final class RegularImmutableMap<K, V>
extends ImmutableMap<K, V> {
    private final transient Map.Entry<K, V>[] entries;
    private final transient ImmutableMapEntry<K, V>[] table;
    private final transient int mask;
    private static final double MAX_LOAD_FACTOR = 1.2;
    private static final long serialVersionUID = 0L;

    static <K, V> RegularImmutableMap<K, V> fromEntries(Map.Entry<K, V> ... entries) {
        return RegularImmutableMap.fromEntryArray(entries.length, entries);
    }

    static <K, V> RegularImmutableMap<K, V> fromEntryArray(int n, Map.Entry<K, V>[] entryArray) {
        Preconditions.checkPositionIndex(n, entryArray.length);
        Map.Entry<K, V>[] entries = n == entryArray.length ? entryArray : ImmutableMapEntry.createEntryArray(n);
        int tableSize = Hashing.closedTableSize(n, 1.2);
        ImmutableMapEntry<K, V>[] table = ImmutableMapEntry.createEntryArray(tableSize);
        int mask = tableSize - 1;
        for (int entryIndex = 0; entryIndex < n; ++entryIndex) {
            ImmutableMapEntry newEntry;
            Map.Entry<K, V> entry = entryArray[entryIndex];
            K key = entry.getKey();
            V value = entry.getValue();
            CollectPreconditions.checkEntryNotNull(key, value);
            int tableIndex = Hashing.smear(key.hashCode()) & mask;
            ImmutableMapEntry existing = table[tableIndex];
            if (existing == null) {
                boolean reusable = entry instanceof ImmutableMapEntry && ((ImmutableMapEntry)entry).isReusable();
                newEntry = reusable ? (ImmutableMapEntry)entry : new ImmutableMapEntry<K, V>(key, value);
            } else {
                newEntry = new ImmutableMapEntry.NonTerminalImmutableMapEntry<K, V>(key, value, existing);
            }
            table[tableIndex] = newEntry;
            entries[entryIndex] = newEntry;
            RegularImmutableMap.checkNoConflictInKeyBucket(key, newEntry, existing);
        }
        return new RegularImmutableMap<K, V>(entries, table, mask);
    }

    private RegularImmutableMap(Map.Entry<K, V>[] entries, ImmutableMapEntry<K, V>[] table, int mask) {
        this.entries = entries;
        this.table = table;
        this.mask = mask;
    }

    static void checkNoConflictInKeyBucket(Object key, Map.Entry<?, ?> entry, @Nullable ImmutableMapEntry<?, ?> keyBucketHead) {
        while (keyBucketHead != null) {
            RegularImmutableMap.checkNoConflict(!key.equals(keyBucketHead.getKey()), "key", entry, keyBucketHead);
            keyBucketHead = keyBucketHead.getNextInKeyBucket();
        }
    }

    @Override
    public V get(@Nullable Object key) {
        return RegularImmutableMap.get(key, this.table, this.mask);
    }

    @Nullable
    static <V> V get(@Nullable Object key, ImmutableMapEntry<?, V>[] keyTable, int mask) {
        if (key == null) {
            return null;
        }
        int index = Hashing.smear(key.hashCode()) & mask;
        for (ImmutableMapEntry<?, V> entry = keyTable[index]; entry != null; entry = entry.getNextInKeyBucket()) {
            Object candidateKey = entry.getKey();
            if (!key.equals(candidateKey)) continue;
            return entry.getValue();
        }
        return null;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        Preconditions.checkNotNull(action);
        for (Map.Entry<K, V> entry : this.entries) {
            action.accept(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public int size() {
        return this.entries.length;
    }

    @Override
    boolean isPartialView() {
        return false;
    }

    @Override
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return new ImmutableMapEntrySet.RegularEntrySet<K, V>(this, this.entries);
    }

    @Override
    ImmutableSet<K> createKeySet() {
        return new KeySet(this);
    }

    @Override
    ImmutableCollection<V> createValues() {
        return new Values(this);
    }

    @GwtCompatible(emulated=true)
    private static final class Values<K, V>
    extends ImmutableList<V> {
        @Weak
        final RegularImmutableMap<K, V> map;

        Values(RegularImmutableMap<K, V> map) {
            this.map = map;
        }

        @Override
        public V get(int index) {
            return ((RegularImmutableMap)this.map).entries[index].getValue();
        }

        @Override
        public int size() {
            return this.map.size();
        }

        @Override
        boolean isPartialView() {
            return true;
        }

        @Override
        @GwtIncompatible
        Object writeReplace() {
            return new SerializedForm<V>(this.map);
        }

        @GwtIncompatible
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

    @GwtCompatible(emulated=true)
    private static final class KeySet<K, V>
    extends ImmutableSet.Indexed<K> {
        @Weak
        private final RegularImmutableMap<K, V> map;

        KeySet(RegularImmutableMap<K, V> map) {
            this.map = map;
        }

        @Override
        K get(int index) {
            return ((RegularImmutableMap)this.map).entries[index].getKey();
        }

        @Override
        public boolean contains(Object object) {
            return this.map.containsKey(object);
        }

        @Override
        boolean isPartialView() {
            return true;
        }

        @Override
        public int size() {
            return this.map.size();
        }

        @Override
        @GwtIncompatible
        Object writeReplace() {
            return new SerializedForm<K>(this.map);
        }

        @GwtIncompatible
        private static class SerializedForm<K>
        implements Serializable {
            final ImmutableMap<K, ?> map;
            private static final long serialVersionUID = 0L;

            SerializedForm(ImmutableMap<K, ?> map) {
                this.map = map;
            }

            Object readResolve() {
                return this.map.keySet();
            }
        }
    }
}

