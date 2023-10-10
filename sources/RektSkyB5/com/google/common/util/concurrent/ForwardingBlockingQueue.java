/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.util.concurrent;

import com.google.common.collect.ForwardingQueue;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class ForwardingBlockingQueue<E>
extends ForwardingQueue<E>
implements BlockingQueue<E> {
    protected ForwardingBlockingQueue() {
    }

    @Override
    protected abstract BlockingQueue<E> delegate();

    @Override
    public int drainTo(Collection<? super E> c2, int maxElements) {
        return this.delegate().drainTo(c2, maxElements);
    }

    @Override
    public int drainTo(Collection<? super E> c2) {
        return this.delegate().drainTo(c2);
    }

    @Override
    public boolean offer(E e2, long timeout, TimeUnit unit) throws InterruptedException {
        return this.delegate().offer(e2, timeout, unit);
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return this.delegate().poll(timeout, unit);
    }

    @Override
    public void put(E e2) throws InterruptedException {
        this.delegate().put(e2);
    }

    @Override
    public int remainingCapacity() {
        return this.delegate().remainingCapacity();
    }

    @Override
    public E take() throws InterruptedException {
        return this.delegate().take();
    }
}

