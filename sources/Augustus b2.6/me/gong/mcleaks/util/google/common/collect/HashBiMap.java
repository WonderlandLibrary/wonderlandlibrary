// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Set;
import java.util.Arrays;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import me.gong.mcleaks.util.google.common.base.Objects;
import javax.annotation.Nullable;
import java.util.Map;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.j2objc.annotations.RetainedWith;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(emulated = true)
public final class HashBiMap<K, V> extends Maps.IteratorBasedAbstractMap<K, V> implements BiMap<K, V>, Serializable
{
    private static final double LOAD_FACTOR = 1.0;
    private transient BiEntry<K, V>[] hashTableKToV;
    private transient BiEntry<K, V>[] hashTableVToK;
    private transient BiEntry<K, V> firstInKeyInsertionOrder;
    private transient BiEntry<K, V> lastInKeyInsertionOrder;
    private transient int size;
    private transient int mask;
    private transient int modCount;
    @RetainedWith
    private transient BiMap<V, K> inverse;
    @GwtIncompatible
    private static final long serialVersionUID = 0L;
    
    public static <K, V> HashBiMap<K, V> create() {
        return create(16);
    }
    
    public static <K, V> HashBiMap<K, V> create(final int expectedSize) {
        return new HashBiMap<K, V>(expectedSize);
    }
    
    public static <K, V> HashBiMap<K, V> create(final Map<? extends K, ? extends V> map) {
        final HashBiMap<K, V> bimap = create(map.size());
        bimap.putAll((Map<?, ?>)map);
        return bimap;
    }
    
    private HashBiMap(final int expectedSize) {
        this.init(expectedSize);
    }
    
    private void init(final int expectedSize) {
        CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
        final int tableSize = Hashing.closedTableSize(expectedSize, 1.0);
        this.hashTableKToV = this.createTable(tableSize);
        this.hashTableVToK = this.createTable(tableSize);
        this.firstInKeyInsertionOrder = null;
        this.lastInKeyInsertionOrder = null;
        this.size = 0;
        this.mask = tableSize - 1;
        this.modCount = 0;
    }
    
    private void delete(final BiEntry<K, V> entry) {
        final int keyBucket = entry.keyHash & this.mask;
        BiEntry<K, V> prevBucketEntry = null;
        for (BiEntry<K, V> bucketEntry = this.hashTableKToV[keyBucket]; bucketEntry != entry; bucketEntry = bucketEntry.nextInKToVBucket) {
            prevBucketEntry = bucketEntry;
        }
        if (prevBucketEntry == null) {
            this.hashTableKToV[keyBucket] = entry.nextInKToVBucket;
        }
        else {
            prevBucketEntry.nextInKToVBucket = entry.nextInKToVBucket;
        }
        final int valueBucket = entry.valueHash & this.mask;
        prevBucketEntry = null;
        for (BiEntry<K, V> bucketEntry2 = this.hashTableVToK[valueBucket]; bucketEntry2 != entry; bucketEntry2 = bucketEntry2.nextInVToKBucket) {
            prevBucketEntry = bucketEntry2;
        }
        if (prevBucketEntry == null) {
            this.hashTableVToK[valueBucket] = entry.nextInVToKBucket;
        }
        else {
            prevBucketEntry.nextInVToKBucket = entry.nextInVToKBucket;
        }
        if (entry.prevInKeyInsertionOrder == null) {
            this.firstInKeyInsertionOrder = entry.nextInKeyInsertionOrder;
        }
        else {
            entry.prevInKeyInsertionOrder.nextInKeyInsertionOrder = entry.nextInKeyInsertionOrder;
        }
        if (entry.nextInKeyInsertionOrder == null) {
            this.lastInKeyInsertionOrder = entry.prevInKeyInsertionOrder;
        }
        else {
            entry.nextInKeyInsertionOrder.prevInKeyInsertionOrder = entry.prevInKeyInsertionOrder;
        }
        --this.size;
        ++this.modCount;
    }
    
    private void insert(final BiEntry<K, V> entry, @Nullable final BiEntry<K, V> oldEntryForKey) {
        final int keyBucket = entry.keyHash & this.mask;
        entry.nextInKToVBucket = this.hashTableKToV[keyBucket];
        this.hashTableKToV[keyBucket] = entry;
        final int valueBucket = entry.valueHash & this.mask;
        entry.nextInVToKBucket = this.hashTableVToK[valueBucket];
        this.hashTableVToK[valueBucket] = entry;
        if (oldEntryForKey == null) {
            entry.prevInKeyInsertionOrder = this.lastInKeyInsertionOrder;
            entry.nextInKeyInsertionOrder = null;
            if (this.lastInKeyInsertionOrder == null) {
                this.firstInKeyInsertionOrder = entry;
            }
            else {
                this.lastInKeyInsertionOrder.nextInKeyInsertionOrder = entry;
            }
            this.lastInKeyInsertionOrder = entry;
        }
        else {
            entry.prevInKeyInsertionOrder = oldEntryForKey.prevInKeyInsertionOrder;
            if (entry.prevInKeyInsertionOrder == null) {
                this.firstInKeyInsertionOrder = entry;
            }
            else {
                entry.prevInKeyInsertionOrder.nextInKeyInsertionOrder = entry;
            }
            entry.nextInKeyInsertionOrder = oldEntryForKey.nextInKeyInsertionOrder;
            if (entry.nextInKeyInsertionOrder == null) {
                this.lastInKeyInsertionOrder = entry;
            }
            else {
                entry.nextInKeyInsertionOrder.prevInKeyInsertionOrder = entry;
            }
        }
        ++this.size;
        ++this.modCount;
    }
    
    private BiEntry<K, V> seekByKey(@Nullable final Object key, final int keyHash) {
        for (BiEntry<K, V> entry = this.hashTableKToV[keyHash & this.mask]; entry != null; entry = entry.nextInKToVBucket) {
            if (keyHash == entry.keyHash && Objects.equal(key, entry.key)) {
                return entry;
            }
        }
        return null;
    }
    
    private BiEntry<K, V> seekByValue(@Nullable final Object value, final int valueHash) {
        for (BiEntry<K, V> entry = this.hashTableVToK[valueHash & this.mask]; entry != null; entry = entry.nextInVToKBucket) {
            if (valueHash == entry.valueHash && Objects.equal(value, entry.value)) {
                return entry;
            }
        }
        return null;
    }
    
    @Override
    public boolean containsKey(@Nullable final Object key) {
        return this.seekByKey(key, Hashing.smearedHash(key)) != null;
    }
    
    @Override
    public boolean containsValue(@Nullable final Object value) {
        return this.seekByValue(value, Hashing.smearedHash(value)) != null;
    }
    
    @Nullable
    @Override
    public V get(@Nullable final Object key) {
        return Maps.valueOrNull(this.seekByKey(key, Hashing.smearedHash(key)));
    }
    
    @CanIgnoreReturnValue
    @Override
    public V put(@Nullable final K key, @Nullable final V value) {
        return this.put(key, value, false);
    }
    
    @CanIgnoreReturnValue
    @Override
    public V forcePut(@Nullable final K key, @Nullable final V value) {
        return this.put(key, value, true);
    }
    
    private V put(@Nullable final K key, @Nullable final V value, final boolean force) {
        final int keyHash = Hashing.smearedHash(key);
        final int valueHash = Hashing.smearedHash(value);
        final BiEntry<K, V> oldEntryForKey = this.seekByKey(key, keyHash);
        if (oldEntryForKey != null && valueHash == oldEntryForKey.valueHash && Objects.equal(value, oldEntryForKey.value)) {
            return value;
        }
        final BiEntry<K, V> oldEntryForValue = this.seekByValue(value, valueHash);
        if (oldEntryForValue != null) {
            if (!force) {
                throw new IllegalArgumentException("value already present: " + value);
            }
            this.delete(oldEntryForValue);
        }
        final BiEntry<K, V> newEntry = new BiEntry<K, V>(key, keyHash, value, valueHash);
        if (oldEntryForKey != null) {
            this.delete(oldEntryForKey);
            this.insert(newEntry, oldEntryForKey);
            oldEntryForKey.prevInKeyInsertionOrder = null;
            oldEntryForKey.nextInKeyInsertionOrder = null;
            this.rehashIfNecessary();
            return oldEntryForKey.value;
        }
        this.insert(newEntry, null);
        this.rehashIfNecessary();
        return null;
    }
    
    @Nullable
    private K putInverse(@Nullable final V value, @Nullable final K key, final boolean force) {
        final int valueHash = Hashing.smearedHash(value);
        final int keyHash = Hashing.smearedHash(key);
        final BiEntry<K, V> oldEntryForValue = this.seekByValue(value, valueHash);
        if (oldEntryForValue != null && keyHash == oldEntryForValue.keyHash && Objects.equal(key, oldEntryForValue.key)) {
            return key;
        }
        final BiEntry<K, V> oldEntryForKey = this.seekByKey(key, keyHash);
        if (oldEntryForKey != null) {
            if (!force) {
                throw new IllegalArgumentException("value already present: " + key);
            }
            this.delete(oldEntryForKey);
        }
        if (oldEntryForValue != null) {
            this.delete(oldEntryForValue);
        }
        final BiEntry<K, V> newEntry = new BiEntry<K, V>(key, keyHash, value, valueHash);
        this.insert(newEntry, oldEntryForKey);
        if (oldEntryForKey != null) {
            oldEntryForKey.prevInKeyInsertionOrder = null;
            oldEntryForKey.nextInKeyInsertionOrder = null;
        }
        this.rehashIfNecessary();
        return Maps.keyOrNull(oldEntryForValue);
    }
    
    private void rehashIfNecessary() {
        final BiEntry<K, V>[] oldKToV = this.hashTableKToV;
        if (Hashing.needsResizing(this.size, oldKToV.length, 1.0)) {
            final int newTableSize = oldKToV.length * 2;
            this.hashTableKToV = this.createTable(newTableSize);
            this.hashTableVToK = this.createTable(newTableSize);
            this.mask = newTableSize - 1;
            this.size = 0;
            for (BiEntry<K, V> entry = this.firstInKeyInsertionOrder; entry != null; entry = entry.nextInKeyInsertionOrder) {
                this.insert(entry, entry);
            }
            ++this.modCount;
        }
    }
    
    private BiEntry<K, V>[] createTable(final int length) {
        return (BiEntry<K, V>[])new BiEntry[length];
    }
    
    @CanIgnoreReturnValue
    @Override
    public V remove(@Nullable final Object key) {
        final BiEntry<K, V> entry = this.seekByKey(key, Hashing.smearedHash(key));
        if (entry == null) {
            return null;
        }
        this.delete(entry);
        entry.prevInKeyInsertionOrder = null;
        entry.nextInKeyInsertionOrder = null;
        return entry.value;
    }
    
    @Override
    public void clear() {
        this.size = 0;
        Arrays.fill(this.hashTableKToV, null);
        Arrays.fill(this.hashTableVToK, null);
        this.firstInKeyInsertionOrder = null;
        this.lastInKeyInsertionOrder = null;
        ++this.modCount;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public Set<K> keySet() {
        return (Set<K>)new KeySet();
    }
    
    @Override
    public Set<V> values() {
        return this.inverse().keySet();
    }
    
    @Override
    Iterator<Map.Entry<K, V>> entryIterator() {
        return new Itr<Map.Entry<K, V>>() {
            @Override
            Map.Entry<K, V> output(final BiEntry<K, V> entry) {
                return new MapEntry(entry);
            }
            
            class MapEntry extends AbstractMapEntry<K, V>
            {
                BiEntry<K, V> delegate;
                
                MapEntry(final BiEntry<K, V> entry) {
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
                public V setValue(final V value) {
                    final V oldValue = this.delegate.value;
                    final int valueHash = Hashing.smearedHash(value);
                    if (valueHash == this.delegate.valueHash && Objects.equal(value, oldValue)) {
                        return value;
                    }
                    Preconditions.checkArgument(HashBiMap.this.seekByValue(value, valueHash) == null, "value already present: %s", value);
                    HashBiMap.this.delete(this.delegate);
                    final BiEntry<K, V> newEntry = new BiEntry<K, V>(this.delegate.key, this.delegate.keyHash, value, valueHash);
                    HashBiMap.this.insert(newEntry, this.delegate);
                    this.delegate.prevInKeyInsertionOrder = null;
                    this.delegate.nextInKeyInsertionOrder = null;
                    Itr.this.expectedModCount = HashBiMap.this.modCount;
                    if (Itr.this.toRemove == this.delegate) {
                        Itr.this.toRemove = newEntry;
                    }
                    this.delegate = newEntry;
                    return oldValue;
                }
            }
        };
    }
    
    @Override
    public void forEach(final BiConsumer<? super K, ? super V> action) {
        Preconditions.checkNotNull(action);
        for (BiEntry<K, V> entry = this.firstInKeyInsertionOrder; entry != null; entry = entry.nextInKeyInsertionOrder) {
            action.accept((Object)entry.key, (Object)entry.value);
        }
    }
    
    @Override
    public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
        Preconditions.checkNotNull(function);
        final BiEntry<K, V> oldFirst = this.firstInKeyInsertionOrder;
        this.clear();
        for (BiEntry<K, V> entry = oldFirst; entry != null; entry = entry.nextInKeyInsertionOrder) {
            this.put(entry.key, function.apply((Object)entry.key, (Object)entry.value));
        }
    }
    
    @Override
    public BiMap<V, K> inverse() {
        return (this.inverse == null) ? (this.inverse = new Inverse()) : this.inverse;
    }
    
    @GwtIncompatible
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        Serialization.writeMap((Map<Object, Object>)this, stream);
    }
    
    @GwtIncompatible
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.init(16);
        final int size = Serialization.readCount(stream);
        Serialization.populateMap((Map<Object, Object>)this, stream, size);
    }
    
    private static final class BiEntry<K, V> extends ImmutableEntry<K, V>
    {
        final int keyHash;
        final int valueHash;
        @Nullable
        BiEntry<K, V> nextInKToVBucket;
        @Nullable
        BiEntry<K, V> nextInVToKBucket;
        @Nullable
        BiEntry<K, V> nextInKeyInsertionOrder;
        @Nullable
        BiEntry<K, V> prevInKeyInsertionOrder;
        
        BiEntry(final K key, final int keyHash, final V value, final int valueHash) {
            super(key, value);
            this.keyHash = keyHash;
            this.valueHash = valueHash;
        }
    }
    
    abstract class Itr<T> implements Iterator<T>
    {
        BiEntry<K, V> next;
        BiEntry<K, V> toRemove;
        int expectedModCount;
        
        Itr() {
            this.next = HashBiMap.this.firstInKeyInsertionOrder;
            this.toRemove = null;
            this.expectedModCount = HashBiMap.this.modCount;
        }
        
        @Override
        public boolean hasNext() {
            if (HashBiMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            return this.next != null;
        }
        
        @Override
        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final BiEntry<K, V> entry = this.next;
            this.next = entry.nextInKeyInsertionOrder;
            this.toRemove = entry;
            return this.output(entry);
        }
        
        @Override
        public void remove() {
            if (HashBiMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            CollectPreconditions.checkRemove(this.toRemove != null);
            HashBiMap.this.delete(this.toRemove);
            this.expectedModCount = HashBiMap.this.modCount;
            this.toRemove = null;
        }
        
        abstract T output(final BiEntry<K, V> p0);
    }
    
    private final class KeySet extends Maps.KeySet<K, V>
    {
        KeySet() {
            super(HashBiMap.this);
        }
        
        @Override
        public Iterator<K> iterator() {
            return new Itr<K>() {
                @Override
                K output(final BiEntry<K, V> entry) {
                    return entry.key;
                }
            };
        }
        
        @Override
        public boolean remove(@Nullable final Object o) {
            final BiEntry<K, V> entry = (BiEntry<K, V>)HashBiMap.this.seekByKey(o, Hashing.smearedHash(o));
            if (entry == null) {
                return false;
            }
            HashBiMap.this.delete(entry);
            entry.prevInKeyInsertionOrder = null;
            entry.nextInKeyInsertionOrder = null;
            return true;
        }
    }
    
    private final class Inverse extends Maps.IteratorBasedAbstractMap<V, K> implements BiMap<V, K>, Serializable
    {
        BiMap<K, V> forward() {
            return (BiMap<K, V>)HashBiMap.this;
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
        public boolean containsKey(@Nullable final Object value) {
            return this.forward().containsValue(value);
        }
        
        @Override
        public K get(@Nullable final Object value) {
            return Maps.keyOrNull((Map.Entry<K, ?>)HashBiMap.this.seekByValue(value, Hashing.smearedHash(value)));
        }
        
        @CanIgnoreReturnValue
        @Override
        public K put(@Nullable final V value, @Nullable final K key) {
            return (K)HashBiMap.this.putInverse(value, key, false);
        }
        
        @Override
        public K forcePut(@Nullable final V value, @Nullable final K key) {
            return (K)HashBiMap.this.putInverse(value, key, true);
        }
        
        @Override
        public K remove(@Nullable final Object value) {
            final BiEntry<K, V> entry = (BiEntry<K, V>)HashBiMap.this.seekByValue(value, Hashing.smearedHash(value));
            if (entry == null) {
                return null;
            }
            HashBiMap.this.delete(entry);
            entry.prevInKeyInsertionOrder = null;
            entry.nextInKeyInsertionOrder = null;
            return entry.key;
        }
        
        @Override
        public BiMap<K, V> inverse() {
            return this.forward();
        }
        
        @Override
        public Set<V> keySet() {
            return (Set<V>)new InverseKeySet();
        }
        
        @Override
        public Set<K> values() {
            return this.forward().keySet();
        }
        
        @Override
        Iterator<Map.Entry<V, K>> entryIterator() {
            return new Itr<Map.Entry<V, K>>() {
                @Override
                Map.Entry<V, K> output(final BiEntry<K, V> entry) {
                    return new InverseEntry(entry);
                }
                
                class InverseEntry extends AbstractMapEntry<V, K>
                {
                    BiEntry<K, V> delegate;
                    
                    InverseEntry(final BiEntry<K, V> entry) {
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
                    public K setValue(final K key) {
                        final K oldKey = this.delegate.key;
                        final int keyHash = Hashing.smearedHash(key);
                        if (keyHash == this.delegate.keyHash && Objects.equal(key, oldKey)) {
                            return key;
                        }
                        Preconditions.checkArgument(HashBiMap.this.seekByKey(key, keyHash) == null, "value already present: %s", key);
                        HashBiMap.this.delete(this.delegate);
                        final BiEntry<K, V> newEntry = new BiEntry<K, V>(key, keyHash, this.delegate.value, this.delegate.valueHash);
                        this.delegate = newEntry;
                        HashBiMap.this.insert(newEntry, null);
                        Itr.this.expectedModCount = HashBiMap.this.modCount;
                        return oldKey;
                    }
                }
            };
        }
        
        @Override
        public void forEach(final BiConsumer<? super V, ? super K> action) {
            Preconditions.checkNotNull(action);
            HashBiMap.this.forEach((k, v) -> action.accept((Object)v, (Object)k));
        }
        
        @Override
        public void replaceAll(final BiFunction<? super V, ? super K, ? extends K> function) {
            Preconditions.checkNotNull(function);
            final BiEntry<K, V> oldFirst = HashBiMap.this.firstInKeyInsertionOrder;
            this.clear();
            for (BiEntry<K, V> entry = oldFirst; entry != null; entry = entry.nextInKeyInsertionOrder) {
                this.put(entry.value, function.apply((Object)entry.value, (Object)entry.key));
            }
        }
        
        Object writeReplace() {
            return new InverseSerializedForm(HashBiMap.this);
        }
        
        private final class InverseKeySet extends Maps.KeySet<V, K>
        {
            InverseKeySet() {
                super(Inverse.this);
            }
            
            @Override
            public boolean remove(@Nullable final Object o) {
                final BiEntry<K, V> entry = (BiEntry<K, V>)HashBiMap.this.seekByValue(o, Hashing.smearedHash(o));
                if (entry == null) {
                    return false;
                }
                HashBiMap.this.delete(entry);
                return true;
            }
            
            @Override
            public Iterator<V> iterator() {
                return new Itr<V>() {
                    @Override
                    V output(final BiEntry<K, V> entry) {
                        return entry.value;
                    }
                };
            }
        }
    }
    
    private static final class InverseSerializedForm<K, V> implements Serializable
    {
        private final HashBiMap<K, V> bimap;
        
        InverseSerializedForm(final HashBiMap<K, V> bimap) {
            this.bimap = bimap;
        }
        
        Object readResolve() {
            return this.bimap.inverse();
        }
    }
}
