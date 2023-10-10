// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@ElementTypesAreNonnullByDefault
abstract class ForwardingLock implements Lock
{
    abstract Lock delegate();
    
    @Override
    public void lock() {
        this.delegate().lock();
    }
    
    @Override
    public void lockInterruptibly() throws InterruptedException {
        this.delegate().lockInterruptibly();
    }
    
    @Override
    public boolean tryLock() {
        return this.delegate().tryLock();
    }
    
    @Override
    public boolean tryLock(final long time, final TimeUnit unit) throws InterruptedException {
        return this.delegate().tryLock(time, unit);
    }
    
    @Override
    public void unlock() {
        this.delegate().unlock();
    }
    
    @Override
    public Condition newCondition() {
        return this.delegate().newCondition();
    }
}
