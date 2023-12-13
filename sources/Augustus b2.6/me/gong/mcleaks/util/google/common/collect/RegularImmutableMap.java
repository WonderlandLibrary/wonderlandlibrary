// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.io.Serializable;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.j2objc.annotations.Weak;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.Map;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
final class RegularImmutableMap<K, V> extends ImmutableMap<K, V>
{
    private final transient Map.Entry<K, V>[] entries;
    private final transient ImmutableMapEntry<K, V>[] table;
    private final transient int mask;
    private static final double MAX_LOAD_FACTOR = 1.2;
    private static final long serialVersionUID = 0L;
    
    static <K, V> RegularImmutableMap<K, V> fromEntries(final Map.Entry<K, V>... entries) {
        return fromEntryArray(entries.length, entries);
    }
    
    static <K, V> RegularImmutableMap<K, V> fromEntryArray(final int n, final Map.Entry<K, V>[] entryArray) {
        Preconditions.checkPositionIndex(n, entryArray.length);
        Map.Entry<K, V>[] entries;
        if (n == entryArray.length) {
            entries = entryArray;
        }
        else {
            entries = (Map.Entry<K, V>[])ImmutableMapEntry.createEntryArray(n);
        }
        final int tableSize = Hashing.closedTableSize(n, 1.2);
        final ImmutableMapEntry<K, V>[] table = ImmutableMapEntry.createEntryArray(tableSize);
        final int mask = tableSize - 1;
        for (int entryIndex = 0; entryIndex < n; ++entryIndex) {
            final Map.Entry<K, V> entry = entryArray[entryIndex];
            final K key = entry.getKey();
            final V value = entry.getValue();
            CollectPreconditions.checkEntryNotNull(key, value);
            final int tableIndex = Hashing.smear(key.hashCode()) & mask;
            final ImmutableMapEntry<K, V> existing = table[tableIndex];
            ImmutableMapEntry<K, V> newEntry;
            if (existing == null) {
                final boolean reusable = entry instanceof ImmutableMapEntry && ((ImmutableMapEntry)entry).isReusable();
                newEntry = (reusable ? ((ImmutableMapEntry)entry) : new ImmutableMapEntry<K, V>((K)key, (V)value));
            }
            else {
                newEntry = new ImmutableMapEntry.NonTerminalImmutableMapEntry<K, V>(key, value, existing);
            }
            table[tableIndex] = newEntry;
            checkNoConflictInKeyBucket(key, entries[entryIndex] = newEntry, existing);
        }
        return new RegularImmutableMap<K, V>(entries, table, mask);
    }
    
    private RegularImmutableMap(final Map.Entry<K, V>[] entries, final ImmutableMapEntry<K, V>[] table, final int mask) {
        this.entries = entries;
        this.table = table;
        this.mask = mask;
    }
    
    static void checkNoConflictInKeyBucket(final Object key, final Map.Entry<?, ?> entry, @Nullable ImmutableMapEntry<?, ?> keyBucketHead) {
        while (keyBucketHead != null) {
            ImmutableMap.checkNoConflict(!key.equals(keyBucketHead.getKey()), "key", entry, keyBucketHead);
            keyBucketHead = keyBucketHead.getNextInKeyBucket();
        }
    }
    
    @Override
    public V get(@Nullable final Object key) {
        return get(key, this.table, this.mask);
    }
    
    @Nullable
    static <V> V get(@Nullable final Object key, final ImmutableMapEntry<?, V>[] keyTable, final int mask) {
        if (key == null) {
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
        return (ImmutableSet<K>)new KeySet((RegularImmutableMap<Object, Object>)this);
    }
    
    @Override
    ImmutableCollection<V> createValues() {
        return (ImmutableCollection<V>)new Values((RegularImmutableMap<Object, Object>)this);
    }
    
    @GwtCompatible(emulated = true)
    private static final class KeySet<K, V> extends Indexed<K>
    {
        @Weak
        private final RegularImmutableMap<K, V> map;
        
        KeySet(final RegularImmutableMap<K, V> map) {
            this.map = map;
        }
        
        @Override
        K get(final int index) {
            return ((RegularImmutableMap<Object, Object>)this.map).entries[index].getKey();
        }
        
        @Override
        public boolean contains(final Object object) {
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
        @Override
        Object writeReplace() {
            return new SerializedForm((ImmutableMap<Object, ?>)this.map);
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
        @Weak
        final RegularImmutableMap<K, V> map;
        
        Values(final RegularImmutableMap<K, V> map) {
            this.map = map;
        }
        
        @Override
        public V get(final int index) {
            return ((RegularImmutableMap<Object, Object>)this.map).entries[index].getValue();
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
        @Override
        Object writeReplace() {
            return new SerializedForm((ImmutableMap<?, Object>)this.map);
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
