// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Spliterator;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.NoSuchElementException;
import java.io.Serializable;

public final class IntSortedSets
{
    public static final EmptySet EMPTY_SET;
    
    private IntSortedSets() {
    }
    
    public static IntSortedSet singleton(final int element) {
        return new Singleton(element);
    }
    
    public static IntSortedSet singleton(final int element, final IntComparator comparator) {
        return new Singleton(element, comparator);
    }
    
    public static IntSortedSet singleton(final Object element) {
        return new Singleton((int)element);
    }
    
    public static IntSortedSet singleton(final Object element, final IntComparator comparator) {
        return new Singleton((int)element, comparator);
    }
    
    public static IntSortedSet synchronize(final IntSortedSet s) {
        return (IntSortedSet)new IntSortedSets.SynchronizedSortedSet(s);
    }
    
    public static IntSortedSet synchronize(final IntSortedSet s, final Object sync) {
        return (IntSortedSet)new IntSortedSets.SynchronizedSortedSet(s, sync);
    }
    
    public static IntSortedSet unmodifiable(final IntSortedSet s) {
        return (IntSortedSet)new IntSortedSets.UnmodifiableSortedSet(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet extends IntSets.EmptySet implements IntSortedSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return IntIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public IntSortedSet headSet(final int from) {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public IntSortedSet tailSet(final int to) {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public int firstInt() {
            throw new NoSuchElementException();
        }
        
        @Override
        public int lastInt() {
            throw new NoSuchElementException();
        }
        
        @Override
        public IntComparator comparator() {
            return null;
        }
        
        @Deprecated
        @Override
        public IntSortedSet subSet(final Integer from, final Integer to) {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public IntSortedSet headSet(final Integer from) {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public IntSortedSet tailSet(final Integer to) {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public Integer first() {
            throw new NoSuchElementException();
        }
        
        @Deprecated
        @Override
        public Integer last() {
            throw new NoSuchElementException();
        }
        
        @Override
        public Object clone() {
            return IntSortedSets.EMPTY_SET;
        }
        
        private Object readResolve() {
            return IntSortedSets.EMPTY_SET;
        }
    }
    
    public static class Singleton extends IntSets.Singleton implements IntSortedSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        final IntComparator comparator;
        
        protected Singleton(final int element, final IntComparator comparator) {
            super(element);
            this.comparator = comparator;
        }
        
        Singleton(final int element) {
            this(element, null);
        }
        
        final int compare(final int k1, final int k2) {
            return (this.comparator == null) ? Integer.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            final IntBidirectionalIterator i = this.iterator();
            if (this.compare(this.element, from) <= 0) {
                i.nextInt();
            }
            return i;
        }
        
        @Override
        public IntComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public IntSpliterator spliterator() {
            return IntSpliterators.singleton(this.element, this.comparator);
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            if (this.compare(from, this.element) <= 0 && this.compare(this.element, to) < 0) {
                return this;
            }
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            if (this.compare(this.element, to) < 0) {
                return this;
            }
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            if (this.compare(from, this.element) <= 0) {
                return this;
            }
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public int firstInt() {
            return this.element;
        }
        
        @Override
        public int lastInt() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public IntSortedSet subSet(final Integer from, final Integer to) {
            return this.subSet((int)from, (int)to);
        }
        
        @Deprecated
        @Override
        public IntSortedSet headSet(final Integer to) {
            return this.headSet((int)to);
        }
        
        @Deprecated
        @Override
        public IntSortedSet tailSet(final Integer from) {
            return this.tailSet((int)from);
        }
        
        @Deprecated
        @Override
        public Integer first() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public Integer last() {
            return this.element;
        }
    }
}
