// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.function.BiConsumer;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Objects;
import com.google.common.base.Preconditions;
import javax.annotation.CheckForNull;
import java.util.Map;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true, emulated = true)
final class RegularImmutableMap<K, V> extends ImmutableMap<K, V>
{
    static final ImmutableMap<Object, Object> EMPTY;
    @VisibleForTesting
    static final double MAX_LOAD_FACTOR = 1.2;
    @VisibleForTesting
    static final double HASH_FLOODING_FPP = 0.001;
    @VisibleForTesting
    static final int MAX_HASH_BUCKET_LENGTH = 8;
    @VisibleForTesting
    final transient Map.Entry<K, V>[] entries;
    @CheckForNull
    private final transient ImmutableMapEntry<K, V>[] table;
    private final transient int mask;
    private static final long serialVersionUID = 0L;
    
    static <K, V> ImmutableMap<K, V> fromEntries(final Map.Entry<K, V>... entries) {
        return fromEntryArray(entries.length, entries);
    }
    
    static <K, V> ImmutableMap<K, V> fromEntryArray(final int n, final Map.Entry<K, V>[] entryArray) {
        Preconditions.checkPositionIndex(n, entryArray.length);
        if (n == 0) {
            return (ImmutableMap<K, V>)(RegularImmutableMap)RegularImmutableMap.EMPTY;
        }
        final Map.Entry<K, V>[] entries = (Map.Entry<K, V>[])((n == entryArray.length) ? entryArray : ImmutableMapEntry.createEntryArray(n));
        final int tableSize = Hashing.closedTableSize(n, 1.2);
        final ImmutableMapEntry<K, V>[] table = ImmutableMapEntry.createEntryArray(tableSize);
        final int mask = tableSize - 1;
        for (int entryIndex = 0; entryIndex < n; ++entryIndex) {
            final Map.Entry<K, V> entry = Objects.requireNonNull(entryArray[entryIndex]);
            final K key = entry.getKey();
            final V value = entry.getValue();
            CollectPreconditions.checkEntryNotNull(key, value);
            final int tableIndex = Hashing.smear(key.hashCode()) & mask;
            final ImmutableMapEntry<K, V> existing = table[tableIndex];
            final ImmutableMapEntry<K, V> newEntry = (existing == null) ? makeImmutable(entry, key, value) : new ImmutableMapEntry.NonTerminalImmutableMapEntry<K, V>(key, value, existing);
            entries[entryIndex] = (table[tableIndex] = newEntry);
            final int bucketSize = checkNoConflictInKeyBucket(key, newEntry, existing);
            if (bucketSize > 8) {
                return JdkBackedImmutableMap.create(n, entryArray);
            }
        }
        return new RegularImmutableMap<K, V>(entries, table, mask);
    }
    
    static <K, V> ImmutableMapEntry<K, V> makeImmutable(final Map.Entry<K, V> entry, final K key, final V value) {
        final boolean reusable = entry instanceof ImmutableMapEntry && ((ImmutableMapEntry)entry).isReusable();
        return reusable ? ((ImmutableMapEntry)entry) : new ImmutableMapEntry<K, V>((K)key, (V)value);
    }
    
    static <K, V> ImmutableMapEntry<K, V> makeImmutable(final Map.Entry<K, V> entry) {
        return makeImmutable(entry, entry.getKey(), entry.getValue());
    }
    
    private RegularImmutableMap(final Map.Entry<K, V>[] entries, @CheckForNull final ImmutableMapEntry<K, V>[] table, final int mask) {
        this.entries = entries;
        this.table = table;
        this.mask = mask;
    }
    
    @CanIgnoreReturnValue
    static int checkNoConflictInKeyBucket(final Object key, final Map.Entry<?, ?> entry, @CheckForNull ImmutableMapEntry<?, ?> keyBucketHead) {
        int bucketSize = 0;
        while (keyBucketHead != null) {
            ImmutableMap.checkNoConflict(!key.equals(keyBucketHead.getKey()), "key", entry, keyBucketHead);
            ++bucketSize;
            keyBucketHead = keyBucketHead.getNextInKeyBucket();
        }
        return bucketSize;
    }
    
    @CheckForNull
    @Override
    public V get(@CheckForNull final Object key) {
        return get(key, this.table, this.mask);
    }
    
    @CheckForNull
    static <V> V get(@CheckForNull final Object key, @CheckForNull final ImmutableMapEntry<?, V>[] keyTable, final int mask) {
        if (key == null || keyTable == null) {
            return null;
        }
        final int index = Hashing.smear(key.hashCode()) & mask;
        for (ImmutableMapEntry<?, V> entry = keyTable[index]; entry != null; entry = entry.getNextInKeyBucket()) {
            final Object candidateKey = entry.getKey();
            if (key.equals(candidateKey)) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    @Override
    public void forEach(final BiConsumer<? super K, ? super V> action) {
        Preconditions.checkNotNull(action);
        for (final Map.Entry<K, V> entry : this.entries) {
            action.accept((Object)entry.getKey(), (Object)entry.getValue());
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
        return (ImmutableSet<Map.Entry<K, V>>)new ImmutableMapEntrySet.RegularEntrySet((ImmutableMap<Object, Object>)this, (Map.Entry<Object, Object>[])this.entries);
    }
    
    @Override
    ImmutableSet<K> createKeySet() {
        return new KeySet<K>(this);
    }
    
    @Override
    ImmutableCollection<V> createValues() {
        return (ImmutableCollection<V>)new Values((RegularImmutableMap<Object, Object>)this);
    }
    
    static {
        EMPTY = new RegularImmutableMap<Object, Object>((Map.Entry<Object, Object>[])ImmutableMap.EMPTY_ENTRY_ARRAY, null, 0);
    }
    
    @GwtCompatible(emulated = true)
    private static final class KeySet<K> extends IndexedImmutableSet<K>
    {
        private final RegularImmutableMap<K, ?> map;
        
        KeySet(final RegularImmutableMap<K, ?> map) {
            this.map = map;
        }
        
        @Override
        K get(final int index) {
            return (K)this.map.entries[index].getKey();
        }
        
        @Override
        public boolean contains(@CheckForNull final Object object) {
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
        
        @GwtIncompatible
        private static class SerializedForm<K> implements Serializable
        {
            final ImmutableMap<K, ?> map;
            private static final long serialVersionUID = 0L;
            
            SerializedForm(final ImmutableMap<K, ?> map) {
                this.map = map;
            }
            
            Object readResolve() {
                return this.map.keySet();
            }
        }
    }
    
    @GwtCompatible(emulated = true)
    private static final class Values<K, V> extends ImmutableList<V>
    {
        final RegularImmutableMap<K, V> map;
        
        Values(final RegularImmutableMap<K, V> map) {
            this.map = map;
        }
        
        @Override
        public V get(final int index) {
            return (V)this.map.entries[index].getValue();
        }
        
        @Override
        public int size() {
            return this.map.size();
        }
        
        @Override
        boolean isPartialView() {
            return true;
        }
        
        @GwtIncompatible
        private static class SerializedForm<V> implements Serializable
        {
            final ImmutableMap<?, V> map;
            private static final long serialVersionUID = 0L;
            
            SerializedForm(final ImmutableMap<?, V> map) {
                this.map = map;
            }
            
            Object readResolve() {
                return this.map.values();
            }
        }
    }
}
