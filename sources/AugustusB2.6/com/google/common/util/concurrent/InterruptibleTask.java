// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.annotations.VisibleForTesting;
import java.util.concurrent.locks.AbstractOwnableSynchronizer;
import java.util.concurrent.locks.LockSupport;
import com.google.j2objc.annotations.ReflectionSupport;
import com.google.common.annotations.GwtCompatible;
import java.util.concurrent.atomic.AtomicReference;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
@ReflectionSupport(ReflectionSupport.Level.FULL)
abstract class InterruptibleTask<T> extends AtomicReference<Runnable> implements Runnable
{
    private static final Runnable DONE;
    private static final Runnable PARKED;
    private static final int MAX_BUSY_WAIT_SPINS = 1000;
    
    @Override
    public final void run() {
        final Thread currentThread = Thread.currentThread();
        if (!this.compareAndSet(null, currentThread)) {
            return;
        }
        final boolean run = !this.isDone();
        T result = null;
        Throwable error = null;
        try {
            if (run) {
                result = this.runInterruptibly();
            }
        }
        catch (Throwable t) {
            error = t;
        }
        finally {
            if (!this.compareAndSet(currentThread, InterruptibleTask.DONE)) {
                this.waitForInterrupt(currentThread);
            }
            if (run) {
                if (error == null) {
                    this.afterRanInterruptiblySuccess(NullnessCasts.uncheckedCastNullableTToT(result));
                }
                else {
                    this.afterRanInterruptiblyFailure(error);
                }
            }
        }
    }
    
    private void waitForInterrupt(final Thread currentThread) {
        boolean restoreInterruptedBit = false;
        int spinCount = 0;
        Runnable state = this.get();
        Blocker blocker = null;
        while (state instanceof Blocker || state == InterruptibleTask.PARKED) {
            if (state instanceof Blocker) {
                blocker = (Blocker)state;
            }
            if (++spinCount > 1000) {
                if (state == InterruptibleTask.PARKED || this.compareAndSet(state, InterruptibleTask.PARKED)) {
                    restoreInterruptedBit = (Thread.interrupted() || restoreInterruptedBit);
                    LockSupport.park(blocker);
                }
            }
            else {
                Thread.yield();
            }
            state = this.get();
        }
        if (restoreInterruptedBit) {
            currentThread.interrupt();
        }
    }
    
    abstract boolean isDone();
    
    @ParametricNullness
    abstract T runInterruptibly() throws Exception;
    
    abstract void afterRanInterruptiblySuccess(@ParametricNullness final T p0);
    
    abstract void afterRanInterruptiblyFailure(final Throwable p0);
    
    final void interruptTask() {
        final Runnable currentRunner = this.get();
        if (currentRunner instanceof Thread) {
            final Blocker blocker = new Blocker(this);
            blocker.setOwner(Thread.currentThread());
            if (this.compareAndSet(currentRunner, blocker)) {
                try {
                    ((Thread)currentRunner).interrupt();
                }
                finally {
                    final Runnable prev = this.getAndSet(InterruptibleTask.DONE);
                    if (prev == InterruptibleTask.PARKED) {
                        LockSupport.unpark((Thread)currentRunner);
                    }
                }
            }
        }
    }
    
    @Override
    public final String toString() {
        final Runnable state = this.get();
        String result;
        if (state == InterruptibleTask.DONE) {
            result = "running=[DONE]";
        }
        else if (state instanceof Blocker) {
            result = "running=[INTERRUPTED]";
        }
        else if (state instanceof Thread) {
            final String name = ((Thread)state).getName();
            result = new StringBuilder(21 + String.valueOf(name).length()).append("running=[RUNNING ON ").append(name).append("]").toString();
        }
        else {
            result = "running=[NOT STARTED YET]";
        }
        final String pendingString = this.toPendingString();
        return new StringBuilder(2 + String.valueOf(result).length() + String.valueOf(pendingString).length()).append(result).append(", ").append(pendingString).toString();
    }
    
    abstract String toPendingString();
    
    static {
        DONE = new DoNothingRunnable();
        PARKED = new DoNothingRunnable();
    }
    
    private static final class DoNothingRunnable implements Runnable
    {
        @Override
        public void run() {
        }
    }
    
    @VisibleForTesting
    static final class Blocker extends AbstractOwnableSynchronizer implements Runnable
    {
        private final InterruptibleTask<?> task;
        
        private Blocker(final InterruptibleTask<?> task) {
            this.task = task;
        }
        
        @Override
        public void run() {
        }
        
        private void setOwner(final Thread thread) {
            super.setExclusiveOwnerThread(thread);
        }
        
        @Override
        public String toString() {
            return this.task.toString();
        }
    }
}
