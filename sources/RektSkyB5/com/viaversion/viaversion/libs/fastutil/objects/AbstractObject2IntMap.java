/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.Size64;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterators;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObject2IntFunction;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectSet;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMaps;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterators;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;

public abstract class AbstractObject2IntMap<K>
extends AbstractObject2IntFunction<K>
implements Object2IntMap<K>,
Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    protected AbstractObject2IntMap() {
    }

    @Override
    public boolean containsKey(Object k2) {
        Iterator i2 = this.object2IntEntrySet().iterator();
        while (i2.hasNext()) {
            if (((Object2IntMap.Entry)i2.next()).getKey() != k2) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(int v2) {
        Iterator i2 = this.object2IntEntrySet().iterator();
        while (i2.hasNext()) {
            if (((Object2IntMap.Entry)i2.next()).getIntValue() != v2) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public final int mergeInt(K key, int value, com.viaversion.viaversion.libs.fastutil.ints.IntBinaryOperator remappingFunction) {
        return this.mergeInt(key, value, (IntBinaryOperator)remappingFunction);
    }

    @Override
    public ObjectSet<K> keySet() {
        return new AbstractObjectSet<K>(){

            @Override
            public boolean contains(Object k2) {
                return AbstractObject2IntMap.this.containsKey(k2);
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
                return new ObjectIterator<K>(){
                    private final ObjectIterator<Object2IntMap.Entry<K>> i;
                    {
                        this.i = Object2IntMaps.fastIterator(AbstractObject2IntMap.this);
                    }

                    @Override
                    public K next() {
                        return ((Object2IntMap.Entry)this.i.next()).getKey();
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
                    public void forEachRemaining(Consumer<? super K> action) {
                        this.i.forEachRemaining((? super E entry) -> action.accept((Object)entry.getKey()));
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
        return new AbstractIntCollection(){

            @Override
            public boolean contains(int k2) {
                return AbstractObject2IntMap.this.containsValue(k2);
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
                return new IntIterator(){
                    private final ObjectIterator<Object2IntMap.Entry<K>> i;
                    {
                        this.i = Object2IntMaps.fastIterator(AbstractObject2IntMap.this);
                    }

                    @Override
                    public int nextInt() {
                        return ((Object2IntMap.Entry)this.i.next()).getIntValue();
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
                        this.i.forEachRemaining((? super E entry) -> action.accept(entry.getIntValue()));
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
    public void putAll(Map<? extends K, ? extends Integer> m2) {
        if (m2 instanceof Object2IntMap) {
            ObjectIterator i2 = Object2IntMaps.fastIterator((Object2IntMap)m2);
            while (i2.hasNext()) {
                Object2IntMap.Entry e2 = (Object2IntMap.Entry)i2.next();
                this.put(e2.getKey(), e2.getIntValue());
            }
        } else {
            int n2 = m2.size();
            Iterator<Map.Entry<K, Integer>> i3 = m2.entrySet().iterator();
            while (n2-- != 0) {
                Map.Entry<K, Integer> e3 = i3.next();
                this.put(e3.getKey(), e3.getValue());
            }
        }
    }

    @Override
    public int hashCode() {
        int h2 = 0;
        int n2 = this.size();
        ObjectIterator i2 = Object2IntMaps.fastIterator(this);
        while (n2-- != 0) {
            h2 += ((Object2IntMap.Entry)i2.next()).hashCode();
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
        return this.object2IntEntrySet().containsAll(m2.entrySet());
    }

    public String toString() {
        StringBuilder s2 = new StringBuilder();
        ObjectIterator i2 = Object2IntMaps.fastIterator(this);
        int n2 = this.size();
        boolean first = true;
        s2.append("{");
        while (n2-- != 0) {
            if (first) {
                first = false;
            } else {
                s2.append(", ");
            }
            Object2IntMap.Entry e2 = (Object2IntMap.Entry)i2.next();
            if (this == e2.getKey()) {
                s2.append("(this map)");
            } else {
                s2.append(String.valueOf(e2.getKey()));
            }
            s2.append("=>");
            s2.append(String.valueOf(e2.getIntValue()));
        }
        s2.append("}");
        return s2.toString();
    }

    public static abstract class BasicEntrySet<K>
    extends AbstractObjectSet<Object2IntMap.Entry<K>> {
        protected final Object2IntMap<K> map;

        public BasicEntrySet(Object2IntMap<K> map) {
            this.map = map;
        }

        @Override
        public boolean contains(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            if (o2 instanceof Object2IntMap.Entry) {
                Object2IntMap.Entry e2 = (Object2IntMap.Entry)o2;
                Object k2 = e2.getKey();
                return this.map.containsKey(k2) && this.map.getInt(k2) == e2.getIntValue();
            }
            Map.Entry e3 = (Map.Entry)o2;
            Object k3 = e3.getKey();
            Object value = e3.getValue();
            if (value == null || !(value instanceof Integer)) {
                return false;
            }
            return this.map.containsKey(k3) && this.map.getInt(k3) == ((Integer)value).intValue();
        }

        @Override
        public boolean remove(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            if (o2 instanceof Object2IntMap.Entry) {
                Object2IntMap.Entry e2 = (Object2IntMap.Entry)o2;
                return this.map.remove(e2.getKey(), e2.getIntValue());
            }
            Map.Entry e3 = (Map.Entry)o2;
            Object k2 = e3.getKey();
            Object value = e3.getValue();
            if (value == null || !(value instanceof Integer)) {
                return false;
            }
            int v2 = (Integer)value;
            return this.map.remove(k2, v2);
        }

        @Override
        public int size() {
            return this.map.size();
        }

        @Override
        public ObjectSpliterator<Object2IntMap.Entry<K>> spliterator() {
            return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this.map), 65);
        }
    }

    public static class BasicEntry<K>
    implements Object2IntMap.Entry<K> {
        protected K key;
        protected int value;

        public BasicEntry() {
        }

        public BasicEntry(K key, Integer value) {
            this.key = key;
            this.value = value;
        }

        public BasicEntry(K key, int value) {
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
        public int setValue(int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            if (o2 instanceof Object2IntMap.Entry) {
                Object2IntMap.Entry e2 = (Object2IntMap.Entry)o2;
                return Objects.equals(this.key, e2.getKey()) && this.value == e2.getIntValue();
            }
            Map.Entry e3 = (Map.Entry)o2;
            Object key = e3.getKey();
            Object value = e3.getValue();
            if (value == null || !(value instanceof Integer)) {
                return false;
            }
            return Objects.equals(this.key, key) && this.value == (Integer)value;
        }

        @Override
        public int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode()) ^ this.value;
        }

        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}

