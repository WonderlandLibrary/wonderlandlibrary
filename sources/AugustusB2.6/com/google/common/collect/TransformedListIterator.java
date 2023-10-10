// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import com.google.common.annotations.GwtCompatible;
import java.util.ListIterator;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class TransformedListIterator<F, T> extends TransformedIterator<F, T> implements ListIterator<T>
{
    TransformedListIterator(final ListIterator<? extends F> backingIterator) {
        super(backingIterator);
    }
    
    private ListIterator<? extends F> backingIterator() {
        return Iterators.cast(this.backingIterator);
    }
    
    @Override
    public final boolean hasPrevious() {
        return this.backingIterator().hasPrevious();
    }
    
    @ParametricNullness
    @Override
    public final T previous() {
        return this.transform((F)this.backingIterator().previous());
    }
    
    @Override
    public final int nextIndex() {
        return this.backingIterator().nextIndex();
    }
    
    @Override
    public final int previousIndex() {
        return this.backingIterator().previousIndex();
    }
    
    @Override
    public void set(@ParametricNullness final T element) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void add(@ParametricNullness final T element) {
        throw new UnsupportedOperationException();
    }
}
