// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.locks.Condition;
import com.google.j2objc.annotations.Weak;
import com.google.common.primitives.Longs;
import java.util.concurrent.TimeUnit;
import java.time.Duration;
import com.google.common.base.Preconditions;
import java.util.function.BooleanSupplier;
import com.google.errorprone.annotations.concurrent.GuardedBy;
import javax.annotation.CheckForNull;
import java.util.concurrent.locks.ReentrantLock;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public final class Monitor
{
    private final boolean fair;
    private final ReentrantLock lock;
    @CheckForNull
    @GuardedBy("lock")
    private Guard activeGuards;
    
    public Monitor() {
        this(false);
    }
    
    public Monitor(final boolean fair) {
        this.activeGuards = null;
        this.fair = fair;
        this.lock = new ReentrantLock(fair);
    }
    
    public Guard newGuard(final BooleanSupplier isSatisfied) {
        Preconditions.checkNotNull(isSatisfied, (Object)"isSatisfied");
        return new Guard(this, this) {
            @Override
            public boolean isSatisfied() {
                return isSatisfied.getAsBoolean();
            }
        };
    }
    
    public void enter() {
        this.lock.lock();
    }
    
    public boolean enter(final Duration time) {
        return this.enter(Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
    }
    
    public boolean enter(final long time, final TimeUnit unit) {
        final long timeoutNanos = toSafeNanos(time, unit);
        final ReentrantLock lock = this.lock;
        if (!this.fair && lock.tryLock()) {
            return true;
        }
        boolean interrupted = Thread.interrupted();
        try {
            final long startTime = System.nanoTime();
            long remainingNanos = timeoutNanos;
            try {
                return lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS);
            }
            catch (InterruptedException interrupt) {
                interrupted = true;
                remainingNanos = remainingNanos(startTime, timeoutNanos);
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public void enterInterruptibly() throws InterruptedException {
        this.lock.lockInterruptibly();
    }
    
    public boolean enterInterruptibly(final Duration time) throws InterruptedException {
        return this.enterInterruptibly(Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
    }
    
    public boolean enterInterruptibly(final long time, final TimeUnit unit) throws InterruptedException {
        return this.lock.tryLock(time, unit);
    }
    
    public boolean tryEnter() {
        return this.lock.tryLock();
    }
    
    public void enterWhen(final Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        final ReentrantLock lock = this.lock;
        final boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
        lock.lockInterruptibly();
        boolean satisfied = false;
        try {
            if (!guard.isSatisfied()) {
                this.await(guard, signalBeforeWaiting);
            }
            satisfied = true;
        }
        finally {
            if (!satisfied) {
                this.leave();
            }
        }
    }
    
    public boolean enterWhen(final Guard guard, final Duration time) throws InterruptedException {
        return this.enterWhen(guard, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
    }
    
    public boolean enterWhen(final Guard guard, final long time, final TimeUnit unit) throws InterruptedException {
        final long timeoutNanos = toSafeNanos(time, unit);
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        final ReentrantLock lock = this.lock;
        final boolean reentrant = lock.isHeldByCurrentThread();
        long startTime = 0L;
        Label_0092: {
            if (!this.fair) {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                if (lock.tryLock()) {
                    break Label_0092;
                }
            }
            startTime = initNanoTime(timeoutNanos);
            if (!lock.tryLock(time, unit)) {
                return false;
            }
        }
        boolean satisfied = false;
        boolean threw = true;
        try {
            satisfied = (guard.isSatisfied() || this.awaitNanos(guard, (startTime == 0L) ? timeoutNanos : remainingNanos(startTime, timeoutNanos), reentrant));
            threw = false;
            return satisfied;
        }
        finally {
            if (!satisfied) {
                try {
                    if (threw && !reentrant) {
                        this.signalNextWaiter();
                    }
                }
                finally {
                    lock.unlock();
                }
            }
        }
    }
    
    public void enterWhenUninterruptibly(final Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        final ReentrantLock lock = this.lock;
        final boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
        lock.lock();
        boolean satisfied = false;
        try {
            if (!guard.isSatisfied()) {
                this.awaitUninterruptibly(guard, signalBeforeWaiting);
            }
            satisfied = true;
        }
        finally {
            if (!satisfied) {
                this.leave();
            }
        }
    }
    
    public boolean enterWhenUninterruptibly(final Guard guard, final Duration time) {
        return this.enterWhenUninterruptibly(guard, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
    }
    
    public boolean enterWhenUninterruptibly(final Guard guard, final long time, final TimeUnit unit) {
        final long timeoutNanos = toSafeNanos(time, unit);
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        final ReentrantLock lock = this.lock;
        long startTime = 0L;
        boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
        boolean interrupted = Thread.interrupted();
        try {
            if (this.fair || !lock.tryLock()) {
                startTime = initNanoTime(timeoutNanos);
                long remainingNanos = timeoutNanos;
                while (true) {
                    try {
                        if (!lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS)) {
                            return false;
                        }
                    }
                    catch (InterruptedException interrupt) {
                        interrupted = true;
                        remainingNanos = remainingNanos(startTime, timeoutNanos);
                        continue;
                    }
                    break;
                }
            }
            boolean satisfied = false;
            try {
                if (guard.isSatisfied()) {
                    satisfied = true;
                }
                else {
                    long remainingNanos2;
                    if (startTime == 0L) {
                        startTime = initNanoTime(timeoutNanos);
                        remainingNanos2 = timeoutNanos;
                    }
                    else {
                        remainingNanos2 = remainingNanos(startTime, timeoutNanos);
                    }
                    satisfied = this.awaitNanos(guard, remainingNanos2, signalBeforeWaiting);
                }
                return satisfied;
            }
            catch (InterruptedException interrupt2) {
                interrupted = true;
                signalBeforeWaiting = false;
            }
            finally {
                if (!satisfied) {
                    lock.unlock();
                }
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public boolean enterIf(final Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        final ReentrantLock lock = this.lock;
        lock.lock();
        boolean satisfied = false;
        try {
            return satisfied = guard.isSatisfied();
        }
        finally {
            if (!satisfied) {
                lock.unlock();
            }
        }
    }
    
    public boolean enterIf(final Guard guard, final Duration time) {
        return this.enterIf(guard, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
    }
    
    public boolean enterIf(final Guard guard, final long time, final TimeUnit unit) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        if (!this.enter(time, unit)) {
            return false;
        }
        boolean satisfied = false;
        try {
            return satisfied = guard.isSatisfied();
        }
        finally {
            if (!satisfied) {
                this.lock.unlock();
            }
        }
    }
    
    public boolean enterIfInterruptibly(final Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        boolean satisfied = false;
        try {
            return satisfied = guard.isSatisfied();
        }
        finally {
            if (!satisfied) {
                lock.unlock();
            }
        }
    }
    
    public boolean enterIfInterruptibly(final Guard guard, final Duration time) throws InterruptedException {
        return this.enterIfInterruptibly(guard, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
    }
    
    public boolean enterIfInterruptibly(final Guard guard, final long time, final TimeUnit unit) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        final ReentrantLock lock = this.lock;
        if (!lock.tryLock(time, unit)) {
            return false;
        }
        boolean satisfied = false;
        try {
            return satisfied = guard.isSatisfied();
        }
        finally {
            if (!satisfied) {
                lock.unlock();
            }
        }
    }
    
    public boolean tryEnterIf(final Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        final ReentrantLock lock = this.lock;
        if (!lock.tryLock()) {
            return false;
        }
        boolean satisfied = false;
        try {
            return satisfied = guard.isSatisfied();
        }
        finally {
            if (!satisfied) {
                lock.unlock();
            }
        }
    }
    
    public void waitFor(final Guard guard) throws InterruptedException {
        if (!(guard.monitor == this & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        if (!guard.isSatisfied()) {
            this.await(guard, true);
        }
    }
    
    public boolean waitFor(final Guard guard, final Duration time) throws InterruptedException {
        return this.waitFor(guard, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
    }
    
    public boolean waitFor(final Guard guard, final long time, final TimeUnit unit) throws InterruptedException {
        final long timeoutNanos = toSafeNanos(time, unit);
        if (!(guard.monitor == this & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        if (guard.isSatisfied()) {
            return true;
        }
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return this.awaitNanos(guard, timeoutNanos, true);
    }
    
    public void waitForUninterruptibly(final Guard guard) {
        if (!(guard.monitor == this & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        if (!guard.isSatisfied()) {
            this.awaitUninterruptibly(guard, true);
        }
    }
    
    public boolean waitForUninterruptibly(final Guard guard, final Duration time) {
        return this.waitForUninterruptibly(guard, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
    }
    
    public boolean waitForUninterruptibly(final Guard guard, final long time, final TimeUnit unit) {
        final long timeoutNanos = toSafeNanos(time, unit);
        if (!(guard.monitor == this & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        if (guard.isSatisfied()) {
            return true;
        }
        boolean signalBeforeWaiting = true;
        final long startTime = initNanoTime(timeoutNanos);
        boolean interrupted = Thread.interrupted();
        try {
            long remainingNanos = timeoutNanos;
            try {
                return this.awaitNanos(guard, remainingNanos, signalBeforeWaiting);
            }
            catch (InterruptedException interrupt) {
                interrupted = true;
                if (guard.isSatisfied()) {
                    return true;
                }
                signalBeforeWaiting = false;
                remainingNanos = remainingNanos(startTime, timeoutNanos);
                return this.awaitNanos(guard, remainingNanos, signalBeforeWaiting);
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public void leave() {
        final ReentrantLock lock = this.lock;
        try {
            if (lock.getHoldCount() == 1) {
                this.signalNextWaiter();
            }
        }
        finally {
            lock.unlock();
        }
    }
    
    public boolean isFair() {
        return this.fair;
    }
    
    public boolean isOccupied() {
        return this.lock.isLocked();
    }
    
    public boolean isOccupiedByCurrentThread() {
        return this.lock.isHeldByCurrentThread();
    }
    
    public int getOccupiedDepth() {
        return this.lock.getHoldCount();
    }
    
    public int getQueueLength() {
        return this.lock.getQueueLength();
    }
    
    public boolean hasQueuedThreads() {
        return this.lock.hasQueuedThreads();
    }
    
    public boolean hasQueuedThread(final Thread thread) {
        return this.lock.hasQueuedThread(thread);
    }
    
    public boolean hasWaiters(final Guard guard) {
        return this.getWaitQueueLength(guard) > 0;
    }
    
    public int getWaitQueueLength(final Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        this.lock.lock();
        try {
            return guard.waiterCount;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    private static long toSafeNanos(final long time, final TimeUnit unit) {
        final long timeoutNanos = unit.toNanos(time);
        return Longs.constrainToRange(timeoutNanos, 0L, 6917529027641081853L);
    }
    
    private static long initNanoTime(final long timeoutNanos) {
        if (timeoutNanos <= 0L) {
            return 0L;
        }
        final long startTime = System.nanoTime();
        return (startTime == 0L) ? 1L : startTime;
    }
    
    private static long remainingNanos(final long startTime, final long timeoutNanos) {
        return (timeoutNanos <= 0L) ? 0L : (timeoutNanos - (System.nanoTime() - startTime));
    }
    
    @GuardedBy("lock")
    private void signalNextWaiter() {
        for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
            if (this.isSatisfied(guard)) {
                guard.condition.signal();
                break;
            }
        }
    }
    
    @GuardedBy("lock")
    private boolean isSatisfied(final Guard guard) {
        try {
            return guard.isSatisfied();
        }
        catch (Throwable throwable) {
            this.signalAllWaiters();
            throw throwable;
        }
    }
    
    @GuardedBy("lock")
    private void signalAllWaiters() {
        for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
            guard.condition.signalAll();
        }
    }
    
    @GuardedBy("lock")
    private void beginWaitingFor(final Guard guard) {
        final int waiters = guard.waiterCount++;
        if (waiters == 0) {
            guard.next = this.activeGuards;
            this.activeGuards = guard;
        }
    }
    
    @GuardedBy("lock")
    private void endWaitingFor(final Guard guard) {
        final int waiterCount = guard.waiterCount - 1;
        guard.waiterCount = waiterCount;
        final int waiters = waiterCount;
        if (waiters == 0) {
            Guard p = this.activeGuards;
            Guard pred = null;
            while (p != guard) {
                pred = p;
                p = p.next;
            }
            if (pred == null) {
                this.activeGuards = p.next;
            }
            else {
                pred.next = p.next;
            }
            p.next = null;
        }
    }
    
    @GuardedBy("lock")
    private void await(final Guard guard, final boolean signalBeforeWaiting) throws InterruptedException {
        if (signalBeforeWaiting) {
            this.signalNextWaiter();
        }
        this.beginWaitingFor(guard);
        try {
            do {
                guard.condition.await();
            } while (!guard.isSatisfied());
        }
        finally {
            this.endWaitingFor(guard);
        }
    }
    
    @GuardedBy("lock")
    private void awaitUninterruptibly(final Guard guard, final boolean signalBeforeWaiting) {
        if (signalBeforeWaiting) {
            this.signalNextWaiter();
        }
        this.beginWaitingFor(guard);
        try {
            do {
                guard.condition.awaitUninterruptibly();
            } while (!guard.isSatisfied());
        }
        finally {
            this.endWaitingFor(guard);
        }
    }
    
    @GuardedBy("lock")
    private boolean awaitNanos(final Guard guard, long nanos, final boolean signalBeforeWaiting) throws InterruptedException {
        boolean firstTime = true;
        try {
            while (nanos > 0L) {
                if (firstTime) {
                    if (signalBeforeWaiting) {
                        this.signalNextWaiter();
                    }
                    this.beginWaitingFor(guard);
                    firstTime = false;
                }
                nanos = guard.condition.awaitNanos(nanos);
                if (guard.isSatisfied()) {
                    return true;
                }
            }
            return false;
        }
        finally {
            if (!firstTime) {
                this.endWaitingFor(guard);
            }
        }
    }
    
    @Beta
    public abstract static class Guard
    {
        @Weak
        final Monitor monitor;
        final Condition condition;
        @GuardedBy("monitor.lock")
        int waiterCount;
        @CheckForNull
        @GuardedBy("monitor.lock")
        Guard next;
        
        protected Guard(final Monitor monitor) {
            this.waiterCount = 0;
            this.monitor = Preconditions.checkNotNull(monitor, (Object)"monitor");
            this.condition = monitor.lock.newCondition();
        }
        
        public abstract boolean isSatisfied();
    }
}
