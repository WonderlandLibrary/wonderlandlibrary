// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Objects;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.Size64;
import java.util.function.Consumer;
import java.io.Serializable;

public abstract class AbstractObject2ObjectMap<K, V> extends AbstractObject2ObjectFunction<K, V> implements Object2ObjectMap<K, V>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractObject2ObjectMap() {
    }
    
    @Override
    public boolean containsKey(final Object k) {
        final ObjectIterator<Entry<K, V>> i = this.object2ObjectEntrySet().iterator();
        while (i.hasNext()) {
            if (i.next().getKey() == k) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final Object v) {
        final ObjectIterator<Entry<K, V>> i = this.object2ObjectEntrySet().iterator();
        while (i.hasNext()) {
            if (i.next().getValue() == v) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public ObjectSet<K> keySet() {
        return new AbstractObjectSet<K>() {
            @Override
            public boolean contains(final Object k) {
                return AbstractObject2ObjectMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractObject2ObjectMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractObject2ObjectMap.this.clear();
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>() {
                    private final ObjectIterator<Entry<K, V>> i = Object2ObjectMaps.fastIterator((Object2ObjectMap<K, V>)AbstractObject2ObjectMap.this);
                    
                    @Override
                    public K next() {
                        return this.i.next().getKey();
                    }
                    
                    @Override
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                    
                    @Override
                    public void remove() {
                        this.i.remove();
                    }
                    
                    @Override
                    public void forEachRemaining(final Consumer<? super K> action) {
                        this.i.forEachRemaining(entry -> action.accept((Object)entry.getKey()));
                    }
                };
            }
            
            @Override
            public ObjectSpliterator<K> spliterator() {
                return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(AbstractObject2ObjectMap.this), 65);
            }
        };
    }
    
    @Override
    public ObjectCollection<V> values() {
        return new AbstractObjectCollection<V>() {
            @Override
            public boolean contains(final Object k) {
                return AbstractObject2ObjectMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractObject2ObjectMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractObject2ObjectMap.this.clear();
            }
            
            @Override
            public ObjectIterator<V> iterator() {
                return new ObjectIterator<V>() {
                    private final ObjectIterator<Entry<K, V>> i = Object2ObjectMaps.fastIterator((Object2ObjectMap<K, V>)AbstractObject2ObjectMap.this);
                    
                    @Override
                    public V next() {
                        return this.i.next().getValue();
                    }
                    
                    @Override
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                    
                    @Override
                    public void remove() {
                        this.i.remove();
                    }
                    
                    @Override
                    public void forEachRemaining(final Consumer<? super V> action) {
                        this.i.forEachRemaining(entry -> action.accept((Object)entry.getValue()));
                    }
                };
            }
            
            @Override
            public ObjectSpliterator<V> spliterator() {
                return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(AbstractObject2ObjectMap.this), 64);
            }
        };
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        if (m instanceof Object2ObjectMap) {
            final ObjectIterator<Entry<K, V>> i = Object2ObjectMaps.fastIterator((Object2ObjectMap<K, V>)(Object2ObjectMap)m);
            while (i.hasNext()) {
                final Entry<? extends K, ? extends V> e = i.next();
                this.put((K)e.getKey(), (V)e.getValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends K, ? extends V>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends K, ? extends V> e2 = (Map.Entry<? extends K, ? extends V>)j.next();
                this.put((K)e2.getKey(), (V)e2.getValue());
            }
        }
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry<K, V>> i = Object2ObjectMaps.fastIterator((Object2ObjectMap<K, V>)this);
        while (n-- != 0) {
            h += i.next().hashCode();
        }
        return h;
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
        return m.size() == this.size() && this.object2ObjectEntrySet().containsAll(m.entrySet());
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry<K, V>> i = Object2ObjectMaps.fastIterator((Object2ObjectMap<K, V>)this);
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            }
            else {
                s.append(", ");
            }
            final Entry<K, V> e = i.next();
            if (this == e.getKey()) {
                s.append("(this map)");
            }
            else {
                s.append(String.valueOf(e.getKey()));
            }
            s.append("=>");
            if (this == e.getValue()) {
                s.append("(this map)");
            }
            else {
                s.append(String.valueOf(e.getValue()));
            }
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry<K, V> implements Entry<K, V>
    {
        protected K key;
        protected V value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final K key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Override
        public V getValue() {
            return this.value;
        }
        
        @Override
        public V setValue(final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K, V> e = (Entry<K, V>)o;
                return Objects.equals(this.key, e.getKey()) && Objects.equals(this.value, e.getValue());
            }
            final Map.Entry<?, ?> e2 = (Map.Entry<?, ?>)o;
            final Object key = e2.getKey();
            final Object value = e2.getValue();
            return Objects.equals(this.key, key) && Objects.equals(this.value, value);
        }
        
        @Override
        public int hashCode() {
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : this.value.hashCode());
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
    
    public abstract static class BasicEntrySet<K, V> extends AbstractObjectSet<Entry<K, V>>
    {
        protected final Object2ObjectMap<K, V> map;
        
        public BasicEntrySet(final Object2ObjectMap<K, V> map) {
            this.map = map;
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K, V> e = (Entry<K, V>)o;
                final K k = e.getKey();
                return this.map.containsKey(k) && Objects.equals(this.map.get(k), e.getValue());
            }
            final Map.Entry<?, ?> e2 = (Map.Entry<?, ?>)o;
            final Object i = e2.getKey();
            final Object value = e2.getValue();
            return this.map.containsKey(i) && Objects.equals(this.map.get(i), value);
        }
        
        @Override
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K, V> e = (Entry<K, V>)o;
                return this.map.remove(e.getKey(), e.getValue());
            }
            final Map.Entry<?, ?> e2 = (Map.Entry<?, ?>)o;
            final Object k = e2.getKey();
            final Object v = e2.getValue();
            return this.map.remove(k, v);
        }
        
        @Override
        public int size() {
            return this.map.size();
        }
        
        @Override
        public ObjectSpliterator<Entry<K, V>> spliterator() {
            return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this.map), 65);
        }
    }
}
