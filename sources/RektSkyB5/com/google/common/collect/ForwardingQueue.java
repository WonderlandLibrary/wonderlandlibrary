/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ForwardingCollection;
import java.util.NoSuchElementException;
import java.util.Queue;

@GwtCompatible
public abstract class ForwardingQueue<E>
extends ForwardingCollection<E>
implements Queue<E> {
    protected ForwardingQueue() {
    }

    @Override
    protected abstract Queue<E> delegate();

    @Override
    public boolean offer(E o2) {
        return this.delegate().offer(o2);
    }

    @Override
    public E poll() {
        return this.delegate().poll();
    }

    @Override
    public E remove() {
        return this.delegate().remove();
    }

    @Override
    public E peek() {
        return this.delegate().peek();
    }

    @Override
    public E element() {
        return this.delegate().element();
    }

    protected boolean standardOffer(E e2) {
        try {
            return this.add(e2);
        }
        catch (IllegalStateException caught) {
            return false;
        }
    }

    protected E standardPeek() {
        try {
            return this.element();
        }
        catch (NoSuchElementException caught) {
            return null;
        }
    }

    protected E standardPoll() {
        try {
            return this.remove();
        }
        catch (NoSuchElementException caught) {
            return null;
        }
    }
}

