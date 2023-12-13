/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.libs.fastutil.ints.IntLists$SynchronizedList
 *  com.viaversion.viaversion.libs.fastutil.ints.IntLists$SynchronizedRandomAccessList
 *  com.viaversion.viaversion.libs.fastutil.ints.IntLists$UnmodifiableList
 *  com.viaversion.viaversion.libs.fastutil.ints.IntLists$UnmodifiableRandomAccessList
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntList;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollections;
import com.viaversion.viaversion.libs.fastutil.ints.IntComparator;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterators;
import com.viaversion.viaversion.libs.fastutil.ints.IntList;
import com.viaversion.viaversion.libs.fastutil.ints.IntListIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntLists;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterators;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public final class IntLists {
    public static final EmptyList EMPTY_LIST = new EmptyList();

    private IntLists() {
    }

    public static IntList shuffle(IntList l2, Random random) {
        int i2 = l2.size();
        while (i2-- != 0) {
            int p2 = random.nextInt(i2 + 1);
            int t2 = l2.getInt(i2);
            l2.set(i2, l2.getInt(p2));
            l2.set(p2, t2);
        }
        return l2;
    }

    public static IntList emptyList() {
        return EMPTY_LIST;
    }

    public static IntList singleton(int element) {
        return new Singleton(element);
    }

    public static IntList singleton(Object element) {
        return new Singleton((Integer)element);
    }

    public static IntList synchronize(IntList l2) {
        return l2 instanceof RandomAccess ? new SynchronizedRandomAccessList(l2) : new SynchronizedList(l2);
    }

    public static IntList synchronize(IntList l2, Object sync) {
        return l2 instanceof RandomAccess ? new SynchronizedRandomAccessList(l2, sync) : new SynchronizedList(l2, sync);
    }

    public static IntList unmodifiable(IntList l2) {
        return l2 instanceof RandomAccess ? new UnmodifiableRandomAccessList(l2) : new UnmodifiableList(l2);
    }

    public static class EmptyList
    extends IntCollections.EmptyCollection
    implements IntList,
    RandomAccess,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptyList() {
        }

        @Override
        public int getInt(int i2) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public boolean rem(int k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int removeInt(int i2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(int index, int k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int set(int index, int k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int indexOf(int k2) {
            return -1;
        }

        @Override
        public int lastIndexOf(int k2) {
            return -1;
        }

        @Override
        public boolean addAll(int i2, Collection<? extends Integer> c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public void replaceAll(UnaryOperator<Integer> operator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void replaceAll(IntUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(IntList c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(int i2, IntCollection c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(int i2, IntList c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public void add(int index, Integer k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public Integer get(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public boolean add(Integer k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public Integer set(int index, Integer k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public Integer remove(int k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public int indexOf(Object k2) {
            return -1;
        }

        @Override
        @Deprecated
        public int lastIndexOf(Object k2) {
            return -1;
        }

        @Override
        public void sort(IntComparator comparator) {
        }

        @Override
        public void unstableSort(IntComparator comparator) {
        }

        @Override
        @Deprecated
        public void sort(Comparator<? super Integer> comparator) {
        }

        @Override
        @Deprecated
        public void unstableSort(Comparator<? super Integer> comparator) {
        }

        @Override
        public IntListIterator listIterator() {
            return IntIterators.EMPTY_ITERATOR;
        }

        @Override
        public IntListIterator iterator() {
            return IntIterators.EMPTY_ITERATOR;
        }

        @Override
        public IntListIterator listIterator(int i2) {
            if (i2 == 0) {
                return IntIterators.EMPTY_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i2));
        }

        @Override
        public IntList subList(int from, int to) {
            if (from == 0 && to == 0) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }

        @Override
        public void getElements(int from, int[] a2, int offset, int length) {
            if (from == 0 && length == 0 && offset >= 0 && offset <= a2.length) {
                return;
            }
            throw new IndexOutOfBoundsException();
        }

        @Override
        public void removeElements(int from, int to) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addElements(int index, int[] a2, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addElements(int index, int[] a2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setElements(int[] a2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setElements(int index, int[] a2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setElements(int index, int[] a2, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void size(int s2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int compareTo(List<? extends Integer> o2) {
            if (o2 == this) {
                return 0;
            }
            return o2.isEmpty() ? 0 : -1;
        }

        public Object clone() {
            return EMPTY_LIST;
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public boolean equals(Object o2) {
            return o2 instanceof List && ((List)o2).isEmpty();
        }

        @Override
        public String toString() {
            return "[]";
        }

        private Object readResolve() {
            return EMPTY_LIST;
        }
    }

    public static class Singleton
    extends AbstractIntList
    implements RandomAccess,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        private final int element;

        protected Singleton(int element) {
            this.element = element;
        }

        @Override
        public int getInt(int i2) {
            if (i2 == 0) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }

        @Override
        public boolean rem(int k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int removeInt(int i2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean contains(int k2) {
            return k2 == this.element;
        }

        @Override
        public int indexOf(int k2) {
            return k2 == this.element ? 0 : -1;
        }

        @Override
        public int[] toIntArray() {
            return new int[]{this.element};
        }

        @Override
        public IntListIterator listIterator() {
            return IntIterators.singleton(this.element);
        }

        @Override
        public IntListIterator iterator() {
            return this.listIterator();
        }

        @Override
        public IntSpliterator spliterator() {
            return IntSpliterators.singleton(this.element);
        }

        @Override
        public IntListIterator listIterator(int i2) {
            if (i2 > 1 || i2 < 0) {
                throw new IndexOutOfBoundsException();
            }
            IntListIterator l2 = this.listIterator();
            if (i2 == 1) {
                l2.nextInt();
            }
            return l2;
        }

        @Override
        public IntList subList(int from, int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            if (from != 0 || to != 1) {
                return EMPTY_LIST;
            }
            return this;
        }

        @Override
        @Deprecated
        public void forEach(Consumer<? super Integer> action) {
            action.accept((Integer)this.element);
        }

        @Override
        public boolean addAll(int i2, Collection<? extends Integer> c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends Integer> c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public boolean removeIf(Predicate<? super Integer> filter) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public void replaceAll(UnaryOperator<Integer> operator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void replaceAll(IntUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void forEach(IntConsumer action) {
            action.accept(this.element);
        }

        @Override
        public boolean addAll(IntList c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(int i2, IntList c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(int i2, IntCollection c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(IntCollection c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(IntCollection c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(IntCollection c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeIf(IntPredicate filter) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public Object[] toArray() {
            return new Object[]{this.element};
        }

        @Override
        public void sort(IntComparator comparator) {
        }

        @Override
        public void unstableSort(IntComparator comparator) {
        }

        @Override
        @Deprecated
        public void sort(Comparator<? super Integer> comparator) {
        }

        @Override
        @Deprecated
        public void unstableSort(Comparator<? super Integer> comparator) {
        }

        @Override
        public void getElements(int from, int[] a2, int offset, int length) {
            if (offset < 0) {
                throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
            }
            if (offset + length > a2.length) {
                throw new ArrayIndexOutOfBoundsException("End index (" + (offset + length) + ") is greater than array length (" + a2.length + ")");
            }
            if (from + length > this.size()) {
                throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + this.size() + ")");
            }
            if (length <= 0) {
                return;
            }
            a2[offset] = this.element;
        }

        @Override
        public void removeElements(int from, int to) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addElements(int index, int[] a2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addElements(int index, int[] a2, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setElements(int[] a2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setElements(int index, int[] a2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setElements(int index, int[] a2, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public void size(int size) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        public Object clone() {
            return this;
        }
    }

    static abstract class ImmutableListBase
    extends AbstractIntList
    implements IntList {
        ImmutableListBase() {
        }

        @Override
        @Deprecated
        public final void add(int index, int k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean add(int k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean addAll(Collection<? extends Integer> c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean addAll(int index, Collection<? extends Integer> c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final int removeInt(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean rem(int k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean removeAll(Collection<?> c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean retainAll(Collection<?> c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean removeIf(Predicate<? super Integer> c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean removeIf(IntPredicate c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final void replaceAll(UnaryOperator<Integer> operator) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final void replaceAll(IntUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final void add(int index, Integer k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean add(Integer k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final Integer remove(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean remove(Object k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final Integer set(int index, Integer k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean addAll(IntCollection c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean addAll(IntList c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean addAll(int index, IntCollection c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean addAll(int index, IntList c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean removeAll(IntCollection c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean retainAll(IntCollection c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final int set(int index, int k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final void size(int size) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final void removeElements(int from, int to) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final void addElements(int index, int[] a2, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final void setElements(int index, int[] a2, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final void sort(IntComparator comp) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final void unstableSort(IntComparator comp) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final void sort(Comparator<? super Integer> comparator) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final void unstableSort(Comparator<? super Integer> comparator) {
            throw new UnsupportedOperationException();
        }
    }
}

