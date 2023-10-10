// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

@ElementTypesAreNonnullByDefault
abstract class ForwardingCondition implements Condition
{
    abstract Condition delegate();
    
    @Override
    public void await() throws InterruptedException {
        this.delegate().await();
    }
    
    @Override
    public boolean await(final long time, final TimeUnit unit) throws InterruptedException {
        return this.delegate().await(time, unit);
    }
    
    @Override
    public void awaitUninterruptibly() {
        this.delegate().awaitUninterruptibly();
    }
    
    @Override
    public long awaitNanos(final long nanosTimeout) throws InterruptedException {
        return this.delegate().awaitNanos(nanosTimeout);
    }
    
    @Override
    public boolean awaitUntil(final Date deadline) throws InterruptedException {
        return this.delegate().awaitUntil(deadline);
    }
    
    @Override
    public void signal() {
        this.delegate().signal();
    }
    
    @Override
    public void signalAll() {
        this.delegate().signalAll();
    }
}
