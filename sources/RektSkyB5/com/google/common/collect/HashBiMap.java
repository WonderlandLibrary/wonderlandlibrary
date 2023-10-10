/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMapEntry;
import com.google.common.collect.BiMap;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Hashing;
import com.google.common.collect.ImmutableEntry;
import com.google.common.collect.Maps;
import com.google.common.collect.Serialization;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
public final class HashBiMap<K, V>
extends AbstractMap<K, V>
implements BiMap<K, V>,
Serializable {
    private static final double LOAD_FACTOR = 1.0;
    private transient BiEntry<K, V>[] hashTableKToV;
    private transient BiEntry<K, V>[] hashTableVToK;
    private transient int size;
    private transient int mask;
    private transient int modCount;
    private transient BiMap<V, K> inverse;
    @GwtIncompatible(value="Not needed in emulated source")
    private static final long serialVersionUID = 0L;

    public static <K, V> HashBiMap<K, V> create() {
        return HashBiMap.create(16);
    }

    public static <K, V> HashBiMap<K, V> create(int expectedSize) {
        return new HashBiMap<K, V>(expectedSize);
    }

    public static <K, V> HashBiMap<K, V> create(Map<? extends K, ? extends V> map) {
        HashBiMap<K, V> bimap = HashBiMap.create(map.size());
        bimap.putAll(map);
        return bimap;
    }

    private HashBiMap(int expectedSize) {
        this.init(expectedSize);
    }

    private void init(int expectedSize) {
        CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
        int tableSize = Hashing.closedTableSize(expectedSize, 1.0);
        this.hashTableKToV = this.createTable(tableSize);
        this.hashTableVToK = this.createTable(tableSize);
        this.mask = tableSize - 1;
        this.modCount = 0;
        this.size = 0;
    }

    private void delete(BiEntry<K, V> entry) {
        int keyBucket = entry.keyHash & this.mask;
        BiEntry<K, V> prevBucketEntry = null;
        BiEntry<K, V> bucketEntry = this.hashTableKToV[keyBucket];
        while (true) {
            if (bucketEntry == entry) {
                if (prevBucketEntry == null) {
                    this.hashTableKToV[keyBucket] = entry.nextInKToVBucket;
                    break;
                }
                prevBucketEntry.nextInKToVBucket = entry.nextInKToVBucket;
                break;
            }
            prevBucketEntry = bucketEntry;
            bucketEntry = bucketEntry.nextInKToVBucket;
        }
        int valueBucket = entry.valueHash & this.mask;
        prevBucketEntry = null;
        BiEntry<K, V> bucketEntry2 = this.hashTableVToK[valueBucket];
        while (true) {
            if (bucketEntry2 == entry) {
                if (prevBucketEntry == null) {
                    this.hashTableVToK[valueBucket] = entry.nextInVToKBucket;
                    break;
                }
                prevBucketEntry.nextInVToKBucket = entry.nextInVToKBucket;
                break;
            }
            prevBucketEntry = bucketEntry2;
            bucketEntry2 = bucketEntry2.nextInVToKBucket;
        }
        --this.size;
        ++this.modCount;
    }

    private void insert(BiEntry<K, V> entry) {
        int keyBucket = entry.keyHash & this.mask;
        entry.nextInKToVBucket = this.hashTableKToV[keyBucket];
        this.hashTableKToV[keyBucket] = entry;
        int valueBucket = entry.valueHash & this.mask;
        entry.nextInVToKBucket = this.hashTableVToK[valueBucket];
        this.hashTableVToK[valueBucket] = entry;
        ++this.size;
        ++this.modCount;
    }

    private static int hash(@Nullable Object o2) {
        return Hashing.smear(o2 == null ? 0 : o2.hashCode());
    }

    private BiEntry<K, V> seekByKey(@Nullable Object key, int keyHash) {
        BiEntry<K, V> entry = this.hashTableKToV[keyHash & this.mask];
        while (entry != null) {
            if (keyHash == entry.keyHash && Objects.equal(key, entry.key)) {
                return entry;
            }
            entry = entry.nextInKToVBucket;
        }
        return null;
    }

    private BiEntry<K, V> seekByValue(@Nullable Object value, int valueHash) {
        BiEntry<K, V> entry = this.hashTableVToK[valueHash & this.mask];
        while (entry != null) {
            if (valueHash == entry.valueHash && Objects.equal(value, entry.value)) {
                return entry;
            }
            entry = entry.nextInVToKBucket;
        }
        return null;
    }

    @Override
    public boolean containsKey(@Nullable Object key) {
        return this.seekByKey(key, HashBiMap.hash(key)) != null;
    }

    @Override
    public boolean containsValue(@Nullable Object value) {
        return this.seekByValue(value, HashBiMap.hash(value)) != null;
    }

    @Override
    @Nullable
    public V get(@Nullable Object key) {
        BiEntry<K, V> entry = this.seekByKey(key, HashBiMap.hash(key));
        return (V)(entry == null ? null : entry.value);
    }

    @Override
    public V put(@Nullable K key, @Nullable V value) {
        return this.put(key, value, false);
    }

    @Override
    public V forcePut(@Nullable K key, @Nullable V value) {
        return this.put(key, value, true);
    }

    private V put(@Nullable K key, @Nullable V value, boolean force) {
        int keyHash = HashBiMap.hash(key);
        int valueHash = HashBiMap.hash(value);
        BiEntry<K, V> oldEntryForKey = this.seekByKey(key, keyHash);
        if (oldEntryForKey != null && valueHash == oldEntryForKey.valueHash && Objects.equal(value, oldEntryForKey.value)) {
            return value;
        }
        BiEntry<K, V> oldEntryForValue = this.seekByValue(value, valueHash);
        if (oldEntryForValue != null) {
            if (force) {
                this.delete(oldEntryForValue);
            } else {
                throw new IllegalArgumentException("value already present: " + value);
            }
        }
        if (oldEntryForKey != null) {
            this.delete(oldEntryForKey);
        }
        BiEntry<K, V> newEntry = new BiEntry<K, V>(key, keyHash, value, valueHash);
        this.insert(newEntry);
        this.rehashIfNecessary();
        return (V)(oldEntryForKey == null ? null : oldEntryForKey.value);
    }

    @Nullable
    private K putInverse(@Nullable V value, @Nullable K key, boolean force) {
        int valueHash = HashBiMap.hash(value);
        int keyHash = HashBiMap.hash(key);
        BiEntry<K, V> oldEntryForValue = this.seekByValue(value, valueHash);
        if (oldEntryForValue != null && keyHash == oldEntryForValue.keyHash && Objects.equal(key, oldEntryForValue.key)) {
            return key;
        }
        BiEntry<K, V> oldEntryForKey = this.seekByKey(key, keyHash);
        if (oldEntryForKey != null) {
            if (force) {
                this.delete(oldEntryForKey);
            } else {
                throw new IllegalArgumentException("value already present: " + key);
            }
        }
        if (oldEntryForValue != null) {
            this.delete(oldEntryForValue);
        }
        BiEntry<K, V> newEntry = new BiEntry<K, V>(key, keyHash, value, valueHash);
        this.insert(newEntry);
        this.rehashIfNecessary();
        return (K)(oldEntryForValue == null ? null : oldEntryForValue.key);
    }

    private void rehashIfNecessary() {
        BiEntry<K, V>[] oldKToV = this.hashTableKToV;
        if (Hashing.needsResizing(this.size, oldKToV.length, 1.0)) {
            int newTableSize = oldKToV.length * 2;
            this.hashTableKToV = this.createTable(newTableSize);
            this.hashTableVToK = this.createTable(newTableSize);
            this.mask = newTableSize - 1;
            this.size = 0;
            for (int bucket = 0; bucket < oldKToV.length; ++bucket) {
                BiEntry<K, V> entry = oldKToV[bucket];
                while (entry != null) {
                    BiEntry nextEntry = entry.nextInKToVBucket;
                    this.insert(entry);
                    entry = nextEntry;
                }
            }
            ++this.modCount;
        }
    }

    private BiEntry<K, V>[] createTable(int length) {
        return new BiEntry[length];
    }

    @Override
    public V remove(@Nullable Object key) {
        BiEntry<K, V> entry = this.seekByKey(key, HashBiMap.hash(key));
        if (entry == null) {
            return null;
        }
        this.delete(entry);
        return (V)entry.value;
    }

    @Override
    public void clear() {
        this.size = 0;
        Arrays.fill(this.hashTableKToV, null);
        Arrays.fill(this.hashTableVToK, null);
        ++this.modCount;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Set<K> keySet() {
        return new KeySet();
    }

    @Override
    public Set<V> values() {
        return this.inverse().keySet();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    @Override
    public BiMap<V, K> inverse() {
        return this.inverse == null ? (this.inverse = new Inverse()) : this.inverse;
    }

    @GwtIncompatible(value="java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        Serialization.writeMap(this, stream);
    }

    @GwtIncompatible(value="java.io.ObjectInputStream")
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        int size = Serialization.readCount(stream);
        this.init(size);
        Serialization.populateMap(this, stream, size);
    }

    private static final class InverseSerializedForm<K, V>
    implements Serializable {
        private final HashBiMap<K, V> bimap;

        InverseSerializedForm(HashBiMap<K, V> bimap) {
            this.bimap = bimap;
        }

        Object readResolve() {
            return this.bimap.inverse();
        }
    }

    private final class Inverse
    extends AbstractMap<V, K>
    implements BiMap<V, K>,
    Serializable {
        private Inverse() {
        }

        BiMap<K, V> forward() {
            return HashBiMap.this;
        }

        @Override
        public int size() {
            return HashBiMap.this.size;
        }

        @Override
        public void clear() {
            this.forward().clear();
        }

        @Override
        public boolean containsKey(@Nullable Object value) {
            return this.forward().containsValue(value);
        }

        @Override
        public K get(@Nullable Object value) {
            BiEntry entry = HashBiMap.this.seekByValue(value, HashBiMap.hash(value));
            return entry == null ? null : entry.key;
        }

        @Override
        public K put(@Nullable V value, @Nullable K key) {
            return HashBiMap.this.putInverse(value, key, false);
        }

        @Override
        public K forcePut(@Nullable V value, @Nullable K key) {
            return HashBiMap.this.putInverse(value, key, true);
        }

        @Override
        public K remove(@Nullable Object value) {
            BiEntry entry = HashBiMap.this.seekByValue(value, HashBiMap.hash(value));
            if (entry == null) {
                return null;
            }
            HashBiMap.this.delete(entry);
            return entry.key;
        }

        @Override
        public BiMap<K, V> inverse() {
            return this.forward();
        }

        @Override
        public Set<V> keySet() {
            return new InverseKeySet();
        }

        @Override
        public Set<K> values() {
            return this.forward().keySet();
        }

        @Override
        public Set<Map.Entry<V, K>> entrySet() {
            return new Maps.EntrySet<V, K>(){

                @Override
                Map<V, K> map() {
                    return Inverse.this;
                }

                @Override
                public Iterator<Map.Entry<V, K>> iterator() {
                    return new Itr<Map.Entry<V, K>>(){

                        @Override
                        Map.Entry<V, K> output(BiEntry<K, V> entry) {
                            return new InverseEntry(entry);
                        }

                        class InverseEntry
                        extends AbstractMapEntry<V, K> {
                            BiEntry<K, V> delegate;

                            InverseEntry(BiEntry<K, V> entry) {
                                this.delegate = entry;
                            }

                            @Override
                            public V getKey() {
                                return this.delegate.value;
                            }

                            @Override
                            public K getValue() {
                                return this.delegate.key;
                            }

                            @Override
                            public K setValue(K key) {
                                Object oldKey = this.delegate.key;
                                int keyHash = HashBiMap.hash(key);
                                if (keyHash == this.delegate.keyHash && Objects.equal(key, oldKey)) {
                                    return key;
                                }
                                Preconditions.checkArgument(HashBiMap.this.seekByKey(key, keyHash) == null, "value already present: %s", key);
                                HashBiMap.this.delete(this.delegate);
                                BiEntry newEntry = new BiEntry(key, keyHash, this.delegate.value, this.delegate.valueHash);
                                HashBiMap.this.insert(newEntry);
                                expectedModCount = HashBiMap.this.modCount;
                                return oldKey;
                            }
                        }
                    };
                }
            };
        }

        Object writeReplace() {
            return new InverseSerializedForm(HashBiMap.this);
        }

        private final class InverseKeySet
        extends Maps.KeySet<V, K> {
            InverseKeySet() {
                super(Inverse.this);
            }

            @Override
            public boolean remove(@Nullable Object o2) {
                BiEntry entry = HashBiMap.this.seekByValue(o2, HashBiMap.hash(o2));
                if (entry == null) {
                    return false;
                }
                HashBiMap.this.delete(entry);
                return true;
            }

            @Override
            public Iterator<V> iterator() {
                return new Itr<V>(){

                    @Override
                    V output(BiEntry<K, V> entry) {
                        return entry.value;
                    }
                };
            }
        }
    }

    private final class EntrySet
    extends Maps.EntrySet<K, V> {
        private EntrySet() {
        }

        @Override
        Map<K, V> map() {
            return HashBiMap.this;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new Itr<Map.Entry<K, V>>(){

                @Override
                Map.Entry<K, V> output(BiEntry<K, V> entry) {
                    return new MapEntry(entry);
                }

                class MapEntry
                extends AbstractMapEntry<K, V> {
                    BiEntry<K, V> delegate;

                    MapEntry(BiEntry<K, V> entry) {
                        this.delegate = entry;
                    }

                    @Override
                    public K getKey() {
                        return this.delegate.key;
                    }

                    @Override
                    public V getValue() {
                        return this.delegate.value;
                    }

                    @Override
                    public V setValue(V value) {
                        Object oldValue = this.delegate.value;
                        int valueHash = HashBiMap.hash(value);
                        if (valueHash == this.delegate.valueHash && Objects.equal(value, oldValue)) {
                            return value;
                        }
                        Preconditions.checkArgument(HashBiMap.this.seekByValue(value, valueHash) == null, "value already present: %s", value);
                        HashBiMap.this.delete(this.delegate);
                        BiEntry newEntry = new BiEntry(this.delegate.key, this.delegate.keyHash, value, valueHash);
                        HashBiMap.this.insert(newEntry);
                        expectedModCount = HashBiMap.this.modCount;
                        if (toRemove == this.delegate) {
                            toRemove = newEntry;
                        }
                        this.delegate = newEntry;
                        return oldValue;
                    }
                }
            };
        }
    }

    private final class KeySet
    extends Maps.KeySet<K, V> {
        KeySet() {
            super(HashBiMap.this);
        }

        @Override
        public Iterator<K> iterator() {
            return new Itr<K>(){

                @Override
                K output(BiEntry<K, V> entry) {
                    return entry.key;
                }
            };
        }

        @Override
        public boolean remove(@Nullable Object o2) {
            BiEntry entry = HashBiMap.this.seekByKey(o2, HashBiMap.hash(o2));
            if (entry == null) {
                return false;
            }
            HashBiMap.this.delete(entry);
            return true;
        }
    }

    abstract class Itr<T>
    implements Iterator<T> {
        int nextBucket = 0;
        BiEntry<K, V> next = null;
        BiEntry<K, V> toRemove = null;
        int expectedModCount = HashBiMap.access$000(HashBiMap.this);

        Itr() {
        }

        private void checkForConcurrentModification() {
            if (HashBiMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            this.checkForConcurrentModification();
            if (this.next != null) {
                return true;
            }
            while (this.nextBucket < HashBiMap.this.hashTableKToV.length) {
                if (HashBiMap.this.hashTableKToV[this.nextBucket] != null) {
                    this.next = HashBiMap.this.hashTableKToV[this.nextBucket++];
                    return true;
                }
                ++this.nextBucket;
            }
            return false;
        }

        @Override
        public T next() {
            this.checkForConcurrentModification();
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            BiEntry entry = this.next;
            this.next = entry.nextInKToVBucket;
            this.toRemove = entry;
            return this.output(entry);
        }

        @Override
        public void remove() {
            this.checkForConcurrentModification();
            CollectPreconditions.checkRemove(this.toRemove != null);
            HashBiMap.this.delete(this.toRemove);
            this.expectedModCount = HashBiMap.this.modCount;
            this.toRemove = null;
        }

        abstract T output(BiEntry<K, V> var1);
    }

    private static final class BiEntry<K, V>
    extends ImmutableEntry<K, V> {
        final int keyHash;
        final int valueHash;
        @Nullable
        BiEntry<K, V> nextInKToVBucket;
        @Nullable
        BiEntry<K, V> nextInVToKBucket;

        BiEntry(K key, int keyHash, V value, int valueHash) {
            super(key, value);
            this.keyHash = keyHash;
            this.valueHash = valueHash;
        }
    }
}

