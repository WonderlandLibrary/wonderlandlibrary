// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Objects;
import java.util.Set;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterators;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterator;
import java.util.function.IntConsumer;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.Size64;
import java.util.function.Consumer;
import com.viaversion.viaversion.libs.fastutil.ints.IntBinaryOperator;
import java.io.Serializable;

public abstract class AbstractObject2IntMap<K> extends AbstractObject2IntFunction<K> implements Object2IntMap<K>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractObject2IntMap() {
    }
    
    @Override
    public boolean containsKey(final Object k) {
        final ObjectIterator<Entry<K>> i = this.object2IntEntrySet().iterator();
        while (i.hasNext()) {
            if (i.next().getKey() == k) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final int v) {
        final ObjectIterator<Entry<K>> i = this.object2IntEntrySet().iterator();
        while (i.hasNext()) {
            if (i.next().getIntValue() == v) {
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
    public final int mergeInt(final K key, final int value, final IntBinaryOperator remappingFunction) {
        return this.mergeInt(key, value, (java.util.function.IntBinaryOperator)remappingFunction);
    }
    
    @Override
    public ObjectSet<K> keySet() {
        return new AbstractObjectSet<K>() {
            @Override
            public boolean contains(final Object k) {
                return AbstractObject2IntMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractObject2IntMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractObject2IntMap.this.clear();
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>() {
                    private final ObjectIterator<Entry<K>> i = Object2IntMaps.fastIterator((Object2IntMap<K>)AbstractObject2IntMap.this);
                    
                    @Override
                    public K next() {
                        return (K)this.i.next().getKey();
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
                        this.i.forEachRemaining(entry -> action.accept(entry.getKey()));
                    }
                };
            }
            
            @Override
            public ObjectSpliterator<K> spliterator() {
                return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(AbstractObject2IntMap.this), 65);
            }
        };
    }
    
    @Override
    public IntCollection values() {
        return new AbstractIntCollection() {
            @Override
            public boolean contains(final int k) {
                return AbstractObject2IntMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractObject2IntMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractObject2IntMap.this.clear();
            }
            
            @Override
            public IntIterator iterator() {
                return new IntIterator() {
                    private final ObjectIterator<Entry<K>> i = Object2IntMaps.fastIterator((Object2IntMap<K>)AbstractObject2IntMap.this);
                    
                    @Override
                    public int nextInt() {
                        return this.i.next().getIntValue();
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
                    public void forEachRemaining(final IntConsumer action) {
                        this.i.forEachRemaining(entry -> action.accept(entry.getIntValue()));
                    }
                };
            }
            
            @Override
            public IntSpliterator spliterator() {
                return IntSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(AbstractObject2IntMap.this), 320);
            }
        };
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends Integer> m) {
        if (m instanceof Object2IntMap) {
            final ObjectIterator<Entry<K>> i = Object2IntMaps.fastIterator((Object2IntMap<K>)(Object2IntMap)m);
            while (i.hasNext()) {
                final Entry<? extends K> e = i.next();
                this.put((K)e.getKey(), e.getIntValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends K, ? extends Integer>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends K, ? extends Integer> e2 = (Map.Entry<? extends K, ? extends Integer>)j.next();
                this.put((K)e2.getKey(), (Integer)e2.getValue());
            }
        }
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry<K>> i = Object2IntMaps.fastIterator((Object2IntMap<K>)this);
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
        return m.size() == this.size() && this.object2IntEntrySet().containsAll(m.entrySet());
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry<K>> i = Object2IntMaps.fastIterator((Object2IntMap<K>)this);
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
            final Entry<K> e = i.next();
            if (this == e.getKey()) {
                s.append("(this map)");
            }
            else {
                s.append(String.valueOf(e.getKey()));
            }
            s.append("=>");
            s.append(String.valueOf(e.getIntValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry<K> implements Entry<K>
    {
        protected K key;
        protected int value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final K key, final Integer value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final K key, final int value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Override
        public int getIntValue() {
            return this.value;
        }
        
        @Override
        public int setValue(final int value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K> e = (Entry<K>)o;
                return Objects.equals(this.key, e.getKey()) && this.value == e.getIntValue();
            }
            final Map.Entry<?, ?> e2 = (Map.Entry<?, ?>)o;
            final Object key = e2.getKey();
            final Object value = e2.getValue();
            return value != null && value instanceof Integer && Objects.equals(this.key, key) && this.value == (int)value;
        }
        
        @Override
        public int hashCode() {
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ this.value;
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
    
    public abstract static class BasicEntrySet<K> extends AbstractObjectSet<Entry<K>>
    {
        protected final Object2IntMap<K> map;
        
        public BasicEntrySet(final Object2IntMap<K> map) {
            this.map = map;
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K> e = (Entry<K>)o;
                final K k = e.getKey();
                return this.map.containsKey(k) && this.map.getInt(k) == e.getIntValue();
            }
            final Map.Entry<?, ?> e2 = (Map.Entry<?, ?>)o;
            final Object i = e2.getKey();
            final Object value = e2.getValue();
            return value != null && value instanceof Integer && this.map.containsKey(i) && this.map.getInt(i) == (int)value;
        }
        
        @Override
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<K> e = (Entry<K>)o;
                return this.map.remove(e.getKey(), e.getIntValue());
            }
            final Map.Entry<?, ?> e2 = (Map.Entry<?, ?>)o;
            final Object k = e2.getKey();
            final Object value = e2.getValue();
            if (value == null || !(value instanceof Integer)) {
                return false;
            }
            final int v = (int)value;
            return this.map.remove(k, v);
        }
        
        @Override
        public int size() {
            return this.map.size();
        }
        
        @Override
        public ObjectSpliterator<Entry<K>> spliterator() {
            return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this.map), 65);
        }
    }
}
