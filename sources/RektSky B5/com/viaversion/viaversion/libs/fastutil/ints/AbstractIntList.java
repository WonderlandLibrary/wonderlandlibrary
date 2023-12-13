/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterators;
import com.viaversion.viaversion.libs.fastutil.ints.IntList;
import com.viaversion.viaversion.libs.fastutil.ints.IntListIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterators;
import com.viaversion.viaversion.libs.fastutil.ints.IntStack;
import com.viaversion.viaversion.libs.fastutil.ints.IntUnaryOperator;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.IntConsumer;

public abstract class AbstractIntList
extends AbstractIntCollection
implements IntList,
IntStack {
    protected AbstractIntList() {
    }

    protected void ensureIndex(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index > this.size()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + this.size() + ")");
        }
    }

    protected void ensureRestrictedIndex(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index >= this.size()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size() + ")");
        }
    }

    @Override
    public void add(int index, int k2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(int k2) {
        this.add(this.size(), k2);
        return true;
    }

    @Override
    public int removeInt(int i2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int set(int index, int k2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends Integer> c2) {
        if (c2 instanceof IntCollection) {
            return this.addAll(index, (IntCollection)c2);
        }
        this.ensureIndex(index);
        Iterator<? extends Integer> i2 = c2.iterator();
        boolean retVal = i2.hasNext();
        while (i2.hasNext()) {
            this.add(index++, (int)i2.next());
        }
        return retVal;
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c2) {
        return this.addAll(this.size(), c2);
    }

    @Override
    public IntListIterator iterator() {
        return this.listIterator();
    }

    @Override
    public IntListIterator listIterator() {
        return this.listIterator(0);
    }

    @Override
    public IntListIterator listIterator(int index) {
        this.ensureIndex(index);
        return new IntIterators.AbstractIndexBasedListIterator(0, index){

            @Override
            protected final int get(int i2) {
                return AbstractIntList.this.getInt(i2);
            }

            @Override
            protected final void add(int i2, int k2) {
                AbstractIntList.this.add(i2, k2);
            }

            @Override
            protected final void set(int i2, int k2) {
                AbstractIntList.this.set(i2, k2);
            }

            @Override
            protected final void remove(int i2) {
                AbstractIntList.this.removeInt(i2);
            }

            @Override
            protected final int getMaxPos() {
                return AbstractIntList.this.size();
            }
        };
    }

    @Override
    public boolean contains(int k2) {
        return this.indexOf(k2) >= 0;
    }

    @Override
    public int indexOf(int k2) {
        IntListIterator i2 = this.listIterator();
        while (i2.hasNext()) {
            int e2 = i2.nextInt();
            if (k2 != e2) continue;
            return i2.previousIndex();
        }
        return -1;
    }

    @Override
    public int lastIndexOf(int k2) {
        IntListIterator i2 = this.listIterator(this.size());
        while (i2.hasPrevious()) {
            int e2 = i2.previousInt();
            if (k2 != e2) continue;
            return i2.nextIndex();
        }
        return -1;
    }

    @Override
    public void size(int size) {
        int i2 = this.size();
        if (size > i2) {
            while (i2++ < size) {
                this.add(0);
            }
        } else {
            while (i2-- != size) {
                this.removeInt(i2);
            }
        }
    }

    @Override
    public IntList subList(int from, int to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return this instanceof RandomAccess ? new IntRandomAccessSubList(this, from, to) : new IntSubList(this, from, to);
    }

    @Override
    public void forEach(IntConsumer action) {
        if (this instanceof RandomAccess) {
            int max = this.size();
            for (int i2 = 0; i2 < max; ++i2) {
                action.accept(this.getInt(i2));
            }
        } else {
            IntList.super.forEach(action);
        }
    }

    @Override
    public void removeElements(int from, int to) {
        this.ensureIndex(to);
        IntListIterator i2 = this.listIterator(from);
        int n2 = to - from;
        if (n2 < 0) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (n2-- != 0) {
            i2.nextInt();
            i2.remove();
        }
    }

    @Override
    public void addElements(int index, int[] a2, int offset, int length) {
        this.ensureIndex(index);
        IntArrays.ensureOffsetLength(a2, offset, length);
        if (this instanceof RandomAccess) {
            while (length-- != 0) {
                this.add(index++, a2[offset++]);
            }
        } else {
            IntListIterator iter = this.listIterator(index);
            while (length-- != 0) {
                iter.add(a2[offset++]);
            }
        }
    }

    @Override
    public void addElements(int index, int[] a2) {
        this.addElements(index, a2, 0, a2.length);
    }

    @Override
    public void getElements(int from, int[] a2, int offset, int length) {
        this.ensureIndex(from);
        IntArrays.ensureOffsetLength(a2, offset, length);
        if (from + length > this.size()) {
            throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + this.size() + ")");
        }
        if (this instanceof RandomAccess) {
            int current = from;
            while (length-- != 0) {
                a2[offset++] = this.getInt(current++);
            }
        } else {
            IntListIterator i2 = this.listIterator(from);
            while (length-- != 0) {
                a2[offset++] = i2.nextInt();
            }
        }
    }

    @Override
    public void setElements(int index, int[] a2, int offset, int length) {
        this.ensureIndex(index);
        IntArrays.ensureOffsetLength(a2, offset, length);
        if (index + length > this.size()) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size() + ")");
        }
        if (this instanceof RandomAccess) {
            for (int i2 = 0; i2 < length; ++i2) {
                this.set(i2 + index, a2[i2 + offset]);
            }
        } else {
            IntListIterator iter = this.listIterator(index);
            int i3 = 0;
            while (i3 < length) {
                iter.nextInt();
                iter.set(a2[offset + i3++]);
            }
        }
    }

    @Override
    public void clear() {
        this.removeElements(0, this.size());
    }

    @Override
    public int hashCode() {
        IntListIterator i2 = this.iterator();
        int h2 = 1;
        int s2 = this.size();
        while (s2-- != 0) {
            int k2 = i2.nextInt();
            h2 = 31 * h2 + k2;
        }
        return h2;
    }

    @Override
    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (!(o2 instanceof List)) {
            return false;
        }
        List l2 = (List)o2;
        int s2 = this.size();
        if (s2 != l2.size()) {
            return false;
        }
        if (l2 instanceof IntList) {
            IntListIterator i1 = this.listIterator();
            IntListIterator i2 = ((IntList)l2).listIterator();
            while (s2-- != 0) {
                if (i1.nextInt() == i2.nextInt()) continue;
                return false;
            }
            return true;
        }
        IntListIterator i1 = this.listIterator();
        ListIterator i2 = l2.listIterator();
        while (s2-- != 0) {
            if (Objects.equals(i1.next(), i2.next())) continue;
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(List<? extends Integer> l2) {
        if (l2 == this) {
            return 0;
        }
        if (l2 instanceof IntList) {
            IntListIterator i1 = this.listIterator();
            IntListIterator i2 = ((IntList)l2).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                int e2;
                int e1 = i1.nextInt();
                int r2 = Integer.compare(e1, e2 = i2.nextInt());
                if (r2 == 0) continue;
                return r2;
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        IntListIterator i1 = this.listIterator();
        ListIterator<? extends Integer> i2 = l2.listIterator();
        while (i1.hasNext() && i2.hasNext()) {
            int r3 = ((Comparable)i1.next()).compareTo(i2.next());
            if (r3 == 0) continue;
            return r3;
        }
        return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
    }

    @Override
    public void push(int o2) {
        this.add(o2);
    }

    @Override
    public int popInt() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.removeInt(this.size() - 1);
    }

    @Override
    public int topInt() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getInt(this.size() - 1);
    }

    @Override
    public int peekInt(int i2) {
        return this.getInt(this.size() - 1 - i2);
    }

    @Override
    public boolean rem(int k2) {
        int index = this.indexOf(k2);
        if (index == -1) {
            return false;
        }
        this.removeInt(index);
        return true;
    }

    @Override
    public int[] toIntArray() {
        int size = this.size();
        int[] ret = new int[size];
        this.getElements(0, ret, 0, size);
        return ret;
    }

    @Override
    public int[] toArray(int[] a2) {
        int size = this.size();
        if (a2.length < size) {
            a2 = Arrays.copyOf(a2, size);
        }
        this.getElements(0, a2, 0, size);
        return a2;
    }

    @Override
    public boolean addAll(int index, IntCollection c2) {
        this.ensureIndex(index);
        IntIterator i2 = c2.iterator();
        boolean retVal = i2.hasNext();
        while (i2.hasNext()) {
            this.add(index++, i2.nextInt());
        }
        return retVal;
    }

    @Override
    public boolean addAll(IntCollection c2) {
        return this.addAll(this.size(), c2);
    }

    @Override
    public final void replaceAll(IntUnaryOperator operator) {
        this.replaceAll((java.util.function.IntUnaryOperator)operator);
    }

    @Override
    public String toString() {
        StringBuilder s2 = new StringBuilder();
        IntListIterator i2 = this.iterator();
        int n2 = this.size();
        boolean first = true;
        s2.append("[");
        while (n2-- != 0) {
            if (first) {
                first = false;
            } else {
                s2.append(", ");
            }
            int k2 = i2.nextInt();
            s2.append(String.valueOf(k2));
        }
        s2.append("]");
        return s2.toString();
    }

    public static class IntRandomAccessSubList
    extends IntSubList
    implements RandomAccess {
        private static final long serialVersionUID = -107070782945191929L;

        public IntRandomAccessSubList(IntList l2, int from, int to) {
            super(l2, from, to);
        }

        @Override
        public IntList subList(int from, int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new IntRandomAccessSubList(this, from, to);
        }
    }

    public static class IntSubList
    extends AbstractIntList
    implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntList l;
        protected final int from;
        protected int to;

        public IntSubList(IntList l2, int from, int to) {
            this.l = l2;
            this.from = from;
            this.to = to;
        }

        private boolean assertRange() {
            assert (this.from <= this.l.size());
            assert (this.to <= this.l.size());
            assert (this.to >= this.from);
            return true;
        }

        @Override
        public boolean add(int k2) {
            this.l.add(this.to, k2);
            ++this.to;
            assert (this.assertRange());
            return true;
        }

        @Override
        public void add(int index, int k2) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k2);
            ++this.to;
            assert (this.assertRange());
        }

        @Override
        public boolean addAll(int index, Collection<? extends Integer> c2) {
            this.ensureIndex(index);
            this.to += c2.size();
            return this.l.addAll(this.from + index, c2);
        }

        @Override
        public int getInt(int index) {
            this.ensureRestrictedIndex(index);
            return this.l.getInt(this.from + index);
        }

        @Override
        public int removeInt(int index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeInt(this.from + index);
        }

        @Override
        public int set(int index, int k2) {
            this.ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k2);
        }

        @Override
        public int size() {
            return this.to - this.from;
        }

        @Override
        public void getElements(int from, int[] a2, int offset, int length) {
            this.ensureIndex(from);
            if (from + length > this.size()) {
                throw new IndexOutOfBoundsException("End index (" + from + length + ") is greater than list size (" + this.size() + ")");
            }
            this.l.getElements(this.from + from, a2, offset, length);
        }

        @Override
        public void removeElements(int from, int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            this.l.removeElements(this.from + from, this.from + to);
            this.to -= to - from;
            assert (this.assertRange());
        }

        @Override
        public void addElements(int index, int[] a2, int offset, int length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a2, offset, length);
            this.to += length;
            assert (this.assertRange());
        }

        @Override
        public void setElements(int index, int[] a2, int offset, int length) {
            this.ensureIndex(index);
            this.l.setElements(this.from + index, a2, offset, length);
            assert (this.assertRange());
        }

        @Override
        public IntListIterator listIterator(int index) {
            this.ensureIndex(index);
            return this.l instanceof RandomAccess ? new RandomAccessIter(index) : new ParentWrappingIter(this.l.listIterator(index + this.from));
        }

        @Override
        public IntSpliterator spliterator() {
            return this.l instanceof RandomAccess ? new IndexBasedSpliterator(this.l, this.from, this.to) : super.spliterator();
        }

        @Override
        public IntList subList(int from, int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new IntSubList(this, from, to);
        }

        @Override
        public boolean rem(int k2) {
            int index = this.indexOf(k2);
            if (index == -1) {
                return false;
            }
            --this.to;
            this.l.removeInt(this.from + index);
            assert (this.assertRange());
            return true;
        }

        @Override
        public boolean addAll(int index, IntCollection c2) {
            this.ensureIndex(index);
            return super.addAll(index, c2);
        }

        @Override
        public boolean addAll(int index, IntList l2) {
            this.ensureIndex(index);
            return super.addAll(index, l2);
        }

        private final class RandomAccessIter
        extends IntIterators.AbstractIndexBasedListIterator {
            RandomAccessIter(int pos) {
                super(0, pos);
            }

            @Override
            protected final int get(int i2) {
                return IntSubList.this.l.getInt(IntSubList.this.from + i2);
            }

            @Override
            protected final void add(int i2, int k2) {
                IntSubList.this.add(i2, k2);
            }

            @Override
            protected final void set(int i2, int k2) {
                IntSubList.this.set(i2, k2);
            }

            @Override
            protected final void remove(int i2) {
                IntSubList.this.removeInt(i2);
            }

            @Override
            protected final int getMaxPos() {
                return IntSubList.this.to - IntSubList.this.from;
            }

            @Override
            public void add(int k2) {
                super.add(k2);
                assert (IntSubList.this.assertRange());
            }

            @Override
            public void remove() {
                super.remove();
                assert (IntSubList.this.assertRange());
            }
        }

        private class ParentWrappingIter
        implements IntListIterator {
            private IntListIterator parent;

            ParentWrappingIter(IntListIterator parent) {
                this.parent = parent;
            }

            @Override
            public int nextIndex() {
                return this.parent.nextIndex() - IntSubList.this.from;
            }

            @Override
            public int previousIndex() {
                return this.parent.previousIndex() - IntSubList.this.from;
            }

            @Override
            public boolean hasNext() {
                return this.parent.nextIndex() < IntSubList.this.to;
            }

            @Override
            public boolean hasPrevious() {
                return this.parent.previousIndex() >= IntSubList.this.from;
            }

            @Override
            public int nextInt() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return this.parent.nextInt();
            }

            @Override
            public int previousInt() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                return this.parent.previousInt();
            }

            @Override
            public void add(int k2) {
                this.parent.add(k2);
            }

            @Override
            public void set(int k2) {
                this.parent.set(k2);
            }

            @Override
            public void remove() {
                this.parent.remove();
            }

            @Override
            public int back(int n2) {
                if (n2 < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
                }
                int currentPos = this.parent.previousIndex();
                int parentNewPos = currentPos - n2;
                if (parentNewPos < IntSubList.this.from - 1) {
                    parentNewPos = IntSubList.this.from - 1;
                }
                int toSkip = parentNewPos - currentPos;
                return this.parent.back(toSkip);
            }

            @Override
            public int skip(int n2) {
                if (n2 < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
                }
                int currentPos = this.parent.nextIndex();
                int parentNewPos = currentPos + n2;
                if (parentNewPos > IntSubList.this.to) {
                    parentNewPos = IntSubList.this.to;
                }
                int toSkip = parentNewPos - currentPos;
                return this.parent.skip(toSkip);
            }
        }
    }

    static final class IndexBasedSpliterator
    extends IntSpliterators.LateBindingSizeIndexBasedSpliterator {
        final IntList l;

        IndexBasedSpliterator(IntList l2, int pos) {
            super(pos);
            this.l = l2;
        }

        IndexBasedSpliterator(IntList l2, int pos, int maxPos) {
            super(pos, maxPos);
            this.l = l2;
        }

        @Override
        protected final int getMaxPosFromBackingStore() {
            return this.l.size();
        }

        @Override
        protected final int get(int i2) {
            return this.l.getInt(i2);
        }

        @Override
        protected final IndexBasedSpliterator makeForSplit(int pos, int maxPos) {
            return new IndexBasedSpliterator(this.l, pos, maxPos);
        }
    }
}

