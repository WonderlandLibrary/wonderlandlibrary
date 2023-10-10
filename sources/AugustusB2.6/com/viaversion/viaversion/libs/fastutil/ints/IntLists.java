// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Spliterator;
import java.util.function.IntPredicate;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.function.Consumer;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Comparator;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;
import java.util.Collection;
import java.io.Serializable;
import java.util.RandomAccess;
import java.util.Random;

public final class IntLists
{
    public static final EmptyList EMPTY_LIST;
    
    private IntLists() {
    }
    
    public static IntList shuffle(final IntList l, final Random random) {
        int i = l.size();
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final int t = l.getInt(i);
            l.set(i, l.getInt(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static IntList emptyList() {
        return IntLists.EMPTY_LIST;
    }
    
    public static IntList singleton(final int element) {
        return new Singleton(element);
    }
    
    public static IntList singleton(final Object element) {
        return new Singleton((int)element);
    }
    
    public static IntList synchronize(final IntList l) {
        return (IntList)((l instanceof RandomAccess) ? new IntLists.SynchronizedRandomAccessList(l) : new IntLists.SynchronizedList(l));
    }
    
    public static IntList synchronize(final IntList l, final Object sync) {
        return (IntList)((l instanceof RandomAccess) ? new IntLists.SynchronizedRandomAccessList(l, sync) : new IntLists.SynchronizedList(l, sync));
    }
    
    public static IntList unmodifiable(final IntList l) {
        return (IntList)((l instanceof RandomAccess) ? new IntLists.UnmodifiableRandomAccessList(l) : new IntLists.UnmodifiableList(l));
    }
    
    static {
        EMPTY_LIST = new EmptyList();
    }
    
    public static class EmptyList extends IntCollections.EmptyCollection implements IntList, RandomAccess, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyList() {
        }
        
        @Override
        public int getInt(final int i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean rem(final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int removeInt(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int index, final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int set(final int index, final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final int k) {
            return -1;
        }
        
        @Override
        public int lastIndexOf(final int k) {
            return -1;
        }
        
        @Override
        public boolean addAll(final int i, final Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public void replaceAll(final UnaryOperator<Integer> operator) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void replaceAll(final IntUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final IntList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final IntList c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public void add(final int index, final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer get(final int index) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean add(final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer set(final int index, final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer remove(final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public int indexOf(final Object k) {
            return -1;
        }
        
        @Deprecated
        @Override
        public int lastIndexOf(final Object k) {
            return -1;
        }
        
        @Override
        public void sort(final IntComparator comparator) {
        }
        
        @Override
        public void unstableSort(final IntComparator comparator) {
        }
        
        @Deprecated
        @Override
        public void sort(final Comparator<? super Integer> comparator) {
        }
        
        @Deprecated
        @Override
        public void unstableSort(final Comparator<? super Integer> comparator) {
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
        public IntListIterator listIterator(final int i) {
            if (i == 0) {
                return IntIterators.EMPTY_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Override
        public IntList subList(final int from, final int to) {
            if (from == 0 && to == 0) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void getElements(final int from, final int[] a, final int offset, final int length) {
            if (from == 0 && length == 0 && offset >= 0 && offset <= a.length) {
                return;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final int[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final int[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setElements(final int[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setElements(final int index, final int[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setElements(final int index, final int[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int s) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int compareTo(final List<? extends Integer> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        public Object clone() {
            return IntLists.EMPTY_LIST;
        }
        
        @Override
        public int hashCode() {
            return 1;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof List && ((List)o).isEmpty();
        }
        
        @Override
        public String toString() {
            return "[]";
        }
        
        private Object readResolve() {
            return IntLists.EMPTY_LIST;
        }
    }
    
    public static class Singleton extends AbstractIntList implements RandomAccess, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final int element;
        
        protected Singleton(final int element) {
            this.element = element;
        }
        
        @Override
        public int getInt(final int i) {
            if (i == 0) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean rem(final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int removeInt(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final int k) {
            return k == this.element;
        }
        
        @Override
        public int indexOf(final int k) {
            return (k == this.element) ? 0 : -1;
        }
        
        @Override
        public int[] toIntArray() {
            return new int[] { this.element };
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
        public IntListIterator listIterator(final int i) {
            if (i > 1 || i < 0) {
                throw new IndexOutOfBoundsException();
            }
            final IntListIterator l = this.listIterator();
            if (i == 1) {
                l.nextInt();
            }
            return l;
        }
        
        @Override
        public IntList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            if (from != 0 || to != 1) {
                return IntLists.EMPTY_LIST;
            }
            return this;
        }
        
        @Deprecated
        @Override
        public void forEach(final Consumer<? super Integer> action) {
            action.accept(this.element);
        }
        
        @Override
        public boolean addAll(final int i, final Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean removeIf(final Predicate<? super Integer> filter) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public void replaceAll(final UnaryOperator<Integer> operator) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void replaceAll(final IntUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void forEach(final IntConsumer action) {
            action.accept(this.element);
        }
        
        @Override
        public boolean addAll(final IntList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final IntList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeIf(final IntPredicate filter) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Object[] toArray() {
            return new Object[] { this.element };
        }
        
        @Override
        public void sort(final IntComparator comparator) {
        }
        
        @Override
        public void unstableSort(final IntComparator comparator) {
        }
        
        @Deprecated
        @Override
        public void sort(final Comparator<? super Integer> comparator) {
        }
        
        @Deprecated
        @Override
        public void unstableSort(final Comparator<? super Integer> comparator) {
        }
        
        @Override
        public void getElements(final int from, final int[] a, final int offset, final int length) {
            if (offset < 0) {
                throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
            }
            if (offset + length > a.length) {
                throw new ArrayIndexOutOfBoundsException("End index (" + (offset + length) + ") is greater than array length (" + a.length + ")");
            }
            if (from + length > this.size()) {
                throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + this.size() + ")");
            }
            if (length <= 0) {
                return;
            }
            a[offset] = this.element;
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final int[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final int[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setElements(final int[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setElements(final int index, final int[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setElements(final int index, final int[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int size() {
            return 1;
        }
        
        @Override
        public void size(final int size) {
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
    
    abstract static class ImmutableListBase extends AbstractIntList implements IntList
    {
        @Deprecated
        @Override
        public final void add(final int index, final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean add(final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean addAll(final Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean addAll(final int index, final Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final int removeInt(final int index) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean rem(final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean removeIf(final Predicate<? super Integer> c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean removeIf(final IntPredicate c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final void replaceAll(final UnaryOperator<Integer> operator) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final void replaceAll(final IntUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final void add(final int index, final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean add(final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final Integer remove(final int index) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean remove(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final Integer set(final int index, final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean addAll(final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean addAll(final IntList c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean addAll(final int index, final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean addAll(final int index, final IntList c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean removeAll(final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean retainAll(final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final int set(final int index, final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final void size(final int size) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final void removeElements(final int from, final int to) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final void addElements(final int index, final int[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final void setElements(final int index, final int[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final void sort(final IntComparator comp) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final void unstableSort(final IntComparator comp) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final void sort(final Comparator<? super Integer> comparator) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final void unstableSort(final Comparator<? super Integer> comparator) {
            throw new UnsupportedOperationException();
        }
    }
}
