// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.NoSuchElementException;
import javax.annotation.CheckForNull;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtCompatible;
import java.util.Queue;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingQueue<E> extends ForwardingCollection<E> implements Queue<E>
{
    protected ForwardingQueue() {
    }
    
    @Override
    protected abstract Queue<E> delegate();
    
    @CanIgnoreReturnValue
    @Override
    public boolean offer(@ParametricNullness final E o) {
        return this.delegate().offer(o);
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public E poll() {
        return this.delegate().poll();
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
    @Override
    public E remove() {
        return this.delegate().remove();
    }
    
    @CheckForNull
    @Override
    public E peek() {
        return this.delegate().peek();
    }
    
    @ParametricNullness
    @Override
    public E element() {
        return this.delegate().element();
    }
    
    protected boolean standardOffer(@ParametricNullness final E e) {
        try {
            return this.add(e);
        }
        catch (IllegalStateException caught) {
            return false;
        }
    }
    
    @CheckForNull
    protected E standardPeek() {
        try {
            return this.element();
        }
        catch (NoSuchElementException caught) {
            return null;
        }
    }
    
    @CheckForNull
    protected E standardPoll() {
        try {
            return this.remove();
        }
        catch (NoSuchElementException caught) {
            return null;
        }
    }
}
