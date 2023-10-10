// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Objects;
import java.util.Set;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public final class Object2ObjectMaps
{
    public static final EmptyMap EMPTY_MAP;
    
    private Object2ObjectMaps() {
    }
    
    public static <K, V> ObjectIterator<Object2ObjectMap.Entry<K, V>> fastIterator(final Object2ObjectMap<K, V> map) {
        final ObjectSet<Object2ObjectMap.Entry<K, V>> entries = map.object2ObjectEntrySet();
        return (entries instanceof Object2ObjectMap.FastEntrySet) ? ((Object2ObjectMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K, V> void fastForEach(final Object2ObjectMap<K, V> map, final Consumer<? super Object2ObjectMap.Entry<K, V>> consumer) {
        final ObjectSet<Object2ObjectMap.Entry<K, V>> entries = map.object2ObjectEntrySet();
        if (entries instanceof Object2ObjectMap.FastEntrySet) {
            ((Object2ObjectMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach(consumer);
        }
    }
    
    public static <K, V> ObjectIterable<Object2ObjectMap.Entry<K, V>> fastIterable(final Object2ObjectMap<K, V> map) {
        final ObjectSet<Object2ObjectMap.Entry<K, V>> entries = map.object2ObjectEntrySet();
        return (entries instanceof Object2ObjectMap.FastEntrySet) ? new ObjectIterable<Object2ObjectMap.Entry<K, V>>() {
            @Override
            public ObjectIterator<Object2ObjectMap.Entry<K, V>> iterator() {
                return ((Object2ObjectMap.FastEntrySet)entries).fastIterator();
            }
            
            @Override
            public ObjectSpliterator<Object2ObjectMap.Entry<K, V>> spliterator() {
                return entries.spliterator();
            }
            
            @Override
            public void forEach(final Consumer<? super Object2ObjectMap.Entry<K, V>> consumer) {
                ((Object2ObjectMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static <K, V> Object2ObjectMap<K, V> emptyMap() {
        return (Object2ObjectMap<K, V>)Object2ObjectMaps.EMPTY_MAP;
    }
    
    public static <K, V> Object2ObjectMap<K, V> singleton(final K key, final V value) {
        return new Singleton<K, V>(key, value);
    }
    
    public static <K, V> Object2ObjectMap<K, V> synchronize(final Object2ObjectMap<K, V> m) {
        return (Object2ObjectMap<K, V>)new Object2ObjectMaps.SynchronizedMap((Object2ObjectMap)m);
    }
    
    public static <K, V> Object2ObjectMap<K, V> synchronize(final Object2ObjectMap<K, V> m, final Object sync) {
        return (Object2ObjectMap<K, V>)new Object2ObjectMaps.SynchronizedMap((Object2ObjectMap)m, sync);
    }
    
    public static <K, V> Object2ObjectMap<K, V> unmodifiable(final Object2ObjectMap<? extends K, ? extends V> m) {
        return (Object2ObjectMap<K, V>)new Object2ObjectMaps.UnmodifiableMap((Object2ObjectMap)m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<K, V> extends Object2ObjectFunctions.EmptyFunction<K, V> implements Object2ObjectMap<K, V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final Object v) {
            return false;
        }
        
        @Override
        public V getOrDefault(final Object key, final V defaultValue) {
            return defaultValue;
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K, V>> object2ObjectEntrySet() {
            return (ObjectSet<Entry<K, V>>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSet<K> keySet() {
            return (ObjectSet<K>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ObjectCollection<V> values() {
            return (ObjectCollection<V>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public void forEach(final BiConsumer<? super K, ? super V> consumer) {
        }
        
        @Override
        public Object clone() {
            return Object2ObjectMaps.EMPTY_MAP;
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
    
    public static class Singleton<K, V> extends Object2ObjectFunctions.Singleton<K, V> implements Object2ObjectMap<K, V>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<K, V>> entries;
        protected transient ObjectSet<K> keys;
        protected transient ObjectCollection<V> values;
        
        protected Singleton(final K key, final V value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final Object v) {
            return Objects.equals(this.value, v);
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K, V>> object2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = (ObjectSet<Entry<K, V>>)ObjectSets.singleton(new AbstractObject2ObjectMap.BasicEntry<K, V>(this.key, this.value));
            }
            return this.entries;
        }
        
        @Override
        public ObjectSet<Map.Entry<K, V>> entrySet() {
            return (ObjectSet<Map.Entry<K, V>>)this.object2ObjectEntrySet();
        }
        
        @Override
        public ObjectSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSets.singleton(this.key);
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
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : this.value.hashCode());
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
