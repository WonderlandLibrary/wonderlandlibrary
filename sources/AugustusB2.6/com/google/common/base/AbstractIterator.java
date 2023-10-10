// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.util.NoSuchElementException;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class AbstractIterator<T> implements Iterator<T>
{
    private State state;
    @CheckForNull
    private T next;
    
    protected AbstractIterator() {
        this.state = State.NOT_READY;
    }
    
    @CheckForNull
    protected abstract T computeNext();
    
    @CheckForNull
    @CanIgnoreReturnValue
    protected final T endOfData() {
        this.state = State.DONE;
        return null;
    }
    
    @Override
    public final boolean hasNext() {
        Preconditions.checkState(this.state != State.FAILED);
        switch (this.state) {
            case DONE: {
                return false;
            }
            case READY: {
                return true;
            }
            default: {
                return this.tryToComputeNext();
            }
        }
    }
    
    private boolean tryToComputeNext() {
        this.state = State.FAILED;
        this.next = this.computeNext();
        if (this.state != State.DONE) {
            this.state = State.READY;
            return true;
        }
        return false;
    }
    
    @ParametricNullness
    @Override
    public final T next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        this.state = State.NOT_READY;
        final T result = NullnessCasts.uncheckedCastNullableTToT(this.next);
        this.next = null;
        return result;
    }
    
    @Override
    public final void remove() {
        throw new UnsupportedOperationException();
    }
    
    private enum State
    {
        READY, 
        NOT_READY, 
        DONE, 
        FAILED;
        
        private static /* synthetic */ State[] $values() {
            return new State[] { State.READY, State.NOT_READY, State.DONE, State.FAILED };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
