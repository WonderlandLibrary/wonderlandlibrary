// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Queue;
import javax.annotation.CheckForNull;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import com.google.common.annotations.GwtIncompatible;
import java.util.Deque;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public abstract class ForwardingDeque<E> extends ForwardingQueue<E> implements Deque<E>
{
    protected ForwardingDeque() {
    }
    
    @Override
    protected abstract Deque<E> delegate();
    
    @Override
    public void addFirst(@ParametricNullness final E e) {
        this.delegate().addFirst(e);
    }
    
    @Override
    public void addLast(@ParametricNullness final E e) {
        this.delegate().addLast(e);
    }
    
    @Override
    public Iterator<E> descendingIterator() {
        return this.delegate().descendingIterator();
    }
    
    @ParametricNullness
    @Override
    public E getFirst() {
        return this.delegate().getFirst();
    }
    
    @ParametricNullness
    @Override
    public E getLast() {
        return this.delegate().getLast();
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean offerFirst(@ParametricNullness final E e) {
        return this.delegate().offerFirst(e);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean offerLast(@ParametricNullness final E e) {
        return this.delegate().offerLast(e);
    }
    
    @CheckForNull
    @Override
    public E peekFirst() {
        return this.delegate().peekFirst();
    }
    
    @CheckForNull
    @Override
    public E peekLast() {
        return this.delegate().peekLast();
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public E pollFirst() {
        return this.delegate().pollFirst();
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public E pollLast() {
        return this.delegate().pollLast();
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
    @Override
    public E pop() {
        return this.delegate().pop();
    }
    
    @Override
    public void push(@ParametricNullness final E e) {
        this.delegate().push(e);
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
    @Override
    public E removeFirst() {
        return this.delegate().removeFirst();
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
    @Override
    public E removeLast() {
        return this.delegate().removeLast();
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean removeFirstOccurrence(@CheckForNull final Object o) {
        return this.delegate().removeFirstOccurrence(o);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean removeLastOccurrence(@CheckForNull final Object o) {
        return this.delegate().removeLastOccurrence(o);
    }
}
