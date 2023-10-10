// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Spliterator;
import java.util.SortedSet;
import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.Comparator;

public final class ObjectSortedSets
{
    public static final EmptySet EMPTY_SET;
    
    private ObjectSortedSets() {
    }
    
    public static <K> ObjectSet<K> emptySet() {
        return (ObjectSet<K>)ObjectSortedSets.EMPTY_SET;
    }
    
    public static <K> ObjectSortedSet<K> singleton(final K element) {
        return new Singleton<K>(element);
    }
    
    public static <K> ObjectSortedSet<K> singleton(final K element, final Comparator<? super K> comparator) {
        return new Singleton<K>(element, comparator);
    }
    
    public static <K> ObjectSortedSet<K> synchronize(final ObjectSortedSet<K> s) {
        return (ObjectSortedSet<K>)new ObjectSortedSets.SynchronizedSortedSet((ObjectSortedSet)s);
    }
    
    public static <K> ObjectSortedSet<K> synchronize(final ObjectSortedSet<K> s, final Object sync) {
        return (ObjectSortedSet<K>)new ObjectSortedSets.SynchronizedSortedSet((ObjectSortedSet)s, sync);
    }
    
    public static <K> ObjectSortedSet<K> unmodifiable(final ObjectSortedSet<K> s) {
        return (ObjectSortedSet<K>)new ObjectSortedSets.UnmodifiableSortedSet((ObjectSortedSet)s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet<K> extends ObjectSets.EmptySet<K> implements ObjectSortedSet<K>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return (ObjectBidirectionalIterator<K>)ObjectIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K from) {
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K to) {
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public K first() {
            throw new NoSuchElementException();
        }
        
        @Override
        public K last() {
            throw new NoSuchElementException();
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public Object clone() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        private Object readResolve() {
            return ObjectSortedSets.EMPTY_SET;
        }
    }
    
    public static class Singleton<K> extends ObjectSets.Singleton<K> implements ObjectSortedSet<K>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        final Comparator<? super K> comparator;
        
        protected Singleton(final K element, final Comparator<? super K> comparator) {
            super(element);
            this.comparator = comparator;
        }
        
        Singleton(final K element) {
            this(element, null);
        }
        
        final int compare(final K k1, final K k2) {
            return (this.comparator == null) ? ((Comparable)k1).compareTo(k2) : this.comparator.compare((Object)k1, (Object)k2);
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            final ObjectBidirectionalIterator<K> i = this.iterator();
            if (this.compare(this.element, from) <= 0) {
                i.next();
            }
            return i;
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSpliterator<K> spliterator() {
            return ObjectSpliterators.singleton(this.element, this.comparator);
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            if (this.compare(from, this.element) <= 0 && this.compare(this.element, to) < 0) {
                return this;
            }
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            if (this.compare(this.element, to) < 0) {
                return this;
            }
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            if (this.compare(from, this.element) <= 0) {
                return this;
            }
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public K first() {
            return this.element;
        }
        
        @Override
        public K last() {
            return this.element;
        }
    }
}
