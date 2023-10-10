// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.SortedMap;
import java.util.Set;
import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.Objects;
import java.util.Map;
import java.util.Comparator;

public final class Object2IntSortedMaps
{
    public static final EmptySortedMap EMPTY_MAP;
    
    private Object2IntSortedMaps() {
    }
    
    public static <K> Comparator<? super Map.Entry<K, ?>> entryComparator(final Comparator<? super K> comparator) {
        return (x, y) -> comparator.compare(x.getKey(), y.getKey());
    }
    
    public static <K> ObjectBidirectionalIterator<Object2IntMap.Entry<K>> fastIterator(final Object2IntSortedMap<K> map) {
        final ObjectSortedSet<Object2IntMap.Entry<K>> entries = map.object2IntEntrySet();
        return (entries instanceof Object2IntSortedMap.FastSortedEntrySet) ? ((Object2IntSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K> ObjectBidirectionalIterable<Object2IntMap.Entry<K>> fastIterable(final Object2IntSortedMap<K> map) {
        final ObjectSortedSet<Object2IntMap.Entry<K>> entries = map.object2IntEntrySet();
        ObjectBidirectionalIterable<Object2IntMap.Entry<K>> objectBidirectionalIterable;
        if (entries instanceof Object2IntSortedMap.FastSortedEntrySet) {
            final Object2IntSortedMap.FastSortedEntrySet obj = (Object2IntSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(obj);
            objectBidirectionalIterable = obj::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static <K> Object2IntSortedMap<K> emptyMap() {
        return (Object2IntSortedMap<K>)Object2IntSortedMaps.EMPTY_MAP;
    }
    
    public static <K> Object2IntSortedMap<K> singleton(final K key, final Integer value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2IntSortedMap<K> singleton(final K key, final Integer value, final Comparator<? super K> comparator) {
        return new Singleton<K>(key, value, comparator);
    }
    
    public static <K> Object2IntSortedMap<K> singleton(final K key, final int value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2IntSortedMap<K> singleton(final K key, final int value, final Comparator<? super K> comparator) {
        return new Singleton<K>(key, value, comparator);
    }
    
    public static <K> Object2IntSortedMap<K> synchronize(final Object2IntSortedMap<K> m) {
        return (Object2IntSortedMap<K>)new Object2IntSortedMaps.SynchronizedSortedMap((Object2IntSortedMap)m);
    }
    
    public static <K> Object2IntSortedMap<K> synchronize(final Object2IntSortedMap<K> m, final Object sync) {
        return (Object2IntSortedMap<K>)new Object2IntSortedMaps.SynchronizedSortedMap((Object2IntSortedMap)m, sync);
    }
    
    public static <K> Object2IntSortedMap<K> unmodifiable(final Object2IntSortedMap<K> m) {
        return (Object2IntSortedMap<K>)new Object2IntSortedMaps.UnmodifiableSortedMap((Object2IntSortedMap)m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<K> extends Object2IntMaps.EmptyMap<K> implements Object2IntSortedMap<K>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Object2IntMap.Entry<K>> object2IntEntrySet() {
            return (ObjectSortedSet<Object2IntMap.Entry<K>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Integer>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<K> keySet() {
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public Object2IntSortedMap<K> subMap(final K from, final K to) {
            return (Object2IntSortedMap<K>)Object2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Object2IntSortedMap<K> headMap(final K to) {
            return (Object2IntSortedMap<K>)Object2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Object2IntSortedMap<K> tailMap(final K from) {
            return (Object2IntSortedMap<K>)Object2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public K firstKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public K lastKey() {
            throw new NoSuchElementException();
        }
    }
    
    public static class Singleton<K> extends Object2IntMaps.Singleton<K> implements Object2IntSortedMap<K>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Comparator<? super K> comparator;
        
        protected Singleton(final K key, final int value, final Comparator<? super K> comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final K key, final int value) {
            this(key, value, null);
        }
        
        final int compare(final K k1, final K k2) {
            return (this.comparator == null) ? ((Comparable)k1).compareTo(k2) : this.comparator.compare((Object)k1, (Object)k2);
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Object2IntMap.Entry<K>> object2IntEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Object2IntMap.Entry<K>>)ObjectSortedSets.singleton(new AbstractObject2IntMap.BasicEntry<K>(this.key, this.value), (Comparator<? super AbstractObject2IntMap.BasicEntry<K>>)Object2IntSortedMaps.entryComparator((Comparator<? super Object>)this.comparator));
            }
            return (ObjectSortedSet<Object2IntMap.Entry<K>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Integer>>)this.object2IntEntrySet();
        }
        
        @Override
        public ObjectSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSortedSets.singleton(this.key, this.comparator);
            }
            return (ObjectSortedSet<K>)(ObjectSortedSet)this.keys;
        }
        
        @Override
        public Object2IntSortedMap<K> subMap(final K from, final K to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return (Object2IntSortedMap<K>)Object2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Object2IntSortedMap<K> headMap(final K to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return (Object2IntSortedMap<K>)Object2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Object2IntSortedMap<K> tailMap(final K from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return (Object2IntSortedMap<K>)Object2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public K firstKey() {
            return this.key;
        }
        
        @Override
        public K lastKey() {
            return this.key;
        }
    }
}
