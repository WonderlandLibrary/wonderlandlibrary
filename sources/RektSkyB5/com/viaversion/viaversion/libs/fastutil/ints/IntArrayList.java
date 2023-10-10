/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.Arrays;
import com.viaversion.viaversion.libs.fastutil.SafeMath;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntList;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollections;
import com.viaversion.viaversion.libs.fastutil.ints.IntComparator;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterators;
import com.viaversion.viaversion.libs.fastutil.ints.IntList;
import com.viaversion.viaversion.libs.fastutil.ints.IntListIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterators;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class IntArrayList
extends AbstractIntList
implements RandomAccess,
Cloneable,
Serializable {
    private static final long serialVersionUID = -7046029254386353130L;
    public static final int DEFAULT_INITIAL_CAPACITY = 10;
    protected transient int[] a;
    protected int size;

    private static final int[] copyArraySafe(int[] a2, int length) {
        if (length == 0) {
            return IntArrays.EMPTY_ARRAY;
        }
        return java.util.Arrays.copyOf(a2, length);
    }

    private static final int[] copyArrayFromSafe(IntArrayList l2) {
        return IntArrayList.copyArraySafe(l2.a, l2.size);
    }

    protected IntArrayList(int[] a2, boolean wrapped) {
        this.a = a2;
    }

    private void initArrayFromCapacity(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        }
        this.a = capacity == 0 ? IntArrays.EMPTY_ARRAY : new int[capacity];
    }

    public IntArrayList(int capacity) {
        this.initArrayFromCapacity(capacity);
    }

    public IntArrayList() {
        this.a = IntArrays.DEFAULT_EMPTY_ARRAY;
    }

    public IntArrayList(Collection<? extends Integer> c2) {
        if (c2 instanceof IntArrayList) {
            this.a = IntArrayList.copyArrayFromSafe((IntArrayList)c2);
            this.size = this.a.length;
        } else {
            this.initArrayFromCapacity(c2.size());
            if (c2 instanceof IntList) {
                this.size = c2.size();
                ((IntList)c2).getElements(0, this.a, 0, this.size);
            } else {
                this.size = IntIterators.unwrap(IntIterators.asIntIterator(c2.iterator()), this.a);
            }
        }
    }

    public IntArrayList(IntCollection c2) {
        if (c2 instanceof IntArrayList) {
            this.a = IntArrayList.copyArrayFromSafe((IntArrayList)c2);
            this.size = this.a.length;
        } else {
            this.initArrayFromCapacity(c2.size());
            if (c2 instanceof IntList) {
                this.size = c2.size();
                ((IntList)c2).getElements(0, this.a, 0, this.size);
            } else {
                this.size = IntIterators.unwrap(c2.iterator(), this.a);
            }
        }
    }

    public IntArrayList(IntList l2) {
        if (l2 instanceof IntArrayList) {
            this.a = IntArrayList.copyArrayFromSafe((IntArrayList)l2);
            this.size = this.a.length;
        } else {
            this.initArrayFromCapacity(l2.size());
            this.size = l2.size();
            l2.getElements(0, this.a, 0, this.size);
        }
    }

    public IntArrayList(int[] a2) {
        this(a2, 0, a2.length);
    }

    public IntArrayList(int[] a2, int offset, int length) {
        this(length);
        System.arraycopy(a2, offset, this.a, 0, length);
        this.size = length;
    }

    public IntArrayList(Iterator<? extends Integer> i2) {
        this();
        while (i2.hasNext()) {
            this.add((int)i2.next());
        }
    }

    public IntArrayList(IntIterator i2) {
        this();
        while (i2.hasNext()) {
            this.add(i2.nextInt());
        }
    }

    public int[] elements() {
        return this.a;
    }

    public static IntArrayList wrap(int[] a2, int length) {
        if (length > a2.length) {
            throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + a2.length + ")");
        }
        IntArrayList l2 = new IntArrayList(a2, true);
        l2.size = length;
        return l2;
    }

    public static IntArrayList wrap(int[] a2) {
        return IntArrayList.wrap(a2, a2.length);
    }

    public static IntArrayList of() {
        return new IntArrayList();
    }

    public static IntArrayList of(int ... init) {
        return IntArrayList.wrap(init);
    }

    public static IntArrayList toList(IntStream stream) {
        return stream.collect(IntArrayList::new, IntArrayList::add, IntList::addAll);
    }

    public static IntArrayList toListWithExpectedSize(IntStream stream, int expectedSize) {
        if (expectedSize <= 10) {
            return IntArrayList.toList(stream);
        }
        return stream.collect(new IntCollections.SizeDecreasingSupplier<IntArrayList>(expectedSize, size -> size <= 10 ? new IntArrayList() : new IntArrayList(size)), IntArrayList::add, IntList::addAll);
    }

    public void ensureCapacity(int capacity) {
        if (capacity <= this.a.length || this.a == IntArrays.DEFAULT_EMPTY_ARRAY && capacity <= 10) {
            return;
        }
        this.a = IntArrays.ensureCapacity(this.a, capacity, this.size);
        assert (this.size <= this.a.length);
    }

    private void grow(int capacity) {
        if (capacity <= this.a.length) {
            return;
        }
        if (this.a != IntArrays.DEFAULT_EMPTY_ARRAY) {
            capacity = (int)Math.max(Math.min((long)this.a.length + (long)(this.a.length >> 1), 0x7FFFFFF7L), (long)capacity);
        } else if (capacity < 10) {
            capacity = 10;
        }
        this.a = IntArrays.forceCapacity(this.a, capacity, this.size);
        assert (this.size <= this.a.length);
    }

    @Override
    public void add(int index, int k2) {
        this.ensureIndex(index);
        this.grow(this.size + 1);
        if (index != this.size) {
            System.arraycopy(this.a, index, this.a, index + 1, this.size - index);
        }
        this.a[index] = k2;
        ++this.size;
        assert (this.size <= this.a.length);
    }

    @Override
    public boolean add(int k2) {
        this.grow(this.size + 1);
        this.a[this.size++] = k2;
        assert (this.size <= this.a.length);
        return true;
    }

    @Override
    public int getInt(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return this.a[index];
    }

    @Override
    public int indexOf(int k2) {
        for (int i2 = 0; i2 < this.size; ++i2) {
            if (k2 != this.a[i2]) continue;
            return i2;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(int k2) {
        int i2 = this.size;
        while (i2-- != 0) {
            if (k2 != this.a[i2]) continue;
            return i2;
        }
        return -1;
    }

    @Override
    public int removeInt(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        int old = this.a[index];
        --this.size;
        if (index != this.size) {
            System.arraycopy(this.a, index + 1, this.a, index, this.size - index);
        }
        assert (this.size <= this.a.length);
        return old;
    }

    @Override
    public boolean rem(int k2) {
        int index = this.indexOf(k2);
        if (index == -1) {
            return false;
        }
        this.removeInt(index);
        assert (this.size <= this.a.length);
        return true;
    }

    @Override
    public int set(int index, int k2) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        int old = this.a[index];
        this.a[index] = k2;
        return old;
    }

    @Override
    public void clear() {
        this.size = 0;
        assert (this.size <= this.a.length);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void size(int size) {
        if (size > this.a.length) {
            this.a = IntArrays.forceCapacity(this.a, size, this.size);
        }
        if (size > this.size) {
            java.util.Arrays.fill(this.a, this.size, size, 0);
        }
        this.size = size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    public void trim() {
        this.trim(0);
    }

    public void trim(int n2) {
        if (n2 >= this.a.length || this.size == this.a.length) {
            return;
        }
        int[] t2 = new int[Math.max(n2, this.size)];
        System.arraycopy(this.a, 0, t2, 0, this.size);
        this.a = t2;
        assert (this.size <= this.a.length);
    }

    @Override
    public IntList subList(int from, int to) {
        if (from == 0 && to == this.size()) {
            return this;
        }
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new SubList(from, to);
    }

    @Override
    public void getElements(int from, int[] a2, int offset, int length) {
        IntArrays.ensureOffsetLength(a2, offset, length);
        System.arraycopy(this.a, from, a2, offset, length);
    }

    @Override
    public void removeElements(int from, int to) {
        Arrays.ensureFromTo(this.size, from, to);
        System.arraycopy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
    }

    @Override
    public void addElements(int index, int[] a2, int offset, int length) {
        this.ensureIndex(index);
        IntArrays.ensureOffsetLength(a2, offset, length);
        this.grow(this.size + length);
        System.arraycopy(this.a, index, this.a, index + length, this.size - index);
        System.arraycopy(a2, offset, this.a, index, length);
        this.size += length;
    }

    @Override
    public void setElements(int index, int[] a2, int offset, int length) {
        this.ensureIndex(index);
        IntArrays.ensureOffsetLength(a2, offset, length);
        if (index + length > this.size) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size + ")");
        }
        System.arraycopy(a2, offset, this.a, index, length);
    }

    @Override
    public void forEach(IntConsumer action) {
        for (int i2 = 0; i2 < this.size; ++i2) {
            action.accept(this.a[i2]);
        }
    }

    @Override
    public boolean addAll(int index, IntCollection c2) {
        if (c2 instanceof IntList) {
            return this.addAll(index, (IntList)c2);
        }
        this.ensureIndex(index);
        int n2 = c2.size();
        if (n2 == 0) {
            return false;
        }
        this.grow(this.size + n2);
        System.arraycopy(this.a, index, this.a, index + n2, this.size - index);
        IntIterator i2 = c2.iterator();
        this.size += n2;
        while (n2-- != 0) {
            this.a[index++] = i2.nextInt();
        }
        assert (this.size <= this.a.length);
        return true;
    }

    @Override
    public boolean addAll(int index, IntList l2) {
        this.ensureIndex(index);
        int n2 = l2.size();
        if (n2 == 0) {
            return false;
        }
        this.grow(this.size + n2);
        System.arraycopy(this.a, index, this.a, index + n2, this.size - index);
        l2.getElements(0, this.a, index, n2);
        this.size += n2;
        assert (this.size <= this.a.length);
        return true;
    }

    @Override
    public boolean removeAll(IntCollection c2) {
        int[] a2 = this.a;
        int j2 = 0;
        for (int i2 = 0; i2 < this.size; ++i2) {
            if (c2.contains(a2[i2])) continue;
            a2[j2++] = a2[i2];
        }
        boolean modified = this.size != j2;
        this.size = j2;
        return modified;
    }

    @Override
    public int[] toArray(int[] a2) {
        if (a2 == null || a2.length < this.size) {
            a2 = java.util.Arrays.copyOf(a2, this.size);
        }
        System.arraycopy(this.a, 0, a2, 0, this.size);
        return a2;
    }

    @Override
    public IntListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new IntListIterator(){
            int pos;
            int last;
            {
                this.pos = index;
                this.last = -1;
            }

            @Override
            public boolean hasNext() {
                return this.pos < IntArrayList.this.size;
            }

            @Override
            public boolean hasPrevious() {
                return this.pos > 0;
            }

            @Override
            public int nextInt() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                this.last = this.pos++;
                return IntArrayList.this.a[this.last];
            }

            @Override
            public int previousInt() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                this.last = --this.pos;
                return IntArrayList.this.a[this.pos];
            }

            @Override
            public int nextIndex() {
                return this.pos;
            }

            @Override
            public int previousIndex() {
                return this.pos - 1;
            }

            @Override
            public void add(int k2) {
                IntArrayList.this.add(this.pos++, k2);
                this.last = -1;
            }

            @Override
            public void set(int k2) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                IntArrayList.this.set(this.last, k2);
            }

            @Override
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                IntArrayList.this.removeInt(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }

            @Override
            public void forEachRemaining(IntConsumer action) {
                while (this.pos < IntArrayList.this.size) {
                    ++this.pos;
                    this.last = this.last;
                    action.accept(IntArrayList.this.a[this.last]);
                }
            }

            @Override
            public int back(int n2) {
                if (n2 < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
                }
                int remaining = IntArrayList.this.size - this.pos;
                if (n2 < remaining) {
                    this.pos -= n2;
                } else {
                    n2 = remaining;
                    this.pos = 0;
                }
                this.last = this.pos;
                return n2;
            }

            @Override
            public int skip(int n2) {
                if (n2 < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
                }
                int remaining = IntArrayList.this.size - this.pos;
                if (n2 < remaining) {
                    this.pos += n2;
                } else {
                    n2 = remaining;
                    this.pos = IntArrayList.this.size;
                }
                this.last = this.pos - 1;
                return n2;
            }
        };
    }

    @Override
    public IntSpliterator spliterator() {
        return new Spliterator();
    }

    @Override
    public void sort(IntComparator comp) {
        if (comp == null) {
            IntArrays.stableSort(this.a, 0, this.size);
        } else {
            IntArrays.stableSort(this.a, 0, this.size, comp);
        }
    }

    @Override
    public void unstableSort(IntComparator comp) {
        if (comp == null) {
            IntArrays.unstableSort(this.a, 0, this.size);
        } else {
            IntArrays.unstableSort(this.a, 0, this.size, comp);
        }
    }

    public IntArrayList clone() {
        IntArrayList cloned = null;
        if (this.getClass() == IntArrayList.class) {
            cloned = new IntArrayList(IntArrayList.copyArraySafe(this.a, this.size), false);
            cloned.size = this.size;
        } else {
            try {
                cloned = (IntArrayList)super.clone();
            }
            catch (CloneNotSupportedException err) {
                throw new InternalError(err);
            }
            cloned.a = IntArrayList.copyArraySafe(this.a, this.size);
        }
        return cloned;
    }

    public boolean equals(IntArrayList l2) {
        if (l2 == this) {
            return true;
        }
        int s2 = this.size();
        if (s2 != l2.size()) {
            return false;
        }
        int[] a1 = this.a;
        int[] a2 = l2.a;
        if (a1 == a2 && s2 == l2.size()) {
            return true;
        }
        while (s2-- != 0) {
            if (a1[s2] == a2[s2]) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (o2 == null) {
            return false;
        }
        if (!(o2 instanceof List)) {
            return false;
        }
        if (o2 instanceof IntArrayList) {
            return this.equals((IntArrayList)o2);
        }
        if (o2 instanceof SubList) {
            return ((SubList)o2).equals(this);
        }
        return super.equals(o2);
    }

    @Override
    public int compareTo(IntArrayList l2) {
        int i2;
        int s1 = this.size();
        int s2 = l2.size();
        int[] a1 = this.a;
        int[] a2 = l2.a;
        if (a1 == a2 && s1 == s2) {
            return 0;
        }
        for (i2 = 0; i2 < s1 && i2 < s2; ++i2) {
            int e1 = a1[i2];
            int e2 = a2[i2];
            int r2 = Integer.compare(e1, e2);
            if (r2 == 0) continue;
            return r2;
        }
        return i2 < s2 ? -1 : (i2 < s1 ? 1 : 0);
    }

    @Override
    public int compareTo(List<? extends Integer> l2) {
        if (l2 instanceof IntArrayList) {
            return this.compareTo((IntArrayList)l2);
        }
        if (l2 instanceof SubList) {
            return -((SubList)l2).compareTo(this);
        }
        return super.compareTo(l2);
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

    private class SubList
    extends AbstractIntList.IntRandomAccessSubList {
        private static final long serialVersionUID = -3185226345314976296L;

        protected SubList(int from, int to) {
            super(IntArrayList.this, from, to);
        }

        private int[] getParentArray() {
            return IntArrayList.this.a;
        }

        @Override
        public int getInt(int i2) {
            this.ensureRestrictedIndex(i2);
            return IntArrayList.this.a[i2 + this.from];
        }

        @Override
        public IntListIterator listIterator(int index) {
            return new SubListIterator(index);
        }

        @Override
        public IntSpliterator spliterator() {
            return new SubListSpliterator();
        }

        boolean contentsEquals(int[] otherA, int otherAFrom, int otherATo) {
            if (IntArrayList.this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return true;
            }
            if (otherATo - otherAFrom != this.size()) {
                return false;
            }
            int pos = this.from;
            int otherPos = otherAFrom;
            while (pos < this.to) {
                if (IntArrayList.this.a[pos++] == otherA[otherPos++]) continue;
                return false;
            }
            return true;
        }

        @Override
        public boolean equals(Object o2) {
            if (o2 == this) {
                return true;
            }
            if (o2 == null) {
                return false;
            }
            if (!(o2 instanceof List)) {
                return false;
            }
            if (o2 instanceof IntArrayList) {
                IntArrayList other = (IntArrayList)o2;
                return this.contentsEquals(other.a, 0, other.size());
            }
            if (o2 instanceof SubList) {
                SubList other = (SubList)o2;
                return this.contentsEquals(other.getParentArray(), other.from, other.to);
            }
            return super.equals(o2);
        }

        int contentsCompareTo(int[] otherA, int otherAFrom, int otherATo) {
            if (IntArrayList.this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return 0;
            }
            int i2 = this.from;
            int j2 = otherAFrom;
            while (i2 < this.to && i2 < otherATo) {
                int e1 = IntArrayList.this.a[i2];
                int e2 = otherA[j2];
                int r2 = Integer.compare(e1, e2);
                if (r2 != 0) {
                    return r2;
                }
                ++i2;
                ++j2;
            }
            return i2 < otherATo ? -1 : (i2 < this.to ? 1 : 0);
        }

        @Override
        public int compareTo(List<? extends Integer> l2) {
            if (l2 instanceof IntArrayList) {
                IntArrayList other = (IntArrayList)l2;
                return this.contentsCompareTo(other.a, 0, other.size());
            }
            if (l2 instanceof SubList) {
                SubList other = (SubList)l2;
                return this.contentsCompareTo(other.getParentArray(), other.from, other.to);
            }
            return super.compareTo(l2);
        }

        private final class SubListIterator
        extends IntIterators.AbstractIndexBasedListIterator {
            SubListIterator(int index) {
                super(0, index);
            }

            @Override
            protected final int get(int i2) {
                return IntArrayList.this.a[SubList.this.from + i2];
            }

            @Override
            protected final void add(int i2, int k2) {
                SubList.this.add(i2, k2);
            }

            @Override
            protected final void set(int i2, int k2) {
                SubList.this.set(i2, k2);
            }

            @Override
            protected final void remove(int i2) {
                SubList.this.removeInt(i2);
            }

            @Override
            protected final int getMaxPos() {
                return SubList.this.to - SubList.this.from;
            }

            @Override
            public int nextInt() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                this.lastReturned = this.pos++;
                return IntArrayList.this.a[SubList.this.from + this.lastReturned];
            }

            @Override
            public int previousInt() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                this.lastReturned = --this.pos;
                return IntArrayList.this.a[SubList.this.from + this.pos];
            }

            @Override
            public void forEachRemaining(IntConsumer action) {
                int max = SubList.this.to - SubList.this.from;
                while (this.pos < max) {
                    ++this.pos;
                    this.lastReturned = this.lastReturned;
                    action.accept(IntArrayList.this.a[SubList.this.from + this.lastReturned]);
                }
            }
        }

        private final class SubListSpliterator
        extends IntSpliterators.LateBindingSizeIndexBasedSpliterator {
            SubListSpliterator() {
                super(SubList.this.from);
            }

            private SubListSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override
            protected final int getMaxPosFromBackingStore() {
                return SubList.this.to;
            }

            @Override
            protected final int get(int i2) {
                return IntArrayList.this.a[i2];
            }

            @Override
            protected final SubListSpliterator makeForSplit(int pos, int maxPos) {
                return new SubListSpliterator(pos, maxPos);
            }

            @Override
            public boolean tryAdvance(IntConsumer action) {
                if (this.pos >= this.getMaxPos()) {
                    return false;
                }
                action.accept(IntArrayList.this.a[this.pos++]);
                return true;
            }

            @Override
            public void forEachRemaining(IntConsumer action) {
                int max = this.getMaxPos();
                while (this.pos < max) {
                    action.accept(IntArrayList.this.a[this.pos++]);
                }
            }
        }
    }

    private final class Spliterator
    implements IntSpliterator {
        boolean hasSplit = false;
        int pos;
        int max;

        public Spliterator() {
            this(0, intArrayList.size, false);
        }

        private Spliterator(int pos, int max, boolean hasSplit) {
            assert (pos <= max) : "pos " + pos + " must be <= max " + max;
            this.pos = pos;
            this.max = max;
            this.hasSplit = hasSplit;
        }

        private int getWorkingMax() {
            return this.hasSplit ? this.max : IntArrayList.this.size;
        }

        @Override
        public int characteristics() {
            return 16720;
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
            action.accept(IntArrayList.this.a[this.pos++]);
            return true;
        }

        @Override
        public void forEachRemaining(IntConsumer action) {
            int max = this.getWorkingMax();
            while (this.pos < max) {
                action.accept(IntArrayList.this.a[this.pos]);
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

