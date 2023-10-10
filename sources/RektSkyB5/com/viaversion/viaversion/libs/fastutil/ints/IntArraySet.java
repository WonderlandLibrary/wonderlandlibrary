/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.SafeMath;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.IntConsumer;

public class IntArraySet
extends AbstractIntSet
implements Serializable,
Cloneable {
    private static final long serialVersionUID = 1L;
    private transient int[] a;
    private int size;

    public IntArraySet(int[] a2) {
        this.a = a2;
        this.size = a2.length;
    }

    public IntArraySet() {
        this.a = IntArrays.EMPTY_ARRAY;
    }

    public IntArraySet(int capacity) {
        this.a = new int[capacity];
    }

    public IntArraySet(IntCollection c2) {
        this(c2.size());
        this.addAll(c2);
    }

    public IntArraySet(Collection<? extends Integer> c2) {
        this(c2.size());
        this.addAll(c2);
    }

    public IntArraySet(IntSet c2) {
        this(c2.size());
        int i2 = 0;
        IntIterator intIterator = c2.iterator();
        while (intIterator.hasNext()) {
            int x2;
            this.a[i2] = x2 = ((Integer)intIterator.next()).intValue();
            ++i2;
        }
        this.size = i2;
    }

    public IntArraySet(Set<? extends Integer> c2) {
        this(c2.size());
        int i2 = 0;
        for (Integer n2 : c2) {
            this.a[i2] = n2;
            ++i2;
        }
        this.size = i2;
    }

    public IntArraySet(int[] a2, int size) {
        this.a = a2;
        this.size = size;
        if (size > a2.length) {
            throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the array size (" + a2.length + ")");
        }
    }

    public static IntArraySet of() {
        return IntArraySet.ofUnchecked();
    }

    public static IntArraySet of(int e2) {
        return IntArraySet.ofUnchecked(e2);
    }

    public static IntArraySet of(int ... a2) {
        if (a2.length == 2) {
            if (a2[0] == a2[1]) {
                throw new IllegalArgumentException("Duplicate element: " + a2[1]);
            }
        } else if (a2.length > 2) {
            IntOpenHashSet.of(a2);
        }
        return IntArraySet.ofUnchecked(a2);
    }

    public static IntArraySet ofUnchecked() {
        return new IntArraySet();
    }

    public static IntArraySet ofUnchecked(int ... a2) {
        return new IntArraySet(a2);
    }

    private int findKey(int o2) {
        int i2 = this.size;
        while (i2-- != 0) {
            if (this.a[i2] != o2) continue;
            return i2;
        }
        return -1;
    }

    @Override
    public IntIterator iterator() {
        return new IntIterator(){
            int next = 0;

            @Override
            public boolean hasNext() {
                return this.next < IntArraySet.this.size;
            }

            @Override
            public int nextInt() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return IntArraySet.this.a[this.next++];
            }

            @Override
            public void remove() {
                int tail = IntArraySet.this.size-- - this.next--;
                System.arraycopy(IntArraySet.this.a, this.next + 1, IntArraySet.this.a, this.next, tail);
            }

            @Override
            public int skip(int n2) {
                if (n2 < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
                }
                int remaining = IntArraySet.this.size - this.next;
                if (n2 < remaining) {
                    this.next += n2;
                    return n2;
                }
                n2 = remaining;
                this.next = IntArraySet.this.size;
                return n2;
            }
        };
    }

    @Override
    public IntSpliterator spliterator() {
        return new Spliterator();
    }

    @Override
    public boolean contains(int k2) {
        return this.findKey(k2) != -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean remove(int k2) {
        int pos = this.findKey(k2);
        if (pos == -1) {
            return false;
        }
        int tail = this.size - pos - 1;
        for (int i2 = 0; i2 < tail; ++i2) {
            this.a[pos + i2] = this.a[pos + i2 + 1];
        }
        --this.size;
        return true;
    }

    @Override
    public boolean add(int k2) {
        int pos = this.findKey(k2);
        if (pos != -1) {
            return false;
        }
        if (this.size == this.a.length) {
            int[] b2 = new int[this.size == 0 ? 2 : this.size * 2];
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
        this.size = 0;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public int[] toIntArray() {
        return Arrays.copyOf(this.a, this.size);
    }

    @Override
    public int[] toArray(int[] a2) {
        if (a2 == null || a2.length < this.size) {
            a2 = new int[this.size];
        }
        System.arraycopy(this.a, 0, a2, 0, this.size);
        return a2;
    }

    public IntArraySet clone() {
        IntArraySet c2;
        try {
            c2 = (IntArraySet)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c2.a = (int[])this.a.clone();
        return c2;
    }

    private void writeObject(ObjectOutputStream s2) throws IOException {
        s2.defaultWriteObject();
        for (int i2 = 0; i2 < this.size; ++i2) {
            s2.writeInt(this.a[i2]);
        }
    }

    private void readObject(ObjectInputStream s2) throws IOException, ClassNotFoundException {
        s2.defaultReadObject();
        this.a = new int[this.size];
        for (int i2 = 0; i2 < this.size; ++i2) {
            this.a[i2] = s2.readInt();
        }
    }

    private final class Spliterator
    implements IntSpliterator {
        boolean hasSplit = false;
        int pos;
        int max;

        public Spliterator() {
            this(0, intArraySet.size, false);
        }

        private Spliterator(int pos, int max, boolean hasSplit) {
            assert (pos <= max) : "pos " + pos + " must be <= max " + max;
            this.pos = pos;
            this.max = max;
            this.hasSplit = hasSplit;
        }

        private int getWorkingMax() {
            return this.hasSplit ? this.max : IntArraySet.this.size;
        }

        @Override
        public int characteristics() {
            return 16721;
        }

        @Override
        public long estimateSize() {
            return this.getWorkingMax() - this.pos;
        }

        @Override
        public boolean tryAdvance(IntConsumer action) {
            if (this.pos >= this.getWorkingMax()) {
                return false;
            }
            action.accept(IntArraySet.this.a[this.pos++]);
            return true;
        }

        @Override
        public void forEachRemaining(IntConsumer action) {
            int max = this.getWorkingMax();
            while (this.pos < max) {
                action.accept(IntArraySet.this.a[this.pos]);
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
        public IntSpliterator trySplit() {
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

