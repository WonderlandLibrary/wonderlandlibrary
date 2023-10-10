// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.function.IntFunction;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.IntPredicate;
import java.util.function.IntConsumer;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Consumer;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrays;

public final class IntCollections
{
    private IntCollections() {
    }
    
    public static IntCollection synchronize(final IntCollection c) {
        return (IntCollection)new IntCollections.SynchronizedCollection(c);
    }
    
    public static IntCollection synchronize(final IntCollection c, final Object sync) {
        return (IntCollection)new IntCollections.SynchronizedCollection(c, sync);
    }
    
    public static IntCollection unmodifiable(final IntCollection c) {
        return (IntCollection)new IntCollections.UnmodifiableCollection(c);
    }
    
    public static IntCollection asCollection(final IntIterable iterable) {
        if (iterable instanceof IntCollection) {
            return (IntCollection)iterable;
        }
        return new IterableCollection(iterable);
    }
    
    public abstract static class EmptyCollection extends AbstractIntCollection
    {
        protected EmptyCollection() {
        }
        
        @Override
        public boolean contains(final int k) {
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
        public IntBidirectionalIterator iterator() {
            return IntIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public IntSpliterator spliterator() {
            return IntSpliterators.EMPTY_SPLITERATOR;
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
        
        @Deprecated
        @Override
        public void forEach(final Consumer<? super Integer> action) {
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            return c.isEmpty();
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
            Objects.requireNonNull(filter);
            return false;
        }
        
        @Override
        public int[] toIntArray() {
            return IntArrays.EMPTY_ARRAY;
        }
        
        @Deprecated
        @Override
        public int[] toIntArray(final int[] a) {
            return a;
        }
        
        @Override
        public void forEach(final IntConsumer action) {
        }
        
        @Override
        public boolean containsAll(final IntCollection c) {
            return c.isEmpty();
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
            Objects.requireNonNull(filter);
            return false;
        }
    }
    
    public static class IterableCollection extends AbstractIntCollection implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntIterable iterable;
        
        protected IterableCollection(final IntIterable iterable) {
            this.iterable = Objects.requireNonNull(iterable);
        }
        
        @Override
        public int size() {
            final long size = this.iterable.spliterator().getExactSizeIfKnown();
            if (size >= 0L) {
                return (int)Math.min(2147483647L, size);
            }
            int c = 0;
            final IntIterator iterator = this.iterator();
            while (iterator.hasNext()) {
                iterator.nextInt();
                ++c;
            }
            return c;
        }
        
        @Override
        public boolean isEmpty() {
            return !this.iterable.iterator().hasNext();
        }
        
        @Override
        public IntIterator iterator() {
            return this.iterable.iterator();
        }
        
        @Override
        public IntSpliterator spliterator() {
            return this.iterable.spliterator();
        }
        
        @Override
        public IntIterator intIterator() {
            return this.iterable.intIterator();
        }
        
        @Override
        public IntSpliterator intSpliterator() {
            return this.iterable.intSpliterator();
        }
    }
    
    static class SizeDecreasingSupplier<C extends IntCollection> implements Supplier<C>
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
