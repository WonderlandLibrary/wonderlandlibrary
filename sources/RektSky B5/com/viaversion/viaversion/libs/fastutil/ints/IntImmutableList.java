/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.SafeMath;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrayList;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterators;
import com.viaversion.viaversion.libs.fastutil.ints.IntList;
import com.viaversion.viaversion.libs.fastutil.ints.IntListIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntLists;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterators;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class IntImmutableList
extends IntLists.ImmutableListBase
implements IntList,
RandomAccess,
Cloneable,
Serializable {
    private static final long serialVersionUID = 0L;
    static final IntImmutableList EMPTY = new IntImmutableList(IntArrays.EMPTY_ARRAY);
    private final int[] a;

    public IntImmutableList(int[] a2) {
        this.a = a2;
    }

    public IntImmutableList(Collection<? extends Integer> c2) {
        this(c2.isEmpty() ? IntArrays.EMPTY_ARRAY : IntIterators.unwrap(IntIterators.asIntIterator(c2.iterator())));
    }

    public IntImmutableList(IntCollection c2) {
        this(c2.isEmpty() ? IntArrays.EMPTY_ARRAY : IntIterators.unwrap(c2.iterator()));
    }

    public IntImmutableList(IntList l2) {
        this(l2.isEmpty() ? IntArrays.EMPTY_ARRAY : new int[l2.size()]);
        l2.getElements(0, this.a, 0, l2.size());
    }

    public IntImmutableList(int[] a2, int offset, int length) {
        this(length == 0 ? IntArrays.EMPTY_ARRAY : new int[length]);
        System.arraycopy(a2, offset, this.a, 0, length);
    }

    public IntImmutableList(IntIterator i2) {
        this(i2.hasNext() ? IntIterators.unwrap(i2) : IntArrays.EMPTY_ARRAY);
    }

    public static IntImmutableList of() {
        return EMPTY;
    }

    public static IntImmutableList of(int ... init) {
        return init.length == 0 ? IntImmutableList.of() : new IntImmutableList(init);
    }

    private static IntImmutableList convertTrustedToImmutableList(IntArrayList arrayList) {
        if (arrayList.isEmpty()) {
            return IntImmutableList.of();
        }
        int[] backingArray = arrayList.elements();
        if (arrayList.size() != backingArray.length) {
            backingArray = Arrays.copyOf(backingArray, arrayList.size());
        }
        return new IntImmutableList(backingArray);
    }

    public static IntImmutableList toList(IntStream stream) {
        return IntImmutableList.convertTrustedToImmutableList(IntArrayList.toList(stream));
    }

    public static IntImmutableList toListWithExpectedSize(IntStream stream, int expectedSize) {
        return IntImmutableList.convertTrustedToImmutableList(IntArrayList.toListWithExpectedSize(stream, expectedSize));
    }

    @Override
    public int getInt(int index) {
        if (index >= this.a.length) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.a.length + ")");
        }
        return this.a[index];
    }

    @Override
    public int indexOf(int k2) {
        int size = this.a.length;
        for (int i2 = 0; i2 < size; ++i2) {
            if (k2 != this.a[i2]) continue;
            return i2;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(int k2) {
        int i2 = this.a.length;
        while (i2-- != 0) {
            if (k2 != this.a[i2]) continue;
            return i2;
        }
        return -1;
    }

    @Override
    public int size() {
        return this.a.length;
    }

    @Override
    public boolean isEmpty() {
        return this.a.length == 0;
    }

    @Override
    public void getElements(int from, int[] a2, int offset, int length) {
        IntArrays.ensureOffsetLength(a2, offset, length);
        System.arraycopy(this.a, from, a2, offset, length);
    }

    @Override
    public void forEach(IntConsumer action) {
        for (int i2 = 0; i2 < this.a.length; ++i2) {
            action.accept(this.a[i2]);
        }
    }

    @Override
    public int[] toIntArray() {
        return (int[])this.a.clone();
    }

    @Override
    public int[] toArray(int[] a2) {
        if (a2 == null || a2.length < this.size()) {
            a2 = new int[this.a.length];
        }
        System.arraycopy(this.a, 0, a2, 0, a2.length);
        return a2;
    }

    @Override
    public IntListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new IntListIterator(){
            int pos;
            {
                this.pos = index;
            }

            @Override
            public boolean hasNext() {
                return this.pos < IntImmutableList.this.a.length;
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
                return IntImmutableList.this.a[this.pos++];
            }

            @Override
            public int previousInt() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                return IntImmutableList.this.a[--this.pos];
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
            public void forEachRemaining(IntConsumer action) {
                while (this.pos < IntImmutableList.this.a.length) {
                    action.accept(IntImmutableList.this.a[this.pos++]);
                }
            }

            @Override
            public void add(int k2) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void set(int k2) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int back(int n2) {
                if (n2 < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
                }
                int remaining = IntImmutableList.this.a.length - this.pos;
                if (n2 < remaining) {
                    this.pos -= n2;
                } else {
                    n2 = remaining;
                    this.pos = 0;
                }
                return n2;
            }

            @Override
            public int skip(int n2) {
                if (n2 < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
                }
                int remaining = IntImmutableList.this.a.length - this.pos;
                if (n2 < remaining) {
                    this.pos += n2;
                } else {
                    n2 = remaining;
                    this.pos = IntImmutableList.this.a.length;
                }
                return n2;
            }
        };
    }

    @Override
    public IntSpliterator spliterator() {
        return new Spliterator();
    }

    @Override
    public IntList subList(int from, int to) {
        if (from == 0 && to == this.size()) {
            return this;
        }
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from == to) {
            return EMPTY;
        }
        if (from > to) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new ImmutableSubList(this, from, to);
    }

    public IntImmutableList clone() {
        return this;
    }

    public boolean equals(IntImmutableList l2) {
        if (l2 == this) {
            return true;
        }
        if (this.a == l2.a) {
            return true;
        }
        int s2 = this.size();
        if (s2 != l2.size()) {
            return false;
        }
        int[] a1 = this.a;
        int[] a2 = l2.a;
        return Arrays.equals(a1, a2);
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
        if (o2 instanceof IntImmutableList) {
            return this.equals((IntImmutableList)o2);
        }
        if (o2 instanceof ImmutableSubList) {
            return ((ImmutableSubList)o2).equals(this);
        }
        return super.equals(o2);
    }

    @Override
    public int compareTo(IntImmutableList l2) {
        int i2;
        if (this.a == l2.a) {
            return 0;
        }
        int s1 = this.size();
        int s2 = l2.size();
        int[] a1 = this.a;
        int[] a2 = l2.a;
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
        if (l2 instanceof IntImmutableList) {
            return this.compareTo((IntImmutableList)l2);
        }
        if (l2 instanceof ImmutableSubList) {
            ImmutableSubList other = (ImmutableSubList)l2;
            return -other.compareTo(this);
        }
        return super.compareTo(l2);
    }

    private final class Spliterator
    implements IntSpliterator {
        int pos;
        int max;

        public Spliterator() {
            this(0, intImmutableList.a.length);
        }

        private Spliterator(int pos, int max) {
            assert (pos <= max) : "pos " + pos + " must be <= max " + max;
            this.pos = pos;
            this.max = max;
        }

        @Override
        public int characteristics() {
            return 17744;
        }

        @Override
        public long estimateSize() {
            return this.max - this.pos;
        }

        @Override
        public boolean tryAdvance(IntConsumer action) {
            if (this.pos >= this.max) {
                return false;
            }
            action.accept(IntImmutableList.this.a[this.pos++]);
            return true;
        }

        @Override
        public void forEachRemaining(IntConsumer action) {
            while (this.pos < this.max) {
                action.accept(IntImmutableList.this.a[this.pos]);
                ++this.pos;
            }
        }

        @Override
        public long skip(long n2) {
            if (n2 < 0L) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
            }
            if (this.pos >= this.max) {
                return 0L;
            }
            int remaining = this.max - this.pos;
            if (n2 < (long)remaining) {
                this.pos = SafeMath.safeLongToInt((long)this.pos + n2);
                return n2;
            }
            n2 = remaining;
            this.pos = this.max;
            return n2;
        }

        @Override
        public IntSpliterator trySplit() {
            int myNewPos;
            int retLen = this.max - this.pos >> 1;
            if (retLen <= 1) {
                return null;
            }
            int retMax = myNewPos = this.pos + retLen;
            int oldPos = this.pos;
            this.pos = myNewPos;
            return new Spliterator(oldPos, retMax);
        }
    }

    private static final class ImmutableSubList
    extends IntLists.ImmutableListBase
    implements RandomAccess,
    Serializable {
        private static final long serialVersionUID = 7054639518438982401L;
        final IntImmutableList innerList;
        final int from;
        final int to;
        final transient int[] a;

        ImmutableSubList(IntImmutableList innerList, int from, int to) {
            this.innerList = innerList;
            this.from = from;
            this.to = to;
            this.a = innerList.a;
        }

        @Override
        public int getInt(int index) {
            this.ensureRestrictedIndex(index);
            return this.a[index + this.from];
        }

        @Override
        public int indexOf(int k2) {
            for (int i2 = this.from; i2 < this.to; ++i2) {
                if (k2 != this.a[i2]) continue;
                return i2 - this.from;
            }
            return -1;
        }

        @Override
        public int lastIndexOf(int k2) {
            int i2 = this.to;
            while (i2-- != this.from) {
                if (k2 != this.a[i2]) continue;
                return i2 - this.from;
            }
            return -1;
        }

        @Override
        public int size() {
            return this.to - this.from;
        }

        @Override
        public boolean isEmpty() {
            return this.to <= this.from;
        }

        @Override
        public void getElements(int fromSublistIndex, int[] a2, int offset, int length) {
            IntArrays.ensureOffsetLength(a2, offset, length);
            this.ensureRestrictedIndex(fromSublistIndex);
            if (this.from + length > this.to) {
                throw new IndexOutOfBoundsException("Final index " + (this.from + length) + " (startingIndex: " + this.from + " + length: " + length + ") is greater then list length " + this.size());
            }
            System.arraycopy(this.a, fromSublistIndex + this.from, a2, offset, length);
        }

        @Override
        public void forEach(IntConsumer action) {
            for (int i2 = this.from; i2 < this.to; ++i2) {
                action.accept(this.a[i2]);
            }
        }

        @Override
        public int[] toIntArray() {
            return Arrays.copyOfRange(this.a, this.from, this.to);
        }

        @Override
        public int[] toArray(int[] a2) {
            if (a2 == null || a2.length < this.size()) {
                a2 = new int[this.size()];
            }
            System.arraycopy(this.a, this.from, a2, 0, this.size());
            return a2;
        }

        @Override
        public IntListIterator listIterator(final int index) {
            this.ensureIndex(index);
            return new IntListIterator(){
                int pos;
                {
                    this.pos = index;
                }

                @Override
                public boolean hasNext() {
                    return this.pos < to;
                }

                @Override
                public boolean hasPrevious() {
                    return this.pos > from;
                }

                @Override
                public int nextInt() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return a[this.pos++ + from];
                }

                @Override
                public int previousInt() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    return a[--this.pos + from];
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
                public void forEachRemaining(IntConsumer action) {
                    while (this.pos < to) {
                        action.accept(a[this.pos++ + from]);
                    }
                }

                @Override
                public void add(int k2) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void set(int k2) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public int back(int n2) {
                    if (n2 < 0) {
                        throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
                    }
                    int remaining = to - this.pos;
                    if (n2 < remaining) {
                        this.pos -= n2;
                    } else {
                        n2 = remaining;
                        this.pos = 0;
                    }
                    return n2;
                }

                @Override
                public int skip(int n2) {
                    if (n2 < 0) {
                        throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
                    }
                    int remaining = to - this.pos;
                    if (n2 < remaining) {
                        this.pos += n2;
                    } else {
                        n2 = remaining;
                        this.pos = to;
                    }
                    return n2;
                }
            };
        }

        @Override
        public IntSpliterator spliterator() {
            return new SubListSpliterator();
        }

        boolean contentsEquals(int[] otherA, int otherAFrom, int otherATo) {
            if (this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return true;
            }
            if (otherATo - otherAFrom != this.size()) {
                return false;
            }
            int pos = this.from;
            int otherPos = otherAFrom;
            while (pos < this.to) {
                if (this.a[pos++] == otherA[otherPos++]) continue;
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
            if (o2 instanceof IntImmutableList) {
                IntImmutableList other = (IntImmutableList)o2;
                return this.contentsEquals(other.a, 0, other.size());
            }
            if (o2 instanceof ImmutableSubList) {
                ImmutableSubList other = (ImmutableSubList)o2;
                return this.contentsEquals(other.a, other.from, other.to);
            }
            return super.equals(o2);
        }

        int contentsCompareTo(int[] otherA, int otherAFrom, int otherATo) {
            if (this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return 0;
            }
            int i2 = this.from;
            int j2 = otherAFrom;
            while (i2 < this.to && i2 < otherATo) {
                int e1 = this.a[i2];
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
            if (l2 instanceof IntImmutableList) {
                IntImmutableList other = (IntImmutableList)l2;
                return this.contentsCompareTo(other.a, 0, other.size());
            }
            if (l2 instanceof ImmutableSubList) {
                ImmutableSubList other = (ImmutableSubList)l2;
                return this.contentsCompareTo(other.a, other.from, other.to);
            }
            return super.compareTo(l2);
        }

        private Object readResolve() throws ObjectStreamException {
            try {
                return this.innerList.subList(this.from, this.to);
            }
            catch (IllegalArgumentException | IndexOutOfBoundsException ex) {
                throw (InvalidObjectException)new InvalidObjectException(ex.getMessage()).initCause(ex);
            }
        }

        @Override
        public IntList subList(int from, int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from == to) {
                return EMPTY;
            }
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new ImmutableSubList(this.innerList, from + this.from, to + this.from);
        }

        private final class SubListSpliterator
        extends IntSpliterators.EarlyBindingSizeIndexBasedSpliterator {
            SubListSpliterator() {
                super(ImmutableSubList.this.from, ImmutableSubList.this.to);
            }

            private SubListSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override
            protected final int get(int i2) {
                return ImmutableSubList.this.a[i2];
            }

            @Override
            protected final SubListSpliterator makeForSplit(int pos, int maxPos) {
                return new SubListSpliterator(pos, maxPos);
            }

            @Override
            public boolean tryAdvance(IntConsumer action) {
                if (this.pos >= this.maxPos) {
                    return false;
                }
                action.accept(ImmutableSubList.this.a[this.pos++]);
                return true;
            }

            @Override
            public void forEachRemaining(IntConsumer action) {
                int max = this.maxPos;
                while (this.pos < max) {
                    action.accept(ImmutableSubList.this.a[this.pos++]);
                }
            }

            @Override
            public int characteristics() {
                return 17744;
            }
        }
    }
}

