/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Hashing;
import com.google.common.collect.ImmutableAsList;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMapEntry;
import com.google.common.collect.ImmutableMapEntrySet;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.RegularImmutableAsList;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true, emulated=true)
class RegularImmutableBiMap<K, V>
extends ImmutableBiMap<K, V> {
    static final double MAX_LOAD_FACTOR = 1.2;
    private final transient ImmutableMapEntry<K, V>[] keyTable;
    private final transient ImmutableMapEntry<K, V>[] valueTable;
    private final transient ImmutableMapEntry<K, V>[] entries;
    private final transient int mask;
    private final transient int hashCode;
    private transient ImmutableBiMap<V, K> inverse;

    RegularImmutableBiMap(ImmutableMapEntry.TerminalEntry<?, ?> ... entriesToAdd) {
        this(entriesToAdd.length, entriesToAdd);
    }

    RegularImmutableBiMap(int n2, ImmutableMapEntry.TerminalEntry<?, ?>[] entriesToAdd) {
        int tableSize = Hashing.closedTableSize(n2, 1.2);
        this.mask = tableSize - 1;
        ImmutableMapEntry<K, V>[] keyTable = RegularImmutableBiMap.createEntryArray(tableSize);
        ImmutableMapEntry<K, V>[] valueTable = RegularImmutableBiMap.createEntryArray(tableSize);
        ImmutableMapEntry<K, V>[] entries = RegularImmutableBiMap.createEntryArray(n2);
        int hashCode = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            ImmutableMapEntry<K, V> nextInValueBucket;
            ImmutableMapEntry<K, V> nextInKeyBucket;
            ImmutableMapEntry.TerminalEntry<?, ?> entry = entriesToAdd[i2];
            Object key = entry.getKey();
            Object value = entry.getValue();
            int keyHash = key.hashCode();
            int valueHash = value.hashCode();
            int keyBucket = Hashing.smear(keyHash) & this.mask;
            int valueBucket = Hashing.smear(valueHash) & this.mask;
            for (ImmutableMapEntry<K, V> keyEntry = nextInKeyBucket = keyTable[keyBucket]; keyEntry != null; keyEntry = keyEntry.getNextInKeyBucket()) {
                RegularImmutableBiMap.checkNoConflict(!key.equals(keyEntry.getKey()), "key", entry, keyEntry);
            }
            for (ImmutableMapEntry<K, V> valueEntry = nextInValueBucket = valueTable[valueBucket]; valueEntry != null; valueEntry = valueEntry.getNextInValueBucket()) {
                RegularImmutableBiMap.checkNoConflict(!value.equals(valueEntry.getValue()), "value", entry, valueEntry);
            }
            ImmutableMapEntry newEntry = nextInKeyBucket == null && nextInValueBucket == null ? entry : new NonTerminalBiMapEntry(entry, nextInKeyBucket, nextInValueBucket);
            keyTable[keyBucket] = newEntry;
            valueTable[valueBucket] = newEntry;
            entries[i2] = newEntry;
            hashCode += keyHash ^ valueHash;
        }
        this.keyTable = keyTable;
        this.valueTable = valueTable;
        this.entries = entries;
        this.hashCode = hashCode;
    }

    RegularImmutableBiMap(Map.Entry<?, ?>[] entriesToAdd) {
        int n2 = entriesToAdd.length;
        int tableSize = Hashing.closedTableSize(n2, 1.2);
        this.mask = tableSize - 1;
        ImmutableMapEntry<K, V>[] keyTable = RegularImmutableBiMap.createEntryArray(tableSize);
        ImmutableMapEntry<K, V>[] valueTable = RegularImmutableBiMap.createEntryArray(tableSize);
        ImmutableMapEntry<K, V>[] entries = RegularImmutableBiMap.createEntryArray(n2);
        int hashCode = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            ImmutableMapEntry<K, V> nextInValueBucket;
            ImmutableMapEntry<K, V> nextInKeyBucket;
            Map.Entry<?, ?> entry = entriesToAdd[i2];
            Object key = entry.getKey();
            Object value = entry.getValue();
            CollectPreconditions.checkEntryNotNull(key, value);
            int keyHash = key.hashCode();
            int valueHash = value.hashCode();
            int keyBucket = Hashing.smear(keyHash) & this.mask;
            int valueBucket = Hashing.smear(valueHash) & this.mask;
            for (ImmutableMapEntry<K, V> keyEntry = nextInKeyBucket = keyTable[keyBucket]; keyEntry != null; keyEntry = keyEntry.getNextInKeyBucket()) {
                RegularImmutableBiMap.checkNoConflict(!key.equals(keyEntry.getKey()), "key", entry, keyEntry);
            }
            for (ImmutableMapEntry<K, V> valueEntry = nextInValueBucket = valueTable[valueBucket]; valueEntry != null; valueEntry = valueEntry.getNextInValueBucket()) {
                RegularImmutableBiMap.checkNoConflict(!value.equals(valueEntry.getValue()), "value", entry, valueEntry);
            }
            ImmutableMapEntry newEntry = nextInKeyBucket == null && nextInValueBucket == null ? new ImmutableMapEntry.TerminalEntry(key, value) : new NonTerminalBiMapEntry(key, value, nextInKeyBucket, nextInValueBucket);
            keyTable[keyBucket] = newEntry;
            valueTable[valueBucket] = newEntry;
            entries[i2] = newEntry;
            hashCode += keyHash ^ valueHash;
        }
        this.keyTable = keyTable;
        this.valueTable = valueTable;
        this.entries = entries;
        this.hashCode = hashCode;
    }

    private static <K, V> ImmutableMapEntry<K, V>[] createEntryArray(int length) {
        return new ImmutableMapEntry[length];
    }

    @Override
    @Nullable
    public V get(@Nullable Object key) {
        if (key == null) {
            return null;
        }
        int bucket = Hashing.smear(key.hashCode()) & this.mask;
        for (ImmutableMapEntry<K, V> entry = this.keyTable[bucket]; entry != null; entry = entry.getNextInKeyBucket()) {
            if (!key.equals(entry.getKey())) continue;
            return entry.getValue();
        }
        return null;
    }

    @Override
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return new ImmutableMapEntrySet<K, V>(){

            @Override
            ImmutableMap<K, V> map() {
                return RegularImmutableBiMap.this;
            }

            @Override
            public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
                return this.asList().iterator();
            }

            @Override
            ImmutableList<Map.Entry<K, V>> createAsList() {
                return new RegularImmutableAsList(this, RegularImmutableBiMap.this.entries);
            }

            @Override
            boolean isHashCodeFast() {
                return true;
            }

            @Override
            public int hashCode() {
                return RegularImmutableBiMap.this.hashCode;
            }
        };
    }

    @Override
    boolean isPartialView() {
        return false;
    }

    @Override
    public int size() {
        return this.entries.length;
    }

    @Override
    public ImmutableBiMap<V, K> inverse() {
        Inverse result = this.inverse;
        return result == null ? (this.inverse = new Inverse()) : result;
    }

    private static class InverseSerializedForm<K, V>
    implements Serializable {
        private final ImmutableBiMap<K, V> forward;
        private static final long serialVersionUID = 1L;

        InverseSerializedForm(ImmutableBiMap<K, V> forward) {
            this.forward = forward;
        }

        Object readResolve() {
            return this.forward.inverse();
        }
    }

    private final class Inverse
    extends ImmutableBiMap<V, K> {
        private Inverse() {
        }

        @Override
        public int size() {
            return this.inverse().size();
        }

        @Override
        public ImmutableBiMap<K, V> inverse() {
            return RegularImmutableBiMap.this;
        }

        @Override
        public K get(@Nullable Object value) {
            if (value == null) {
                return null;
            }
            int bucket = Hashing.smear(value.hashCode()) & RegularImmutableBiMap.this.mask;
            for (ImmutableMapEntry entry = RegularImmutableBiMap.this.valueTable[bucket]; entry != null; entry = entry.getNextInValueBucket()) {
                if (!value.equals(entry.getValue())) continue;
                return entry.getKey();
            }
            return null;
        }

        @Override
        ImmutableSet<Map.Entry<V, K>> createEntrySet() {
            return new InverseEntrySet();
        }

        @Override
        boolean isPartialView() {
            return false;
        }

        @Override
        Object writeReplace() {
            return new InverseSerializedForm(RegularImmutableBiMap.this);
        }

        final class InverseEntrySet
        extends ImmutableMapEntrySet<V, K> {
            InverseEntrySet() {
            }

            @Override
            ImmutableMap<V, K> map() {
                return Inverse.this;
            }

            @Override
            boolean isHashCodeFast() {
                return true;
            }

            @Override
            public int hashCode() {
                return RegularImmutableBiMap.this.hashCode;
            }

            @Override
            public UnmodifiableIterator<Map.Entry<V, K>> iterator() {
                return this.asList().iterator();
            }

            @Override
            ImmutableList<Map.Entry<V, K>> createAsList() {
                return new ImmutableAsList<Map.Entry<V, K>>(){

                    @Override
                    public Map.Entry<V, K> get(int index) {
                        ImmutableMapEntry entry = RegularImmutableBiMap.this.entries[index];
                        return Maps.immutableEntry(entry.getValue(), entry.getKey());
                    }

                    @Override
                    ImmutableCollection<Map.Entry<V, K>> delegateCollection() {
                        return InverseEntrySet.this;
                    }
                };
            }
        }
    }

    private static final class NonTerminalBiMapEntry<K, V>
    extends ImmutableMapEntry<K, V> {
        @Nullable
        private final ImmutableMapEntry<K, V> nextInKeyBucket;
        @Nullable
        private final ImmutableMapEntry<K, V> nextInValueBucket;

        NonTerminalBiMapEntry(K key, V value, @Nullable ImmutableMapEntry<K, V> nextInKeyBucket, @Nullable ImmutableMapEntry<K, V> nextInValueBucket) {
            super(key, value);
            this.nextInKeyBucket = nextInKeyBucket;
            this.nextInValueBucket = nextInValueBucket;
        }

        NonTerminalBiMapEntry(ImmutableMapEntry<K, V> contents, @Nullable ImmutableMapEntry<K, V> nextInKeyBucket, @Nullable ImmutableMapEntry<K, V> nextInValueBucket) {
            super(contents);
            this.nextInKeyBucket = nextInKeyBucket;
            this.nextInValueBucket = nextInValueBucket;
        }

        @Override
        @Nullable
        ImmutableMapEntry<K, V> getNextInKeyBucket() {
            return this.nextInKeyBucket;
        }

        @Override
        @Nullable
        ImmutableMapEntry<K, V> getNextInValueBucket() {
            return this.nextInValueBucket;
        }
    }
}

