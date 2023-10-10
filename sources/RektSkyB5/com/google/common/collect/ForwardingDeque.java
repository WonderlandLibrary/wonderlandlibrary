/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.collect;

import com.google.common.collect.ForwardingQueue;
import java.util.Deque;
import java.util.Iterator;

public abstract class ForwardingDeque<E>
extends ForwardingQueue<E>
implements Deque<E> {
    protected ForwardingDeque() {
    }

    @Override
    protected abstract Deque<E> delegate();

    @Override
    public void addFirst(E e2) {
        this.delegate().addFirst(e2);
    }

    @Override
    public void addLast(E e2) {
        this.delegate().addLast(e2);
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

    @Override
    public boolean offerFirst(E e2) {
        return this.delegate().offerFirst(e2);
    }

    @Override
    public boolean offerLast(E e2) {
        return this.delegate().offerLast(e2);
    }

    @Override
    public E peekFirst() {
        return this.delegate().peekFirst();
    }

    @Override
    public E peekLast() {
        return this.delegate().peekLast();
    }

    @Override
    public E pollFirst() {
        return this.delegate().pollFirst();
    }

    @Override
    public E pollLast() {
        return this.delegate().pollLast();
    }

    @Override
    public E pop() {
        return this.delegate().pop();
    }

    @Override
    public void push(E e2) {
        this.delegate().push(e2);
    }

    @Override
    public E removeFirst() {
        return this.delegate().removeFirst();
    }

    @Override
    public E removeLast() {
        return this.delegate().removeLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o2) {
        return this.delegate().removeFirstOccurrence(o2);
    }

    @Override
    public boolean removeLastOccurrence(Object o2) {
        return this.delegate().removeLastOccurrence(o2);
    }
}

