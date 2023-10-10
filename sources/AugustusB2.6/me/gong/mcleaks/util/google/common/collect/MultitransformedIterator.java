// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.NoSuchElementException;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import java.util.Iterator;

@GwtCompatible
abstract class MultitransformedIterator<F, T> implements Iterator<T>
{
    final Iterator<? extends F> backingIterator;
    private Iterator<? extends T> current;
    private Iterator<? extends T> removeFrom;
    
    MultitransformedIterator(final Iterator<? extends F> backingIterator) {
        this.current = (Iterator<? extends T>)Iterators.emptyIterator();
        this.backingIterator = Preconditions.checkNotNull(backingIterator);
    }
    
    abstract Iterator<? extends T> transform(final F p0);
    
    @Override
    public boolean hasNext() {
        Preconditions.checkNotNull(this.current);
        if (this.current.hasNext()) {
            return true;
        }
        while (this.backingIterator.hasNext()) {
            Preconditions.checkNotNull(this.current = this.transform(this.backingIterator.next()));
            if (this.current.hasNext()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public T next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        this.removeFrom = this.current;
        return (T)this.current.next();
    }
    
    @Override
    public void remove() {
        CollectPreconditions.checkRemove(this.removeFrom != null);
        this.removeFrom.remove();
        this.removeFrom = null;
    }
}
