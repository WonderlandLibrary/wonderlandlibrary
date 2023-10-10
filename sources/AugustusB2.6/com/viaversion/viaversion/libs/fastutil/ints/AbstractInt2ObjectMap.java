// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectSet;
import java.util.Objects;
import java.util.Set;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterators;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import java.util.function.Consumer;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.Size64;
import java.util.function.IntConsumer;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractInt2ObjectMap<V> extends AbstractInt2ObjectFunction<V> implements Int2ObjectMap<V>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractInt2ObjectMap() {
    }
    
    @Override
    public boolean containsKey(final int k) {
        final ObjectIterator<Entry<V>> i = this.int2ObjectEntrySet().iterator();
        while (i.hasNext()) {
            if (i.next().getIntKey() == k) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final Object v) {
        final ObjectIterator<Entry<V>> i = this.int2ObjectEntrySet().iterator();
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
    public IntSet keySet() {
        return new AbstractIntSet() {
            @Override
            public boolean contains(final int k) {
                return AbstractInt2ObjectMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractInt2ObjectMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractInt2ObjectMap.this.clear();
            }
            
            @Override
            public IntIterator iterator() {
                return new IntIterator() {
                    private final ObjectIterator<Entry<V>> i = Int2ObjectMaps.fastIterator((Int2ObjectMap<V>)AbstractInt2ObjectMap.this);
                    
                    @Override
                    public int nextInt() {
                        return this.i.next().getIntKey();
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
                        this.i.forEachRemaining(entry -> action.accept(entry.getIntKey()));
                    }
                };
            }
            
            @Override
            public IntSpliterator spliterator() {
                return IntSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(AbstractInt2ObjectMap.this), 321);
            }
        };
    }
    
    @Override
    public ObjectCollection<V> values() {
        return new AbstractObjectCollection<V>() {
            @Override
            public boolean contains(final Object k) {
                return AbstractInt2ObjectMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractInt2ObjectMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractInt2ObjectMap.this.clear();
            }
            
            @Override
            public ObjectIterator<V> iterator() {
                return new ObjectIterator<V>() {
                    private final ObjectIterator<Entry<V>> i = Int2ObjectMaps.fastIterator((Int2ObjectMap<V>)AbstractInt2ObjectMap.this);
                    
                    @Override
                    public V next() {
                        return (V)this.i.next().getValue();
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
                        this.i.forEachRemaining(entry -> action.accept(entry.getValue()));
                    }
                };
            }
            
            @Override
            public ObjectSpliterator<V> spliterator() {
                return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(AbstractInt2ObjectMap.this), 64);
            }
        };
    }
    
    @Override
    public void putAll(final Map<? extends Integer, ? extends V> m) {
        if (m instanceof Int2ObjectMap) {
            final ObjectIterator<Entry<V>> i = Int2ObjectMaps.fastIterator((Int2ObjectMap<V>)(Int2ObjectMap)m);
            while (i.hasNext()) {
                final Entry<? extends V> e = i.next();
                this.put(e.getIntKey(), (V)e.getValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Integer, ? extends V>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Integer, ? extends V> e2 = (Map.Entry<? extends Integer, ? extends V>)j.next();
                this.put((Integer)e2.getKey(), (V)e2.getValue());
            }
        }
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry<V>> i = Int2ObjectMaps.fastIterator((Int2ObjectMap<V>)this);
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
        return m.size() == this.size() && this.int2ObjectEntrySet().containsAll(m.entrySet());
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry<V>> i = Int2ObjectMaps.fastIterator((Int2ObjectMap<V>)this);
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
            final Entry<V> e = i.next();
            s.append(String.valueOf(e.getIntKey()));
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
    
    public static class BasicEntry<V> implements Entry<V>
    {
        protected int key;
        protected V value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Integer key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final int key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public int getIntKey() {
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
                final Entry<V> e = (Entry<V>)o;
                return this.key == e.getIntKey() && Objects.equals(this.value, e.getValue());
            }
            final Map.Entry<?, ?> e2 = (Map.Entry<?, ?>)o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            final Object value = e2.getValue();
            return this.key == (int)key && Objects.equals(this.value, value);
        }
        
        @Override
        public int hashCode() {
            return this.key ^ ((this.value == null) ? 0 : this.value.hashCode());
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
    
    public abstract static class BasicEntrySet<V> extends AbstractObjectSet<Entry<V>>
    {
        protected final Int2ObjectMap<V> map;
        
        public BasicEntrySet(final Int2ObjectMap<V> map) {
            this.map = map;
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<V> e = (Entry<V>)o;
                final int k = e.getIntKey();
                return this.map.containsKey(k) && Objects.equals(this.map.get(k), e.getValue());
            }
            final Map.Entry<?, ?> e2 = (Map.Entry<?, ?>)o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            final int i = (int)key;
            final Object value = e2.getValue();
            return this.map.containsKey(i) && Objects.equals(this.map.get(i), value);
        }
        
        @Override
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry<V> e = (Entry<V>)o;
                return this.map.remove(e.getIntKey(), e.getValue());
            }
            final Map.Entry<?, ?> e2 = (Map.Entry<?, ?>)o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            final int k = (int)key;
            final Object v = e2.getValue();
            return this.map.remove(k, v);
        }
        
        @Override
        public int size() {
            return this.map.size();
        }
        
        @Override
        public ObjectSpliterator<Entry<V>> spliterator() {
            return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this.map), 65);
        }
    }
}
