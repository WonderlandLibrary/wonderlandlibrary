// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Set;
import java.util.Collection;
import java.util.function.BiConsumer;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSets;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Spliterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterable;
import java.util.function.Consumer;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;

public final class Int2IntMaps
{
    public static final EmptyMap EMPTY_MAP;
    
    private Int2IntMaps() {
    }
    
    public static ObjectIterator<Int2IntMap.Entry> fastIterator(final Int2IntMap map) {
        final ObjectSet<Int2IntMap.Entry> entries = map.int2IntEntrySet();
        return (entries instanceof Int2IntMap.FastEntrySet) ? ((Int2IntMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Int2IntMap map, final Consumer<? super Int2IntMap.Entry> consumer) {
        final ObjectSet<Int2IntMap.Entry> entries = map.int2IntEntrySet();
        if (entries instanceof Int2IntMap.FastEntrySet) {
            ((Int2IntMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach(consumer);
        }
    }
    
    public static ObjectIterable<Int2IntMap.Entry> fastIterable(final Int2IntMap map) {
        final ObjectSet<Int2IntMap.Entry> entries = map.int2IntEntrySet();
        return (entries instanceof Int2IntMap.FastEntrySet) ? new ObjectIterable<Int2IntMap.Entry>() {
            @Override
            public ObjectIterator<Int2IntMap.Entry> iterator() {
                return ((Int2IntMap.FastEntrySet)entries).fastIterator();
            }
            
            @Override
            public ObjectSpliterator<Int2IntMap.Entry> spliterator() {
                return entries.spliterator();
            }
            
            @Override
            public void forEach(final Consumer<? super Int2IntMap.Entry> consumer) {
                ((Int2IntMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Int2IntMap singleton(final int key, final int value) {
        return new Singleton(key, value);
    }
    
    public static Int2IntMap singleton(final Integer key, final Integer value) {
        return new Singleton(key, value);
    }
    
    public static Int2IntMap synchronize(final Int2IntMap m) {
        return (Int2IntMap)new Int2IntMaps.SynchronizedMap(m);
    }
    
    public static Int2IntMap synchronize(final Int2IntMap m, final Object sync) {
        return (Int2IntMap)new Int2IntMaps.SynchronizedMap(m, sync);
    }
    
    public static Int2IntMap unmodifiable(final Int2IntMap m) {
        return (Int2IntMap)new Int2IntMaps.UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Int2IntFunctions.EmptyFunction implements Int2IntMap, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final int v) {
            return false;
        }
        
        @Deprecated
        @Override
        public Integer getOrDefault(final Object key, final Integer defaultValue) {
            return defaultValue;
        }
        
        @Override
        public int getOrDefault(final int key, final int defaultValue) {
            return defaultValue;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        @Override
        public void putAll(final Map<? extends Integer, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> int2IntEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public IntSet keySet() {
            return IntSets.EMPTY_SET;
        }
        
        @Override
        public IntCollection values() {
            return IntSets.EMPTY_SET;
        }
        
        @Override
        public void forEach(final BiConsumer<? super Integer, ? super Integer> consumer) {
        }
        
        @Override
        public Object clone() {
            return Int2IntMaps.EMPTY_MAP;
        }
        
        @Override
        public boolean isEmpty() {
            return true;
        }
        
        @Override
        public int hashCode() {
            return 0;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Map && ((Map)o).isEmpty();
        }
        
        @Override
        public String toString() {
            return "{}";
        }
    }
    
    public static class Singleton extends Int2IntFunctions.Singleton implements Int2IntMap, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient IntSet keys;
        protected transient IntCollection values;
        
        protected Singleton(final int key, final int value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final int v) {
            return this.value == v;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return (int)ov == this.value;
        }
        
        @Override
        public void putAll(final Map<? extends Integer, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> int2IntEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Entry>)ObjectSets.singleton(new AbstractInt2IntMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Integer, Integer>> entrySet() {
            return (ObjectSet<Map.Entry<Integer, Integer>>)this.int2IntEntrySet();
        }
        
        @Override
        public IntSet keySet() {
            if (this.keys == null) {
                this.keys = IntSets.singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public IntCollection values() {
            if (this.values == null) {
                this.values = IntSets.singleton(this.value);
            }
            return this.values;
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.key ^ this.value;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Map)) {
                return false;
            }
            final Map<?, ?> m = (Map<?, ?>)o;
            return m.size() == 1 && m.entrySet().iterator().next().equals(this.entrySet().iterator().next());
        }
        
        @Override
        public String toString() {
            return "{" + this.key + "=>" + this.value + "}";
        }
    }
}
