/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util;

import io.netty.util.ResourceLeak;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import io.netty.util.internal.MpscLinkedQueueNode;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Collections;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class HashedWheelTimer
implements Timer {
    private final HashedWheelBucket[] wheel;
    private volatile long startTime;
    private final Worker worker = new Worker();
    public static final int WORKER_STATE_INIT = 0;
    static final InternalLogger logger = InternalLoggerFactory.getInstance(HashedWheelTimer.class);
    private final ResourceLeak leak;
    public static final int WORKER_STATE_STARTED = 1;
    private final Queue<HashedWheelTimeout> timeouts;
    private volatile int workerState = 0;
    private static final ResourceLeakDetector<HashedWheelTimer> leakDetector = new ResourceLeakDetector(HashedWheelTimer.class, 1, (long)(Runtime.getRuntime().availableProcessors() * 4));
    private final Queue<Runnable> cancelledTimeouts;
    private final long tickDuration;
    private final Thread workerThread;
    private static final AtomicIntegerFieldUpdater<HashedWheelTimer> WORKER_STATE_UPDATER;
    private final int mask;
    public static final int WORKER_STATE_SHUTDOWN = 2;
    private final CountDownLatch startTimeInitialized = new CountDownLatch(1);

    private static HashedWheelBucket[] createWheel(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + n);
        }
        if (n > 0x40000000) {
            throw new IllegalArgumentException("ticksPerWheel may not be greater than 2^30: " + n);
        }
        n = HashedWheelTimer.normalizeTicksPerWheel(n);
        HashedWheelBucket[] hashedWheelBucketArray = new HashedWheelBucket[n];
        for (int i = 0; i < hashedWheelBucketArray.length; ++i) {
            hashedWheelBucketArray[i] = new HashedWheelBucket();
        }
        return hashedWheelBucketArray;
    }

    static {
        AtomicIntegerFieldUpdater<Object> atomicIntegerFieldUpdater = PlatformDependent.newAtomicIntegerFieldUpdater(HashedWheelTimer.class, "workerState");
        if (atomicIntegerFieldUpdater == null) {
            atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimer.class, "workerState");
        }
        WORKER_STATE_UPDATER = atomicIntegerFieldUpdater;
    }

    public HashedWheelTimer() {
        this(Executors.defaultThreadFactory());
    }

    private static int normalizeTicksPerWheel(int n) {
        int n2;
        for (n2 = 1; n2 < n; n2 <<= 1) {
        }
        return n2;
    }

    public HashedWheelTimer(long l, TimeUnit timeUnit, int n) {
        this(Executors.defaultThreadFactory(), l, timeUnit, n);
    }

    @Override
    public Set<Timeout> stop() {
        if (Thread.currentThread() == this.workerThread) {
            throw new IllegalStateException(HashedWheelTimer.class.getSimpleName() + ".stop() cannot be called from " + TimerTask.class.getSimpleName());
        }
        if (!WORKER_STATE_UPDATER.compareAndSet(this, 1, 2)) {
            WORKER_STATE_UPDATER.set(this, 2);
            if (this.leak != null) {
                this.leak.close();
            }
            return Collections.emptySet();
        }
        boolean bl = false;
        while (this.workerThread.isAlive()) {
            this.workerThread.interrupt();
            try {
                this.workerThread.join(100L);
            }
            catch (InterruptedException interruptedException) {
                bl = true;
            }
        }
        if (bl) {
            Thread.currentThread().interrupt();
        }
        if (this.leak != null) {
            this.leak.close();
        }
        return this.worker.unprocessedTimeouts();
    }

    @Override
    public Timeout newTimeout(TimerTask timerTask, long l, TimeUnit timeUnit) {
        if (timerTask == null) {
            throw new NullPointerException("task");
        }
        if (timeUnit == null) {
            throw new NullPointerException("unit");
        }
        this.start();
        long l2 = System.nanoTime() + timeUnit.toNanos(l) - this.startTime;
        HashedWheelTimeout hashedWheelTimeout = new HashedWheelTimeout(this, timerTask, l2);
        this.timeouts.add(hashedWheelTimeout);
        return hashedWheelTimeout;
    }

    public HashedWheelTimer(ThreadFactory threadFactory) {
        this(threadFactory, 100L, TimeUnit.MILLISECONDS);
    }

    public HashedWheelTimer(ThreadFactory threadFactory, long l, TimeUnit timeUnit, int n) {
        this.timeouts = PlatformDependent.newMpscQueue();
        this.cancelledTimeouts = PlatformDependent.newMpscQueue();
        if (threadFactory == null) {
            throw new NullPointerException("threadFactory");
        }
        if (timeUnit == null) {
            throw new NullPointerException("unit");
        }
        if (l <= 0L) {
            throw new IllegalArgumentException("tickDuration must be greater than 0: " + l);
        }
        if (n <= 0) {
            throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + n);
        }
        this.wheel = HashedWheelTimer.createWheel(n);
        this.mask = this.wheel.length - 1;
        this.tickDuration = timeUnit.toNanos(l);
        if (this.tickDuration >= Long.MAX_VALUE / (long)this.wheel.length) {
            throw new IllegalArgumentException(String.format("tickDuration: %d (expected: 0 < tickDuration in nanos < %d", l, Long.MAX_VALUE / (long)this.wheel.length));
        }
        this.workerThread = threadFactory.newThread(this.worker);
        this.leak = leakDetector.open(this);
    }

    public HashedWheelTimer(long l, TimeUnit timeUnit) {
        this(Executors.defaultThreadFactory(), l, timeUnit);
    }

    public void start() {
        switch (WORKER_STATE_UPDATER.get(this)) {
            case 0: {
                if (!WORKER_STATE_UPDATER.compareAndSet(this, 0, 1)) break;
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

    public HashedWheelTimer(ThreadFactory threadFactory, long l, TimeUnit timeUnit) {
        this(threadFactory, l, timeUnit, 512);
    }

    private static final class HashedWheelBucket {
        private HashedWheelTimeout tail;
        private HashedWheelTimeout head;

        public void addTimeout(HashedWheelTimeout hashedWheelTimeout) {
            assert (hashedWheelTimeout.bucket == null);
            hashedWheelTimeout.bucket = this;
            if (this.head == null) {
                this.head = this.tail = hashedWheelTimeout;
            } else {
                this.tail.next = hashedWheelTimeout;
                hashedWheelTimeout.prev = this.tail;
                this.tail = hashedWheelTimeout;
            }
        }

        private HashedWheelBucket() {
        }

        private HashedWheelTimeout pollTimeout() {
            HashedWheelTimeout hashedWheelTimeout = this.head;
            if (hashedWheelTimeout == null) {
                return null;
            }
            HashedWheelTimeout hashedWheelTimeout2 = hashedWheelTimeout.next;
            if (hashedWheelTimeout2 == null) {
                this.head = null;
                this.tail = null;
            } else {
                this.head = hashedWheelTimeout2;
                hashedWheelTimeout2.prev = null;
            }
            hashedWheelTimeout.next = null;
            hashedWheelTimeout.prev = null;
            hashedWheelTimeout.bucket = null;
            return hashedWheelTimeout;
        }

        public void clearTimeouts(Set<Timeout> set) {
            HashedWheelTimeout hashedWheelTimeout;
            while ((hashedWheelTimeout = this.pollTimeout()) != null) {
                if (hashedWheelTimeout.isExpired() || hashedWheelTimeout.isCancelled()) continue;
                set.add(hashedWheelTimeout);
            }
            return;
        }

        public void remove(HashedWheelTimeout hashedWheelTimeout) {
            HashedWheelTimeout hashedWheelTimeout2 = hashedWheelTimeout.next;
            if (hashedWheelTimeout.prev != null) {
                hashedWheelTimeout.prev.next = hashedWheelTimeout2;
            }
            if (hashedWheelTimeout.next != null) {
                hashedWheelTimeout.next.prev = hashedWheelTimeout.prev;
            }
            if (hashedWheelTimeout == this.head) {
                if (hashedWheelTimeout == this.tail) {
                    this.tail = null;
                    this.head = null;
                } else {
                    this.head = hashedWheelTimeout2;
                }
            } else if (hashedWheelTimeout == this.tail) {
                this.tail = hashedWheelTimeout.prev;
            }
            hashedWheelTimeout.prev = null;
            hashedWheelTimeout.next = null;
            hashedWheelTimeout.bucket = null;
        }

        public void expireTimeouts(long l) {
            HashedWheelTimeout hashedWheelTimeout = this.head;
            while (hashedWheelTimeout != null) {
                boolean bl = false;
                if (hashedWheelTimeout.remainingRounds <= 0L) {
                    if (hashedWheelTimeout.deadline > l) {
                        throw new IllegalStateException(String.format("timeout.deadline (%d) > deadline (%d)", hashedWheelTimeout.deadline, l));
                    }
                    hashedWheelTimeout.expire();
                    bl = true;
                } else if (hashedWheelTimeout.isCancelled()) {
                    bl = true;
                } else {
                    --hashedWheelTimeout.remainingRounds;
                }
                HashedWheelTimeout hashedWheelTimeout2 = hashedWheelTimeout.next;
                if (bl) {
                    this.remove(hashedWheelTimeout);
                }
                hashedWheelTimeout = hashedWheelTimeout2;
            }
        }
    }

    private final class Worker
    implements Runnable {
        private long tick;
        private final Set<Timeout> unprocessedTimeouts = new HashSet<Timeout>();

        private void processCancelledTasks() {
            Runnable runnable;
            while ((runnable = (Runnable)HashedWheelTimer.this.cancelledTimeouts.poll()) != null) {
                try {
                    runnable.run();
                }
                catch (Throwable throwable) {
                    if (!logger.isWarnEnabled()) continue;
                    logger.warn("An exception was thrown while process a cancellation task", throwable);
                }
            }
        }

        public Set<Timeout> unprocessedTimeouts() {
            return Collections.unmodifiableSet(this.unprocessedTimeouts);
        }

        private Worker() {
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private long waitForNextTick() {
            long l = HashedWheelTimer.this.tickDuration * (this.tick + 1L);
            while (true) {
                long l2;
                long l3;
                if ((l3 = (l - (l2 = System.nanoTime() - HashedWheelTimer.this.startTime) + 999999L) / 1000000L) <= 0L) {
                    if (l2 != Long.MIN_VALUE) return l2;
                    return -9223372036854775807L;
                }
                if (PlatformDependent.isWindows()) {
                    l3 = l3 / 10L * 10L;
                }
                try {
                    Thread.sleep(l3);
                    continue;
                }
                catch (InterruptedException interruptedException) {
                    if (WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == 2) return Long.MIN_VALUE;
                    continue;
                }
                break;
            }
        }

        private void transferTimeoutsToBuckets() {
            HashedWheelTimeout hashedWheelTimeout;
            for (int i = 0; i < 100000 && (hashedWheelTimeout = (HashedWheelTimeout)HashedWheelTimer.this.timeouts.poll()) != null; ++i) {
                if (hashedWheelTimeout.state() == 1) continue;
                long l = hashedWheelTimeout.deadline / HashedWheelTimer.this.tickDuration;
                hashedWheelTimeout.remainingRounds = (l - this.tick) / (long)HashedWheelTimer.this.wheel.length;
                long l2 = Math.max(l, this.tick);
                int n = (int)(l2 & (long)HashedWheelTimer.this.mask);
                HashedWheelBucket hashedWheelBucket = HashedWheelTimer.this.wheel[n];
                hashedWheelBucket.addTimeout(hashedWheelTimeout);
            }
        }

        @Override
        public void run() {
            HashedWheelBucket hashedWheelBucket;
            int n;
            HashedWheelTimer.this.startTime = System.nanoTime();
            if (HashedWheelTimer.this.startTime == 0L) {
                HashedWheelTimer.this.startTime = 1L;
            }
            HashedWheelTimer.this.startTimeInitialized.countDown();
            do {
                long l;
                if ((l = this.waitForNextTick()) <= 0L) continue;
                n = (int)(this.tick & (long)HashedWheelTimer.this.mask);
                this.processCancelledTasks();
                hashedWheelBucket = HashedWheelTimer.this.wheel[n];
                this.transferTimeoutsToBuckets();
                hashedWheelBucket.expireTimeouts(l);
                ++this.tick;
            } while (WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == 1);
            Object object = HashedWheelTimer.this.wheel;
            int n2 = ((HashedWheelBucket[])object).length;
            for (n = 0; n < n2; ++n) {
                hashedWheelBucket = object[n];
                hashedWheelBucket.clearTimeouts(this.unprocessedTimeouts);
            }
            while ((object = (HashedWheelTimeout)HashedWheelTimer.this.timeouts.poll()) != null) {
                if (((HashedWheelTimeout)object).isCancelled()) continue;
                this.unprocessedTimeouts.add((Timeout)object);
            }
            this.processCancelledTasks();
        }
    }

    private static final class HashedWheelTimeout
    extends MpscLinkedQueueNode<Timeout>
    implements Timeout {
        private static final int ST_INIT = 0;
        HashedWheelBucket bucket;
        private final long deadline;
        private final HashedWheelTimer timer;
        private final TimerTask task;
        private static final int ST_EXPIRED = 2;
        HashedWheelTimeout next;
        HashedWheelTimeout prev;
        private volatile int state = 0;
        long remainingRounds;
        private static final AtomicIntegerFieldUpdater<HashedWheelTimeout> STATE_UPDATER;
        private static final int ST_CANCELLED = 1;

        @Override
        public TimerTask task() {
            return this.task;
        }

        HashedWheelTimeout(HashedWheelTimer hashedWheelTimer, TimerTask timerTask, long l) {
            this.timer = hashedWheelTimer;
            this.task = timerTask;
            this.deadline = l;
        }

        public String toString() {
            long l = System.nanoTime();
            long l2 = this.deadline - l + this.timer.startTime;
            StringBuilder stringBuilder = new StringBuilder(192);
            stringBuilder.append(StringUtil.simpleClassName(this));
            stringBuilder.append('(');
            stringBuilder.append("deadline: ");
            if (l2 > 0L) {
                stringBuilder.append(l2);
                stringBuilder.append(" ns later");
            } else if (l2 < 0L) {
                stringBuilder.append(-l2);
                stringBuilder.append(" ns ago");
            } else {
                stringBuilder.append("now");
            }
            if (this.isCancelled()) {
                stringBuilder.append(", cancelled");
            }
            stringBuilder.append(", task: ");
            stringBuilder.append(this.task());
            return stringBuilder.append(')').toString();
        }

        static {
            AtomicIntegerFieldUpdater<Object> atomicIntegerFieldUpdater = PlatformDependent.newAtomicIntegerFieldUpdater(HashedWheelTimeout.class, "state");
            if (atomicIntegerFieldUpdater == null) {
                atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimeout.class, "state");
            }
            STATE_UPDATER = atomicIntegerFieldUpdater;
        }

        public void expire() {
            block3: {
                if (!this.compareAndSetState(0, 2)) {
                    return;
                }
                try {
                    this.task.run(this);
                }
                catch (Throwable throwable) {
                    if (!logger.isWarnEnabled()) break block3;
                    logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName() + '.', throwable);
                }
            }
        }

        @Override
        public boolean cancel() {
            if (!this.compareAndSetState(0, 1)) {
                return false;
            }
            this.timer.cancelledTimeouts.add(new Runnable(){

                @Override
                public void run() {
                    HashedWheelBucket hashedWheelBucket = HashedWheelTimeout.this.bucket;
                    if (hashedWheelBucket != null) {
                        hashedWheelBucket.remove(HashedWheelTimeout.this);
                    }
                }
            });
            return true;
        }

        public boolean compareAndSetState(int n, int n2) {
            return STATE_UPDATER.compareAndSet(this, n, n2);
        }

        public int state() {
            return this.state;
        }

        @Override
        public HashedWheelTimeout value() {
            return this;
        }

        @Override
        public boolean isExpired() {
            return this.state() == 2;
        }

        @Override
        public boolean isCancelled() {
            return this.state() == 1;
        }

        @Override
        public Timer timer() {
            return this.timer;
        }
    }
}

