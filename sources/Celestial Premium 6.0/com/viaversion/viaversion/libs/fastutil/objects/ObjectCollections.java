/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.libs.fastutil.objects.ObjectCollections$SynchronizedCollection
 *  com.viaversion.viaversion.libs.fastutil.objects.ObjectCollections$UnmodifiableCollection
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrays;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectBidirectionalIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollections;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterable;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterators;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

public final class ObjectCollections {
    private ObjectCollections() {
    }

    public static <K> ObjectCollection<K> synchronize(ObjectCollection<K> c) {
        return new SynchronizedCollection(c);
    }

    public static <K> ObjectCollection<K> synchronize(ObjectCollection<K> c, Object sync) {
        return new SynchronizedCollection(c, sync);
    }

    public static <K> ObjectCollection<K> unmodifiable(ObjectCollection<K> c) {
        return new UnmodifiableCollection(c);
    }

    public static <K> ObjectCollection<K> asCollection(ObjectIterable<K> iterable) {
        if (iterable instanceof ObjectCollection) {
            return (ObjectCollection)iterable;
        }
        return new IterableCollection<K>(iterable);
    }

    public static class IterableCollection<K>
    extends AbstractObjectCollection<K>
    implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectIterable<K> iterable;

        protected IterableCollection(ObjectIterable<K> iterable) {
            if (iterable == null) {
                throw new NullPointerException();
            }
            this.iterable = iterable;
        }

        @Override
        public int size() {
            int c = 0;
            Iterator iterator = this.iterator();
            while (iterator.hasNext()) {
                iterator.next();
                ++c;
            }
            return c;
        }

        @Override
        public boolean isEmpty() {
            return !this.iterable.iterator().hasNext();
        }

        @Override
        public ObjectIterator<K> iterator() {
            return this.iterable.iterator();
        }
    }

    public static abstract class EmptyCollection<K>
    extends AbstractObjectCollection<K> {
        protected EmptyCollection() {
        }

        @Override
        public boolean contains(Object k) {
            return false;
        }

        @Override
        public Object[] toArray() {
            return ObjectArrays.EMPTY_ARRAY;
        }

        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return ObjectIterators.EMPTY_ITERATOR;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public void clear() {
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Collection)) {
                return false;
            }
            return ((Collection)o).isEmpty();
        }

        @Override
        public boolean addAll(Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }
    }
}

