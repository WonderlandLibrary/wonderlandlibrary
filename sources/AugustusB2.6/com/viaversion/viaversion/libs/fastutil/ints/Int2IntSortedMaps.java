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

public final class Int2IntSortedMaps
{
    public static final EmptySortedMap EMPTY_MAP;
    
    private Int2IntSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Integer, ?>> entryComparator(final IntComparator comparator) {
        return (x, y) -> comparator.compare((int)x.getKey(), (int)y.getKey());
    }
    
    public static ObjectBidirectionalIterator<Int2IntMap.Entry> fastIterator(final Int2IntSortedMap map) {
        final ObjectSortedSet<Int2IntMap.Entry> entries = map.int2IntEntrySet();
        return (entries instanceof Int2IntSortedMap.FastSortedEntrySet) ? ((Int2IntSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Int2IntMap.Entry> fastIterable(final Int2IntSortedMap map) {
        final ObjectSortedSet<Int2IntMap.Entry> entries = map.int2IntEntrySet();
        ObjectBidirectionalIterable<Int2IntMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Int2IntSortedMap.FastSortedEntrySet) {
            final Int2IntSortedMap.FastSortedEntrySet obj = (Int2IntSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(obj);
            objectBidirectionalIterable = obj::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Int2IntSortedMap singleton(final Integer key, final Integer value) {
        return new Singleton(key, value);
    }
    
    public static Int2IntSortedMap singleton(final Integer key, final Integer value, final IntComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Int2IntSortedMap singleton(final int key, final int value) {
        return new Singleton(key, value);
    }
    
    public static Int2IntSortedMap singleton(final int key, final int value, final IntComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Int2IntSortedMap synchronize(final Int2IntSortedMap m) {
        return (Int2IntSortedMap)new Int2IntSortedMaps.SynchronizedSortedMap(m);
    }
    
    public static Int2IntSortedMap synchronize(final Int2IntSortedMap m, final Object sync) {
        return (Int2IntSortedMap)new Int2IntSortedMaps.SynchronizedSortedMap(m, sync);
    }
    
    public static Int2IntSortedMap unmodifiable(final Int2IntSortedMap m) {
        return (Int2IntSortedMap)new Int2IntSortedMaps.UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Int2IntMaps.EmptyMap implements Int2IntSortedMap, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public IntComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Int2IntMap.Entry> int2IntEntrySet() {
            return (ObjectSortedSet<Int2IntMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, Integer>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public IntSortedSet keySet() {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public Int2IntSortedMap subMap(final int from, final int to) {
            return Int2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2IntSortedMap headMap(final int to) {
            return Int2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2IntSortedMap tailMap(final int from) {
            return Int2IntSortedMaps.EMPTY_MAP;
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
        public Int2IntSortedMap headMap(final Integer oto) {
            return this.headMap((int)oto);
        }
        
        @Deprecated
        @Override
        public Int2IntSortedMap tailMap(final Integer ofrom) {
            return this.tailMap((int)ofrom);
        }
        
        @Deprecated
        @Override
        public Int2IntSortedMap subMap(final Integer ofrom, final Integer oto) {
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
    
    public static class Singleton extends Int2IntMaps.Singleton implements Int2IntSortedMap, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntComparator comparator;
        
        protected Singleton(final int key, final int value, final IntComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final int key, final int value) {
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
        public ObjectSortedSet<Int2IntMap.Entry> int2IntEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Int2IntMap.Entry>)ObjectSortedSets.singleton(new AbstractInt2IntMap.BasicEntry(this.key, this.value), Int2IntSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Int2IntMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, Integer>>)this.int2IntEntrySet();
        }
        
        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.singleton(this.key, this.comparator);
            }
            return (IntSortedSet)this.keys;
        }
        
        @Override
        public Int2IntSortedMap subMap(final int from, final int to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Int2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2IntSortedMap headMap(final int to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Int2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2IntSortedMap tailMap(final int from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Int2IntSortedMaps.EMPTY_MAP;
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
        public Int2IntSortedMap headMap(final Integer oto) {
            return this.headMap((int)oto);
        }
        
        @Deprecated
        @Override
        public Int2IntSortedMap tailMap(final Integer ofrom) {
            return this.tailMap((int)ofrom);
        }
        
        @Deprecated
        @Override
        public Int2IntSortedMap subMap(final Integer ofrom, final Integer oto) {
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
