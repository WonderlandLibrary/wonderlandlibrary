// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.NoSuchElementException;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class AbstractSequentialIterator<T> extends UnmodifiableIterator<T>
{
    @CheckForNull
    private T nextOrNull;
    
    protected AbstractSequentialIterator(@CheckForNull final T firstOrNull) {
        this.nextOrNull = firstOrNull;
    }
    
    @CheckForNull
    protected abstract T computeNext(final T p0);
    
    @Override
    public final boolean hasNext() {
        return this.nextOrNull != null;
    }
    
    @Override
    public final T next() {
        if (this.nextOrNull == null) {
            throw new NoSuchElementException();
        }
        final T oldNext = this.nextOrNull;
        this.nextOrNull = this.computeNext(oldNext);
        return oldNext;
    }
}
