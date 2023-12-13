/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.libs.fastutil.objects.ObjectArrayList$SubList.SubListSpliterator
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.Arrays;
import com.viaversion.viaversion.libs.fastutil.SafeMath;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectList;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrayList;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrays;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollections;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterators;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectList;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectListIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterators;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.util.stream.Collector;

public class ObjectArrayList<K>
extends AbstractObjectList<K>
implements RandomAccess,
Cloneable,
Serializable {
    private static final long serialVersionUID = -7046029254386353131L;
    public static final int DEFAULT_INITIAL_CAPACITY = 10;
    protected final boolean wrapped;
    protected transient K[] a;
    protected int size;
    private static final Collector<Object, ?, ObjectArrayList<Object>> TO_LIST_COLLECTOR = Collector.of(ObjectArrayList::new, ObjectArrayList::add, ObjectArrayList::combine, new Collector.Characteristics[0]);

    private static final <K> K[] copyArraySafe(K[] a2, int length) {
        if (length == 0) {
            return ObjectArrays.EMPTY_ARRAY;
        }
        return java.util.Arrays.copyOf(a2, length, Object[].class);
    }

    private static final <K> K[] copyArrayFromSafe(ObjectArrayList<K> l2) {
        return ObjectArrayList.copyArraySafe(l2.a, l2.size);
    }

    protected ObjectArrayList(K[] a2, boolean wrapped) {
        this.a = a2;
        this.wrapped = wrapped;
    }

    private void initArrayFromCapacity(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        }
        this.a = capacity == 0 ? ObjectArrays.EMPTY_ARRAY : new Object[capacity];
    }

    public ObjectArrayList(int capacity) {
        this.initArrayFromCapacity(capacity);
        this.wrapped = false;
    }

    public ObjectArrayList() {
        this.a = ObjectArrays.DEFAULT_EMPTY_ARRAY;
        this.wrapped = false;
    }

    public ObjectArrayList(Collection<? extends K> c2) {
        if (c2 instanceof ObjectArrayList) {
            this.a = ObjectArrayList.copyArrayFromSafe((ObjectArrayList)c2);
            this.size = this.a.length;
        } else {
            this.initArrayFromCapacity(c2.size());
            if (c2 instanceof ObjectList) {
                this.size = c2.size();
                ((ObjectList)c2).getElements(0, this.a, 0, this.size);
            } else {
                this.size = ObjectIterators.unwrap(c2.iterator(), this.a);
            }
        }
        this.wrapped = false;
    }

    public ObjectArrayList(ObjectCollection<? extends K> c2) {
        if (c2 instanceof ObjectArrayList) {
            this.a = ObjectArrayList.copyArrayFromSafe((ObjectArrayList)c2);
            this.size = this.a.length;
        } else {
            this.initArrayFromCapacity(c2.size());
            if (c2 instanceof ObjectList) {
                this.size = c2.size();
                ((ObjectList)c2).getElements(0, this.a, 0, this.size);
            } else {
                this.size = ObjectIterators.unwrap(c2.iterator(), this.a);
            }
        }
        this.wrapped = false;
    }

    public ObjectArrayList(ObjectList<? extends K> l2) {
        if (l2 instanceof ObjectArrayList) {
            this.a = ObjectArrayList.copyArrayFromSafe((ObjectArrayList)l2);
            this.size = this.a.length;
        } else {
            this.initArrayFromCapacity(l2.size());
            this.size = l2.size();
            l2.getElements(0, this.a, 0, this.size);
        }
        this.wrapped = false;
    }

    public ObjectArrayList(K[] a2) {
        this(a2, 0, a2.length);
    }

    public ObjectArrayList(K[] a2, int offset, int length) {
        this(length);
        System.arraycopy(a2, offset, this.a, 0, length);
        this.size = length;
    }

    public ObjectArrayList(Iterator<? extends K> i2) {
        this();
        while (i2.hasNext()) {
            this.add(i2.next());
        }
    }

    public ObjectArrayList(ObjectIterator<? extends K> i2) {
        this();
        while (i2.hasNext()) {
            this.add((K)i2.next());
        }
    }

    public K[] elements() {
        return this.a;
    }

    public static <K> ObjectArrayList<K> wrap(K[] a2, int length) {
        if (length > a2.length) {
            throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + a2.length + ")");
        }
        ObjectArrayList<K> l2 = new ObjectArrayList<K>(a2, true);
        l2.size = length;
        return l2;
    }

    public static <K> ObjectArrayList<K> wrap(K[] a2) {
        return ObjectArrayList.wrap(a2, a2.length);
    }

    public static <K> ObjectArrayList<K> of() {
        return new ObjectArrayList<K>();
    }

    @SafeVarargs
    public static <K> ObjectArrayList<K> of(K ... init) {
        return ObjectArrayList.wrap(init);
    }

    ObjectArrayList<K> combine(ObjectArrayList<? extends K> toAddFrom) {
        this.addAll(toAddFrom);
        return this;
    }

    public static <K> Collector<K, ?, ObjectArrayList<K>> toList() {
        return TO_LIST_COLLECTOR;
    }

    public static <K> Collector<K, ?, ObjectArrayList<K>> toListWithExpectedSize(int expectedSize) {
        if (expectedSize <= 10) {
            return ObjectArrayList.toList();
        }
        return Collector.of(new ObjectCollections.SizeDecreasingSupplier(expectedSize, size -> size <= 10 ? new ObjectArrayList() : new ObjectArrayList(size)), ObjectArrayList::add, ObjectArrayList::combine, new Collector.Characteristics[0]);
    }

    public void ensureCapacity(int capacity) {
        if (capacity <= this.a.length || this.a == ObjectArrays.DEFAULT_EMPTY_ARRAY && capacity <= 10) {
            return;
        }
        if (this.wrapped) {
            this.a = ObjectArrays.ensureCapacity(this.a, capacity, this.size);
        } else if (capacity > this.a.length) {
            Object[] t2 = new Object[capacity];
            System.arraycopy(this.a, 0, t2, 0, this.size);
            this.a = t2;
        }
        assert (this.size <= this.a.length);
    }

    private void grow(int capacity) {
        if (capacity <= this.a.length) {
            return;
        }
        if (this.a != ObjectArrays.DEFAULT_EMPTY_ARRAY) {
            capacity = (int)Math.max(Math.min((long)this.a.length + (long)(this.a.length >> 1), 0x7FFFFFF7L), (long)capacity);
        } else if (capacity < 10) {
            capacity = 10;
        }
        if (this.wrapped) {
            this.a = ObjectArrays.forceCapacity(this.a, capacity, this.size);
        } else {
            Object[] t2 = new Object[capacity];
            System.arraycopy(this.a, 0, t2, 0, this.size);
            this.a = t2;
        }
        assert (this.size <= this.a.length);
    }

    @Override
    public void add(int index, K k2) {
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
    public boolean add(K k2) {
        this.grow(this.size + 1);
        this.a[this.size++] = k2;
        assert (this.size <= this.a.length);
        return true;
    }

    @Override
    public K get(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return this.a[index];
    }

    @Override
    public int indexOf(Object k2) {
        for (int i2 = 0; i2 < this.size; ++i2) {
            if (!Objects.equals(k2, this.a[i2])) continue;
            return i2;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object k2) {
        int i2 = this.size;
        while (i2-- != 0) {
            if (!Objects.equals(k2, this.a[i2])) continue;
            return i2;
        }
        return -1;
    }

    @Override
    public K remove(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        K old = this.a[index];
        --this.size;
        if (index != this.size) {
            System.arraycopy(this.a, index + 1, this.a, index, this.size - index);
        }
        this.a[this.size] = null;
        assert (this.size <= this.a.length);
        return old;
    }

    @Override
    public boolean remove(Object k2) {
        int index = this.indexOf(k2);
        if (index == -1) {
            return false;
        }
        this.remove(index);
        assert (this.size <= this.a.length);
        return true;
    }

    @Override
    public K set(int index, K k2) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        K old = this.a[index];
        this.a[index] = k2;
        return old;
    }

    @Override
    public void clear() {
        java.util.Arrays.fill(this.a, 0, this.size, null);
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
            this.a = ObjectArrays.forceCapacity(this.a, size, this.size);
        }
        if (size > this.size) {
            java.util.Arrays.fill(this.a, this.size, size, null);
        } else {
            java.util.Arrays.fill(this.a, size, this.size, null);
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
        Object[] t2 = new Object[Math.max(n2, this.size)];
        System.arraycopy(this.a, 0, t2, 0, this.size);
        this.a = t2;
        assert (this.size <= this.a.length);
    }

    @Override
    public ObjectList<K> subList(int from, int to) {
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
    public void getElements(int from, Object[] a2, int offset, int length) {
        ObjectArrays.ensureOffsetLength(a2, offset, length);
        System.arraycopy(this.a, from, a2, offset, length);
    }

    @Override
    public void removeElements(int from, int to) {
        Arrays.ensureFromTo(this.size, from, to);
        System.arraycopy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
        int i2 = to - from;
        while (i2-- != 0) {
            this.a[this.size + i2] = null;
        }
    }

    @Override
    public void addElements(int index, K[] a2, int offset, int length) {
        this.ensureIndex(index);
        ObjectArrays.ensureOffsetLength(a2, offset, length);
        this.grow(this.size + length);
        System.arraycopy(this.a, index, this.a, index + length, this.size - index);
        System.arraycopy(a2, offset, this.a, index, length);
        this.size += length;
    }

    @Override
    public void setElements(int index, K[] a2, int offset, int length) {
        this.ensureIndex(index);
        ObjectArrays.ensureOffsetLength(a2, offset, length);
        if (index + length > this.size) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size + ")");
        }
        System.arraycopy(a2, offset, this.a, index, length);
    }

    @Override
    public void forEach(Consumer<? super K> action) {
        for (int i2 = 0; i2 < this.size; ++i2) {
            action.accept(this.a[i2]);
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends K> c2) {
        if (c2 instanceof ObjectList) {
            return this.addAll(index, (ObjectList)c2);
        }
        this.ensureIndex(index);
        int n2 = c2.size();
        if (n2 == 0) {
            return false;
        }
        this.grow(this.size + n2);
        System.arraycopy(this.a, index, this.a, index + n2, this.size - index);
        Iterator<K> i2 = c2.iterator();
        this.size += n2;
        while (n2-- != 0) {
            this.a[index++] = i2.next();
        }
        assert (this.size <= this.a.length);
        return true;
    }

    @Override
    public boolean addAll(int index, ObjectList<? extends K> l2) {
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
    public boolean removeAll(Collection<?> c2) {
        Object[] a2 = this.a;
        int j2 = 0;
        for (int i2 = 0; i2 < this.size; ++i2) {
            if (c2.contains(a2[i2])) continue;
            a2[j2++] = a2[i2];
        }
        java.util.Arrays.fill(a2, j2, this.size, null);
        boolean modified = this.size != j2;
        this.size = j2;
        return modified;
    }

    @Override
    public Object[] toArray() {
        return java.util.Arrays.copyOf(this.a, this.size(), Object[].class);
    }

    @Override
    public <K> K[] toArray(K[] a2) {
        if (a2 == null) {
            a2 = new Object[this.size()];
        } else if (a2.length < this.size()) {
            a2 = (Object[])Array.newInstance(a2.getClass().getComponentType(), this.size());
        }
        System.arraycopy(this.a, 0, a2, 0, this.size());
        if (a2.length > this.size()) {
            a2[this.size()] = null;
        }
        return a2;
    }

    @Override
    public ObjectListIterator<K> listIterator(final int index) {
        this.ensureIndex(index);
        return new ObjectListIterator<K>(){
            int pos;
            int last;
            {
                this.pos = index;
                this.last = -1;
            }

            @Override
            public boolean hasNext() {
                return this.pos < ObjectArrayList.this.size;
            }

            @Override
            public boolean hasPrevious() {
                return this.pos > 0;
            }

            @Override
            public K next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                this.last = this.pos++;
                return ObjectArrayList.this.a[this.last];
            }

            @Override
            public K previous() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                this.last = --this.pos;
                return ObjectArrayList.this.a[this.pos];
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
            public void add(K k2) {
                ObjectArrayList.this.add(this.pos++, k2);
                this.last = -1;
            }

            @Override
            public void set(K k2) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                ObjectArrayList.this.set(this.last, k2);
            }

            @Override
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                ObjectArrayList.this.remove(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }

            @Override
            public void forEachRemaining(Consumer<? super K> action) {
                while (this.pos < ObjectArrayList.this.size) {
                    ++this.pos;
                    this.last = this.last;
                    action.accept(ObjectArrayList.this.a[this.last]);
                }
            }

            @Override
            public int back(int n2) {
                if (n2 < 0) {
                    throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
                }
                int remaining = ObjectArrayList.this.size - this.pos;
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
                int remaining = ObjectArrayList.this.size - this.pos;
                if (n2 < remaining) {
                    this.pos += n2;
                } else {
                    n2 = remaining;
                    this.pos = ObjectArrayList.this.size;
                }
                this.last = this.pos - 1;
                return n2;
            }
        };
    }

    @Override
    public ObjectSpliterator<K> spliterator() {
        return new Spliterator();
    }

    @Override
    public void sort(Comparator<? super K> comp) {
        if (comp == null) {
            ObjectArrays.stableSort(this.a, 0, this.size);
        } else {
            ObjectArrays.stableSort(this.a, 0, this.size, comp);
        }
    }

    @Override
    public void unstableSort(Comparator<? super K> comp) {
        if (comp == null) {
            ObjectArrays.unstableSort(this.a, 0, this.size);
        } else {
            ObjectArrays.unstableSort(this.a, 0, this.size, comp);
        }
    }

    public ObjectArrayList<K> clone() {
        ObjectArrayList<K> cloned = null;
        if (this.getClass() == ObjectArrayList.class) {
            cloned = new ObjectArrayList<K>(ObjectArrayList.copyArraySafe(this.a, this.size), false);
            cloned.size = this.size;
        } else {
            try {
                cloned = (ObjectArrayList<K>)super.clone();
            }
            catch (CloneNotSupportedException err) {
                throw new InternalError(err);
            }
            cloned.a = ObjectArrayList.copyArraySafe(this.a, this.size);
        }
        return cloned;
    }

    public boolean equals(ObjectArrayList<K> l2) {
        if (l2 == this) {
            return true;
        }
        int s2 = this.size();
        if (s2 != l2.size()) {
            return false;
        }
        K[] a1 = this.a;
        K[] a2 = l2.a;
        if (a1 == a2 && s2 == l2.size()) {
            return true;
        }
        while (s2-- != 0) {
            if (Objects.equals(a1[s2], a2[s2])) continue;
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
        if (o2 instanceof ObjectArrayList) {
            return this.equals((ObjectArrayList)o2);
        }
        if (o2 instanceof SubList) {
            return ((SubList)o2).equals(this);
        }
        return super.equals(o2);
    }

    @Override
    public int compareTo(ObjectArrayList<? extends K> l2) {
        int i2;
        int s1 = this.size();
        int s2 = l2.size();
        K[] a1 = this.a;
        K[] a2 = l2.a;
        for (i2 = 0; i2 < s1 && i2 < s2; ++i2) {
            K e1 = a1[i2];
            K e2 = a2[i2];
            int r2 = ((Comparable)e1).compareTo(e2);
            if (r2 == 0) continue;
            return r2;
        }
        return i2 < s2 ? -1 : (i2 < s1 ? 1 : 0);
    }

    @Override
    public int compareTo(List<? extends K> l2) {
        if (l2 instanceof ObjectArrayList) {
            return this.compareTo((ObjectArrayList)l2);
        }
        if (l2 instanceof SubList) {
            return -((SubList)l2).compareTo(this);
        }
        return super.compareTo(l2);
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

    private class SubList
    extends AbstractObjectList.ObjectRandomAccessSubList<K> {
        private static final long serialVersionUID = -3185226345314976296L;

        protected SubList(int from, int to) {
            super(ObjectArrayList.this, from, to);
        }

        private K[] getParentArray() {
            return ObjectArrayList.this.a;
        }

        @Override
        public K get(int i2) {
            this.ensureRestrictedIndex(i2);
            return ObjectArrayList.this.a[i2 + this.from];
        }

        @Override
        public ObjectListIterator<K> listIterator(int index) {
            return new SubListIterator(index);
        }

        @Override
        public ObjectSpliterator<K> spliterator() {
            return new SubListSpliterator();
        }

        boolean contentsEquals(K[] otherA, int otherAFrom, int otherATo) {
            if (ObjectArrayList.this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return true;
            }
            if (otherATo - otherAFrom != this.size()) {
                return false;
            }
            int pos = this.from;
            int otherPos = otherAFrom;
            while (pos < this.to) {
                if (Objects.equals(ObjectArrayList.this.a[pos++], otherA[otherPos++])) continue;
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
            if (o2 instanceof ObjectArrayList) {
                ObjectArrayList other = (ObjectArrayList)o2;
                return this.contentsEquals(other.a, 0, other.size());
            }
            if (o2 instanceof SubList) {
                SubList other = (SubList)o2;
                return this.contentsEquals(other.getParentArray(), other.from, other.to);
            }
            return super.equals(o2);
        }

        int contentsCompareTo(K[] otherA, int otherAFrom, int otherATo) {
            int i2 = this.from;
            int j2 = otherAFrom;
            while (i2 < this.to && i2 < otherATo) {
                Object e1 = ObjectArrayList.this.a[i2];
                Object e2 = otherA[j2];
                int r2 = ((Comparable)e1).compareTo(e2);
                if (r2 != 0) {
                    return r2;
                }
                ++i2;
                ++j2;
            }
            return i2 < otherATo ? -1 : (i2 < this.to ? 1 : 0);
        }

        @Override
        public int compareTo(List<? extends K> l2) {
            if (l2 instanceof ObjectArrayList) {
                ObjectArrayList other = (ObjectArrayList)l2;
                return this.contentsCompareTo(other.a, 0, other.size());
            }
            if (l2 instanceof SubList) {
                SubList other = (SubList)l2;
                return this.contentsCompareTo(other.getParentArray(), other.from, other.to);
            }
            return super.compareTo(l2);
        }

        private final class SubListIterator
        extends ObjectIterators.AbstractIndexBasedListIterator<K> {
            SubListIterator(int index) {
                super(0, index);
            }

            @Override
            protected final K get(int i2) {
                return ObjectArrayList.this.a[SubList.this.from + i2];
            }

            @Override
            protected final void add(int i2, K k2) {
                SubList.this.add(i2, k2);
            }

            @Override
            protected final void set(int i2, K k2) {
                SubList.this.set(i2, k2);
            }

            @Override
            protected final void remove(int i2) {
                SubList.this.remove(i2);
            }

            @Override
            protected final int getMaxPos() {
                return SubList.this.to - SubList.this.from;
            }

            @Override
            public K next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                this.lastReturned = this.pos++;
                return ObjectArrayList.this.a[SubList.this.from + this.lastReturned];
            }

            @Override
            public K previous() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                this.lastReturned = --this.pos;
                return ObjectArrayList.this.a[SubList.this.from + this.pos];
            }

            @Override
            public void forEachRemaining(Consumer<? super K> action) {
                int max = SubList.this.to - SubList.this.from;
                while (this.pos < max) {
                    ++this.pos;
                    this.lastReturned = this.lastReturned;
                    action.accept(ObjectArrayList.this.a[SubList.this.from + this.lastReturned]);
                }
            }
        }

        private final class SubListSpliterator
        extends ObjectSpliterators.LateBindingSizeIndexBasedSpliterator<K> {
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
            protected final K get(int i2) {
                return ObjectArrayList.this.a[i2];
            }

            protected final com.viaversion.viaversion.libs.fastutil.objects.ObjectArrayList$SubList.SubListSpliterator makeForSplit(int pos, int maxPos) {
                return new SubListSpliterator(pos, maxPos);
            }

            @Override
            public boolean tryAdvance(Consumer<? super K> action) {
                if (this.pos >= this.getMaxPos()) {
                    return false;
                }
                action.accept(ObjectArrayList.this.a[this.pos++]);
                return true;
            }

            @Override
            public void forEachRemaining(Consumer<? super K> action) {
                int max = this.getMaxPos();
                while (this.pos < max) {
                    action.accept(ObjectArrayList.this.a[this.pos++]);
                }
            }
        }
    }

    private final class Spliterator
    implements ObjectSpliterator<K> {
        boolean hasSplit = false;
        int pos;
        int max;

        public Spliterator() {
            this(0, objectArrayList.size, false);
        }

        private Spliterator(int pos, int max, boolean hasSplit) {
            assert (pos <= max) : "pos " + pos + " must be <= max " + max;
            this.pos = pos;
            this.max = max;
            this.hasSplit = hasSplit;
        }

        private int getWorkingMax() {
            return this.hasSplit ? this.max : ObjectArrayList.this.size;
        }

        @Override
        public int characteristics() {
            return 16464;
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
            action.accept(ObjectArrayList.this.a[this.pos++]);
            return true;
        }

        @Override
        public void forEachRemaining(Consumer<? super K> action) {
            int max = this.getWorkingMax();
            while (this.pos < max) {
                action.accept(ObjectArrayList.this.a[this.pos]);
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

