// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterators;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.Size64;
import java.util.function.IntConsumer;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractInt2IntMap extends AbstractInt2IntFunction implements Int2IntMap, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractInt2IntMap() {
    }
    
    @Override
    public boolean containsKey(final int k) {
        final ObjectIterator<Entry> i = this.int2IntEntrySet().iterator();
        while (i.hasNext()) {
            if (i.next().getIntKey() == k) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final int v) {
        final ObjectIterator<Entry> i = this.int2IntEntrySet().iterator();
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
    public final int mergeInt(final int key, final int value, final IntBinaryOperator remappingFunction) {
        return this.mergeInt(key, value, (java.util.function.IntBinaryOperator)remappingFunction);
    }
    
    @Override
    public IntSet keySet() {
        return new AbstractIntSet() {
            @Override
            public boolean contains(final int k) {
                return AbstractInt2IntMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractInt2IntMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractInt2IntMap.this.clear();
            }
            
            @Override
            public IntIterator iterator() {
                return new IntIterator() {
                    private final ObjectIterator<Entry> i = Int2IntMaps.fastIterator(AbstractInt2IntMap.this);
                    
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
                return IntSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(AbstractInt2IntMap.this), 321);
            }
        };
    }
    
    @Override
    public IntCollection values() {
        return new AbstractIntCollection() {
            @Override
            public boolean contains(final int k) {
                return AbstractInt2IntMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractInt2IntMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractInt2IntMap.this.clear();
            }
            
            @Override
            public IntIterator iterator() {
                return new IntIterator() {
                    private final ObjectIterator<Entry> i = Int2IntMaps.fastIterator(AbstractInt2IntMap.this);
                    
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
                return IntSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(AbstractInt2IntMap.this), 320);
            }
        };
    }
    
    @Override
    public void putAll(final Map<? extends Integer, ? extends Integer> m) {
        if (m instanceof Int2IntMap) {
            final ObjectIterator<Entry> i = Int2IntMaps.fastIterator((Int2IntMap)m);
            while (i.hasNext()) {
                final Entry e = i.next();
                this.put(e.getIntKey(), e.getIntValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Integer, ? extends Integer>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Integer, ? extends Integer> e2 = (Map.Entry<? extends Integer, ? extends Integer>)j.next();
                this.put((Integer)e2.getKey(), (Integer)e2.getValue());
            }
        }
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Int2IntMaps.fastIterator(this);
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
        return m.size() == this.size() && this.int2IntEntrySet().containsAll(m.entrySet());
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Int2IntMaps.fastIterator(this);
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
            final Entry e = i.next();
            s.append(String.valueOf(e.getIntKey()));
            s.append("=>");
            s.append(String.valueOf(e.getIntValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry
    {
        protected int key;
        protected int value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Integer key, final Integer value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final int key, final int value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public int getIntKey() {
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
                final Entry e = (Entry)o;
                return this.key == e.getIntKey() && this.value == e.getIntValue();
            }
            final Map.Entry<?, ?> e2 = (Map.Entry<?, ?>)o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Integer && this.key == (int)key && this.value == (int)value;
        }
        
        @Override
        public int hashCode() {
            return this.key ^ this.value;
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry>
    {
        protected final Int2IntMap map;
        
        public BasicEntrySet(final Int2IntMap map) {
            this.map = map;
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final int k = e.getIntKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getIntValue();
            }
            final Map.Entry<?, ?> e2 = (Map.Entry<?, ?>)o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            final int i = (int)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Integer && this.map.containsKey(i) && this.map.get(i) == (int)value;
        }
        
        @Override
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getIntKey(), e.getIntValue());
            }
            final Map.Entry<?, ?> e2 = (Map.Entry<?, ?>)o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            final int k = (int)key;
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
        public ObjectSpliterator<Entry> spliterator() {
            return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this.map), 65);
        }
    }
}
