// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.function.IntFunction;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Consumer;
import java.util.Collection;

public final class ObjectCollections
{
    private ObjectCollections() {
    }
    
    public static <K> ObjectCollection<K> synchronize(final ObjectCollection<K> c) {
        return (ObjectCollection<K>)new ObjectCollections.SynchronizedCollection((ObjectCollection)c);
    }
    
    public static <K> ObjectCollection<K> synchronize(final ObjectCollection<K> c, final Object sync) {
        return (ObjectCollection<K>)new ObjectCollections.SynchronizedCollection((ObjectCollection)c, sync);
    }
    
    public static <K> ObjectCollection<K> unmodifiable(final ObjectCollection<? extends K> c) {
        return (ObjectCollection<K>)new ObjectCollections.UnmodifiableCollection((ObjectCollection)c);
    }
    
    public static <K> ObjectCollection<K> asCollection(final ObjectIterable<K> iterable) {
        if (iterable instanceof ObjectCollection) {
            return (ObjectCollection<K>)(ObjectCollection)iterable;
        }
        return new IterableCollection<K>(iterable);
    }
    
    public abstract static class EmptyCollection<K> extends AbstractObjectCollection<K>
    {
        protected EmptyCollection() {
        }
        
        @Override
        public boolean contains(final Object k) {
            return false;
        }
        
        @Override
        public Object[] toArray() {
            return ObjectArrays.EMPTY_ARRAY;
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            if (array.length > 0) {
                array[0] = null;
            }
            return array;
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return (ObjectBidirectionalIterator<K>)ObjectIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ObjectSpliterator<K> spliterator() {
            return (ObjectSpliterator<K>)ObjectSpliterators.EMPTY_SPLITERATOR;
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
        public boolean equals(final Object o) {
            return o == this || (o instanceof Collection && ((Collection)o).isEmpty());
        }
        
        @Override
        public void forEach(final Consumer<? super K> action) {
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            return c.isEmpty();
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
            Objects.requireNonNull(filter);
            return false;
        }
    }
    
    public static class IterableCollection<K> extends AbstractObjectCollection<K> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectIterable<K> iterable;
        
        protected IterableCollection(final ObjectIterable<K> iterable) {
            this.iterable = Objects.requireNonNull(iterable);
        }
        
        @Override
        public int size() {
            final long size = this.iterable.spliterator().getExactSizeIfKnown();
            if (size >= 0L) {
                return (int)Math.min(2147483647L, size);
            }
            int c = 0;
            final ObjectIterator<K> iterator = this.iterator();
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
        
        @Override
        public ObjectSpliterator<K> spliterator() {
            return this.iterable.spliterator();
        }
    }
    
    static class SizeDecreasingSupplier<K, C extends ObjectCollection<K>> implements Supplier<C>
    {
        static final int RECOMMENDED_MIN_SIZE = 8;
        final AtomicInteger suppliedCount;
        final int expectedFinalSize;
        final IntFunction<C> builder;
        
        SizeDecreasingSupplier(final int expectedFinalSize, final IntFunction<C> builder) {
            this.suppliedCount = new AtomicInteger(0);
            this.expectedFinalSize = expectedFinalSize;
            this.builder = builder;
        }
        
        @Override
        public C get() {
            int expectedNeededNextSize = 1 + (this.expectedFinalSize - 1) / this.suppliedCount.incrementAndGet();
            if (expectedNeededNextSize < 0) {
                expectedNeededNextSize = 8;
            }
            return this.builder.apply(expectedNeededNextSize);
        }
    }
}
