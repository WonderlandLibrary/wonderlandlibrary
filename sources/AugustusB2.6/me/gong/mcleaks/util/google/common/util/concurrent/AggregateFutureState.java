// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import java.util.logging.Level;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import me.gong.mcleaks.util.google.common.collect.Sets;
import java.util.logging.Logger;
import java.util.Set;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
abstract class AggregateFutureState
{
    private volatile Set<Throwable> seenExceptions;
    private volatile int remaining;
    private static final AtomicHelper ATOMIC_HELPER;
    private static final Logger log;
    
    AggregateFutureState(final int remainingFutures) {
        this.seenExceptions = null;
        this.remaining = remainingFutures;
    }
    
    final Set<Throwable> getOrInitSeenExceptions() {
        Set<Throwable> seenExceptionsLocal = this.seenExceptions;
        if (seenExceptionsLocal == null) {
            seenExceptionsLocal = Sets.newConcurrentHashSet();
            this.addInitialException(seenExceptionsLocal);
            AggregateFutureState.ATOMIC_HELPER.compareAndSetSeenExceptions(this, null, seenExceptionsLocal);
            seenExceptionsLocal = this.seenExceptions;
        }
        return seenExceptionsLocal;
    }
    
    abstract void addInitialException(final Set<Throwable> p0);
    
    final int decrementRemainingAndGet() {
        return AggregateFutureState.ATOMIC_HELPER.decrementAndGetRemainingCount(this);
    }
    
    static {
        log = Logger.getLogger(AggregateFutureState.class.getName());
        AtomicHelper helper;
        try {
            helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(AggregateFutureState.class, Set.class, "seenExceptions"), AtomicIntegerFieldUpdater.newUpdater(AggregateFutureState.class, "remaining"));
        }
        catch (Throwable reflectionFailure) {
            AggregateFutureState.log.log(Level.SEVERE, "SafeAtomicHelper is broken!", reflectionFailure);
            helper = new SynchronizedAtomicHelper();
        }
        ATOMIC_HELPER = helper;
    }
    
    private abstract static class AtomicHelper
    {
        abstract void compareAndSetSeenExceptions(final AggregateFutureState p0, final Set<Throwable> p1, final Set<Throwable> p2);
        
        abstract int decrementAndGetRemainingCount(final AggregateFutureState p0);
    }
    
    private static final class SafeAtomicHelper extends AtomicHelper
    {
        final AtomicReferenceFieldUpdater<AggregateFutureState, Set<Throwable>> seenExceptionsUpdater;
        final AtomicIntegerFieldUpdater<AggregateFutureState> remainingCountUpdater;
        
        SafeAtomicHelper(final AtomicReferenceFieldUpdater seenExceptionsUpdater, final AtomicIntegerFieldUpdater remainingCountUpdater) {
            this.seenExceptionsUpdater = (AtomicReferenceFieldUpdater<AggregateFutureState, Set<Throwable>>)seenExceptionsUpdater;
            this.remainingCountUpdater = (AtomicIntegerFieldUpdater<AggregateFutureState>)remainingCountUpdater;
        }
        
        @Override
        void compareAndSetSeenExceptions(final AggregateFutureState state, final Set<Throwable> expect, final Set<Throwable> update) {
            this.seenExceptionsUpdater.compareAndSet(state, expect, update);
        }
        
        @Override
        int decrementAndGetRemainingCount(final AggregateFutureState state) {
            return this.remainingCountUpdater.decrementAndGet(state);
        }
    }
    
    private static final class SynchronizedAtomicHelper extends AtomicHelper
    {
        @Override
        void compareAndSetSeenExceptions(final AggregateFutureState state, final Set<Throwable> expect, final Set<Throwable> update) {
            synchronized (state) {
                if (state.seenExceptions == expect) {
                    state.seenExceptions = update;
                }
            }
        }
        
        @Override
        int decrementAndGetRemainingCount(final AggregateFutureState state) {
            synchronized (state) {
                state.remaining--;
                return state.remaining;
            }
        }
    }
}
