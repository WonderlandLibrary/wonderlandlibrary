// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.Collection;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.BlockingQueue;
import me.gong.mcleaks.util.google.common.collect.ForwardingQueue;

@CanIgnoreReturnValue
@GwtIncompatible
public abstract class ForwardingBlockingQueue<E> extends ForwardingQueue<E> implements BlockingQueue<E>
{
    protected ForwardingBlockingQueue() {
    }
    
    @Override
    protected abstract BlockingQueue<E> delegate();
    
    @Override
    public int drainTo(final Collection<? super E> c, final int maxElements) {
        return this.delegate().drainTo(c, maxElements);
    }
    
    @Override
    public int drainTo(final Collection<? super E> c) {
        return this.delegate().drainTo(c);
    }
    
    @Override
    public boolean offer(final E e, final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.delegate().offer(e, timeout, unit);
    }
    
    @Override
    public E poll(final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.delegate().poll(timeout, unit);
    }
    
    @Override
    public void put(final E e) throws InterruptedException {
        this.delegate().put(e);
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
