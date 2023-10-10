// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.j2objc.annotations.RetainedWith;
import me.gong.mcleaks.util.google.errorprone.annotations.concurrent.LazyInit;
import java.util.Map;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
class RegularImmutableBiMap<K, V> extends ImmutableBiMap<K, V>
{
    static final RegularImmutableBiMap<Object, Object> EMPTY;
    static final double MAX_LOAD_FACTOR = 1.2;
    private final transient ImmutableMapEntry<K, V>[] keyTable;
    private final transient ImmutableMapEntry<K, V>[] valueTable;
    private final transient Map.Entry<K, V>[] entries;
    private final transient int mask;
    private final transient int hashCode;
    @LazyInit
    @RetainedWith
    private transient ImmutableBiMap<V, K> inverse;
    
    static <K, V> RegularImmutableBiMap<K, V> fromEntries(final Map.Entry<K, V>... entries) {
        return fromEntryArray(entries.length, entries);
    }
    
    static <K, V> RegularImmutableBiMap<K, V> fromEntryArray(final int n, final Map.Entry<K, V>[] entryArray) {
        Preconditions.checkPositionIndex(n, entryArray.length);
        final int tableSize = Hashing.closedTableSize(n, 1.2);
        final int mask = tableSize - 1;
        final ImmutableMapEntry<K, V>[] keyTable = ImmutableMapEntry.createEntryArray(tableSize);
        final ImmutableMapEntry<K, V>[] valueTable = ImmutableMapEntry.createEntryArray(tableSize);
        Map.Entry<K, V>[] entries;
        if (n == entryArray.length) {
            entries = entryArray;
        }
        else {
            entries = (Map.Entry<K, V>[])ImmutableMapEntry.createEntryArray(n);
        }
        int hashCode = 0;
        for (int i = 0; i < n; ++i) {
            final Map.Entry<K, V> entry = entryArray[i];
            final K key = entry.getKey();
            final V value = entry.getValue();
            CollectPreconditions.checkEntryNotNull(key, value);
            final int keyHash = key.hashCode();
            final int valueHash = value.hashCode();
            final int keyBucket = Hashing.smear(keyHash) & mask;
            final int valueBucket = Hashing.smear(valueHash) & mask;
            final ImmutableMapEntry<K, V> nextInKeyBucket = keyTable[keyBucket];
            RegularImmutableMap.checkNoConflictInKeyBucket(key, entry, nextInKeyBucket);
            final ImmutableMapEntry<K, V> nextInValueBucket = valueTable[valueBucket];
            checkNoConflictInValueBucket(value, entry, nextInValueBucket);
            ImmutableMapEntry<K, V> newEntry;
            if (nextInValueBucket == null && nextInKeyBucket == null) {
                final boolean reusable = entry instanceof ImmutableMapEntry && ((ImmutableMapEntry)entry).isReusable();
                newEntry = (reusable ? ((ImmutableMapEntry)entry) : new ImmutableMapEntry<K, V>((K)key, (V)value));
            }
            else {
                newEntry = new ImmutableMapEntry.NonTerminalImmutableBiMapEntry<K, V>(key, value, nextInKeyBucket, nextInValueBucket);
            }
            keyTable[keyBucket] = newEntry;
            entries[i] = (valueTable[valueBucket] = newEntry);
            hashCode += (keyHash ^ valueHash);
        }
        return new RegularImmutableBiMap<K, V>(keyTable, valueTable, entries, mask, hashCode);
    }
    
    private RegularImmutableBiMap(final ImmutableMapEntry<K, V>[] keyTable, final ImmutableMapEntry<K, V>[] valueTable, final Map.Entry<K, V>[] entries, final int mask, final int hashCode) {
        this.keyTable = keyTable;
        this.valueTable = valueTable;
        this.entries = entries;
        this.mask = mask;
        this.hashCode = hashCode;
    }
    
    private static void checkNoConflictInValueBucket(final Object value, final Map.Entry<?, ?> entry, @Nullable ImmutableMapEntry<?, ?> valueBucketHead) {
        while (valueBucketHead != null) {
            ImmutableMap.checkNoConflict(!value.equals(valueBucketHead.getValue()), "value", entry, valueBucketHead);
            valueBucketHead = valueBucketHead.getNextInValueBucket();
        }
    }
    
    @Nullable
    @Override
    public V get(@Nullable final Object key) {
        return (this.keyTable == null) ? null : RegularImmutableMap.get(key, this.keyTable, this.mask);
    }
    
    @Override
    ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return (ImmutableSet<Map.Entry<K, V>>)(this.isEmpty() ? ImmutableSet.of() : new ImmutableMapEntrySet.RegularEntrySet((ImmutableMap<Object, Object>)this, (Map.Entry<Object, Object>[])this.entries));
    }
    
    @Override
    public void forEach(final BiConsumer<? super K, ? super V> action) {
        Preconditions.checkNotNull(action);
        for (final Map.Entry<K, V> entry : this.entries) {
            action.accept((Object)entry.getKey(), (Object)entry.getValue());
        }
    }
    
    @Override
    boolean isHashCodeFast() {
        return true;
    }
    
    @Override
    public int hashCode() {
        return this.hashCode;
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
        if (this.isEmpty()) {
            return ImmutableBiMap.of();
        }
        final ImmutableBiMap<V, K> result = this.inverse;
        return (result == null) ? (this.inverse = new Inverse()) : result;
    }
    
    static {
        EMPTY = new RegularImmutableBiMap<Object, Object>(null, null, (Map.Entry<Object, Object>[])ImmutableMap.EMPTY_ENTRY_ARRAY, 0, 0);
    }
    
    private final class Inverse extends ImmutableBiMap<V, K>
    {
        @Override
        public int size() {
            return this.inverse().size();
        }
        
        @Override
        public ImmutableBiMap<K, V> inverse() {
            return (ImmutableBiMap<K, V>)RegularImmutableBiMap.this;
        }
        
        @Override
        public void forEach(final BiConsumer<? super V, ? super K> action) {
            Preconditions.checkNotNull(action);
            RegularImmutableBiMap.this.forEach((k, v) -> action.accept((Object)v, (Object)k));
        }
        
        @Override
        public K get(@Nullable final Object value) {
            if (value == null || RegularImmutableBiMap.this.valueTable == null) {
                return null;
            }
            final int bucket = Hashing.smear(value.hashCode()) & RegularImmutableBiMap.this.mask;
            for (ImmutableMapEntry<K, V> entry = RegularImmutableBiMap.this.valueTable[bucket]; entry != null; entry = entry.getNextInValueBucket()) {
                if (value.equals(entry.getValue())) {
                    return entry.getKey();
                }
            }
            return null;
        }
        
        @Override
        ImmutableSet<Map.Entry<V, K>> createEntrySet() {
            return (ImmutableSet<Map.Entry<V, K>>)new InverseEntrySet();
        }
        
        @Override
        boolean isPartialView() {
            return false;
        }
        
        @Override
        Object writeReplace() {
            return new InverseSerializedForm(RegularImmutableBiMap.this);
        }
        
        final class InverseEntrySet extends ImmutableMapEntrySet<V, K>
        {
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
                return (UnmodifiableIterator<Map.Entry<V, K>>)this.asList().iterator();
            }
            
            @Override
            public void forEach(final Consumer<? super Map.Entry<V, K>> action) {
                this.asList().forEach((Consumer<? super Map.Entry<K, V>>)action);
            }
            
            @Override
            ImmutableList<Map.Entry<V, K>> createAsList() {
                return new ImmutableAsList<Map.Entry<V, K>>() {
                    @Override
                    public Map.Entry<V, K> get(final int index) {
                        final Map.Entry<K, V> entry = RegularImmutableBiMap.this.entries[index];
                        return Maps.immutableEntry(entry.getValue(), entry.getKey());
                    }
                    
                    @Override
                    ImmutableCollection<Map.Entry<V, K>> delegateCollection() {
                        return (ImmutableCollection<Map.Entry<V, K>>)InverseEntrySet.this;
                    }
                };
            }
        }
    }
    
    private static class InverseSerializedForm<K, V> implements Serializable
    {
        private final ImmutableBiMap<K, V> forward;
        private static final long serialVersionUID = 1L;
        
        InverseSerializedForm(final ImmutableBiMap<K, V> forward) {
            this.forward = forward;
        }
        
        Object readResolve() {
            return this.forward.inverse();
        }
    }
}
