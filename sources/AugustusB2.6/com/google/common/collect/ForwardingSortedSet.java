// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;
import com.google.common.annotations.Beta;
import java.util.NoSuchElementException;
import javax.annotation.CheckForNull;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;
import java.util.SortedSet;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingSortedSet<E> extends ForwardingSet<E> implements SortedSet<E>
{
    protected ForwardingSortedSet() {
    }
    
    @Override
    protected abstract SortedSet<E> delegate();
    
    @CheckForNull
    @Override
    public Comparator<? super E> comparator() {
        return this.delegate().comparator();
    }
    
    @ParametricNullness
    @Override
    public E first() {
        return this.delegate().first();
    }
    
    @Override
    public SortedSet<E> headSet(@ParametricNullness final E toElement) {
        return this.delegate().headSet(toElement);
    }
    
    @ParametricNullness
    @Override
    public E last() {
        return this.delegate().last();
    }
    
    @Override
    public SortedSet<E> subSet(@ParametricNullness final E fromElement, @ParametricNullness final E toElement) {
        return this.delegate().subSet(fromElement, toElement);
    }
    
    @Override
    public SortedSet<E> tailSet(@ParametricNullness final E fromElement) {
        return this.delegate().tailSet(fromElement);
    }
    
    @Beta
    @Override
    protected boolean standardContains(@CheckForNull final Object object) {
        try {
            final SortedSet<Object> self = (SortedSet<Object>)this;
            final Object ceiling = self.tailSet(object).first();
            return ForwardingSortedMap.unsafeCompare(this.comparator(), ceiling, object) == 0;
        }
        catch (ClassCastException | NoSuchElementException | NullPointerException ex2) {
            final RuntimeException ex;
            final RuntimeException e = ex;
            return false;
        }
    }
    
    @Beta
    @Override
    protected boolean standardRemove(@CheckForNull final Object object) {
        try {
            final SortedSet<Object> self = (SortedSet<Object>)this;
            final Iterator<?> iterator = self.tailSet(object).iterator();
            if (iterator.hasNext()) {
                final Object ceiling = iterator.next();
                if (ForwardingSortedMap.unsafeCompare(this.comparator(), ceiling, object) == 0) {
                    iterator.remove();
                    return true;
                }
            }
        }
        catch (ClassCastException | NullPointerException ex2) {
            final RuntimeException ex;
            final RuntimeException e = ex;
            return false;
        }
        return false;
    }
    
    @Beta
    protected SortedSet<E> standardSubSet(@ParametricNullness final E fromElement, @ParametricNullness final E toElement) {
        return this.tailSet(fromElement).headSet(toElement);
    }
}
