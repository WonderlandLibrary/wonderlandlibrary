/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.libs.fastutil.objects.Object2IntArrayMap$EntrySet.EntrySetSpliterator
 *  com.viaversion.viaversion.libs.fastutil.objects.Object2IntArrayMap$KeySet.KeySetSpliterator
 *  com.viaversion.viaversion.libs.fastutil.objects.Object2IntArrayMap$ValuesCollection.ValuesSpliterator
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterators;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObject2IntMap;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectSet;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntArrayMap;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrays;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSet;
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

public class Object2IntArrayMap<K>
extends AbstractObject2IntMap<K>
implements Serializable,
Cloneable {
    private static final long serialVersionUID = 1L;
    private transient Object[] key;
    private transient int[] value;
    private int size;
    private transient Object2IntMap.FastEntrySet<K> entries;
    private transient ObjectSet<K> keys;
    private transient IntCollection values;

    public Object2IntArrayMap(Object[] key, int[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
    }

    public Object2IntArrayMap() {
        this.key = ObjectArrays.EMPTY_ARRAY;
        this.value = IntArrays.EMPTY_ARRAY;
    }

    public Object2IntArrayMap(int capacity) {
        this.key = new Object[capacity];
        this.value = new int[capacity];
    }

    public Object2IntArrayMap(Object2IntMap<K> m2) {
        this(m2.size());
        int i2 = 0;
        for (Object2IntMap.Entry entry : m2.object2IntEntrySet()) {
            this.key[i2] = entry.getKey();
            this.value[i2] = entry.getIntValue();
            ++i2;
        }
        this.size = i2;
    }

    public Object2IntArrayMap(Map<? extends K, ? extends Integer> m2) {
        this(m2.size());
        int i2 = 0;
        for (Map.Entry<K, Integer> e2 : m2.entrySet()) {
            this.key[i2] = e2.getKey();
            this.value[i2] = e2.getValue();
            ++i2;
        }
        this.size = i2;
    }

    public Object2IntArrayMap(Object[] key, int[] value, int size) {
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

    public Object2IntMap.FastEntrySet<K> object2IntEntrySet() {
        if (this.entries == null) {
            this.entries = new EntrySet();
        }
        return this.entries;
    }

    private int findKey(Object k2) {
        Object[] key = this.key;
        int i2 = this.size;
        while (i2-- != 0) {
            if (!Objects.equals(key[i2], k2)) continue;
            return i2;
        }
        return -1;
    }

    @Override
    public int getInt(Object k2) {
        Object[] key = this.key;
        int i2 = this.size;
        while (i2-- != 0) {
            if (!Objects.equals(key[i2], k2)) continue;
            return this.value[i2];
        }
        return this.defRetValue;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        int i2 = this.size;
        while (i2-- != 0) {
            this.key[i2] = null;
        }
        this.size = 0;
    }

    @Override
    public boolean containsKey(Object k2) {
        return this.findKey(k2) != -1;
    }

    @Override
    public boolean containsValue(int v2) {
        int i2 = this.size;
        while (i2-- != 0) {
            if (this.value[i2] != v2) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public int put(K k2, int v2) {
        int oldKey = this.findKey(k2);
        if (oldKey != -1) {
            int oldValue = this.value[oldKey];
            this.value[oldKey] = v2;
            return oldValue;
        }
        if (this.size == this.key.length) {
            Object[] newKey = new Object[this.size == 0 ? 2 : this.size * 2];
            int[] newValue = new int[this.size == 0 ? 2 : this.size * 2];
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
        return this.defRetValue;
    }

    @Override
    public int removeInt(Object k2) {
        int oldPos = this.findKey(k2);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        int oldValue = this.value[oldPos];
        int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        this.key[this.size] = null;
        return oldValue;
    }

    @Override
    public ObjectSet<K> keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }

    @Override
    public IntCollection values() {
        if (this.values == null) {
            this.values = new ValuesCollection();
        }
        return this.values;
    }

    public Object2IntArrayMap<K> clone() {
        Object2IntArrayMap c2;
        try {
            c2 = (Object2IntArrayMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c2.key = (Object[])this.key.clone();
        c2.value = (int[])this.value.clone();
        c2.entries = null;
        c2.keys = null;
        c2.values = null;
        return c2;
    }

    private void writeObject(ObjectOutputStream s2) throws IOException {
        s2.defaultWriteObject();
        int max = this.size;
        for (int i2 = 0; i2 < max; ++i2) {
            s2.writeObject(this.key[i2]);
            s2.writeInt(this.value[i2]);
        }
    }

    private void readObject(ObjectInputStream s2) throws IOException, ClassNotFoundException {
        s2.defaultReadObject();
        this.key = new Object[this.size];
        this.value = new int[this.size];
        for (int i2 = 0; i2 < this.size; ++i2) {
            this.key[i2] = s2.readObject();
            this.value[i2] = s2.readInt();
        }
    }

    private final class EntrySet
    extends AbstractObjectSet<Object2IntMap.Entry<K>>
    implements Object2IntMap.FastEntrySet<K> {
        private EntrySet() {
        }

        @Override
        public ObjectIterator<Object2IntMap.Entry<K>> iterator() {
            return new ObjectIterator<Object2IntMap.Entry<K>>(){
                int curr = -1;
                int next = 0;

                @Override
                public boolean hasNext() {
                    return this.next < Object2IntArrayMap.this.size;
                }

                @Override
                public Object2IntMap.Entry<K> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.curr = this.next;
                    return new AbstractObject2IntMap.BasicEntry<Object>(Object2IntArrayMap.this.key[this.curr], Object2IntArrayMap.this.value[this.next++]);
                }

                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    int tail = Object2IntArrayMap.this.size-- - this.next--;
                    System.arraycopy(Object2IntArrayMap.this.key, this.next + 1, Object2IntArrayMap.this.key, this.next, tail);
                    System.arraycopy(Object2IntArrayMap.this.value, this.next + 1, Object2IntArrayMap.this.value, this.next, tail);
                    ((Object2IntArrayMap)Object2IntArrayMap.this).key[((Object2IntArrayMap)Object2IntArrayMap.this).size] = null;
                }

                @Override
                public void forEachRemaining(Consumer<? super Object2IntMap.Entry<K>> action) {
                    int max = Object2IntArrayMap.this.size;
                    while (this.next < max) {
                        this.curr = this.next;
                        action.accept(new AbstractObject2IntMap.BasicEntry<Object>(Object2IntArrayMap.this.key[this.curr], Object2IntArrayMap.this.value[this.next++]));
                    }
                }
            };
        }

        @Override
        public ObjectIterator<Object2IntMap.Entry<K>> fastIterator() {
            return new ObjectIterator<Object2IntMap.Entry<K>>(){
                int next = 0;
                int curr = -1;
                final AbstractObject2IntMap.BasicEntry<K> entry = new AbstractObject2IntMap.BasicEntry();

                @Override
                public boolean hasNext() {
                    return this.next < Object2IntArrayMap.this.size;
                }

                @Override
                public Object2IntMap.Entry<K> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.curr = this.next;
                    this.entry.key = Object2IntArrayMap.this.key[this.curr];
                    this.entry.value = Object2IntArrayMap.this.value[this.next++];
                    return this.entry;
                }

                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    int tail = Object2IntArrayMap.this.size-- - this.next--;
                    System.arraycopy(Object2IntArrayMap.this.key, this.next + 1, Object2IntArrayMap.this.key, this.next, tail);
                    System.arraycopy(Object2IntArrayMap.this.value, this.next + 1, Object2IntArrayMap.this.value, this.next, tail);
                    ((Object2IntArrayMap)Object2IntArrayMap.this).key[((Object2IntArrayMap)Object2IntArrayMap.this).size] = null;
                }

                @Override
                public void forEachRemaining(Consumer<? super Object2IntMap.Entry<K>> action) {
                    int max = Object2IntArrayMap.this.size;
                    while (this.next < max) {
                        this.curr = this.next;
                        this.entry.key = Object2IntArrayMap.this.key[this.curr];
                        this.entry.value = Object2IntArrayMap.this.value[this.next++];
                        action.accept(this.entry);
                    }
                }
            };
        }

        @Override
        public ObjectSpliterator<Object2IntMap.Entry<K>> spliterator() {
            return new EntrySetSpliterator(0, Object2IntArrayMap.this.size);
        }

        @Override
        public void forEach(Consumer<? super Object2IntMap.Entry<K>> action) {
            int max = Object2IntArrayMap.this.size;
            for (int i2 = 0; i2 < max; ++i2) {
                action.accept(new AbstractObject2IntMap.BasicEntry<Object>(Object2IntArrayMap.this.key[i2], Object2IntArrayMap.this.value[i2]));
            }
        }

        @Override
        public void fastForEach(Consumer<? super Object2IntMap.Entry<K>> action) {
            AbstractObject2IntMap.BasicEntry entry = new AbstractObject2IntMap.BasicEntry();
            int max = Object2IntArrayMap.this.size;
            for (int i2 = 0; i2 < max; ++i2) {
                entry.key = Object2IntArrayMap.this.key[i2];
                entry.value = Object2IntArrayMap.this.value[i2];
                action.accept(entry);
            }
        }

        @Override
        public int size() {
            return Object2IntArrayMap.this.size;
        }

        @Override
        public boolean contains(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e2 = (Map.Entry)o2;
            if (e2.getValue() == null || !(e2.getValue() instanceof Integer)) {
                return false;
            }
            Object k2 = e2.getKey();
            return Object2IntArrayMap.this.containsKey(k2) && Object2IntArrayMap.this.getInt(k2) == ((Integer)e2.getValue()).intValue();
        }

        @Override
        public boolean remove(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e2 = (Map.Entry)o2;
            if (e2.getValue() == null || !(e2.getValue() instanceof Integer)) {
                return false;
            }
            Object k2 = e2.getKey();
            int v2 = (Integer)e2.getValue();
            int oldPos = Object2IntArrayMap.this.findKey(k2);
            if (oldPos == -1 || v2 != Object2IntArrayMap.this.value[oldPos]) {
                return false;
            }
            int tail = Object2IntArrayMap.this.size - oldPos - 1;
            System.arraycopy(Object2IntArrayMap.this.key, oldPos + 1, Object2IntArrayMap.this.key, oldPos, tail);
            System.arraycopy(Object2IntArrayMap.this.value, oldPos + 1, Object2IntArrayMap.this.value, oldPos, tail);
            Object2IntArrayMap.this.size--;
            ((Object2IntArrayMap)Object2IntArrayMap.this).key[((Object2IntArrayMap)Object2IntArrayMap.this).size] = null;
            return true;
        }

        final class EntrySetSpliterator
        extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<Object2IntMap.Entry<K>>
        implements ObjectSpliterator<Object2IntMap.Entry<K>> {
            EntrySetSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override
            public int characteristics() {
                return 16465;
            }

            @Override
            protected final Object2IntMap.Entry<K> get(int location) {
                return new AbstractObject2IntMap.BasicEntry<Object>(Object2IntArrayMap.this.key[location], Object2IntArrayMap.this.value[location]);
            }

            protected final com.viaversion.viaversion.libs.fastutil.objects.Object2IntArrayMap$EntrySet.EntrySetSpliterator makeForSplit(int pos, int maxPos) {
                return new EntrySetSpliterator(pos, maxPos);
            }
        }
    }

    private final class KeySet
    extends AbstractObjectSet<K> {
        private KeySet() {
        }

        @Override
        public boolean contains(Object k2) {
            return Object2IntArrayMap.this.findKey(k2) != -1;
        }

        @Override
        public boolean remove(Object k2) {
            int oldPos = Object2IntArrayMap.this.findKey(k2);
            if (oldPos == -1) {
                return false;
            }
            int tail = Object2IntArrayMap.this.size - oldPos - 1;
            System.arraycopy(Object2IntArrayMap.this.key, oldPos + 1, Object2IntArrayMap.this.key, oldPos, tail);
            System.arraycopy(Object2IntArrayMap.this.value, oldPos + 1, Object2IntArrayMap.this.value, oldPos, tail);
            Object2IntArrayMap.this.size--;
            ((Object2IntArrayMap)Object2IntArrayMap.this).key[((Object2IntArrayMap)Object2IntArrayMap.this).size] = null;
            return true;
        }

        @Override
        public ObjectIterator<K> iterator() {
            return new ObjectIterator<K>(){
                int pos = 0;

                @Override
                public boolean hasNext() {
                    return this.pos < Object2IntArrayMap.this.size;
                }

                @Override
                public K next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return Object2IntArrayMap.this.key[this.pos++];
                }

                @Override
                public void remove() {
                    if (this.pos == 0) {
                        throw new IllegalStateException();
                    }
                    int tail = Object2IntArrayMap.this.size - this.pos;
                    System.arraycopy(Object2IntArrayMap.this.key, this.pos, Object2IntArrayMap.this.key, this.pos - 1, tail);
                    System.arraycopy(Object2IntArrayMap.this.value, this.pos, Object2IntArrayMap.this.value, this.pos - 1, tail);
                    Object2IntArrayMap.this.size--;
                    --this.pos;
                    ((Object2IntArrayMap)Object2IntArrayMap.this).key[((Object2IntArrayMap)Object2IntArrayMap.this).size] = null;
                }

                @Override
                public void forEachRemaining(Consumer<? super K> action) {
                    int max = Object2IntArrayMap.this.size;
                    while (this.pos < max) {
                        action.accept(Object2IntArrayMap.this.key[this.pos++]);
                    }
                }
            };
        }

        @Override
        public ObjectSpliterator<K> spliterator() {
            return new KeySetSpliterator(0, Object2IntArrayMap.this.size);
        }

        @Override
        public void forEach(Consumer<? super K> action) {
            int max = Object2IntArrayMap.this.size;
            for (int i2 = 0; i2 < max; ++i2) {
                action.accept(Object2IntArrayMap.this.key[i2]);
            }
        }

        @Override
        public int size() {
            return Object2IntArrayMap.this.size;
        }

        @Override
        public void clear() {
            Object2IntArrayMap.this.clear();
        }

        final class KeySetSpliterator
        extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<K>
        implements ObjectSpliterator<K> {
            KeySetSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override
            public int characteristics() {
                return 16465;
            }

            @Override
            protected final K get(int location) {
                return Object2IntArrayMap.this.key[location];
            }

            protected final com.viaversion.viaversion.libs.fastutil.objects.Object2IntArrayMap$KeySet.KeySetSpliterator makeForSplit(int pos, int maxPos) {
                return new KeySetSpliterator(pos, maxPos);
            }

            @Override
            public void forEachRemaining(Consumer<? super K> action) {
                int max = Object2IntArrayMap.this.size;
                while (this.pos < max) {
                    action.accept(Object2IntArrayMap.this.key[this.pos++]);
                }
            }
        }
    }

    private final class ValuesCollection
    extends AbstractIntCollection {
        private ValuesCollection() {
        }

        @Override
        public boolean contains(int v2) {
            return Object2IntArrayMap.this.containsValue(v2);
        }

        @Override
        public IntIterator iterator() {
            return new IntIterator(){
                int pos = 0;

                @Override
                public boolean hasNext() {
                    return this.pos < Object2IntArrayMap.this.size;
                }

                @Override
                public int nextInt() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return Object2IntArrayMap.this.value[this.pos++];
                }

                @Override
                public void remove() {
                    if (this.pos == 0) {
                        throw new IllegalStateException();
                    }
                    int tail = Object2IntArrayMap.this.size - this.pos;
                    System.arraycopy(Object2IntArrayMap.this.key, this.pos, Object2IntArrayMap.this.key, this.pos - 1, tail);
                    System.arraycopy(Object2IntArrayMap.this.value, this.pos, Object2IntArrayMap.this.value, this.pos - 1, tail);
                    Object2IntArrayMap.this.size--;
                    --this.pos;
                    ((Object2IntArrayMap)Object2IntArrayMap.this).key[((Object2IntArrayMap)Object2IntArrayMap.this).size] = null;
                }

                @Override
                public void forEachRemaining(IntConsumer action) {
                    int max = Object2IntArrayMap.this.size;
                    while (this.pos < max) {
                        action.accept(Object2IntArrayMap.this.value[this.pos++]);
                    }
                }
            };
        }

        @Override
        public IntSpliterator spliterator() {
            return new ValuesSpliterator(0, Object2IntArrayMap.this.size);
        }

        @Override
        public void forEach(IntConsumer action) {
            int max = Object2IntArrayMap.this.size;
            for (int i2 = 0; i2 < max; ++i2) {
                action.accept(Object2IntArrayMap.this.value[i2]);
            }
        }

        @Override
        public int size() {
            return Object2IntArrayMap.this.size;
        }

        @Override
        public void clear() {
            Object2IntArrayMap.this.clear();
        }

        final class ValuesSpliterator
        extends IntSpliterators.EarlyBindingSizeIndexBasedSpliterator
        implements IntSpliterator {
            ValuesSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override
            public int characteristics() {
                return 16720;
            }

            @Override
            protected final int get(int location) {
                return Object2IntArrayMap.this.value[location];
            }

            protected final com.viaversion.viaversion.libs.fastutil.objects.Object2IntArrayMap$ValuesCollection.ValuesSpliterator makeForSplit(int pos, int maxPos) {
                return new ValuesSpliterator(pos, maxPos);
            }

            @Override
            public void forEachRemaining(IntConsumer action) {
                int max = Object2IntArrayMap.this.size;
                while (this.pos < max) {
                    action.accept(Object2IntArrayMap.this.value[this.pos++]);
                }
            }
        }
    }
}

