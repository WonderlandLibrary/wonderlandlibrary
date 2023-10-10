/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.ints.AbstractInt2IntMap;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntSet;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterators;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterators;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class Int2IntArrayMap
extends AbstractInt2IntMap
implements Serializable,
Cloneable {
    private static final long serialVersionUID = 1L;
    private transient int[] key;
    private transient int[] value;
    private int size;
    private transient Int2IntMap.FastEntrySet entries;
    private transient IntSet keys;
    private transient IntCollection values;

    public Int2IntArrayMap(int[] key, int[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
    }

    public Int2IntArrayMap() {
        this.key = IntArrays.EMPTY_ARRAY;
        this.value = IntArrays.EMPTY_ARRAY;
    }

    public Int2IntArrayMap(int capacity) {
        this.key = new int[capacity];
        this.value = new int[capacity];
    }

    public Int2IntArrayMap(Int2IntMap m2) {
        this(m2.size());
        int i2 = 0;
        for (Int2IntMap.Entry e2 : m2.int2IntEntrySet()) {
            this.key[i2] = e2.getIntKey();
            this.value[i2] = e2.getIntValue();
            ++i2;
        }
        this.size = i2;
    }

    public Int2IntArrayMap(Map<? extends Integer, ? extends Integer> m2) {
        this(m2.size());
        int i2 = 0;
        for (Map.Entry<? extends Integer, ? extends Integer> e2 : m2.entrySet()) {
            this.key[i2] = e2.getKey();
            this.value[i2] = e2.getValue();
            ++i2;
        }
        this.size = i2;
    }

    public Int2IntArrayMap(int[] key, int[] value, int size) {
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

    public Int2IntMap.FastEntrySet int2IntEntrySet() {
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
    public int get(int k2) {
        int[] key = this.key;
        int i2 = this.size;
        while (i2-- != 0) {
            if (key[i2] != k2) continue;
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
        this.size = 0;
    }

    @Override
    public boolean containsKey(int k2) {
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
    public int put(int k2, int v2) {
        int oldKey = this.findKey(k2);
        if (oldKey != -1) {
            int oldValue = this.value[oldKey];
            this.value[oldKey] = v2;
            return oldValue;
        }
        if (this.size == this.key.length) {
            int[] newKey = new int[this.size == 0 ? 2 : this.size * 2];
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
    public int remove(int k2) {
        int oldPos = this.findKey(k2);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        int oldValue = this.value[oldPos];
        int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        return oldValue;
    }

    @Override
    public IntSet keySet() {
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

    public Int2IntArrayMap clone() {
        Int2IntArrayMap c2;
        try {
            c2 = (Int2IntArrayMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c2.key = (int[])this.key.clone();
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
            s2.writeInt(this.key[i2]);
            s2.writeInt(this.value[i2]);
        }
    }

    private void readObject(ObjectInputStream s2) throws IOException, ClassNotFoundException {
        s2.defaultReadObject();
        this.key = new int[this.size];
        this.value = new int[this.size];
        for (int i2 = 0; i2 < this.size; ++i2) {
            this.key[i2] = s2.readInt();
            this.value[i2] = s2.readInt();
        }
    }

    private final class EntrySet
    extends AbstractObjectSet<Int2IntMap.Entry>
    implements Int2IntMap.FastEntrySet {
        private EntrySet() {
        }

        @Override
        public ObjectIterator<Int2IntMap.Entry> iterator() {
            return new ObjectIterator<Int2IntMap.Entry>(){
                int curr = -1;
                int next = 0;

                @Override
                public boolean hasNext() {
                    return this.next < Int2IntArrayMap.this.size;
                }

                @Override
                public Int2IntMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.curr = this.next;
                    return new AbstractInt2IntMap.BasicEntry(Int2IntArrayMap.this.key[this.curr], Int2IntArrayMap.this.value[this.next++]);
                }

                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    int tail = Int2IntArrayMap.this.size-- - this.next--;
                    System.arraycopy(Int2IntArrayMap.this.key, this.next + 1, Int2IntArrayMap.this.key, this.next, tail);
                    System.arraycopy(Int2IntArrayMap.this.value, this.next + 1, Int2IntArrayMap.this.value, this.next, tail);
                }

                @Override
                public void forEachRemaining(Consumer<? super Int2IntMap.Entry> action) {
                    int max = Int2IntArrayMap.this.size;
                    while (this.next < max) {
                        this.curr = this.next;
                        action.accept(new AbstractInt2IntMap.BasicEntry(Int2IntArrayMap.this.key[this.curr], Int2IntArrayMap.this.value[this.next++]));
                    }
                }
            };
        }

        @Override
        public ObjectIterator<Int2IntMap.Entry> fastIterator() {
            return new ObjectIterator<Int2IntMap.Entry>(){
                int next = 0;
                int curr = -1;
                final AbstractInt2IntMap.BasicEntry entry = new AbstractInt2IntMap.BasicEntry();

                @Override
                public boolean hasNext() {
                    return this.next < Int2IntArrayMap.this.size;
                }

                @Override
                public Int2IntMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.curr = this.next;
                    this.entry.key = Int2IntArrayMap.this.key[this.curr];
                    this.entry.value = Int2IntArrayMap.this.value[this.next++];
                    return this.entry;
                }

                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    int tail = Int2IntArrayMap.this.size-- - this.next--;
                    System.arraycopy(Int2IntArrayMap.this.key, this.next + 1, Int2IntArrayMap.this.key, this.next, tail);
                    System.arraycopy(Int2IntArrayMap.this.value, this.next + 1, Int2IntArrayMap.this.value, this.next, tail);
                }

                @Override
                public void forEachRemaining(Consumer<? super Int2IntMap.Entry> action) {
                    int max = Int2IntArrayMap.this.size;
                    while (this.next < max) {
                        this.curr = this.next;
                        this.entry.key = Int2IntArrayMap.this.key[this.curr];
                        this.entry.value = Int2IntArrayMap.this.value[this.next++];
                        action.accept(this.entry);
                    }
                }
            };
        }

        @Override
        public ObjectSpliterator<Int2IntMap.Entry> spliterator() {
            return new EntrySetSpliterator(0, Int2IntArrayMap.this.size);
        }

        @Override
        public void forEach(Consumer<? super Int2IntMap.Entry> action) {
            int max = Int2IntArrayMap.this.size;
            for (int i2 = 0; i2 < max; ++i2) {
                action.accept(new AbstractInt2IntMap.BasicEntry(Int2IntArrayMap.this.key[i2], Int2IntArrayMap.this.value[i2]));
            }
        }

        @Override
        public void fastForEach(Consumer<? super Int2IntMap.Entry> action) {
            AbstractInt2IntMap.BasicEntry entry = new AbstractInt2IntMap.BasicEntry();
            int max = Int2IntArrayMap.this.size;
            for (int i2 = 0; i2 < max; ++i2) {
                entry.key = Int2IntArrayMap.this.key[i2];
                entry.value = Int2IntArrayMap.this.value[i2];
                action.accept(entry);
            }
        }

        @Override
        public int size() {
            return Int2IntArrayMap.this.size;
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
            if (e2.getValue() == null || !(e2.getValue() instanceof Integer)) {
                return false;
            }
            int k2 = (Integer)e2.getKey();
            return Int2IntArrayMap.this.containsKey(k2) && Int2IntArrayMap.this.get(k2) == ((Integer)e2.getValue()).intValue();
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
            if (e2.getValue() == null || !(e2.getValue() instanceof Integer)) {
                return false;
            }
            int k2 = (Integer)e2.getKey();
            int v2 = (Integer)e2.getValue();
            int oldPos = Int2IntArrayMap.this.findKey(k2);
            if (oldPos == -1 || v2 != Int2IntArrayMap.this.value[oldPos]) {
                return false;
            }
            int tail = Int2IntArrayMap.this.size - oldPos - 1;
            System.arraycopy(Int2IntArrayMap.this.key, oldPos + 1, Int2IntArrayMap.this.key, oldPos, tail);
            System.arraycopy(Int2IntArrayMap.this.value, oldPos + 1, Int2IntArrayMap.this.value, oldPos, tail);
            Int2IntArrayMap.this.size--;
            return true;
        }

        final class EntrySetSpliterator
        extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<Int2IntMap.Entry>
        implements ObjectSpliterator<Int2IntMap.Entry> {
            EntrySetSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override
            public int characteristics() {
                return 16465;
            }

            @Override
            protected final Int2IntMap.Entry get(int location) {
                return new AbstractInt2IntMap.BasicEntry(Int2IntArrayMap.this.key[location], Int2IntArrayMap.this.value[location]);
            }

            protected final EntrySetSpliterator makeForSplit(int pos, int maxPos) {
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
            return Int2IntArrayMap.this.findKey(k2) != -1;
        }

        @Override
        public boolean remove(int k2) {
            int oldPos = Int2IntArrayMap.this.findKey(k2);
            if (oldPos == -1) {
                return false;
            }
            int tail = Int2IntArrayMap.this.size - oldPos - 1;
            System.arraycopy(Int2IntArrayMap.this.key, oldPos + 1, Int2IntArrayMap.this.key, oldPos, tail);
            System.arraycopy(Int2IntArrayMap.this.value, oldPos + 1, Int2IntArrayMap.this.value, oldPos, tail);
            Int2IntArrayMap.this.size--;
            return true;
        }

        @Override
        public IntIterator iterator() {
            return new IntIterator(){
                int pos = 0;

                @Override
                public boolean hasNext() {
                    return this.pos < Int2IntArrayMap.this.size;
                }

                @Override
                public int nextInt() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return Int2IntArrayMap.this.key[this.pos++];
                }

                @Override
                public void remove() {
                    if (this.pos == 0) {
                        throw new IllegalStateException();
                    }
                    int tail = Int2IntArrayMap.this.size - this.pos;
                    System.arraycopy(Int2IntArrayMap.this.key, this.pos, Int2IntArrayMap.this.key, this.pos - 1, tail);
                    System.arraycopy(Int2IntArrayMap.this.value, this.pos, Int2IntArrayMap.this.value, this.pos - 1, tail);
                    Int2IntArrayMap.this.size--;
                    --this.pos;
                }

                @Override
                public void forEachRemaining(IntConsumer action) {
                    int max = Int2IntArrayMap.this.size;
                    while (this.pos < max) {
                        action.accept(Int2IntArrayMap.this.key[this.pos++]);
                    }
                }
            };
        }

        @Override
        public IntSpliterator spliterator() {
            return new KeySetSpliterator(0, Int2IntArrayMap.this.size);
        }

        @Override
        public void forEach(IntConsumer action) {
            int max = Int2IntArrayMap.this.size;
            for (int i2 = 0; i2 < max; ++i2) {
                action.accept(Int2IntArrayMap.this.key[i2]);
            }
        }

        @Override
        public int size() {
            return Int2IntArrayMap.this.size;
        }

        @Override
        public void clear() {
            Int2IntArrayMap.this.clear();
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
                return Int2IntArrayMap.this.key[location];
            }

            @Override
            protected final KeySetSpliterator makeForSplit(int pos, int maxPos) {
                return new KeySetSpliterator(pos, maxPos);
            }

            @Override
            public void forEachRemaining(IntConsumer action) {
                int max = Int2IntArrayMap.this.size;
                while (this.pos < max) {
                    action.accept(Int2IntArrayMap.this.key[this.pos++]);
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
            return Int2IntArrayMap.this.containsValue(v2);
        }

        @Override
        public IntIterator iterator() {
            return new IntIterator(){
                int pos = 0;

                @Override
                public boolean hasNext() {
                    return this.pos < Int2IntArrayMap.this.size;
                }

                @Override
                public int nextInt() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return Int2IntArrayMap.this.value[this.pos++];
                }

                @Override
                public void remove() {
                    if (this.pos == 0) {
                        throw new IllegalStateException();
                    }
                    int tail = Int2IntArrayMap.this.size - this.pos;
                    System.arraycopy(Int2IntArrayMap.this.key, this.pos, Int2IntArrayMap.this.key, this.pos - 1, tail);
                    System.arraycopy(Int2IntArrayMap.this.value, this.pos, Int2IntArrayMap.this.value, this.pos - 1, tail);
                    Int2IntArrayMap.this.size--;
                    --this.pos;
                }

                @Override
                public void forEachRemaining(IntConsumer action) {
                    int max = Int2IntArrayMap.this.size;
                    while (this.pos < max) {
                        action.accept(Int2IntArrayMap.this.value[this.pos++]);
                    }
                }
            };
        }

        @Override
        public IntSpliterator spliterator() {
            return new ValuesSpliterator(0, Int2IntArrayMap.this.size);
        }

        @Override
        public void forEach(IntConsumer action) {
            int max = Int2IntArrayMap.this.size;
            for (int i2 = 0; i2 < max; ++i2) {
                action.accept(Int2IntArrayMap.this.value[i2]);
            }
        }

        @Override
        public int size() {
            return Int2IntArrayMap.this.size;
        }

        @Override
        public void clear() {
            Int2IntArrayMap.this.clear();
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
                return Int2IntArrayMap.this.value[location];
            }

            @Override
            protected final ValuesSpliterator makeForSplit(int pos, int maxPos) {
                return new ValuesSpliterator(pos, maxPos);
            }

            @Override
            public void forEachRemaining(IntConsumer action) {
                int max = Int2IntArrayMap.this.size;
                while (this.pos < max) {
                    action.accept(Int2IntArrayMap.this.value[this.pos++]);
                }
            }
        }
    }
}

