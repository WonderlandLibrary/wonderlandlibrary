// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Objects;
import java.util.Set;
import java.util.Collection;
import java.util.function.BiConsumer;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
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

public final class Int2ObjectMaps
{
    public static final EmptyMap EMPTY_MAP;
    
    private Int2ObjectMaps() {
    }
    
    public static <V> ObjectIterator<Int2ObjectMap.Entry<V>> fastIterator(final Int2ObjectMap<V> map) {
        final ObjectSet<Int2ObjectMap.Entry<V>> entries = map.int2ObjectEntrySet();
        return (entries instanceof Int2ObjectMap.FastEntrySet) ? ((Int2ObjectMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <V> void fastForEach(final Int2ObjectMap<V> map, final Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
        final ObjectSet<Int2ObjectMap.Entry<V>> entries = map.int2ObjectEntrySet();
        if (entries instanceof Int2ObjectMap.FastEntrySet) {
            ((Int2ObjectMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach(consumer);
        }
    }
    
    public static <V> ObjectIterable<Int2ObjectMap.Entry<V>> fastIterable(final Int2ObjectMap<V> map) {
        final ObjectSet<Int2ObjectMap.Entry<V>> entries = map.int2ObjectEntrySet();
        return (entries instanceof Int2ObjectMap.FastEntrySet) ? new ObjectIterable<Int2ObjectMap.Entry<V>>() {
            @Override
            public ObjectIterator<Int2ObjectMap.Entry<V>> iterator() {
                return ((Int2ObjectMap.FastEntrySet)entries).fastIterator();
            }
            
            @Override
            public ObjectSpliterator<Int2ObjectMap.Entry<V>> spliterator() {
                return entries.spliterator();
            }
            
            @Override
            public void forEach(final Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
                ((Int2ObjectMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static <V> Int2ObjectMap<V> emptyMap() {
        return (Int2ObjectMap<V>)Int2ObjectMaps.EMPTY_MAP;
    }
    
    public static <V> Int2ObjectMap<V> singleton(final int key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Int2ObjectMap<V> singleton(final Integer key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Int2ObjectMap<V> synchronize(final Int2ObjectMap<V> m) {
        return (Int2ObjectMap<V>)new Int2ObjectMaps.SynchronizedMap((Int2ObjectMap)m);
    }
    
    public static <V> Int2ObjectMap<V> synchronize(final Int2ObjectMap<V> m, final Object sync) {
        return (Int2ObjectMap<V>)new Int2ObjectMaps.SynchronizedMap((Int2ObjectMap)m, sync);
    }
    
    public static <V> Int2ObjectMap<V> unmodifiable(final Int2ObjectMap<? extends V> m) {
        return (Int2ObjectMap<V>)new Int2ObjectMaps.UnmodifiableMap((Int2ObjectMap)m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<V> extends Int2ObjectFunctions.EmptyFunction<V> implements Int2ObjectMap<V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final Object v) {
            return false;
        }
        
        @Deprecated
        @Override
        public V getOrDefault(final Object key, final V defaultValue) {
            return defaultValue;
        }
        
        @Override
        public V getOrDefault(final int key, final V defaultValue) {
            return defaultValue;
        }
        
        @Override
        public void putAll(final Map<? extends Integer, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> int2ObjectEntrySet() {
            return (ObjectSet<Entry<V>>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public IntSet keySet() {
            return IntSets.EMPTY_SET;
        }
        
        @Override
        public ObjectCollection<V> values() {
            return (ObjectCollection<V>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public void forEach(final BiConsumer<? super Integer, ? super V> consumer) {
        }
        
        @Override
        public Object clone() {
            return Int2ObjectMaps.EMPTY_MAP;
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
    
    public static class Singleton<V> extends Int2ObjectFunctions.Singleton<V> implements Int2ObjectMap<V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient IntSet keys;
        protected transient ObjectCollection<V> values;
        
        protected Singleton(final int key, final V value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final Object v) {
            return Objects.equals(this.value, v);
        }
        
        @Override
        public void putAll(final Map<? extends Integer, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> int2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Entry<V>>)ObjectSets.singleton(new AbstractInt2ObjectMap.BasicEntry<V>(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Integer, V>> entrySet() {
            return (ObjectSet<Map.Entry<Integer, V>>)this.int2ObjectEntrySet();
        }
        
        @Override
        public IntSet keySet() {
            if (this.keys == null) {
                this.keys = IntSets.singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public ObjectCollection<V> values() {
            if (this.values == null) {
                this.values = ObjectSets.singleton(this.value);
            }
            return this.values;
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.key ^ ((this.value == null) ? 0 : this.value.hashCode());
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
