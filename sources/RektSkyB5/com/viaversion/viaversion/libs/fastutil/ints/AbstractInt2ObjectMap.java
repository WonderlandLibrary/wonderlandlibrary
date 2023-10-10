/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.Size64;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractInt2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntSet;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMaps;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterators;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterators;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public abstract class AbstractInt2ObjectMap<V>
extends AbstractInt2ObjectFunction<V>
implements Int2ObjectMap<V>,
Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    protected AbstractInt2ObjectMap() {
    }

    @Override
    public boolean containsKey(int k2) {
        Iterator i2 = this.int2ObjectEntrySet().iterator();
        while (i2.hasNext()) {
            if (((Int2ObjectMap.Entry)i2.next()).getIntKey() != k2) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object v2) {
        Iterator i2 = this.int2ObjectEntrySet().iterator();
        while (i2.hasNext()) {
            if (((Int2ObjectMap.Entry)i2.next()).getValue() != v2) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public IntSet keySet() {
        return new AbstractIntSet(){

            @Override
            public boolean contains(int k2) {
                return AbstractInt2ObjectMap.this.containsKey(k2);
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
                return new IntIterator(){
                    private final ObjectIterator<Int2ObjectMap.Entry<V>> i;
                    {
                        this.i = Int2ObjectMaps.fastIterator(AbstractInt2ObjectMap.this);
                    }

                    @Override
                    public int nextInt() {
                        return ((Int2ObjectMap.Entry)this.i.next()).getIntKey();
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
                    public void forEachRemaining(IntConsumer action) {
                        this.i.forEachRemaining((? super E entry) -> action.accept(entry.getIntKey()));
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
        return new AbstractObjectCollection<V>(){

            @Override
            public boolean contains(Object k2) {
                return AbstractInt2ObjectMap.this.containsValue(k2);
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
                return new ObjectIterator<V>(){
                    private final ObjectIterator<Int2ObjectMap.Entry<V>> i;
                    {
                        this.i = Int2ObjectMaps.fastIterator(AbstractInt2ObjectMap.this);
                    }

                    @Override
                    public V next() {
                        return ((Int2ObjectMap.Entry)this.i.next()).getValue();
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
                    public void forEachRemaining(Consumer<? super V> action) {
                        this.i.forEachRemaining((? super E entry) -> action.accept((Object)entry.getValue()));
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
    public void putAll(Map<? extends Integer, ? extends V> m2) {
        if (m2 instanceof Int2ObjectMap) {
            ObjectIterator i2 = Int2ObjectMaps.fastIterator((Int2ObjectMap)m2);
            while (i2.hasNext()) {
                Int2ObjectMap.Entry e2 = (Int2ObjectMap.Entry)i2.next();
                this.put(e2.getIntKey(), e2.getValue());
            }
        } else {
            int n2 = m2.size();
            Iterator<Map.Entry<Integer, V>> i3 = m2.entrySet().iterator();
            while (n2-- != 0) {
                Map.Entry<Integer, V> e3 = i3.next();
                this.put(e3.getKey(), e3.getValue());
            }
        }
    }

    @Override
    public int hashCode() {
        int h2 = 0;
        int n2 = this.size();
        ObjectIterator i2 = Int2ObjectMaps.fastIterator(this);
        while (n2-- != 0) {
            h2 += ((Int2ObjectMap.Entry)i2.next()).hashCode();
        }
        return h2;
    }

    @Override
    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (!(o2 instanceof Map)) {
            return false;
        }
        Map m2 = (Map)o2;
        if (m2.size() != this.size()) {
            return false;
        }
        return this.int2ObjectEntrySet().containsAll(m2.entrySet());
    }

    public String toString() {
        StringBuilder s2 = new StringBuilder();
        ObjectIterator i2 = Int2ObjectMaps.fastIterator(this);
        int n2 = this.size();
        boolean first = true;
        s2.append("{");
        while (n2-- != 0) {
            if (first) {
                first = false;
            } else {
                s2.append(", ");
            }
            Int2ObjectMap.Entry e2 = (Int2ObjectMap.Entry)i2.next();
            s2.append(String.valueOf(e2.getIntKey()));
            s2.append("=>");
            if (this == e2.getValue()) {
                s2.append("(this map)");
                continue;
            }
            s2.append(String.valueOf(e2.getValue()));
        }
        s2.append("}");
        return s2.toString();
    }

    public static abstract class BasicEntrySet<V>
    extends AbstractObjectSet<Int2ObjectMap.Entry<V>> {
        protected final Int2ObjectMap<V> map;

        public BasicEntrySet(Int2ObjectMap<V> map) {
            this.map = map;
        }

        @Override
        public boolean contains(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            if (o2 instanceof Int2ObjectMap.Entry) {
                Int2ObjectMap.Entry e2 = (Int2ObjectMap.Entry)o2;
                int k2 = e2.getIntKey();
                return this.map.containsKey(k2) && Objects.equals(this.map.get(k2), e2.getValue());
            }
            Map.Entry e3 = (Map.Entry)o2;
            Object key = e3.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            int k3 = (Integer)key;
            Object value = e3.getValue();
            return this.map.containsKey(k3) && Objects.equals(this.map.get(k3), value);
        }

        @Override
        public boolean remove(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            if (o2 instanceof Int2ObjectMap.Entry) {
                Int2ObjectMap.Entry e2 = (Int2ObjectMap.Entry)o2;
                return this.map.remove(e2.getIntKey(), e2.getValue());
            }
            Map.Entry e3 = (Map.Entry)o2;
            Object key = e3.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            int k2 = (Integer)key;
            Object v2 = e3.getValue();
            return this.map.remove(k2, v2);
        }

        @Override
        public int size() {
            return this.map.size();
        }

        @Override
        public ObjectSpliterator<Int2ObjectMap.Entry<V>> spliterator() {
            return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this.map), 65);
        }
    }

    public static class BasicEntry<V>
    implements Int2ObjectMap.Entry<V> {
        protected int key;
        protected V value;

        public BasicEntry() {
        }

        public BasicEntry(Integer key, V value) {
            this.key = key;
            this.value = value;
        }

        public BasicEntry(int key, V value) {
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
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            if (o2 instanceof Int2ObjectMap.Entry) {
                Int2ObjectMap.Entry e2 = (Int2ObjectMap.Entry)o2;
                return this.key == e2.getIntKey() && Objects.equals(this.value, e2.getValue());
            }
            Map.Entry e3 = (Map.Entry)o2;
            Object key = e3.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            Object value = e3.getValue();
            return this.key == (Integer)key && Objects.equals(this.value, value);
        }

        @Override
        public int hashCode() {
            return this.key ^ (this.value == null ? 0 : this.value.hashCode());
        }

        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}

