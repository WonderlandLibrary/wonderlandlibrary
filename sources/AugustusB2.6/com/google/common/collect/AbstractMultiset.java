// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.Collection;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.Set;
import com.google.common.annotations.GwtCompatible;
import java.util.AbstractCollection;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class AbstractMultiset<E> extends AbstractCollection<E> implements Multiset<E>
{
    @LazyInit
    @CheckForNull
    private transient Set<E> elementSet;
    @LazyInit
    @CheckForNull
    private transient Set<Entry<E>> entrySet;
    
    @Override
    public boolean isEmpty() {
        return this.entrySet().isEmpty();
    }
    
    @Override
    public boolean contains(@CheckForNull final Object element) {
        return this.count(element) > 0;
    }
    
    @CanIgnoreReturnValue
    @Override
    public final boolean add(@ParametricNullness final E element) {
        this.add(element, 1);
        return true;
    }
    
    @CanIgnoreReturnValue
    @Override
    public int add(@ParametricNullness final E element, final int occurrences) {
        throw new UnsupportedOperationException();
    }
    
    @CanIgnoreReturnValue
    @Override
    public final boolean remove(@CheckForNull final Object element) {
        return this.remove(element, 1) > 0;
    }
    
    @CanIgnoreReturnValue
    @Override
    public int remove(@CheckForNull final Object element, final int occurrences) {
        throw new UnsupportedOperationException();
    }
    
    @CanIgnoreReturnValue
    @Override
    public int setCount(@ParametricNullness final E element, final int count) {
        return Multisets.setCountImpl(this, element, count);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean setCount(@ParametricNullness final E element, final int oldCount, final int newCount) {
        return Multisets.setCountImpl(this, element, oldCount, newCount);
    }
    
    @CanIgnoreReturnValue
    @Override
    public final boolean addAll(final Collection<? extends E> elementsToAdd) {
        return Multisets.addAllImpl((Multiset<Object>)this, elementsToAdd);
    }
    
    @CanIgnoreReturnValue
    @Override
    public final boolean removeAll(final Collection<?> elementsToRemove) {
        return Multisets.removeAllImpl(this, elementsToRemove);
    }
    
    @CanIgnoreReturnValue
    @Override
    public final boolean retainAll(final Collection<?> elementsToRetain) {
        return Multisets.retainAllImpl(this, elementsToRetain);
    }
    
    @Override
    public abstract void clear();
    
    @Override
    public Set<E> elementSet() {
        Set<E> result = this.elementSet;
        if (result == null) {
            result = (this.elementSet = this.createElementSet());
        }
        return result;
    }
    
    Set<E> createElementSet() {
        return new ElementSet();
    }
    
    abstract Iterator<E> elementIterator();
    
    @Override
    public Set<Entry<E>> entrySet() {
        Set<Entry<E>> result = this.entrySet;
        if (result == null) {
            result = (this.entrySet = this.createEntrySet());
        }
        return result;
    }
    
    Set<Entry<E>> createEntrySet() {
        return (Set<Entry<E>>)new EntrySet();
    }
    
    abstract Iterator<Entry<E>> entryIterator();
    
    abstract int distinctElements();
    
    @Override
    public final boolean equals(@CheckForNull final Object object) {
        return Multisets.equalsImpl(this, object);
    }
    
    @Override
    public final int hashCode() {
        return this.entrySet().hashCode();
    }
    
    @Override
    public final String toString() {
        return this.entrySet().toString();
    }
    
    class ElementSet extends Multisets.ElementSet<E>
    {
        @Override
        Multiset<E> multiset() {
            return (Multiset<E>)AbstractMultiset.this;
        }
        
        @Override
        public Iterator<E> iterator() {
            return AbstractMultiset.this.elementIterator();
        }
    }
    
    class EntrySet extends Multisets.EntrySet<E>
    {
        @Override
        Multiset<E> multiset() {
            return (Multiset<E>)AbstractMultiset.this;
        }
        
        @Override
        public Iterator<Entry<E>> iterator() {
            return AbstractMultiset.this.entryIterator();
        }
        
        @Override
        public int size() {
            return AbstractMultiset.this.distinctElements();
        }
    }
}
