/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.SafeMath;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrayList;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrays;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollections;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterators;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectList;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectListIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectLists;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterators;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.util.stream.Collector;

public class ObjectImmutableList<K>
extends ObjectLists.ImmutableListBase<K>
implements ObjectList<K>,
RandomAccess,
Cloneable,
Serializable {
    private static final long serialVersionUID = 0L;
    static final ObjectImmutableList EMPTY = new ObjectImmutableList<Object>(ObjectArrays.EMPTY_ARRAY);
    private final K[] a;
    private static final Collector<Object, ?, ObjectImmutableList<Object>> TO_LIST_COLLECTOR = Collector.of(ObjectArrayList::new, ObjectArrayList::add, ObjectArrayList::combine, ObjectImmutableList::convertTrustedToImmutableList, new Collector.Characteristics[0]);

    private static final <K> K[] emptyArray() {
        return ObjectArrays.EMPTY_ARRAY;
    }

    public ObjectImmutableList(K[] a2) {
        this.a = a2;
    }

    public ObjectImmutableList(Collection<? extends K> c2) {
        this(c2.isEmpty() ? ObjectImmutableList.emptyArray() : ObjectIterators.unwrap(c2.iterator()));
    }

    public ObjectImmutableList(ObjectCollection<? extends K> c2) {
        this(c2.isEmpty() ? ObjectImmutableList.emptyArray() : ObjectIterators.unwrap(c2.iterator()));
    }

    public ObjectImmutableList(ObjectList<? extends K> l2) {
        this(l2.isEmpty() ? ObjectImmutableList.emptyArray() : new Object[l2.size()]);
        l2.getElements(0, this.a, 0, l2.size());
    }

    public ObjectImmutableList(K[] a2, int offset, int length) {
        this(length == 0 ? ObjectImmutableList.emptyArray() : new Object[length]);
        System.arraycopy(a2, offset, this.a, 0, length);
    }

    public ObjectImmutableList(ObjectIterator<? extends K> i2) {
        this(i2.hasNext() ? ObjectIterators.unwrap(i2) : ObjectImmutableList.emptyArray());
    }

    public static <K> ObjectImmutableList<K> of() {
        return EMPTY;
    }

    @SafeVarargs
    public static <K> ObjectImmutableList<K> of(K ... init) {
        return init.length == 0 ? ObjectImmutableList.of() : new ObjectImmutableList<K>(init);
    }

    private static <K> ObjectImmutableList<K> convertTrustedToImmutableList(ObjectArrayList<K> arrayList) {
        if (arrayList.isEmpty()) {
            return ObjectImmutableList.of();
        }
        K[] backingArray = arrayList.elements();
        if (arrayList.size() != backingArray.length) {
            backingArray = Arrays.copyOf(backingArray, arrayList.size());
        }
        return new ObjectImmutableList<K>(backingArray);
    }

    public static <K> Collector<K, ?, ObjectImmutableList<K>> toList() {
        return TO_LIST_COLLECTOR;
    }

    public static <K> Collector<K, ?, ObjectImmutableList<K>> toListWithExpectedSize(int expectedSize) {
        if (expectedSize <= 10) {
            return ObjectImmutableList.toList();
        }
        return Collector.of(new ObjectCollections.SizeDecreasingSupplier(expectedSize, size -> size <= 10 ? new ObjectArrayList() : new ObjectArrayList(size)), ObjectArrayList::add, ObjectArrayList::combine, ObjectImmutableList::convertTrustedToImmutableList, new Collector.Characteristics[0]);
    }

    @Override
    public K get(int index) {
        if (index >= this.a.length) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.a.length + ")");
        }
        return this.a[index];
    }

    @Override
    public int indexOf(Object k2) {
        int size = this.a.length;
        for (int i2 = 0; i2 < size; ++i2) {
            if (!Objects.equals(k2, this.a[i2])) continue;
            return i2;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object k2) {
        int i2 = this.a.length;
        while (i2-- != 0) {
            if (!Objects.equals(k2, this.a[i2])) continue;
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
    public void getElements(int from, Object[] a2, int offset, int length) {
        ObjectArrays.ensureOffsetLength(a2, offset, length);
        System.arraycopy(this.a, from, a2, offset, length);
    }

    @Override
    public void forEach(Consumer<? super K> action) {
        for (int i2 = 0; i2 < this.a.length; ++i2) {
            action.accept(this.a[i2]);
        }
    }

    @Override
    public Object[] toArray() {
        if (this.a.getClass().equals(Object[].class)) {
            return (Object[])this.a.clone();
        }
        return Arrays.copyOf(this.a, this.a.length, Object[].class);
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
            {
                this.pos = index;
            }

            @Override
            public boolean hasNext() {
                return this.pos < ObjectImmutableList.this.a.length;
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
                return ObjectImmutableList.this.a[this.pos++];
            }

            @Override
            public K previous() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                return ObjectImmutableList.this.a[--this.pos];
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
            public void forEachRemaining(Consumer<? super K> action) {
                while (this.pos < ObjectImmutableList.this.a.length) {
                    action.accept(ObjectImmutableList.this.a[this.pos++]);
                }
            }

            @Override
            public void add(K k2) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void set(K k2) {
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
                int remaining = ObjectImmutableList.this.a.length - this.pos;
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
                int remaining = ObjectImmutableList.this.a.length - this.pos;
                if (n2 < remaining) {
                    this.pos += n2;
                } else {
                    n2 = remaining;
                    this.pos = ObjectImmutableList.this.a.length;
                }
                return n2;
            }
        };
    }

    @Override
    public ObjectSpliterator<K> spliterator() {
        return new Spliterator();
    }

    @Override
    public ObjectList<K> subList(int from, int to) {
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

    public ObjectImmutableList<K> clone() {
        return this;
    }

    public boolean equals(ObjectImmutableList<K> l2) {
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
        Object[] a1 = this.a;
        Object[] a2 = l2.a;
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
        if (o2 instanceof ObjectImmutableList) {
            return this.equals((ObjectImmutableList)o2);
        }
        if (o2 instanceof ImmutableSubList) {
            return ((ImmutableSubList)o2).equals(this);
        }
        return super.equals(o2);
    }

    @Override
    public int compareTo(ObjectImmutableList<? extends K> l2) {
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
        if (l2 instanceof ObjectImmutableList) {
            return this.compareTo((ObjectImmutableList)l2);
        }
        if (l2 instanceof ImmutableSubList) {
            ImmutableSubList other = (ImmutableSubList)l2;
            return -other.compareTo(this);
        }
        return super.compareTo(l2);
    }

    private final class Spliterator
    implements ObjectSpliterator<K> {
        int pos;
        int max;

        public Spliterator() {
            this(0, objectImmutableList.a.length);
        }

        private Spliterator(int pos, int max) {
            assert (pos <= max) : "pos " + pos + " must be <= max " + max;
            this.pos = pos;
            this.max = max;
        }

        @Override
        public int characteristics() {
            return 17488;
        }

        @Override
        public long estimateSize() {
            return this.max - this.pos;
        }

        @Override
        public boolean tryAdvance(Consumer<? super K> action) {
            if (this.pos >= this.max) {
                return false;
            }
            action.accept(ObjectImmutableList.this.a[this.pos++]);
            return true;
        }

        @Override
        public void forEachRemaining(Consumer<? super K> action) {
            while (this.pos < this.max) {
                action.accept(ObjectImmutableList.this.a[this.pos]);
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
        public ObjectSpliterator<K> trySplit() {
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

    private static final class ImmutableSubList<K>
    extends ObjectLists.ImmutableListBase<K>
    implements RandomAccess,
    Serializable {
        private static final long serialVersionUID = 7054639518438982401L;
        final ObjectImmutableList<K> innerList;
        final int from;
        final int to;
        final transient K[] a;

        ImmutableSubList(ObjectImmutableList<K> innerList, int from, int to) {
            this.innerList = innerList;
            this.from = from;
            this.to = to;
            this.a = ((ObjectImmutableList)innerList).a;
        }

        @Override
        public K get(int index) {
            this.ensureRestrictedIndex(index);
            return this.a[index + this.from];
        }

        @Override
        public int indexOf(Object k2) {
            for (int i2 = this.from; i2 < this.to; ++i2) {
                if (!Objects.equals(k2, this.a[i2])) continue;
                return i2 - this.from;
            }
            return -1;
        }

        @Override
        public int lastIndexOf(Object k2) {
            int i2 = this.to;
            while (i2-- != this.from) {
                if (!Objects.equals(k2, this.a[i2])) continue;
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
        public void getElements(int fromSublistIndex, Object[] a2, int offset, int length) {
            ObjectArrays.ensureOffsetLength(a2, offset, length);
            this.ensureRestrictedIndex(fromSublistIndex);
            if (this.from + length > this.to) {
                throw new IndexOutOfBoundsException("Final index " + (this.from + length) + " (startingIndex: " + this.from + " + length: " + length + ") is greater then list length " + this.size());
            }
            System.arraycopy(this.a, fromSublistIndex + this.from, a2, offset, length);
        }

        @Override
        public void forEach(Consumer<? super K> action) {
            for (int i2 = this.from; i2 < this.to; ++i2) {
                action.accept(this.a[i2]);
            }
        }

        @Override
        public Object[] toArray() {
            return Arrays.copyOfRange(this.a, this.from, this.to, Object[].class);
        }

        @Override
        public <K> K[] toArray(K[] a2) {
            int size = this.size();
            if (a2 == null) {
                a2 = new Object[size];
            } else if (a2.length < size) {
                a2 = (Object[])Array.newInstance(a2.getClass().getComponentType(), size);
            }
            System.arraycopy(this.a, this.from, a2, 0, size);
            if (a2.length > size) {
                a2[size] = null;
            }
            return a2;
        }

        @Override
        public ObjectListIterator<K> listIterator(final int index) {
            this.ensureIndex(index);
            return new ObjectListIterator<K>(){
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
                public K next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return a[this.pos++ + from];
                }

                @Override
                public K previous() {
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
                public void forEachRemaining(Consumer<? super K> action) {
                    while (this.pos < to) {
                        action.accept(a[this.pos++ + from]);
                    }
                }

                @Override
                public void add(K k2) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void set(K k2) {
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
        public ObjectSpliterator<K> spliterator() {
            return new SubListSpliterator();
        }

        boolean contentsEquals(K[] otherA, int otherAFrom, int otherATo) {
            if (this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
                return true;
            }
            if (otherATo - otherAFrom != this.size()) {
                return false;
            }
            int pos = this.from;
            int otherPos = otherAFrom;
            while (pos < this.to) {
                if (Objects.equals(this.a[pos++], otherA[otherPos++])) continue;
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
            if (o2 instanceof ObjectImmutableList) {
                ObjectImmutableList other = (ObjectImmutableList)o2;
                return this.contentsEquals(other.a, 0, other.size());
            }
            if (o2 instanceof ImmutableSubList) {
                ImmutableSubList other = (ImmutableSubList)o2;
                return this.contentsEquals(other.a, other.from, other.to);
            }
            return super.equals(o2);
        }

        int contentsCompareTo(K[] otherA, int otherAFrom, int otherATo) {
            int i2 = this.from;
            int j2 = otherAFrom;
            while (i2 < this.to && i2 < otherATo) {
                K e1 = this.a[i2];
                K e2 = otherA[j2];
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
            if (l2 instanceof ObjectImmutableList) {
                ObjectImmutableList other = (ObjectImmutableList)l2;
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
        public ObjectList<K> subList(int from, int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from == to) {
                return EMPTY;
            }
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new ImmutableSubList<K>(this.innerList, from + this.from, to + this.from);
        }

        private final class SubListSpliterator
        extends ObjectSpliterators.EarlyBindingSizeIndexBasedSpliterator<K> {
            SubListSpliterator() {
                super(ImmutableSubList.this.from, ImmutableSubList.this.to);
            }

            private SubListSpliterator(int pos, int maxPos) {
                super(pos, maxPos);
            }

            @Override
            protected final K get(int i2) {
                return ImmutableSubList.this.a[i2];
            }

            protected final SubListSpliterator makeForSplit(int pos, int maxPos) {
                return new SubListSpliterator(pos, maxPos);
            }

            @Override
            public boolean tryAdvance(Consumer<? super K> action) {
                if (this.pos >= this.maxPos) {
                    return false;
                }
                action.accept(ImmutableSubList.this.a[this.pos++]);
                return true;
            }

            @Override
            public void forEachRemaining(Consumer<? super K> action) {
                int max = this.maxPos;
                while (this.pos < max) {
                    action.accept(ImmutableSubList.this.a[this.pos++]);
                }
            }

            @Override
            public int characteristics() {
                return 17488;
            }
        }
    }
}

