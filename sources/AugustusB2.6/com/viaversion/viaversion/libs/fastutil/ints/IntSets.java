// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Spliterator;
import java.util.function.IntPredicate;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.Set;
import java.io.Serializable;
import java.util.Iterator;

public final class IntSets
{
    static final int ARRAY_SET_CUTOFF = 4;
    public static final EmptySet EMPTY_SET;
    static final IntSet UNMODIFIABLE_EMPTY_SET;
    
    private IntSets() {
    }
    
    public static IntSet emptySet() {
        return IntSets.EMPTY_SET;
    }
    
    public static IntSet singleton(final int element) {
        return new Singleton(element);
    }
    
    public static IntSet singleton(final Integer element) {
        return new Singleton(element);
    }
    
    public static IntSet synchronize(final IntSet s) {
        return (IntSet)new IntSets.SynchronizedSet(s);
    }
    
    public static IntSet synchronize(final IntSet s, final Object sync) {
        return (IntSet)new IntSets.SynchronizedSet(s, sync);
    }
    
    public static IntSet unmodifiable(final IntSet s) {
        return (IntSet)new IntSets.UnmodifiableSet(s);
    }
    
    public static IntSet fromTo(final int from, final int to) {
        return new AbstractIntSet() {
            @Override
            public boolean contains(final int x) {
                return x >= from && x < to;
            }
            
            @Override
            public IntIterator iterator() {
                return IntIterators.fromTo(from, to);
            }
            
            @Override
            public int size() {
                final long size = to - (long)from;
                return (size >= 0L && size <= 2147483647L) ? ((int)size) : Integer.MAX_VALUE;
            }
        };
    }
    
    public static IntSet from(final int from) {
        return new AbstractIntSet() {
            @Override
            public boolean contains(final int x) {
                return x >= from;
            }
            
            @Override
            public IntIterator iterator() {
                return IntIterators.concat(IntIterators.fromTo(from, Integer.MAX_VALUE), IntSets.singleton(Integer.MAX_VALUE).iterator());
            }
            
            @Override
            public int size() {
                final long size = 2147483647L - from + 1L;
                return (size >= 0L && size <= 2147483647L) ? ((int)size) : Integer.MAX_VALUE;
            }
        };
    }
    
    public static IntSet to(final int to) {
        return new AbstractIntSet() {
            @Override
            public boolean contains(final int x) {
                return x < to;
            }
            
            @Override
            public IntIterator iterator() {
                return IntIterators.fromTo(Integer.MIN_VALUE, to);
            }
            
            @Override
            public int size() {
                final long size = to + 2147483648L;
                return (size >= 0L && size <= 2147483647L) ? ((int)size) : Integer.MAX_VALUE;
            }
        };
    }
    
    static {
        EMPTY_SET = new EmptySet();
        UNMODIFIABLE_EMPTY_SET = unmodifiable(new IntArraySet(IntArrays.EMPTY_ARRAY));
    }
    
    public static class EmptySet extends IntCollections.EmptyCollection implements IntSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public boolean remove(final int ok) {
            throw new UnsupportedOperationException();
        }
        
        public Object clone() {
            return IntSets.EMPTY_SET;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Set && ((Set)o).isEmpty();
        }
        
        @Deprecated
        @Override
        public boolean rem(final int k) {
            return super.rem(k);
        }
        
        private Object readResolve() {
            return IntSets.EMPTY_SET;
        }
    }
    
    public static class Singleton extends AbstractIntSet implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final int element;
        
        protected Singleton(final int element) {
            this.element = element;
        }
        
        @Override
        public boolean contains(final int k) {
            return k == this.element;
        }
        
        @Override
        public boolean remove(final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public IntListIterator iterator() {
            return IntIterators.singleton(this.element);
        }
        
        @Override
        public IntSpliterator spliterator() {
            return IntSpliterators.singleton(this.element);
        }
        
        @Override
        public int size() {
            return 1;
        }
        
        @Override
        public int[] toIntArray() {
            return new int[] { this.element };
        }
        
        @Deprecated
        @Override
        public void forEach(final Consumer<? super Integer> action) {
            action.accept(this.element);
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
        
        @Override
        public void forEach(final IntConsumer action) {
            action.accept(this.element);
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
        
        public Object clone() {
            return this;
        }
    }
}
