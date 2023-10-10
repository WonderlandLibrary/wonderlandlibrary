/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.Size64;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractInt2IntFunction;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntSet;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMaps;
import com.viaversion.viaversion.libs.fastutil.ints.IntBinaryOperator;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterators;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterators;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.function.IntConsumer;

public abstract class AbstractInt2IntMap
extends AbstractInt2IntFunction
implements Int2IntMap,
Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    protected AbstractInt2IntMap() {
    }

    @Override
    public boolean containsKey(int k2) {
        Iterator i2 = this.int2IntEntrySet().iterator();
        while (i2.hasNext()) {
            if (((Int2IntMap.Entry)i2.next()).getIntKey() != k2) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(int v2) {
        Iterator i2 = this.int2IntEntrySet().iterator();
        while (i2.hasNext()) {
            if (((Int2IntMap.Entry)i2.next()).getIntValue() != v2) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public final int mergeInt(int key, int value, IntBinaryOperator remappingFunction) {
        return this.mergeInt(key, value, (java.util.function.IntBinaryOperator)remappingFunction);
    }

    @Override
    public IntSet keySet() {
        return new AbstractIntSet(){

            @Override
            public boolean contains(int k2) {
                return AbstractInt2IntMap.this.containsKey(k2);
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
                return new IntIterator(){
                    private final ObjectIterator<Int2IntMap.Entry> i;
                    {
                        this.i = Int2IntMaps.fastIterator(AbstractInt2IntMap.this);
                    }

                    @Override
                    public int nextInt() {
                        return ((Int2IntMap.Entry)this.i.next()).getIntKey();
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
                return IntSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(AbstractInt2IntMap.this), 321);
            }
        };
    }

    @Override
    public IntCollection values() {
        return new AbstractIntCollection(){

            @Override
            public boolean contains(int k2) {
                return AbstractInt2IntMap.this.containsValue(k2);
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
                return new IntIterator(){
                    private final ObjectIterator<Int2IntMap.Entry> i;
                    {
                        this.i = Int2IntMaps.fastIterator(AbstractInt2IntMap.this);
                    }

                    @Override
                    public int nextInt() {
                        return ((Int2IntMap.Entry)this.i.next()).getIntValue();
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
                return IntSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(AbstractInt2IntMap.this), 320);
            }
        };
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Integer> m2) {
        if (m2 instanceof Int2IntMap) {
            ObjectIterator<Int2IntMap.Entry> i2 = Int2IntMaps.fastIterator((Int2IntMap)m2);
            while (i2.hasNext()) {
                Int2IntMap.Entry e2 = (Int2IntMap.Entry)i2.next();
                this.put(e2.getIntKey(), e2.getIntValue());
            }
        } else {
            int n2 = m2.size();
            Iterator<Map.Entry<? extends Integer, ? extends Integer>> i3 = m2.entrySet().iterator();
            while (n2-- != 0) {
                Map.Entry<? extends Integer, ? extends Integer> e3 = i3.next();
                this.put(e3.getKey(), e3.getValue());
            }
        }
    }

    @Override
    public int hashCode() {
        int h2 = 0;
        int n2 = this.size();
        ObjectIterator<Int2IntMap.Entry> i2 = Int2IntMaps.fastIterator(this);
        while (n2-- != 0) {
            h2 += ((Int2IntMap.Entry)i2.next()).hashCode();
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
        return this.int2IntEntrySet().containsAll(m2.entrySet());
    }

    public String toString() {
        StringBuilder s2 = new StringBuilder();
        ObjectIterator<Int2IntMap.Entry> i2 = Int2IntMaps.fastIterator(this);
        int n2 = this.size();
        boolean first = true;
        s2.append("{");
        while (n2-- != 0) {
            if (first) {
                first = false;
            } else {
                s2.append(", ");
            }
            Int2IntMap.Entry e2 = (Int2IntMap.Entry)i2.next();
            s2.append(String.valueOf(e2.getIntKey()));
            s2.append("=>");
            s2.append(String.valueOf(e2.getIntValue()));
        }
        s2.append("}");
        return s2.toString();
    }

    public static abstract class BasicEntrySet
    extends AbstractObjectSet<Int2IntMap.Entry> {
        protected final Int2IntMap map;

        public BasicEntrySet(Int2IntMap map) {
            this.map = map;
        }

        @Override
        public boolean contains(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            if (o2 instanceof Int2IntMap.Entry) {
                Int2IntMap.Entry e2 = (Int2IntMap.Entry)o2;
                int k2 = e2.getIntKey();
                return this.map.containsKey(k2) && this.map.get(k2) == e2.getIntValue();
            }
            Map.Entry e3 = (Map.Entry)o2;
            Object key = e3.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            int k3 = (Integer)key;
            Object value = e3.getValue();
            if (value == null || !(value instanceof Integer)) {
                return false;
            }
            return this.map.containsKey(k3) && this.map.get(k3) == ((Integer)value).intValue();
        }

        @Override
        public boolean remove(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            if (o2 instanceof Int2IntMap.Entry) {
                Int2IntMap.Entry e2 = (Int2IntMap.Entry)o2;
                return this.map.remove(e2.getIntKey(), e2.getIntValue());
            }
            Map.Entry e3 = (Map.Entry)o2;
            Object key = e3.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            int k2 = (Integer)key;
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
        public ObjectSpliterator<Int2IntMap.Entry> spliterator() {
            return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this.map), 65);
        }
    }

    public static class BasicEntry
    implements Int2IntMap.Entry {
        protected int key;
        protected int value;

        public BasicEntry() {
        }

        public BasicEntry(Integer key, Integer value) {
            this.key = key;
            this.value = value;
        }

        public BasicEntry(int key, int value) {
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
        public int setValue(int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            if (o2 instanceof Int2IntMap.Entry) {
                Int2IntMap.Entry e2 = (Int2IntMap.Entry)o2;
                return this.key == e2.getIntKey() && this.value == e2.getIntValue();
            }
            Map.Entry e3 = (Map.Entry)o2;
            Object key = e3.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            Object value = e3.getValue();
            if (value == null || !(value instanceof Integer)) {
                return false;
            }
            return this.key == (Integer)key && this.value == (Integer)value;
        }

        @Override
        public int hashCode() {
            return this.key ^ this.value;
        }

        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}

