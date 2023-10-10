// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.function.Consumer;
import java.util.Objects;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Comparator;
import java.util.function.UnaryOperator;
import java.util.Collection;
import java.io.Serializable;
import java.util.RandomAccess;
import java.util.Random;

public final class ObjectLists
{
    public static final EmptyList EMPTY_LIST;
    
    private ObjectLists() {
    }
    
    public static <K> ObjectList<K> shuffle(final ObjectList<K> l, final Random random) {
        int i = l.size();
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final K t = l.get(i);
            l.set(i, l.get(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static <K> ObjectList<K> emptyList() {
        return (ObjectList<K>)ObjectLists.EMPTY_LIST;
    }
    
    public static <K> ObjectList<K> singleton(final K element) {
        return new Singleton<K>(element);
    }
    
    public static <K> ObjectList<K> synchronize(final ObjectList<K> l) {
        return (ObjectList<K>)((l instanceof RandomAccess) ? new ObjectLists.SynchronizedRandomAccessList((ObjectList)l) : new ObjectLists.SynchronizedList((ObjectList)l));
    }
    
    public static <K> ObjectList<K> synchronize(final ObjectList<K> l, final Object sync) {
        return (ObjectList<K>)((l instanceof RandomAccess) ? new ObjectLists.SynchronizedRandomAccessList((ObjectList)l, sync) : new ObjectLists.SynchronizedList((ObjectList)l, sync));
    }
    
    public static <K> ObjectList<K> unmodifiable(final ObjectList<? extends K> l) {
        return (ObjectList<K>)((l instanceof RandomAccess) ? new ObjectLists.UnmodifiableRandomAccessList((ObjectList)l) : new ObjectLists.UnmodifiableList((ObjectList)l));
    }
    
    static {
        EMPTY_LIST = new EmptyList();
    }
    
    public static class EmptyList<K> extends ObjectCollections.EmptyCollection<K> implements ObjectList<K>, RandomAccess, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyList() {
        }
        
        @Override
        public K get(final int i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean remove(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public K remove(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int index, final K k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public K set(final int index, final K k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final Object k) {
            return -1;
        }
        
        @Override
        public int lastIndexOf(final Object k) {
            return -1;
        }
        
        @Override
        public boolean addAll(final int i, final Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void replaceAll(final UnaryOperator<K> operator) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void sort(final Comparator<? super K> comparator) {
        }
        
        @Override
        public void unstableSort(final Comparator<? super K> comparator) {
        }
        
        @Override
        public ObjectListIterator<K> listIterator() {
            return (ObjectListIterator<K>)ObjectIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ObjectListIterator<K> iterator() {
            return (ObjectListIterator<K>)ObjectIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ObjectListIterator<K> listIterator(final int i) {
            if (i == 0) {
                return (ObjectListIterator<K>)ObjectIterators.EMPTY_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Override
        public ObjectList<K> subList(final int from, final int to) {
            if (from == 0 && to == 0) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void getElements(final int from, final Object[] a, final int offset, final int length) {
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
        public void addElements(final int index, final K[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final K[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setElements(final K[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setElements(final int index, final K[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setElements(final int index, final K[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int s) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int compareTo(final List<? extends K> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        public Object clone() {
            return ObjectLists.EMPTY_LIST;
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
            return ObjectLists.EMPTY_LIST;
        }
    }
    
    public static class Singleton<K> extends AbstractObjectList<K> implements RandomAccess, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final K element;
        
        protected Singleton(final K element) {
            this.element = element;
        }
        
        @Override
        public K get(final int i) {
            if (i == 0) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean remove(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public K remove(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final Object k) {
            return Objects.equals(k, this.element);
        }
        
        @Override
        public int indexOf(final Object k) {
            return Objects.equals(k, this.element) ? 0 : -1;
        }
        
        @Override
        public Object[] toArray() {
            return new Object[] { this.element };
        }
        
        @Override
        public ObjectListIterator<K> listIterator() {
            return ObjectIterators.singleton(this.element);
        }
        
        @Override
        public ObjectListIterator<K> iterator() {
            return this.listIterator();
        }
        
        @Override
        public ObjectSpliterator<K> spliterator() {
            return ObjectSpliterators.singleton(this.element);
        }
        
        @Override
        public ObjectListIterator<K> listIterator(final int i) {
            if (i > 1 || i < 0) {
                throw new IndexOutOfBoundsException();
            }
            final ObjectListIterator<K> l = this.listIterator();
            if (i == 1) {
                l.next();
            }
            return l;
        }
        
        @Override
        public ObjectList<K> subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            if (from != 0 || to != 1) {
                return (ObjectList<K>)ObjectLists.EMPTY_LIST;
            }
            return this;
        }
        
        @Override
        public void forEach(final Consumer<? super K> action) {
            action.accept((Object)this.element);
        }
        
        @Override
        public boolean addAll(final int i, final Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends K> c) {
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
        
        @Override
        public boolean removeIf(final Predicate<? super K> filter) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void replaceAll(final UnaryOperator<K> operator) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void sort(final Comparator<? super K> comparator) {
        }
        
        @Override
        public void unstableSort(final Comparator<? super K> comparator) {
        }
        
        @Override
        public void getElements(final int from, final Object[] a, final int offset, final int length) {
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
        public void addElements(final int index, final K[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final K[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setElements(final K[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setElements(final int index, final K[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setElements(final int index, final K[] a, final int offset, final int length) {
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
    
    abstract static class ImmutableListBase<K> extends AbstractObjectList<K> implements ObjectList<K>
    {
        @Deprecated
        @Override
        public final void add(final int index, final K k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean add(final K k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean addAll(final Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean addAll(final int index, final Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final K remove(final int index) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final boolean remove(final Object k) {
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
        public final boolean removeIf(final Predicate<? super K> c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final void replaceAll(final UnaryOperator<K> operator) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final K set(final int index, final K k) {
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
        public final void addElements(final int index, final K[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final void setElements(final int index, final K[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final void sort(final Comparator<? super K> comparator) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public final void unstableSort(final Comparator<? super K> comparator) {
            throw new UnsupportedOperationException();
        }
    }
}
