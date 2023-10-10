// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.SortedMap;
import java.util.Set;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSet;
import java.util.NoSuchElementException;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSortedSets;
import java.io.Serializable;
import java.util.Objects;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectBidirectionalIterable;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSortedSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Map;
import java.util.Comparator;

public final class Int2ObjectSortedMaps
{
    public static final EmptySortedMap EMPTY_MAP;
    
    private Int2ObjectSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Integer, ?>> entryComparator(final IntComparator comparator) {
        return (x, y) -> comparator.compare((int)x.getKey(), (int)y.getKey());
    }
    
    public static <V> ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>> fastIterator(final Int2ObjectSortedMap<V> map) {
        final ObjectSortedSet<Int2ObjectMap.Entry<V>> entries = map.int2ObjectEntrySet();
        return (entries instanceof Int2ObjectSortedMap.FastSortedEntrySet) ? ((Int2ObjectSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <V> ObjectBidirectionalIterable<Int2ObjectMap.Entry<V>> fastIterable(final Int2ObjectSortedMap<V> map) {
        final ObjectSortedSet<Int2ObjectMap.Entry<V>> entries = map.int2ObjectEntrySet();
        ObjectBidirectionalIterable<Int2ObjectMap.Entry<V>> objectBidirectionalIterable;
        if (entries instanceof Int2ObjectSortedMap.FastSortedEntrySet) {
            final Int2ObjectSortedMap.FastSortedEntrySet obj = (Int2ObjectSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(obj);
            objectBidirectionalIterable = obj::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static <V> Int2ObjectSortedMap<V> emptyMap() {
        return (Int2ObjectSortedMap<V>)Int2ObjectSortedMaps.EMPTY_MAP;
    }
    
    public static <V> Int2ObjectSortedMap<V> singleton(final Integer key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Int2ObjectSortedMap<V> singleton(final Integer key, final V value, final IntComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Int2ObjectSortedMap<V> singleton(final int key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Int2ObjectSortedMap<V> singleton(final int key, final V value, final IntComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Int2ObjectSortedMap<V> synchronize(final Int2ObjectSortedMap<V> m) {
        return (Int2ObjectSortedMap<V>)new Int2ObjectSortedMaps.SynchronizedSortedMap((Int2ObjectSortedMap)m);
    }
    
    public static <V> Int2ObjectSortedMap<V> synchronize(final Int2ObjectSortedMap<V> m, final Object sync) {
        return (Int2ObjectSortedMap<V>)new Int2ObjectSortedMaps.SynchronizedSortedMap((Int2ObjectSortedMap)m, sync);
    }
    
    public static <V> Int2ObjectSortedMap<V> unmodifiable(final Int2ObjectSortedMap<? extends V> m) {
        return (Int2ObjectSortedMap<V>)new Int2ObjectSortedMaps.UnmodifiableSortedMap((Int2ObjectSortedMap)m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<V> extends Int2ObjectMaps.EmptyMap<V> implements Int2ObjectSortedMap<V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public IntComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
            return (ObjectSortedSet<Int2ObjectMap.Entry<V>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, V>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public IntSortedSet keySet() {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public Int2ObjectSortedMap<V> subMap(final int from, final int to) {
            return (Int2ObjectSortedMap<V>)Int2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2ObjectSortedMap<V> headMap(final int to) {
            return (Int2ObjectSortedMap<V>)Int2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2ObjectSortedMap<V> tailMap(final int from) {
            return (Int2ObjectSortedMap<V>)Int2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public int firstIntKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public int lastIntKey() {
            throw new NoSuchElementException();
        }
        
        @Deprecated
        @Override
        public Int2ObjectSortedMap<V> headMap(final Integer oto) {
            return this.headMap((int)oto);
        }
        
        @Deprecated
        @Override
        public Int2ObjectSortedMap<V> tailMap(final Integer ofrom) {
            return this.tailMap((int)ofrom);
        }
        
        @Deprecated
        @Override
        public Int2ObjectSortedMap<V> subMap(final Integer ofrom, final Integer oto) {
            return this.subMap((int)ofrom, (int)oto);
        }
        
        @Deprecated
        @Override
        public Integer firstKey() {
            return this.firstIntKey();
        }
        
        @Deprecated
        @Override
        public Integer lastKey() {
            return this.lastIntKey();
        }
    }
    
    public static class Singleton<V> extends Int2ObjectMaps.Singleton<V> implements Int2ObjectSortedMap<V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntComparator comparator;
        
        protected Singleton(final int key, final V value, final IntComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final int key, final V value) {
            this(key, value, null);
        }
        
        final int compare(final int k1, final int k2) {
            return (this.comparator == null) ? Integer.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public IntComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Int2ObjectMap.Entry<V>>)ObjectSortedSets.singleton(new AbstractInt2ObjectMap.BasicEntry<V>(this.key, this.value), Int2ObjectSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Int2ObjectMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, V>>)this.int2ObjectEntrySet();
        }
        
        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.singleton(this.key, this.comparator);
            }
            return (IntSortedSet)this.keys;
        }
        
        @Override
        public Int2ObjectSortedMap<V> subMap(final int from, final int to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return (Int2ObjectSortedMap<V>)Int2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2ObjectSortedMap<V> headMap(final int to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return (Int2ObjectSortedMap<V>)Int2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2ObjectSortedMap<V> tailMap(final int from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return (Int2ObjectSortedMap<V>)Int2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public int firstIntKey() {
            return this.key;
        }
        
        @Override
        public int lastIntKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Int2ObjectSortedMap<V> headMap(final Integer oto) {
            return this.headMap((int)oto);
        }
        
        @Deprecated
        @Override
        public Int2ObjectSortedMap<V> tailMap(final Integer ofrom) {
            return this.tailMap((int)ofrom);
        }
        
        @Deprecated
        @Override
        public Int2ObjectSortedMap<V> subMap(final Integer ofrom, final Integer oto) {
            return this.subMap((int)ofrom, (int)oto);
        }
        
        @Deprecated
        @Override
        public Integer firstKey() {
            return this.firstIntKey();
        }
        
        @Deprecated
        @Override
        public Integer lastKey() {
            return this.lastIntKey();
        }
    }
}
