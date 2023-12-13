/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.libs.fastutil.objects.ObjectLists$SynchronizedList
 *  com.viaversion.viaversion.libs.fastutil.objects.ObjectLists$SynchronizedRandomAccessList
 *  com.viaversion.viaversion.libs.fastutil.objects.ObjectLists$UnmodifiableList
 *  com.viaversion.viaversion.libs.fastutil.objects.ObjectLists$UnmodifiableRandomAccessList
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectList;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollections;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterators;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectList;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectListIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectLists;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterators;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public final class ObjectLists {
    public static final EmptyList EMPTY_LIST = new EmptyList();

    private ObjectLists() {
    }

    public static <K> ObjectList<K> shuffle(ObjectList<K> l2, Random random) {
        int i2 = l2.size();
        while (i2-- != 0) {
            int p2 = random.nextInt(i2 + 1);
            Object t2 = l2.get(i2);
            l2.set(i2, l2.get(p2));
            l2.set(p2, t2);
        }
        return l2;
    }

    public static <K> ObjectList<K> emptyList() {
        return EMPTY_LIST;
    }

    public static <K> ObjectList<K> singleton(K element) {
        return new Singleton<K>(element);
    }

    public static <K> ObjectList<K> synchronize(ObjectList<K> l2) {
        return l2 instanceof RandomAccess ? new SynchronizedRandomAccessList(l2) : new SynchronizedList(l2);
    }

    public static <K> ObjectList<K> synchronize(ObjectList<K> l2, Object sync) {
        return l2 instanceof RandomAccess ? new SynchronizedRandomAccessList(l2, sync) : new SynchronizedList(l2, sync);
    }

    public static <K> ObjectList<K> unmodifiable(ObjectList<? extends K> l2) {
        return l2 instanceof RandomAccess ? new UnmodifiableRandomAccessList(l2) : new UnmodifiableList(l2);
    }

    public static class EmptyList<K>
    extends ObjectCollections.EmptyCollection<K>
    implements ObjectList<K>,
    RandomAccess,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptyList() {
        }

        @Override
        public K get(int i2) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public boolean remove(Object k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public K remove(int i2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(int index, K k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public K set(int index, K k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int indexOf(Object k2) {
            return -1;
        }

        @Override
        public int lastIndexOf(Object k2) {
            return -1;
        }

        @Override
        public boolean addAll(int i2, Collection<? extends K> c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void replaceAll(UnaryOperator<K> operator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void sort(Comparator<? super K> comparator) {
        }

        @Override
        public void unstableSort(Comparator<? super K> comparator) {
        }

        @Override
        public ObjectListIterator<K> listIterator() {
            return ObjectIterators.EMPTY_ITERATOR;
        }

        @Override
        public ObjectListIterator<K> iterator() {
            return ObjectIterators.EMPTY_ITERATOR;
        }

        @Override
        public ObjectListIterator<K> listIterator(int i2) {
            if (i2 == 0) {
                return ObjectIterators.EMPTY_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i2));
        }

        @Override
        public ObjectList<K> subList(int from, int to) {
            if (from == 0 && to == 0) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }

        @Override
        public void getElements(int from, Object[] a2, int offset, int length) {
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
        public void addElements(int index, K[] a2, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addElements(int index, K[] a2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setElements(K[] a2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setElements(int index, K[] a2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setElements(int index, K[] a2, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void size(int s2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int compareTo(List<? extends K> o2) {
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

    public static class Singleton<K>
    extends AbstractObjectList<K>
    implements RandomAccess,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        private final K element;

        protected Singleton(K element) {
            this.element = element;
        }

        @Override
        public K get(int i2) {
            if (i2 == 0) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }

        @Override
        public boolean remove(Object k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public K remove(int i2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean contains(Object k2) {
            return Objects.equals(k2, this.element);
        }

        @Override
        public int indexOf(Object k2) {
            return Objects.equals(k2, this.element) ? 0 : -1;
        }

        @Override
        public Object[] toArray() {
            return new Object[]{this.element};
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
        public ObjectListIterator<K> listIterator(int i2) {
            if (i2 > 1 || i2 < 0) {
                throw new IndexOutOfBoundsException();
            }
            ListIterator l2 = this.listIterator();
            if (i2 == 1) {
                l2.next();
            }
            return l2;
        }

        @Override
        public ObjectList<K> subList(int from, int to) {
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
        public void forEach(Consumer<? super K> action) {
            action.accept(this.element);
        }

        @Override
        public boolean addAll(int i2, Collection<? extends K> c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends K> c2) {
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
        public boolean removeIf(Predicate<? super K> filter) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void replaceAll(UnaryOperator<K> operator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void sort(Comparator<? super K> comparator) {
        }

        @Override
        public void unstableSort(Comparator<? super K> comparator) {
        }

        @Override
        public void getElements(int from, Object[] a2, int offset, int length) {
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
        public void addElements(int index, K[] a2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addElements(int index, K[] a2, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setElements(K[] a2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setElements(int index, K[] a2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setElements(int index, K[] a2, int offset, int length) {
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

    static abstract class ImmutableListBase<K>
    extends AbstractObjectList<K>
    implements ObjectList<K> {
        ImmutableListBase() {
        }

        @Override
        @Deprecated
        public final void add(int index, K k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean add(K k2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean addAll(Collection<? extends K> c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean addAll(int index, Collection<? extends K> c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final K remove(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final boolean remove(Object k2) {
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
        public final boolean removeIf(Predicate<? super K> c2) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final void replaceAll(UnaryOperator<K> operator) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final K set(int index, K k2) {
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
        public final void addElements(int index, K[] a2, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final void setElements(int index, K[] a2, int offset, int length) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final void sort(Comparator<? super K> comparator) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public final void unstableSort(Comparator<? super K> comparator) {
            throw new UnsupportedOperationException();
        }
    }
}

