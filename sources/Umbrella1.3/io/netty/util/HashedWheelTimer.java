/*
 * Decompiled with CFR 0.150.
 */
package io.netty.util;

import io.netty.util.ResourceLeak;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HashedWheelTimer
implements Timer {
    static final InternalLogger logger = InternalLoggerFactory.getInstance(HashedWheelTimer.class);
    private static final ResourceLeakDetector<HashedWheelTimer> leakDetector = new ResourceLeakDetector(HashedWheelTimer.class, 1, (long)(Runtime.getRuntime().availableProcessors() * 4));
    private final ResourceLeak leak;
    private final Worker worker = new Worker();
    final Thread workerThread;
    public static final int WORKER_STATE_INIT = 0;
    public static final int WORKER_STATE_STARTED = 1;
    public static final int WORKER_STATE_SHUTDOWN = 2;
    final AtomicInteger workerState = new AtomicInteger();
    final long tickDuration;
    final Set<HashedWheelTimeout>[] wheel;
    final int mask;
    final ReadWriteLock lock = new ReentrantReadWriteLock();
    final CountDownLatch startTimeInitialized = new CountDownLatch(1);
    volatile long startTime;
    volatile long tick;

    public HashedWheelTimer() {
        this(Executors.defaultThreadFactory());
    }

    public HashedWheelTimer(long tickDuration, TimeUnit unit) {
        this(Executors.defaultThreadFactory(), tickDuration, unit);
    }

    public HashedWheelTimer(long tickDuration, TimeUnit unit, int ticksPerWheel) {
        this(Executors.defaultThreadFactory(), tickDuration, unit, ticksPerWheel);
    }

    public HashedWheelTimer(ThreadFactory threadFactory) {
        this(threadFactory, 100L, TimeUnit.MILLISECONDS);
    }

    public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit) {
        this(threadFactory, tickDuration, unit, 512);
    }

    public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel) {
        if (threadFactory == null) {
            throw new NullPointerException("threadFactory");
        }
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (tickDuration <= 0L) {
            throw new IllegalArgumentException("tickDuration must be greater than 0: " + tickDuration);
        }
        if (ticksPerWheel <= 0) {
            throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + ticksPerWheel);
        }
        this.wheel = HashedWheelTimer.createWheel(ticksPerWheel);
        this.mask = this.wheel.length - 1;
        this.tickDuration = unit.toNanos(tickDuration);
        if (this.tickDuration >= Long.MAX_VALUE / (long)this.wheel.length) {
            throw new IllegalArgumentException(String.format("tickDuration: %d (expected: 0 < tickDuration in nanos < %d", tickDuration, Long.MAX_VALUE / (long)this.wheel.length));
        }
        this.workerThread = threadFactory.newThread(this.worker);
        this.leak = leakDetector.open(this);
    }

    private static Set<HashedWheelTimeout>[] createWheel(int ticksPerWheel) {
        if (ticksPerWheel <= 0) {
            throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + ticksPerWheel);
        }
        if (ticksPerWheel > 0x40000000) {
            throw new IllegalArgumentException("ticksPerWheel may not be greater than 2^30: " + ticksPerWheel);
        }
        ticksPerWheel = HashedWheelTimer.normalizeTicksPerWheel(ticksPerWheel);
        Set[] wheel = new Set[ticksPerWheel];
        for (int i = 0; i < wheel.length; ++i) {
            wheel[i] = Collections.newSetFromMap(PlatformDependent.newConcurrentHashMap());
        }
        return wheel;
    }

    private static int normalizeTicksPerWheel(int ticksPerWheel) {
        int normalizedTicksPerWheel;
        for (normalizedTicksPerWheel = 1; normalizedTicksPerWheel < ticksPerWheel; normalizedTicksPerWheel <<= 1) {
        }
        return normalizedTicksPerWheel;
    }

    public void start() {
        switch (this.workerState.get()) {
            case 0: {
                if (!this.workerState.compareAndSet(0, 1)) break;
                this.workerThread.start();
                break;
            }
            case 1: {
                break;
            }
            case 2: {
                throw new IllegalStateException("cannot be started once stopped");
            }
            default: {
                throw new Error("Invalid WorkerState");
            }
        }
        while (this.startTime == 0L) {
            try {
                this.startTimeInitialized.await();
            }
            catch (InterruptedException interruptedException) {}
        }
    }

    @Override
    public Set<Timeout> stop() {
        if (Thread.currentThread() == this.workerThread) {
            throw new IllegalStateException(HashedWheelTimer.class.getSimpleName() + ".stop() cannot be called from " + TimerTask.class.getSimpleName());
        }
        if (!this.workerState.compareAndSet(1, 2)) {
            this.workerState.set(2);
            if (this.leak != null) {
                this.leak.close();
            }
            return Collections.emptySet();
        }
        boolean interrupted = false;
        while (this.workerThread.isAlive()) {
            this.workerThread.interrupt();
            try {
                this.workerThread.join(100L);
            }
            catch (InterruptedException e) {
                interrupted = true;
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        if (this.leak != null) {
            this.leak.close();
        }
        HashSet<HashedWheelTimeout> unprocessedTimeouts = new HashSet<HashedWheelTimeout>();
        for (Set<HashedWheelTimeout> bucket : this.wheel) {
            unprocessedTimeouts.addAll(bucket);
            bucket.clear();
        }
        return Collections.unmodifiableSet(unprocessedTimeouts);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
        HashedWheelTimeout timeout;
        this.start();
        if (task == null) {
            throw new NullPointerException("task");
        }
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        long deadline = System.nanoTime() + unit.toNanos(delay) - this.startTime;
        this.lock.readLock().lock();
        try {
            timeout = new HashedWheelTimeout(task, deadline);
            if (this.workerState.get() == 2) {
                throw new IllegalStateException("Cannot enqueue after shutdown");
            }
            this.wheel[timeout.stopIndex].add(timeout);
        }
        finally {
            this.lock.readLock().unlock();
        }
        return timeout;
    }

    private final class HashedWheelTimeout
    implements Timeout {
        private static final int ST_INIT = 0;
        private static final int ST_CANCELLED = 1;
        private static final int ST_EXPIRED = 2;
        private final TimerTask task;
        final long deadline;
        final int stopIndex;
        volatile long remainingRounds;
        private final AtomicInteger state = new AtomicInteger(0);

        HashedWheelTimeout(TimerTask task, long deadline) {
            this.task = task;
            this.deadline = deadline;
            long calculated = deadline / HashedWheelTimer.this.tickDuration;
            long ticks = Math.max(calculated, HashedWheelTimer.this.tick);
            this.stopIndex = (int)(ticks & (long)HashedWheelTimer.this.mask);
            this.remainingRounds = (calculated - HashedWheelTimer.this.tick) / (long)HashedWheelTimer.this.wheel.length;
        }

        @Override
        public Timer timer() {
            return HashedWheelTimer.this;
        }

        @Override
        public TimerTask task() {
            return this.task;
        }

        @Override
        public boolean cancel() {
            if (!this.state.compareAndSet(0, 1)) {
                return false;
            }
            HashedWheelTimer.this.wheel[this.stopIndex].remove(this);
            return true;
        }

        @Override
        public boolean isCancelled() {
            return this.state.get() == 1;
        }

        @Override
        public boolean isExpired() {
            return this.state.get() != 0;
        }

        public void expire() {
            block3: {
                if (!this.state.compareAndSet(0, 2)) {
                    return;
                }
                try {
                    this.task.run(this);
                }
                catch (Throwable t) {
                    if (!logger.isWarnEnabled()) break block3;
                    logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName() + '.', t);
                }
            }
        }

        public String toString() {
            long currentTime = System.nanoTime();
            long remaining = this.deadline - currentTime + HashedWheelTimer.this.startTime;
            StringBuilder buf = new StringBuilder(192);
            buf.append(StringUtil.simpleClassName(this));
            buf.append('(');
            buf.append("deadline: ");
            if (remaining > 0L) {
                buf.append(remaining);
                buf.append(" ns later");
            } else if (remaining < 0L) {
                buf.append(-remaining);
                buf.append(" ns ago");
            } else {
                buf.append("now");
            }
            if (this.isCancelled()) {
                buf.append(", cancelled");
            }
            buf.append(", task: ");
            buf.append(this.task());
            return buf.append(')').toString();
        }
    }

    private final class Worker
    implements Runnable {
        Worker() {
        }

        @Override
        public void run() {
            HashedWheelTimer.this.startTime = System.nanoTime();
            if (HashedWheelTimer.this.startTime == 0L) {
                HashedWheelTimer.this.startTime = 1L;
            }
            HashedWheelTimer.this.startTimeInitialized.countDown();
            ArrayList<HashedWheelTimeout> expiredTimeouts = new ArrayList<HashedWheelTimeout>();
            do {
                long deadline;
                if ((deadline = this.waitForNextTick()) <= 0L) continue;
                this.fetchExpiredTimeouts(expiredTimeouts, deadline);
                this.notifyExpiredTimeouts(expiredTimeouts);
            } while (HashedWheelTimer.this.workerState.get() == 1);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void fetchExpiredTimeouts(List<HashedWheelTimeout> expiredTimeouts, long deadline) {
            HashedWheelTimer.this.lock.writeLock().lock();
            try {
                this.fetchExpiredTimeouts(expiredTimeouts, HashedWheelTimer.this.wheel[(int)(HashedWheelTimer.this.tick & (long)HashedWheelTimer.this.mask)].iterator(), deadline);
            }
            finally {
                ++HashedWheelTimer.this.tick;
                HashedWheelTimer.this.lock.writeLock().unlock();
            }
        }

        private void fetchExpiredTimeouts(List<HashedWheelTimeout> expiredTimeouts, Iterator<HashedWheelTimeout> i, long deadline) {
            while (i.hasNext()) {
                HashedWheelTimeout timeout = i.next();
                if (timeout.remainingRounds <= 0L) {
                    i.remove();
                    if (timeout.deadline <= deadline) {
                        expiredTimeouts.add(timeout);
                        continue;
                    }
                    throw new Error(String.format("timeout.deadline (%d) > deadline (%d)", timeout.deadline, deadline));
                }
                --timeout.remainingRounds;
            }
        }

        private void notifyExpiredTimeouts(List<HashedWheelTimeout> expiredTimeouts) {
            for (int i = expiredTimeouts.size() - 1; i >= 0; --i) {
                expiredTimeouts.get(i).expire();
            }
            expiredTimeouts.clear();
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private long waitForNextTick() {
            long deadline = HashedWheelTimer.this.tickDuration * (HashedWheelTimer.this.tick + 1L);
            while (true) {
                long currentTime;
                long sleepTimeMs;
                if ((sleepTimeMs = (deadline - (currentTime = System.nanoTime() - HashedWheelTimer.this.startTime) + 999999L) / 1000000L) <= 0L) {
                    if (currentTime != Long.MIN_VALUE) return currentTime;
                    return -9223372036854775807L;
                }
                if (PlatformDependent.isWindows()) {
                    sleepTimeMs = sleepTimeMs / 10L * 10L;
                }
                try {
                    Thread.sleep(sleepTimeMs);
                    continue;
                }
                catch (InterruptedException e) {
                    if (HashedWheelTimer.this.workerState.get() == 2) return Long.MIN_VALUE;
                    continue;
                }
                break;
            }
        }
    }
}

