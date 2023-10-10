// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Set;
import java.util.Collection;
import java.util.function.BiConsumer;
import com.viaversion.viaversion.libs.fastutil.ints.IntSets;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public final class Object2IntMaps
{
    public static final EmptyMap EMPTY_MAP;
    
    private Object2IntMaps() {
    }
    
    public static <K> ObjectIterator<Object2IntMap.Entry<K>> fastIterator(final Object2IntMap<K> map) {
        final ObjectSet<Object2IntMap.Entry<K>> entries = map.object2IntEntrySet();
        return (entries instanceof Object2IntMap.FastEntrySet) ? ((Object2IntMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K> void fastForEach(final Object2IntMap<K> map, final Consumer<? super Object2IntMap.Entry<K>> consumer) {
        final ObjectSet<Object2IntMap.Entry<K>> entries = map.object2IntEntrySet();
        if (entries instanceof Object2IntMap.FastEntrySet) {
            ((Object2IntMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach(consumer);
        }
    }
    
    public static <K> ObjectIterable<Object2IntMap.Entry<K>> fastIterable(final Object2IntMap<K> map) {
        final ObjectSet<Object2IntMap.Entry<K>> entries = map.object2IntEntrySet();
        return (entries instanceof Object2IntMap.FastEntrySet) ? new ObjectIterable<Object2IntMap.Entry<K>>() {
            @Override
            public ObjectIterator<Object2IntMap.Entry<K>> iterator() {
                return ((Object2IntMap.FastEntrySet)entries).fastIterator();
            }
            
            @Override
            public ObjectSpliterator<Object2IntMap.Entry<K>> spliterator() {
                return entries.spliterator();
            }
            
            @Override
            public void forEach(final Consumer<? super Object2IntMap.Entry<K>> consumer) {
                ((Object2IntMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static <K> Object2IntMap<K> emptyMap() {
        return (Object2IntMap<K>)Object2IntMaps.EMPTY_MAP;
    }
    
    public static <K> Object2IntMap<K> singleton(final K key, final int value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2IntMap<K> singleton(final K key, final Integer value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2IntMap<K> synchronize(final Object2IntMap<K> m) {
        return (Object2IntMap<K>)new Object2IntMaps.SynchronizedMap((Object2IntMap)m);
    }
    
    public static <K> Object2IntMap<K> synchronize(final Object2IntMap<K> m, final Object sync) {
        return (Object2IntMap<K>)new Object2IntMaps.SynchronizedMap((Object2IntMap)m, sync);
    }
    
    public static <K> Object2IntMap<K> unmodifiable(final Object2IntMap<? extends K> m) {
        return (Object2IntMap<K>)new Object2IntMaps.UnmodifiableMap((Object2IntMap)m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<K> extends Object2IntFunctions.EmptyFunction<K> implements Object2IntMap<K>, Serializable, Cloneable
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
        public int getOrDefault(final Object key, final int defaultValue) {
            return defaultValue;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> object2IntEntrySet() {
            return (ObjectSet<Entry<K>>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSet<K> keySet() {
            return (ObjectSet<K>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public IntCollection values() {
            return IntSets.EMPTY_SET;
        }
        
        @Override
        public void forEach(final BiConsumer<? super K, ? super Integer> consumer) {
        }
        
        @Override
        public Object clone() {
            return Object2IntMaps.EMPTY_MAP;
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
    
    public static class Singleton<K> extends Object2IntFunctions.Singleton<K> implements Object2IntMap<K>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected transient IntCollection values;
        
        protected Singleton(final K key, final int value) {
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
        public void putAll(final Map<? extends K, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> object2IntEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Entry<K>>)ObjectSets.singleton(new AbstractObject2IntMap.BasicEntry<K>(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Integer>> entrySet() {
            return (ObjectSet<Map.Entry<K, Integer>>)this.object2IntEntrySet();
        }
        
        @Override
        public ObjectSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSets.singleton(this.key);
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
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ this.value;
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
