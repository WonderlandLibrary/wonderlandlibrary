/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.concurrent.GuardedBy
 */
package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.concurrent.GuardedBy;

@Beta
public final class Monitor {
    private final boolean fair;
    private final ReentrantLock lock;
    @GuardedBy(value="lock")
    private Guard activeGuards = null;

    public Monitor() {
        this(false);
    }

    public Monitor(boolean fair) {
        this.fair = fair;
        this.lock = new ReentrantLock(fair);
    }

    public void enter() {
        this.lock.lock();
    }

    public void enterInterruptibly() throws InterruptedException {
        this.lock.lockInterruptibly();
    }

    public boolean enter(long time, TimeUnit unit) {
        long timeoutNanos = unit.toNanos(time);
        ReentrantLock lock = this.lock;
        if (!this.fair && lock.tryLock()) {
            return true;
        }
        long deadline = System.nanoTime() + timeoutNanos;
        boolean interrupted = Thread.interrupted();
        while (true) {
            try {
                boolean bl = lock.tryLock(timeoutNanos, TimeUnit.NANOSECONDS);
                return bl;
            }
            catch (InterruptedException interrupt) {
                interrupted = true;
                timeoutNanos = deadline - System.nanoTime();
                continue;
            }
            break;
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean enterInterruptibly(long time, TimeUnit unit) throws InterruptedException {
        return this.lock.tryLock(time, unit);
    }

    public boolean tryEnter() {
        return this.lock.tryLock();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void enterWhen(Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void enterWhenUninterruptibly(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean enterWhen(Guard guard, long time, TimeUnit unit) throws InterruptedException {
        long timeoutNanos = unit.toNanos(time);
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        boolean reentrant = lock.isHeldByCurrentThread();
        if (this.fair || !lock.tryLock()) {
            long deadline = System.nanoTime() + timeoutNanos;
            if (!lock.tryLock(time, unit)) {
                return false;
            }
            timeoutNanos = deadline - System.nanoTime();
        }
        boolean satisfied = false;
        boolean threw = true;
        try {
            satisfied = guard.isSatisfied() || this.awaitNanos(guard, timeoutNanos, reentrant);
            threw = false;
            boolean bl = satisfied;
            return bl;
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

    /*
     * Loose catch block
     */
    public boolean enterWhenUninterruptibly(Guard guard, long time, TimeUnit unit) {
        long timeoutNanos = unit.toNanos(time);
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        long deadline = System.nanoTime() + timeoutNanos;
        boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
        boolean interrupted = Thread.interrupted();
        try {
            if (this.fair || !lock.tryLock()) {
                boolean locked = false;
                do {
                    try {
                        locked = lock.tryLock(timeoutNanos, TimeUnit.NANOSECONDS);
                        if (!locked) {
                            boolean bl = false;
                            return bl;
                        }
                    }
                    catch (InterruptedException interrupt) {
                        interrupted = true;
                    }
                    timeoutNanos = deadline - System.nanoTime();
                } while (!locked);
            }
            boolean satisfied = false;
            while (true) {
                try {
                    satisfied = guard.isSatisfied() || this.awaitNanos(guard, timeoutNanos, signalBeforeWaiting);
                    boolean interrupt = satisfied;
                    return interrupt;
                }
                catch (InterruptedException interrupt) {
                    interrupted = true;
                    signalBeforeWaiting = false;
                    timeoutNanos = deadline - System.nanoTime();
                    continue;
                }
                break;
            }
            finally {
                if (!satisfied) {
                    lock.unlock();
                }
            }
            {
                catch (Throwable throwable) {
                    throw throwable;
                }
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean enterIf(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        lock.lock();
        boolean satisfied = false;
        try {
            boolean bl = satisfied = guard.isSatisfied();
            return bl;
        }
        finally {
            if (!satisfied) {
                lock.unlock();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean enterIfInterruptibly(Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        boolean satisfied = false;
        try {
            boolean bl = satisfied = guard.isSatisfied();
            return bl;
        }
        finally {
            if (!satisfied) {
                lock.unlock();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean enterIf(Guard guard, long time, TimeUnit unit) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        if (!this.enter(time, unit)) {
            return false;
        }
        boolean satisfied = false;
        try {
            boolean bl = satisfied = guard.isSatisfied();
            return bl;
        }
        finally {
            if (!satisfied) {
                this.lock.unlock();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean enterIfInterruptibly(Guard guard, long time, TimeUnit unit) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        if (!lock.tryLock(time, unit)) {
            return false;
        }
        boolean satisfied = false;
        try {
            boolean bl = satisfied = guard.isSatisfied();
            return bl;
        }
        finally {
            if (!satisfied) {
                lock.unlock();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean tryEnterIf(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        if (!lock.tryLock()) {
            return false;
        }
        boolean satisfied = false;
        try {
            boolean bl = satisfied = guard.isSatisfied();
            return bl;
        }
        finally {
            if (!satisfied) {
                lock.unlock();
            }
        }
    }

    public void waitFor(Guard guard) throws InterruptedException {
        if (!(guard.monitor == this & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        if (!guard.isSatisfied()) {
            this.await(guard, true);
        }
    }

    public void waitForUninterruptibly(Guard guard) {
        if (!(guard.monitor == this & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        if (!guard.isSatisfied()) {
            this.awaitUninterruptibly(guard, true);
        }
    }

    public boolean waitFor(Guard guard, long time, TimeUnit unit) throws InterruptedException {
        long timeoutNanos = unit.toNanos(time);
        if (!(guard.monitor == this & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        return guard.isSatisfied() || this.awaitNanos(guard, timeoutNanos, true);
    }

    public boolean waitForUninterruptibly(Guard guard, long time, TimeUnit unit) {
        long timeoutNanos = unit.toNanos(time);
        if (!(guard.monitor == this & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        if (guard.isSatisfied()) {
            return true;
        }
        boolean signalBeforeWaiting = true;
        long deadline = System.nanoTime() + timeoutNanos;
        boolean interrupted = Thread.interrupted();
        while (true) {
            try {
                boolean bl = this.awaitNanos(guard, timeoutNanos, signalBeforeWaiting);
                return bl;
            }
            catch (InterruptedException interrupt) {
                interrupted = true;
                if (guard.isSatisfied()) {
                    boolean bl = true;
                    return bl;
                }
                signalBeforeWaiting = false;
                timeoutNanos = deadline - System.nanoTime();
                continue;
            }
            break;
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void leave() {
        ReentrantLock lock = this.lock;
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

    public boolean hasQueuedThread(Thread thread) {
        return this.lock.hasQueuedThread(thread);
    }

    public boolean hasWaiters(Guard guard) {
        return this.getWaitQueueLength(guard) > 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getWaitQueueLength(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        this.lock.lock();
        try {
            int n2 = guard.waiterCount;
            return n2;
        }
        finally {
            this.lock.unlock();
        }
    }

    @GuardedBy(value="lock")
    private void signalNextWaiter() {
        Guard guard = this.activeGuards;
        while (guard != null) {
            if (this.isSatisfied(guard)) {
                guard.condition.signal();
                break;
            }
            guard = guard.next;
        }
    }

    @GuardedBy(value="lock")
    private boolean isSatisfied(Guard guard) {
        try {
            return guard.isSatisfied();
        }
        catch (Throwable throwable) {
            this.signalAllWaiters();
            throw Throwables.propagate(throwable);
        }
    }

    @GuardedBy(value="lock")
    private void signalAllWaiters() {
        Guard guard = this.activeGuards;
        while (guard != null) {
            guard.condition.signalAll();
            guard = guard.next;
        }
    }

    @GuardedBy(value="lock")
    private void beginWaitingFor(Guard guard) {
        int waiters;
        if ((waiters = guard.waiterCount++) == 0) {
            guard.next = this.activeGuards;
            this.activeGuards = guard;
        }
    }

    @GuardedBy(value="lock")
    private void endWaitingFor(Guard guard) {
        int waiters;
        if ((waiters = --guard.waiterCount) == 0) {
            Guard p2 = this.activeGuards;
            Guard pred = null;
            while (true) {
                if (p2 == guard) {
                    if (pred == null) {
                        this.activeGuards = p2.next;
                    } else {
                        pred.next = p2.next;
                    }
                    p2.next = null;
                    break;
                }
                pred = p2;
                p2 = p2.next;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @GuardedBy(value="lock")
    private void await(Guard guard, boolean signalBeforeWaiting) throws InterruptedException {
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @GuardedBy(value="lock")
    private void awaitUninterruptibly(Guard guard, boolean signalBeforeWaiting) {
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @GuardedBy(value="lock")
    private boolean awaitNanos(Guard guard, long nanos, boolean signalBeforeWaiting) throws InterruptedException {
        if (signalBeforeWaiting) {
            this.signalNextWaiter();
        }
        this.beginWaitingFor(guard);
        try {
            do {
                if (nanos < 0L) {
                    boolean bl = false;
                    return bl;
                }
                nanos = guard.condition.awaitNanos(nanos);
            } while (!guard.isSatisfied());
            boolean bl = true;
            return bl;
        }
        finally {
            this.endWaitingFor(guard);
        }
    }

    @Beta
    public static abstract class Guard {
        final Monitor monitor;
        final Condition condition;
        @GuardedBy(value="monitor.lock")
        int waiterCount = 0;
        @GuardedBy(value="monitor.lock")
        Guard next;

        protected Guard(Monitor monitor) {
            this.monitor = Preconditions.checkNotNull(monitor, "monitor");
            this.condition = monitor.lock.newCondition();
        }

        public abstract boolean isSatisfied();
    }
}

