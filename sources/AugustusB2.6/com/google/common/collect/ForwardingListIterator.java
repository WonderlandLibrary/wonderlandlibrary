// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtCompatible;
import java.util.ListIterator;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingListIterator<E> extends ForwardingIterator<E> implements ListIterator<E>
{
    protected ForwardingListIterator() {
    }
    
    @Override
    protected abstract ListIterator<E> delegate();
    
    @Override
    public void add(@ParametricNullness final E element) {
        this.delegate().add(element);
    }
    
    @Override
    public boolean hasPrevious() {
        return this.delegate().hasPrevious();
    }
    
    @Override
    public int nextIndex() {
        return this.delegate().nextIndex();
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
    @Override
    public E previous() {
        return this.delegate().previous();
    }
    
    @Override
    public int previousIndex() {
        return this.delegate().previousIndex();
    }
    
    @Override
    public void set(@ParametricNullness final E element) {
        this.delegate().set(element);
    }
}
