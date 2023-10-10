// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Collection;
import java.util.Queue;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import java.util.Deque;

@GwtIncompatible
public abstract class ForwardingDeque<E> extends ForwardingQueue<E> implements Deque<E>
{
    protected ForwardingDeque() {
    }
    
    @Override
    protected abstract Deque<E> delegate();
    
    @Override
    public void addFirst(final E e) {
        this.delegate().addFirst(e);
    }
    
    @Override
    public void addLast(final E e) {
        this.delegate().addLast(e);
    }
    
    @Override
    public Iterator<E> descendingIterator() {
        return this.delegate().descendingIterator();
    }
    
    @Override
    public E getFirst() {
        return this.delegate().getFirst();
    }
    
    @Override
    public E getLast() {
        return this.delegate().getLast();
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean offerFirst(final E e) {
        return this.delegate().offerFirst(e);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean offerLast(final E e) {
        return this.delegate().offerLast(e);
    }
    
    @Override
    public E peekFirst() {
        return this.delegate().peekFirst();
    }
    
    @Override
    public E peekLast() {
        return this.delegate().peekLast();
    }
    
    @CanIgnoreReturnValue
    @Override
    public E pollFirst() {
        return this.delegate().pollFirst();
    }
    
    @CanIgnoreReturnValue
    @Override
    public E pollLast() {
        return this.delegate().pollLast();
    }
    
    @CanIgnoreReturnValue
    @Override
    public E pop() {
        return this.delegate().pop();
    }
    
    @Override
    public void push(final E e) {
        this.delegate().push(e);
    }
    
    @CanIgnoreReturnValue
    @Override
    public E removeFirst() {
        return this.delegate().removeFirst();
    }
    
    @CanIgnoreReturnValue
    @Override
    public E removeLast() {
        return this.delegate().removeLast();
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean removeFirstOccurrence(final Object o) {
        return this.delegate().removeFirstOccurrence(o);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean removeLastOccurrence(final Object o) {
        return this.delegate().removeLastOccurrence(o);
    }
}
