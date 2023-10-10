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

public final class Object2ObjectSortedMaps
{
    public static final EmptySortedMap EMPTY_MAP;
    
    private Object2ObjectSortedMaps() {
    }
    
    public static <K> Comparator<? super Map.Entry<K, ?>> entryComparator(final Comparator<? super K> comparator) {
        return (x, y) -> comparator.compare(x.getKey(), y.getKey());
    }
    
    public static <K, V> ObjectBidirectionalIterator<Object2ObjectMap.Entry<K, V>> fastIterator(final Object2ObjectSortedMap<K, V> map) {
        final ObjectSortedSet<Object2ObjectMap.Entry<K, V>> entries = map.object2ObjectEntrySet();
        return (entries instanceof Object2ObjectSortedMap.FastSortedEntrySet) ? ((Object2ObjectSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K, V> ObjectBidirectionalIterable<Object2ObjectMap.Entry<K, V>> fastIterable(final Object2ObjectSortedMap<K, V> map) {
        final ObjectSortedSet<Object2ObjectMap.Entry<K, V>> entries = map.object2ObjectEntrySet();
        ObjectBidirectionalIterable<Object2ObjectMap.Entry<K, V>> objectBidirectionalIterable;
        if (entries instanceof Object2ObjectSortedMap.FastSortedEntrySet) {
            final Object2ObjectSortedMap.FastSortedEntrySet obj = (Object2ObjectSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(obj);
            objectBidirectionalIterable = obj::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static <K, V> Object2ObjectSortedMap<K, V> emptyMap() {
        return (Object2ObjectSortedMap<K, V>)Object2ObjectSortedMaps.EMPTY_MAP;
    }
    
    public static <K, V> Object2ObjectSortedMap<K, V> singleton(final K key, final V value) {
        return new Singleton<K, V>(key, value);
    }
    
    public static <K, V> Object2ObjectSortedMap<K, V> singleton(final K key, final V value, final Comparator<? super K> comparator) {
        return new Singleton<K, V>(key, value, comparator);
    }
    
    public static <K, V> Object2ObjectSortedMap<K, V> synchronize(final Object2ObjectSortedMap<K, V> m) {
        return (Object2ObjectSortedMap<K, V>)new Object2ObjectSortedMaps.SynchronizedSortedMap((Object2ObjectSortedMap)m);
    }
    
    public static <K, V> Object2ObjectSortedMap<K, V> synchronize(final Object2ObjectSortedMap<K, V> m, final Object sync) {
        return (Object2ObjectSortedMap<K, V>)new Object2ObjectSortedMaps.SynchronizedSortedMap((Object2ObjectSortedMap)m, sync);
    }
    
    public static <K, V> Object2ObjectSortedMap<K, V> unmodifiable(final Object2ObjectSortedMap<K, ? extends V> m) {
        return (Object2ObjectSortedMap<K, V>)new Object2ObjectSortedMaps.UnmodifiableSortedMap((Object2ObjectSortedMap)m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<K, V> extends Object2ObjectMaps.EmptyMap<K, V> implements Object2ObjectSortedMap<K, V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Object2ObjectMap.Entry<K, V>> object2ObjectEntrySet() {
            return (ObjectSortedSet<Object2ObjectMap.Entry<K, V>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<K, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, V>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<K> keySet() {
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public Object2ObjectSortedMap<K, V> subMap(final K from, final K to) {
            return (Object2ObjectSortedMap<K, V>)Object2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Object2ObjectSortedMap<K, V> headMap(final K to) {
            return (Object2ObjectSortedMap<K, V>)Object2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Object2ObjectSortedMap<K, V> tailMap(final K from) {
            return (Object2ObjectSortedMap<K, V>)Object2ObjectSortedMaps.EMPTY_MAP;
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
    
    public static class Singleton<K, V> extends Object2ObjectMaps.Singleton<K, V> implements Object2ObjectSortedMap<K, V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Comparator<? super K> comparator;
        
        protected Singleton(final K key, final V value, final Comparator<? super K> comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final K key, final V value) {
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
        public ObjectSortedSet<Object2ObjectMap.Entry<K, V>> object2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Object2ObjectMap.Entry<K, V>>)ObjectSortedSets.singleton(new AbstractObject2ObjectMap.BasicEntry<K, V>(this.key, this.value), (Comparator<? super AbstractObject2ObjectMap.BasicEntry<K, V>>)Object2ObjectSortedMaps.entryComparator((Comparator<? super Object>)this.comparator));
            }
            return (ObjectSortedSet<Object2ObjectMap.Entry<K, V>>)(ObjectSortedSet)this.entries;
        }
        
        @Override
        public ObjectSortedSet<Map.Entry<K, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, V>>)this.object2ObjectEntrySet();
        }
        
        @Override
        public ObjectSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSortedSets.singleton(this.key, this.comparator);
            }
            return (ObjectSortedSet<K>)(ObjectSortedSet)this.keys;
        }
        
        @Override
        public Object2ObjectSortedMap<K, V> subMap(final K from, final K to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return (Object2ObjectSortedMap<K, V>)Object2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Object2ObjectSortedMap<K, V> headMap(final K to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return (Object2ObjectSortedMap<K, V>)Object2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Object2ObjectSortedMap<K, V> tailMap(final K from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return (Object2ObjectSortedMap<K, V>)Object2ObjectSortedMaps.EMPTY_MAP;
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
