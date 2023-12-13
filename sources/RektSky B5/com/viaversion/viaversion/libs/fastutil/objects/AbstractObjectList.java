/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.Stack;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrays;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterators;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectList;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectListIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterators;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Consumer;

public abstract class AbstractObjectList<K>
extends AbstractObjectCollection<K>
implements ObjectList<K>,
Stack<K> {
    protected AbstractObjectList() {
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
    public void add(int index, K k2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(K k2) {
        this.add(this.size(), k2);
        return true;
    }

    @Override
    public K remove(int i2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public K set(int index, K k2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends K> c2) {
        this.ensureIndex(index);
        Iterator<K> i2 = c2.iterator();
        boolean retVal = i2.hasNext();
        while (i2.hasNext()) {
            this.add(index++, i2.next());
        }
        return retVal;
    }

    @Override
    public boolean addAll(Collection<? extends K> c2) {
        return this.addAll(this.size(), c2);
    }

    @Override
    public ObjectListIterator<K> iterator() {
        return this.listIterator();
    }

    @Override
    public ObjectListIterator<K> listIterator() {
        return this.listIterator(0);
    }

    @Override
    public ObjectListIterator<K> listIterator(int index) {
        this.ensureIndex(index);
        return new ObjectIterators.AbstractIndexBasedListIterator<K>(0, index){

            @Override
            protected final K get(int i2) {
                return AbstractObjectList.this.get(i2);
            }

            @Override
            protected final void add(int i2, K k2) {
                AbstractObjectList.this.add(i2, k2);
            }

            @Override
            protected final void set(int i2, K k2) {
                AbstractObjectList.this.set(i2, k2);
            }

            @Override
            protected final void remove(int i2) {
                AbstractObjectList.this.remove(i2);
            }

            @Override
            protected final int getMaxPos() {
                return AbstractObjectList.this.size();
            }
        };
    }

    @Override
    public boolean contains(Object k2) {
        return this.indexOf(k2) >= 0;
    }

    @Override
    public int indexOf(Object k2) {
        ListIterator i2 = this.listIterator();
        while (i2.hasNext()) {
            Object e2 = i2.next();
            if (!Objects.equals(k2, e2)) continue;
            return i2.previousIndex();
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object k2) {
        ListIterator i2 = this.listIterator(this.size());
        while (i2.hasPrevious()) {
            Object e2 = i2.previous();
            if (!Objects.equals(k2, e2)) continue;
            return i2.nextIndex();
        }
        return -1;
    }

    @Override
    public void size(int size) {
        int i2 = this.size();
        if (size > i2) {
            while (i2++ < size) {
                this.add((K)null);
            }
        } else {
            while (i2-- != size) {
                this.remove(i2);
            }
        }
    }

    @Override
    public ObjectList<K> subList(int from, int to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return this instanceof RandomAccess ? new ObjectRandomAccessSubList(this, from, to) : new ObjectSubList(this, from, to);
    }

    @Override
    public void forEach(Consumer<? super K> action) {
        if (this instanceof RandomAccess) {
            int max = this.size();
            for (int i2 = 0; i2 < max; ++i2) {
                action.accept(this.get(i2));
            }
        } else {
            ObjectList.super.forEach(action);
        }
    }

    @Override
    public void removeElements(int from, int to) {
        this.ensureIndex(to);
        ListIterator i2 = this.listIterator(from);
        int n2 = to - from;
        if (n2 < 0) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (n2-- != 0) {
            i2.next();
            i2.remove();
        }
    }

    @Override
    public void addElements(int index, K[] a2, int offset, int length) {
        this.ensureIndex(index);
        ObjectArrays.ensureOffsetLength(a2, offset, length);
        if (this instanceof RandomAccess) {
            while (length-- != 0) {
                this.add(index++, a2[offset++]);
            }
        } else {
            ListIterator iter = this.listIterator(index);
            while (length-- != 0) {
                iter.add(a2[offset++]);
            }
        }
    }

    @Override
    public void addElements(int index, K[] a2) {
        this.addElements(index, a2, 0, a2.length);
    }

    @Override
    public void getElements(int from, Object[] a2, int offset, int length) {
        this.ensureIndex(from);
        ObjectArrays.ensureOffsetLength(a2, offset, length);
        if (from + length > this.size()) {
            throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + this.size() + ")");
        }
        if (this instanceof RandomAccess) {
            int current = from;
            while (length-- != 0) {
                a2[offset++] = this.get(current++);
            }
        } else {
            ListIterator i2 = this.listIterator(from);
            while (length-- != 0) {
                a2[offset++] = i2.next();
            }
        }
    }

    @Override
    public void setElements(int index, K[] a2, int offset, int length) {
        this.ensureIndex(index);
        ObjectArrays.ensureOffsetLength(a2, offset, length);
        if (index + length > this.size()) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size() + ")");
        }
        if (this instanceof RandomAccess) {
            for (int i2 = 0; i2 < length; ++i2) {
                this.set(i2 + index, a2[i2 + offset]);
            }
        } else {
            ListIterator iter = this.listIterator(index);
            int i3 = 0;
            while (i3 < length) {
                iter.next();
                iter.set(a2[offset + i3++]);
            }
        }
    }

    @Override
    public void clear() {
        this.removeElements(0, this.size());
    }

    @Override
    public Object[] toArray() {
        int size = this.size();
        Object[] ret = new Object[size];
        this.getElements(0, ret, 0, size);
        return ret;
    }

    @Override
    public <T> T[] toArray(T[] a2) {
        int size = this.size();
        if (a2.length < size) {
            a2 = Arrays.copyOf(a2, size);
        }
        this.getElements(0, a2, 0, size);
        if (a2.length > size) {
            a2[size] = null;
        }
        return a2;
    }

    @Override
    public int hashCode() {
        ObjectIterator i2 = this.iterator();
        int h2 = 1;
        int s2 = this.size();
        while (s2-- != 0) {
            Object k2 = i2.next();
            h2 = 31 * h2 + (k2 == null ? 0 : k2.hashCode());
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
        ListIterator i1 = this.listIterator();
        ListIterator i2 = l2.listIterator();
        while (s2-- != 0) {
            if (Objects.equals(i1.next(), i2.next())) continue;
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(List<? extends K> l2) {
        if (l2 == this) {
            return 0;
        }
        if (l2 instanceof ObjectList) {
            ListIterator i1 = this.listIterator();
            ListIterator i2 = ((ObjectList)l2).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                Object e2;
                Object e1 = i1.next();
                int r2 = ((Comparable)e1).compareTo(e2 = i2.next());
                if (r2 == 0) continue;
                return r2;
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        ListIterator i1 = this.listIterator();
        ListIterator<K> i2 = l2.listIterator();
        while (i1.hasNext() && i2.hasNext()) {
            int r3 = ((Comparable)i1.next()).compareTo(i2.next());
            if (r3 == 0) continue;
            return r3;
        }
        return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
    }

    @Override
    public void push(K o2) {
        this.add(o2);
    }

    @Override
    public K pop() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.remove(this.size() - 1);
    }

    @Override
    public K top() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return (K)this.get(this.size() - 1);
    }

    @Override
    public K peek(int i2) {
        return (K)this.get(this.size() - 1 - i2);
    }

    @Override
    public String toString() {
        StringBuilder s2 = new StringBuilder();
        ObjectIterator i2 = this.iterator();
        int n2 = this.size();
        boolean first = true;
        s2.append("[");
        while (n2-- != 0) {
            if (first) {
                first = false;
            } else {
                s2.append(", ");
            }
            Object k2 = i2.next();
            if (this == k2) {
                s2.append("(this list)");
                continue;
            }
            s2.append(String.valueOf(k2));
        }
        s2.append("]");
        return s2.toString();
    }

    public static class ObjectRandomAccessSubList<K>
    extends ObjectSubList<K>
    implements RandomAccess {
        private static final long serialVersionUID = -107070782945191929L;

        public ObjectRandomAccessSubList(ObjectList<K> l2, int from, int to) {
            super(l2, from, to);
        }

        @Override
        public ObjectList<K> subList(int from, int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new ObjectRandomAccessSubList<K>(this, from, to);
        }
    }

    public static class ObjectSubList<K>
    extends AbstractObjectList<K>
    implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectList<K> l;
        protected final int from;
        protected int to;

        public ObjectSubList(ObjectList<K> l2, int from, int to) {
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
        public boolean add(K k2) {
            this.l.add(this.to, k2);
            ++this.to;
            assert (this.assertRange());
            return true;
        }

        @Override
        public void add(int index, K k2) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k2);
            ++this.to;
            assert (this.assertRange());
        }

        @Override
        public boolean addAll(int index, Collection<? extends K> c2) {
            this.ensureIndex(index);
            this.to += c2.size();
            return this.l.addAll(this.from + index, c2);
        }

        @Override
        public K get(int index) {
            this.ensureRestrictedIndex(index);
            return (K)this.l.get(this.from + index);
        }

        @Override
        public K remove(int index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return (K)this.l.remove(this.from + index);
        }

        @Override
        public K set(int index, K k2) {
            this.ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k2);
        }

        @Override
        public int size() {
            return this.to - this.from;
        }

        @Override
        public void getElements(int from, Object[] a2, int offset, int length) {
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
        public void addElements(int index, K[] a2, int offset, int length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a2, offset, length);
            this.to += length;
            assert (this.assertRange());
        }

        @Override
        public void setElements(int index, K[] a2, int offset, int length) {
            this.ensureIndex(index);
            this.l.setElements(this.from + index, a2, offset, length);
            assert (this.assertRange());
        }

        @Override
        public ObjectListIterator<K> listIterator(int index) {
            this.ensureIndex(index);
            return this.l instanceof RandomAccess ? new RandomAccessIter(index) : new ParentWrappingIter(this.l.listIterator(index + this.from));
        }

        @Override
        public ObjectSpliterator<K> spliterator() {
            return this.l instanceof RandomAccess ? new IndexBasedSpliterator<K>(this.l, this.from, this.to) : super.spliterator();
        }

        @Override
        public ObjectList<K> subList(int from, int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new ObjectSubList<K>(this, from, to);
        }

        private final class RandomAccessIter
        extends ObjectIterators.AbstractIndexBasedListIterator<K> {
            RandomAccessIter(int pos) {
                super(0, pos);
            }

            @Override
            protected final K get(int i2) {
                return ObjectSubList.this.l.get(ObjectSubList.this.from + i2);
            }

            @Override
            protected final void add(int i2, K k2) {
                ObjectSubList.this.add(i2, k2);
            }

            @Override
            protected final void set(int i2, K k2) {
                ObjectSubList.this.set(i2, k2);
            }

            @Override
            protected final void remove(int i2) {
                ObjectSubList.this.remove(i2);
            }

            @Override
            protected final int getMaxPos() {
                return ObjectSubList.this.to - ObjectSubList.this.from;
            }

            @Override
            public void add(K k2) {
                super.add(k2);
                assert (ObjectSubList.this.assertRange());
            }

            @Override
            public void remove() {
                super.remove();
                assert (ObjectSubList.this.assertRange());
            }
        }

        private class ParentWrappingIter
        implements ObjectListIterator<K> {
            private ObjectListIterator<K> parent;

            ParentWrappingIter(ObjectListIterator<K> parent) {
                this.parent = parent;
            }

            @Override
            public int nextIndex() {
                return this.parent.nextIndex() - ObjectSubList.this.from;
            }

            @Override
            public int previousIndex() {
                return this.parent.previousIndex() - ObjectSubList.this.from;
            }

            @Override
            public boolean hasNext() {
                return this.parent.nextIndex() < ObjectSubList.this.to;
            }

            @Override
            public boolean hasPrevious() {
                return this.parent.previousIndex() >= ObjectSubList.this.from;
            }

            @Override
            public K next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return this.parent.next();
            }

            @Override
            public K previous() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                return this.parent.previous();
            }

            @Override
            public void add(K k2) {
                this.parent.add(k2);
            }

            @Override
            public void set(K k2) {
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
                if (parentNewPos < ObjectSubList.this.from - 1) {
                    parentNewPos = ObjectSubList.this.from - 1;
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
                if (parentNewPos > ObjectSubList.this.to) {
                    parentNewPos = ObjectSubList.this.to;
                }
                int toSkip = parentNewPos - currentPos;
                return this.parent.skip(toSkip);
            }
        }
    }

    static final class IndexBasedSpliterator<K>
    extends ObjectSpliterators.LateBindingSizeIndexBasedSpliterator<K> {
        final ObjectList<K> l;

        IndexBasedSpliterator(ObjectList<K> l2, int pos) {
            super(pos);
            this.l = l2;
        }

        IndexBasedSpliterator(ObjectList<K> l2, int pos, int maxPos) {
            super(pos, maxPos);
            this.l = l2;
        }

        @Override
        protected final int getMaxPosFromBackingStore() {
            return this.l.size();
        }

        @Override
        protected final K get(int i2) {
            return (K)this.l.get(i2);
        }

        @Override
        protected final IndexBasedSpliterator<K> makeForSplit(int pos, int maxPos) {
            return new IndexBasedSpliterator<K>(this.l, pos, maxPos);
        }
    }
}

