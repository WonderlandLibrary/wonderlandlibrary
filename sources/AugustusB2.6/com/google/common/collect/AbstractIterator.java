// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.NoSuchElementException;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class AbstractIterator<T> extends UnmodifiableIterator<T>
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
    
    @CanIgnoreReturnValue
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
    @CanIgnoreReturnValue
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
    
    @ParametricNullness
    public final T peek() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        return NullnessCasts.uncheckedCastNullableTToT(this.next);
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
