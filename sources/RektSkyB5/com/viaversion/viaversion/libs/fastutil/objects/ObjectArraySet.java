/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.SafeMath;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrays;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class ObjectArraySet<K>
extends AbstractObjectSet<K>
implements Serializable,
Cloneable {
    private static final long serialVersionUID = 1L;
    private transient Object[] a;
    private int size;

    public ObjectArraySet(Object[] a2) {
        this.a = a2;
        this.size = a2.length;
    }

    public ObjectArraySet() {
        this.a = ObjectArrays.EMPTY_ARRAY;
    }

    public ObjectArraySet(int capacity) {
        this.a = new Object[capacity];
    }

    public ObjectArraySet(ObjectCollection<K> c2) {
        this(c2.size());
        this.addAll(c2);
    }

    public ObjectArraySet(Collection<? extends K> c2) {
        this(c2.size());
        this.addAll(c2);
    }

    public ObjectArraySet(ObjectSet<K> c2) {
        this(c2.size());
        int i2 = 0;
        for (Object x2 : c2) {
            this.a[i2] = x2;
            ++i2;
        }
        this.size = i2;
    }

    public ObjectArraySet(Set<? extends K> c2) {
        this(c2.size());
        int i2 = 0;
        for (K x2 : c2) {
            this.a[i2] = x2;
            ++i2;
        }
        this.size = i2;
    }

    public ObjectArraySet(Object[] a2, int size) {
        this.a = a2;
        this.size = size;
        if (size > a2.length) {
            throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the array size (" + a2.length + ")");
        }
    }

    public static <K> ObjectArraySet<K> of() {
        return ObjectArraySet.ofUnchecked();
    }

    public static <K> ObjectArraySet<K> of(K e2) {
        return ObjectArraySet.ofUnchecked(e2);
    }

    @SafeVarargs
    public static <K> ObjectArraySet<K> of(K ... a2) {
        if (a2.length == 2) {
            if (Objects.equals(a2[0], a2[1])) {
                throw new IllegalArgumentException("Duplicate element: " + a2[1]);
            }
        } else if (a2.length > 2) {
            ObjectOpenHashSet.of(a2);
        }
        return ObjectArraySet.ofUnchecked(a2);
    }

    public static <K> ObjectArraySet<K> ofUnchecked() {
        return new ObjectArraySet<K>();
    }

    @SafeVarargs
    public static <K> ObjectArraySet<K> ofUnchecked(K ... a2) {
        return new ObjectArraySet<K>(a2);
    }

    private int findKey(Object o2) {
        int i2 = this.size;
        while (i2-- != 0) {
            if (!Objects.equals(this.a[i2], o2)) continue;
            return i2;
        }
        return -1;
    }

    @Override
    public ObjectIterator<K> iterator() {
        return new ObjectIterator<K>(){
            int next = 0;

            @Override
            public boolean hasNext() {
                return this.next < ObjectArraySet.this.size;
            }

            @Override
            public K next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return ObjectArraySet.this.a[this.next++];
            }

            @Override
            public void remove() {
                int tail = ObjectArraySet.this.size-- - this.next--;
                System.arraycopy(ObjectArraySet.this.a, this.next + 1, ObjectArraySet.this.a, this.next, tail);
                ((ObjectArraySet)ObjectArraySet.this).a[((ObjectArraySet)ObjectArraySet.this).size] = null;
            }

            @Override
            public int skip(int n2) {
                if (n2 < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
                }
                int remaining = ObjectArraySet.this.size - this.next;
                if (n2 < remaining) {
                    this.next += n2;
                    return n2;
                }
                n2 = remaining;
                this.next = ObjectArraySet.this.size;
                return n2;
            }
        };
    }

    @Override
    public ObjectSpliterator<K> spliterator() {
        return new Spliterator();
    }

    @Override
    public boolean contains(Object k2) {
        return this.findKey(k2) != -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean remove(Object k2) {
        int pos = this.findKey(k2);
        if (pos == -1) {
            return false;
        }
        int tail = this.size - pos - 1;
        for (int i2 = 0; i2 < tail; ++i2) {
            this.a[pos + i2] = this.a[pos + i2 + 1];
        }
        --this.size;
        this.a[this.size] = null;
        return true;
    }

    @Override
    public boolean add(K k2) {
        int pos = this.findKey(k2);
        if (pos != -1) {
            return false;
        }
        if (this.size == this.a.length) {
            Object[] b2 = new Object[this.size == 0 ? 2 : this.size * 2];
            int i2 = this.size;
            while (i2-- != 0) {
                b2[i2] = this.a[i2];
            }
            this.a = b2;
        }
        this.a[this.size++] = k2;
        return true;
    }

    @Override
    public void clear() {
        Arrays.fill(this.a, 0, this.size, null);
        this.size = 0;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(this.a, this.size, Object[].class);
    }

    @Override
    public <K> K[] toArray(K[] a2) {
        if (a2 == null) {
            a2 = new Object[this.size];
        } else if (a2.length < this.size) {
            a2 = (Object[])Array.newInstance(a2.getClass().getComponentType(), this.size);
        }
        System.arraycopy(this.a, 0, a2, 0, this.size);
        if (a2.length > this.size) {
            a2[this.size] = null;
        }
        return a2;
    }

    public ObjectArraySet<K> clone() {
        ObjectArraySet c2;
        try {
            c2 = (ObjectArraySet)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c2.a = (Object[])this.a.clone();
        return c2;
    }

    private void writeObject(ObjectOutputStream s2) throws IOException {
        s2.defaultWriteObject();
        for (int i2 = 0; i2 < this.size; ++i2) {
            s2.writeObject(this.a[i2]);
        }
    }

    private void readObject(ObjectInputStream s2) throws IOException, ClassNotFoundException {
        s2.defaultReadObject();
        this.a = new Object[this.size];
        for (int i2 = 0; i2 < this.size; ++i2) {
            this.a[i2] = s2.readObject();
        }
    }

    private final class Spliterator
    implements ObjectSpliterator<K> {
        boolean hasSplit = false;
        int pos;
        int max;

        public Spliterator() {
            this(0, objectArraySet.size, false);
        }

        private Spliterator(int pos, int max, boolean hasSplit) {
            assert (pos <= max) : "pos " + pos + " must be <= max " + max;
            this.pos = pos;
            this.max = max;
            this.hasSplit = hasSplit;
        }

        private int getWorkingMax() {
            return this.hasSplit ? this.max : ObjectArraySet.this.size;
        }

        @Override
        public int characteristics() {
            return 16465;
        }

        @Override
        public long estimateSize() {
            return this.getWorkingMax() - this.pos;
        }

        @Override
        public boolean tryAdvance(Consumer<? super K> action) {
            if (this.pos >= this.getWorkingMax()) {
                return false;
            }
            action.accept(ObjectArraySet.this.a[this.pos++]);
            return true;
        }

        @Override
        public void forEachRemaining(Consumer<? super K> action) {
            int max = this.getWorkingMax();
            while (this.pos < max) {
                action.accept(ObjectArraySet.this.a[this.pos]);
                ++this.pos;
            }
        }

        @Override
        public long skip(long n2) {
            if (n2 < 0L) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
            }
            int max = this.getWorkingMax();
            if (this.pos >= max) {
                return 0L;
            }
            int remaining = max - this.pos;
            if (n2 < (long)remaining) {
                this.pos = SafeMath.safeLongToInt((long)this.pos + n2);
                return n2;
            }
            n2 = remaining;
            this.pos = max;
            return n2;
        }

        @Override
        public ObjectSpliterator<K> trySplit() {
            int myNewPos;
            int max = this.getWorkingMax();
            int retLen = max - this.pos >> 1;
            if (retLen <= 1) {
                return null;
            }
            this.max = max;
            int retMax = myNewPos = this.pos + retLen;
            int oldPos = this.pos;
            this.pos = myNewPos;
            this.hasSplit = true;
            return new Spliterator(oldPos, retMax, true);
        }
    }
}

