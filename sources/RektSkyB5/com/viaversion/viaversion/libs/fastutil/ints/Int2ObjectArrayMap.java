/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectArrayMap$EntrySet.EntrySetSpliterator
 *  com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectArrayMap$KeySet.KeySetSpliterator
 *  com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectArrayMap$ValuesCollection.ValuesSpliterator
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.ints.AbstractInt2ObjectMap;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntSet;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectArrayMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterators;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrays;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterators;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class Int2ObjectArrayMap<V>
extends AbstractInt2ObjectMap<V>
implements Serializable,
Cloneable {
    private static final long serialVersionUID = 1L;
    private transient int[] key;
    private transient Object[] value;
    private int size;
    private transient Int2ObjectMap.FastEntrySet<V> entries;
    private transient IntSet keys;
    private transient ObjectCollection<V> values;

    public Int2ObjectArrayMap(int[] key, Object[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
    }

    public Int2ObjectArrayMap() {
        this.key = IntArrays.EMPTY_ARRAY;
        this.value = ObjectArrays.EMPTY_ARRAY;
    }

    public Int2ObjectArrayMap(int capacity) {
        this.key = new int[capacity];
        this.value = new Object[capacity];
    }

    public Int2ObjectArrayMap(Int2ObjectMap<V> m2) {
        this(m2.size());
        int i2 = 0;
        for (Int2ObjectMap.Entry entry : m2.int2ObjectEntrySet()) {
            this.key[i2] = entry.getIntKey();
            this.value[i2] = entry.getValue();
            ++i2;
        }
        this.size = i2;
    }

    public Int2ObjectArrayMap(Map<? extends Integer, ? extends V> m2) {
        this(m2.size());
        int i2 = 0;
        for (Map.Entry<Integer, V> e2 : m2.entrySet()) {
            this.key[i2] = e2.getKey();
            this.value[i2] = e2.getValue();
            ++i2;
        }
        this.size = i2;
    }

    public Int2ObjectArrayMap(int[] key, Object[] value, int size) {
        this.key = key;
        this.value = value;
        this.size = size;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
        if (size > key.length) {
            throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the backing-arrays size (" + key.length + ")");
        }
    }

    public Int2ObjectMap.FastEntrySet<V> int2ObjectEntrySet() {
        if (this.entries == null) {
            this.entries = new EntrySet();
        }
        return this.entries;
    }

    private int findKey(int k2) {
        int[] key = this.key;
        int i2 = this.size;
        while (i2-- != 0) {
            if (key[i2] != k2) continue;
            return i2;
        }
        return -1;
    }

    @Override
    public V get(int k2) {
        int[] key = this.key;
        int i2 = this.size;
        while (i2-- != 0) {
            if (key[i2] != k2) continue;
            return (V)this.value[i2];
        }
        return (V)this.defRetValue;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        int i2 = this.size;
        while (i2-- != 0) {
            this.value[i2] = null;
        }
        this.size = 0;
    }

    @Override
    public boolean containsKey(int k2) {
        return this.findKey(k2) != -1;
    }

    @Override
    public boolean containsValue(Object v2) {
        int i2 = this.size;
        while (i2-- != 0) {
            if (!Objects.equals(this.value[i2], v2)) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public V put(int k2, V v2) {
        int oldKey = this.findKey(k2);
        if (oldKey != -1) {
            Object oldValue = this.value[oldKey];
            this.value[oldKey] = v2;
            return (V)oldValue;
        }
        if (this.size == this.key.length) {
            int[] newKey = new int[this.size == 0 ? 2 : this.size * 2];
            Object[] newValue = new Object[this.size == 0 ? 2 : this.size * 2];
            int i2 = this.size;
            while (i2-- != 0) {
                newKey[i2] = this.key[i2];
                newValue[i2] = this.value[i2];
            }
            this.key = newKey;
            this.value = newValue;
        }
        this.key[this.size] = k2;
        this.value[this.size] = v2;
        ++this.size;
        return (V)this.defRetValue;
    }

    @Override
    public V remove(int k2) {
        int oldPos = this.findKey(k2);
        if (oldPos == -1) {
            return (V)this.defRetValue;
        }
        Object oldValue = this.value[oldPos];
        int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        this.value[this.size] = null;
        return (V)oldValue;
    }

    @Override
    public IntSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }

    @Override
    public ObjectCollection<V> values() {
        if (this.values == null) {
            this.values = new ValuesCollection();
        }
        return this.values;
    }

    public Int2ObjectArrayMap<V> clone() {
        Int2ObjectArrayMap c2;
        try {
            c2 = (Int2ObjectArrayMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c2.key = (int[])this.key.clone();
        c2.value = (Object[])this.value.clone();
        c2.entries = null;
        c2.keys = null;
        c2.values = null;
        return c2;
    }

    private void writeObject(ObjectOutputStream s2) throws IOException {
        s2.defaultWriteObject();
        int max = this.size;
        for (int i2 = 0; i2 < max; ++i2) {
            s2.writeInt(this.key[i2]);
            s2.writeObject(this.value[i2]);
        }
    }

    private void readObject(ObjectInputStream s2) throws IOException, ClassNotFoundException {
        s2.defaultReadObject();
        this.key = new int[this.size];
        this.value = new Object[this.size];
        for (int i2 = 0; i2 < this.size; ++i2) {
            this.key[i2] = s2.readInt();
            this.value[i2] = s2.readObject();
        }
    }

    private final class EntrySet
    extends AbstractObjectSet<Int2ObjectMap.Entry<V>>
    implements Int2ObjectMap.FastEntrySet<V> {
        private EntrySet() {
        }

        @Override
        public ObjectIterator<Int2ObjectMap.Entry<V>> iterator() {
            return new ObjectIterator<Int2ObjectMap.Entry<V>>(){
                int curr = -1;
                int next = 0;

                @Override
                public boolean hasNext() {
                    return this.next < Int2ObjectArrayMap.this.size;
                }

                @Override
                public Int2ObjectMap.Entry<V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.curr = this.next;
                    return new AbstractInt2ObjectMap.BasicEntry<Object>(Int2ObjectArrayMap.this.key[this.curr], Int2ObjectArrayMap.this.value[this.next++]);
                }

                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    int tail = Int2ObjectArrayMap.this.size-- - this.next--;
                    System.arraycopy(Int2ObjectArrayMap.this.key, this.next + 1, Int2ObjectArrayMap.this.key, this.next, tail);
                    System.arraycopy(Int2ObjectArrayMap.this.value, this.next + 1, Int2ObjectArrayMap.this.value, this.next, tail);
                    ((Int2ObjectArrayMap)Int2ObjectArrayMap.this).value[((Int2ObjectArrayMap)Int2ObjectArrayMap.this).size] = null;
                }

                @Override
                public void forEachRemaining(Consumer<? super Int2ObjectMap.Entry<V>> action) {
                    int max = Int2ObjectArrayMap.this.size;
                    while (this.next < max) {
                        this.curr = this.next;
                        action.accept(new AbstractInt2ObjectMap.BasicEntry<Object>(Int2ObjectArrayMap.this.key[this.curr], Int2ObjectArrayMap.this.value[this.next++]));
                    }
                }
            };
        }

        @Override
        public ObjectIterator<Int2ObjectMap.Entry<V>> fastIterator() {
            return new ObjectIterator<Int2ObjectMap.Entry<V>>(){
                int next = 0;
                int curr = -1;
                final AbstractInt2ObjectMap.BasicEntry<V> entry = new AbstractInt2ObjectMap.BasicEntry();

                @Override
                public boolean hasNext() {
                    return this.next < Int2ObjectArrayMap.this.size;
                }

                @Override
                public Int2ObjectMap.Entry<V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.curr = this.next;
                    this.entry.key = Int2ObjectArrayMap.this.key[this.curr];
                    this.entry.value = Int2ObjectArrayMap.this.value[this.next++];
                    return this.entry;
                }

                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    int tail = Int2ObjectArrayMap.this.size-- - this.next--;
                    System.arraycopy(Int2ObjectArrayMap.this.key, this.next + 1, Int2ObjectArrayMap.this.key, this.next, tail);
                    System.arraycopy(Int2ObjectArrayMap.this.value, this.next + 1, Int2ObjectArrayMap.this.value, this.next, tail);
                    ((Int2ObjectArrayMap)Int2ObjectArrayMap.this).value[((Int2ObjectArrayMap)Int2ObjectArrayMap.this).size] = null;
                }

                @Override
                public void forEachRemaining(Consumer<? super Int2ObjectMap.Entry<V>> action) {
                    int max = Int2ObjectArrayMap.this.size;
                    while (this.next < max) {
                        this.curr = this.next;
                        this.entry.key = Int2ObjectArrayMap.this.key[this.curr];
                        this.entry.value = Int2ObjectArrayMap.this.value[this.next++];
                        action.accept(this.entry);
                    }
                }
            };
        }

        @Override
        public ObjectSpliterator<Int2ObjectMap.Entry<V>> spliterator() {
            return new EntrySetSpliterator(0, Int2ObjectArrayMap.this.size);
        }

        @Override
        public void forEach(Consumer<? super Int2ObjectMap.Entry<V>> action) {
            int max = Int2ObjectArrayMap.this.size;
            for (int i2 = 0; i2 < max; ++i2) {
                action.accept(new AbstractInt2ObjectMap.BasicEntry<Object>(Int2ObjectArrayMap.this.key[i2], Int2ObjectArrayMap.this.value[i2]));
            }
        }

        @Override
        public void fastForEach(Consumer<? super Int2ObjectMap.Entry<V>> action) {
            AbstractInt2ObjectMap.BasicEntry entry = new AbstractInt2ObjectMap.BasicEntry();
            int max = Int2ObjectArrayMap.this.size;
            for (int i2 = 0; i2 < max; ++i2) {
                entry.key = Int2ObjectArrayMap.this.key[i2];
                entry.value = Int2ObjectArrayMap.this.value[i2];
                action.accept(entry);
            }
        }

        @Override
        public int size() {
            return Int2ObjectArrayMap.this.size;
        }

        @Override
        public boolean contains(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e2 = (Map.Entry)o2;
            if (e2.getKey() == null || !(e2.getKey() instanceof Integer)) {
                return false;
            }
            int k2 = (Integer)e2.getKey();
            return Int2ObjectArrayMap.this.containsKey(k2) && Objects.equals(Int2ObjectArrayMap.this.get(k2), e2.getValue());
        }

        @Override
        public boolean remove(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e2 = (Map.Entry)o2;
            if (e2.getKey() == null || !(e2.getKey() instanceof Integer)) {
                return false;
            }
            int k2 = (Integer)e2.getKey();
            Object v2 = e2.getValue();
            int oldPos = Int2ObjectArrayMap.this.findKey(k2);
            if (oldPos == -1 || !Objects.equals(v2, Int2ObjectArrayMap.this.value[oldPos])) {
                return false;
            }
            int tail = Int2ObjectArrayMap.this.size - oldPos - 1;
            System.arraycopy(Int2ObjectArrayMap.this.key, oldPos + 1, Int2ObjectArrayMap.this.key, oldPos, tail);
            System.arraycopy(Int2ObjectArrayMap.this.value, oldPos + 1, Int2ObjectArrayMap.this.value, oldPos, tail);
            Int2ObjectArrayMap.this.size--;
            ((Int2ObjectArrayMap)Int2ObjectArrayMap.this).value[((Int2ObjectArrayMap)Int2ObjectArrayMap.this).size] = null;
            return true;
        }

        final class EntrySetSpliterator
        extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<Int2ObjectMap.Entry<V>>
        implements ObjectSpliterator<Int2ObjectMap.Entry<V>> {
            EntrySetSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override
            public int characteristics() {
                return 16465;
            }

            @Override
            protected final Int2ObjectMap.Entry<V> get(int location) {
                return new AbstractInt2ObjectMap.BasicEntry<Object>(Int2ObjectArrayMap.this.key[location], Int2ObjectArrayMap.this.value[location]);
            }

            protected final com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectArrayMap$EntrySet.EntrySetSpliterator makeForSplit(int pos, int maxPos) {
                return new EntrySetSpliterator(pos, maxPos);
            }
        }
    }

    private final class KeySet
    extends AbstractIntSet {
        private KeySet() {
        }

        @Override
        public boolean contains(int k2) {
            return Int2ObjectArrayMap.this.findKey(k2) != -1;
        }

        @Override
        public boolean remove(int k2) {
            int oldPos = Int2ObjectArrayMap.this.findKey(k2);
            if (oldPos == -1) {
                return false;
            }
            int tail = Int2ObjectArrayMap.this.size - oldPos - 1;
            System.arraycopy(Int2ObjectArrayMap.this.key, oldPos + 1, Int2ObjectArrayMap.this.key, oldPos, tail);
            System.arraycopy(Int2ObjectArrayMap.this.value, oldPos + 1, Int2ObjectArrayMap.this.value, oldPos, tail);
            Int2ObjectArrayMap.this.size--;
            ((Int2ObjectArrayMap)Int2ObjectArrayMap.this).value[((Int2ObjectArrayMap)Int2ObjectArrayMap.this).size] = null;
            return true;
        }

        @Override
        public IntIterator iterator() {
            return new IntIterator(){
                int pos = 0;

                @Override
                public boolean hasNext() {
                    return this.pos < Int2ObjectArrayMap.this.size;
                }

                @Override
                public int nextInt() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return Int2ObjectArrayMap.this.key[this.pos++];
                }

                @Override
                public void remove() {
                    if (this.pos == 0) {
                        throw new IllegalStateException();
                    }
                    int tail = Int2ObjectArrayMap.this.size - this.pos;
                    System.arraycopy(Int2ObjectArrayMap.this.key, this.pos, Int2ObjectArrayMap.this.key, this.pos - 1, tail);
                    System.arraycopy(Int2ObjectArrayMap.this.value, this.pos, Int2ObjectArrayMap.this.value, this.pos - 1, tail);
                    Int2ObjectArrayMap.this.size--;
                    --this.pos;
                    ((Int2ObjectArrayMap)Int2ObjectArrayMap.this).value[((Int2ObjectArrayMap)Int2ObjectArrayMap.this).size] = null;
                }

                @Override
                public void forEachRemaining(IntConsumer action) {
                    int max = Int2ObjectArrayMap.this.size;
                    while (this.pos < max) {
                        action.accept(Int2ObjectArrayMap.this.key[this.pos++]);
                    }
                }
            };
        }

        @Override
        public IntSpliterator spliterator() {
            return new KeySetSpliterator(0, Int2ObjectArrayMap.this.size);
        }

        @Override
        public void forEach(IntConsumer action) {
            int max = Int2ObjectArrayMap.this.size;
            for (int i2 = 0; i2 < max; ++i2) {
                action.accept(Int2ObjectArrayMap.this.key[i2]);
            }
        }

        @Override
        public int size() {
            return Int2ObjectArrayMap.this.size;
        }

        @Override
        public void clear() {
            Int2ObjectArrayMap.this.clear();
        }

        final class KeySetSpliterator
        extends IntSpliterators.EarlyBindingSizeIndexBasedSpliterator
        implements IntSpliterator {
            KeySetSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override
            public int characteristics() {
                return 16721;
            }

            @Override
            protected final int get(int location) {
                return Int2ObjectArrayMap.this.key[location];
            }

            protected final com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectArrayMap$KeySet.KeySetSpliterator makeForSplit(int pos, int maxPos) {
                return new KeySetSpliterator(pos, maxPos);
            }

            @Override
            public void forEachRemaining(IntConsumer action) {
                int max = Int2ObjectArrayMap.this.size;
                while (this.pos < max) {
                    action.accept(Int2ObjectArrayMap.this.key[this.pos++]);
                }
            }
        }
    }

    private final class ValuesCollection
    extends AbstractObjectCollection<V> {
        private ValuesCollection() {
        }

        @Override
        public boolean contains(Object v2) {
            return Int2ObjectArrayMap.this.containsValue(v2);
        }

        @Override
        public ObjectIterator<V> iterator() {
            return new ObjectIterator<V>(){
                int pos = 0;

                @Override
                public boolean hasNext() {
                    return this.pos < Int2ObjectArrayMap.this.size;
                }

                @Override
                public V next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return Int2ObjectArrayMap.this.value[this.pos++];
                }

                @Override
                public void remove() {
                    if (this.pos == 0) {
                        throw new IllegalStateException();
                    }
                    int tail = Int2ObjectArrayMap.this.size - this.pos;
                    System.arraycopy(Int2ObjectArrayMap.this.key, this.pos, Int2ObjectArrayMap.this.key, this.pos - 1, tail);
                    System.arraycopy(Int2ObjectArrayMap.this.value, this.pos, Int2ObjectArrayMap.this.value, this.pos - 1, tail);
                    Int2ObjectArrayMap.this.size--;
                    --this.pos;
                    ((Int2ObjectArrayMap)Int2ObjectArrayMap.this).value[((Int2ObjectArrayMap)Int2ObjectArrayMap.this).size] = null;
                }

                @Override
                public void forEachRemaining(Consumer<? super V> action) {
                    int max = Int2ObjectArrayMap.this.size;
                    while (this.pos < max) {
                        action.accept(Int2ObjectArrayMap.this.value[this.pos++]);
                    }
                }
            };
        }

        @Override
        public ObjectSpliterator<V> spliterator() {
            return new ValuesSpliterator(0, Int2ObjectArrayMap.this.size);
        }

        @Override
        public void forEach(Consumer<? super V> action) {
            int max = Int2ObjectArrayMap.this.size;
            for (int i2 = 0; i2 < max; ++i2) {
                action.accept(Int2ObjectArrayMap.this.value[i2]);
            }
        }

        @Override
        public int size() {
            return Int2ObjectArrayMap.this.size;
        }

        @Override
        public void clear() {
            Int2ObjectArrayMap.this.clear();
        }

        final class ValuesSpliterator
        extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<V>
        implements ObjectSpliterator<V> {
            ValuesSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override
            public int characteristics() {
                return 16464;
            }

            @Override
            protected final V get(int location) {
                return Int2ObjectArrayMap.this.value[location];
            }

            protected final com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectArrayMap$ValuesCollection.ValuesSpliterator makeForSplit(int pos, int maxPos) {
                return new ValuesSpliterator(pos, maxPos);
            }

            @Override
            public void forEachRemaining(Consumer<? super V> action) {
                int max = Int2ObjectArrayMap.this.size;
                while (this.pos < max) {
                    action.accept(Int2ObjectArrayMap.this.value[this.pos++]);
                }
            }
        }
    }
}

