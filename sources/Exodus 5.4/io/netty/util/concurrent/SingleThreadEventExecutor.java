/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.concurrent;

import io.netty.util.concurrent.AbstractEventExecutor;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.concurrent.ScheduledFutureTask;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public abstract class SingleThreadEventExecutor
extends AbstractEventExecutor {
    private final Queue<Runnable> taskQueue;
    private volatile int state = 1;
    private final boolean addTaskWakesUp;
    private static final int ST_SHUTDOWN = 4;
    private final Promise<?> terminationFuture;
    final Queue<ScheduledFutureTask<?>> delayedTaskQueue = new PriorityQueue();
    private static final Runnable WAKEUP_TASK;
    private final EventExecutorGroup parent;
    private static final int ST_SHUTTING_DOWN = 3;
    private static final int ST_STARTED = 2;
    private static final AtomicIntegerFieldUpdater<SingleThreadEventExecutor> STATE_UPDATER;
    private static final long SCHEDULE_PURGE_INTERVAL;
    private final Thread thread;
    private static final int ST_TERMINATED = 5;
    private static final int ST_NOT_STARTED = 1;
    private long lastExecutionTime;
    private final Set<Runnable> shutdownHooks;
    private volatile long gracefulShutdownQuietPeriod;
    private static final InternalLogger logger;
    private final Semaphore threadLock = new Semaphore(0);
    private volatile long gracefulShutdownTimeout;
    private long gracefulShutdownStartTime;

    protected boolean runAllTasks() {
        this.fetchFromDelayedQueue();
        Runnable runnable = this.pollTask();
        if (runnable == null) {
            return false;
        }
        do {
            try {
                runnable.run();
            }
            catch (Throwable throwable) {
                logger.warn("A task raised an exception.", throwable);
            }
        } while ((runnable = this.pollTask()) != null);
        this.lastExecutionTime = ScheduledFutureTask.nanoTime();
        return true;
    }

    public void removeShutdownHook(final Runnable runnable) {
        if (this.inEventLoop()) {
            this.shutdownHooks.remove(runnable);
        } else {
            this.execute(new Runnable(){

                @Override
                public void run() {
                    SingleThreadEventExecutor.this.shutdownHooks.remove(runnable);
                }
            });
        }
    }

    @Override
    public Future<?> shutdownGracefully(long l, long l2, TimeUnit timeUnit) {
        boolean bl;
        int n;
        int n2;
        if (l < 0L) {
            throw new IllegalArgumentException("quietPeriod: " + l + " (expected >= 0)");
        }
        if (l2 < l) {
            throw new IllegalArgumentException("timeout: " + l2 + " (expected >= quietPeriod (" + l + "))");
        }
        if (timeUnit == null) {
            throw new NullPointerException("unit");
        }
        if (this.isShuttingDown()) {
            return this.terminationFuture();
        }
        boolean bl2 = this.inEventLoop();
        do {
            if (this.isShuttingDown()) {
                return this.terminationFuture();
            }
            bl = true;
            n2 = STATE_UPDATER.get(this);
            if (bl2) {
                n = 3;
                continue;
            }
            switch (n2) {
                case 1: 
                case 2: {
                    n = 3;
                    break;
                }
                default: {
                    n = n2;
                    bl = false;
                }
            }
        } while (!STATE_UPDATER.compareAndSet(this, n2, n));
        this.gracefulShutdownQuietPeriod = timeUnit.toNanos(l);
        this.gracefulShutdownTimeout = timeUnit.toNanos(l2);
        if (n2 == 1) {
            this.thread.start();
        }
        if (bl) {
            this.wakeup(bl2);
        }
        return this.terminationFuture();
    }

    public void addShutdownHook(final Runnable runnable) {
        if (this.inEventLoop()) {
            this.shutdownHooks.add(runnable);
        } else {
            this.execute(new Runnable(){

                @Override
                public void run() {
                    SingleThreadEventExecutor.this.shutdownHooks.add(runnable);
                }
            });
        }
    }

    protected void addTask(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("task");
        }
        if (this.isShutdown()) {
            SingleThreadEventExecutor.reject();
        }
        this.taskQueue.add(runnable);
    }

    protected boolean hasScheduledTasks() {
        assert (this.inEventLoop());
        ScheduledFutureTask<?> scheduledFutureTask = this.delayedTaskQueue.peek();
        return scheduledFutureTask != null && scheduledFutureTask.deadlineNanos() <= ScheduledFutureTask.nanoTime();
    }

    protected SingleThreadEventExecutor(EventExecutorGroup eventExecutorGroup, ThreadFactory threadFactory, boolean bl) {
        this.shutdownHooks = new LinkedHashSet<Runnable>();
        this.terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
        if (threadFactory == null) {
            throw new NullPointerException("threadFactory");
        }
        this.parent = eventExecutorGroup;
        this.addTaskWakesUp = bl;
        this.thread = threadFactory.newThread(new Runnable(){

            @Override
            public void run() {
                block10: {
                    block9: {
                        int n;
                        boolean bl = false;
                        SingleThreadEventExecutor.this.updateLastExecutionTime();
                        try {
                            SingleThreadEventExecutor.this.run();
                            bl = true;
                        }
                        catch (Throwable throwable) {
                            int n2;
                            logger.warn("Unexpected exception from an event executor: ", throwable);
                            while ((n2 = STATE_UPDATER.get(SingleThreadEventExecutor.this)) < 3 && !STATE_UPDATER.compareAndSet(SingleThreadEventExecutor.this, n2, 3)) {
                            }
                            if (!bl || SingleThreadEventExecutor.this.gracefulShutdownStartTime != 0L) break block9;
                            logger.error("Buggy " + EventExecutor.class.getSimpleName() + " implementation; " + SingleThreadEventExecutor.class.getSimpleName() + ".confirmShutdown() must be called " + "before run() implementation terminates.");
                        }
                        while ((n = STATE_UPDATER.get(SingleThreadEventExecutor.this)) < 3 && !STATE_UPDATER.compareAndSet(SingleThreadEventExecutor.this, n, 3)) {
                        }
                        if (bl && SingleThreadEventExecutor.this.gracefulShutdownStartTime == 0L) {
                            logger.error("Buggy " + EventExecutor.class.getSimpleName() + " implementation; " + SingleThreadEventExecutor.class.getSimpleName() + ".confirmShutdown() must be called " + "before run() implementation terminates.");
                        }
                        while (!SingleThreadEventExecutor.this.confirmShutdown()) {
                        }
                        SingleThreadEventExecutor.this.cleanup();
                        STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                        SingleThreadEventExecutor.this.threadLock.release();
                        if (!SingleThreadEventExecutor.this.taskQueue.isEmpty()) {
                            logger.warn("An event executor terminated with non-empty task queue (" + SingleThreadEventExecutor.this.taskQueue.size() + ')');
                        }
                        SingleThreadEventExecutor.this.terminationFuture.setSuccess(null);
                        break block10;
                    }
                    while (!SingleThreadEventExecutor.this.confirmShutdown()) {
                    }
                    SingleThreadEventExecutor.this.cleanup();
                    STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                    SingleThreadEventExecutor.this.threadLock.release();
                    if (!SingleThreadEventExecutor.this.taskQueue.isEmpty()) {
                        logger.warn("An event executor terminated with non-empty task queue (" + SingleThreadEventExecutor.this.taskQueue.size() + ')');
                    }
                    SingleThreadEventExecutor.this.terminationFuture.setSuccess(null);
                }
            }
        });
        this.taskQueue = this.newTaskQueue();
    }

    protected void wakeup(boolean bl) {
        if (!bl || STATE_UPDATER.get(this) == 3) {
            this.taskQueue.add(WAKEUP_TASK);
        }
    }

    @Override
    public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
        if (timeUnit == null) {
            throw new NullPointerException("unit");
        }
        if (this.inEventLoop()) {
            throw new IllegalStateException("cannot await termination of the current thread");
        }
        if (this.threadLock.tryAcquire(l, timeUnit)) {
            this.threadLock.release();
        }
        return this.isTerminated();
    }

    private void fetchFromDelayedQueue() {
        ScheduledFutureTask<?> scheduledFutureTask;
        long l = 0L;
        while ((scheduledFutureTask = this.delayedTaskQueue.peek()) != null) {
            if (l == 0L) {
                l = ScheduledFutureTask.nanoTime();
            }
            if (scheduledFutureTask.deadlineNanos() > l) break;
            this.delayedTaskQueue.remove();
            this.taskQueue.add(scheduledFutureTask);
        }
    }

    protected Runnable peekTask() {
        assert (this.inEventLoop());
        return this.taskQueue.peek();
    }

    protected Runnable pollTask() {
        Runnable runnable;
        assert (this.inEventLoop());
        while ((runnable = this.taskQueue.poll()) == WAKEUP_TASK) {
        }
        return runnable;
    }

    protected void cleanup() {
    }

    protected void updateLastExecutionTime() {
        this.lastExecutionTime = ScheduledFutureTask.nanoTime();
    }

    protected Queue<Runnable> newTaskQueue() {
        return new LinkedBlockingQueue<Runnable>();
    }

    @Override
    public boolean inEventLoop(Thread thread) {
        return thread == this.thread;
    }

    @Override
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable runnable, long l, TimeUnit timeUnit) {
        if (runnable == null) {
            throw new NullPointerException("command");
        }
        if (timeUnit == null) {
            throw new NullPointerException("unit");
        }
        if (l < 0L) {
            throw new IllegalArgumentException(String.format("delay: %d (expected: >= 0)", l));
        }
        return this.schedule(new ScheduledFutureTask<Object>((EventExecutor)this, this.delayedTaskQueue, runnable, null, ScheduledFutureTask.deadlineNanos(timeUnit.toNanos(l))));
    }

    @Override
    public void execute(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("task");
        }
        boolean bl = this.inEventLoop();
        if (bl) {
            this.addTask(runnable);
        } else {
            this.startThread();
            this.addTask(runnable);
            if (this.isShutdown() && this.removeTask(runnable)) {
                SingleThreadEventExecutor.reject();
            }
        }
        if (!this.addTaskWakesUp && this.wakesUpForTask(runnable)) {
            this.wakeup(bl);
        }
    }

    protected Runnable takeTask() {
        Runnable runnable;
        assert (this.inEventLoop());
        if (!(this.taskQueue instanceof BlockingQueue)) {
            throw new UnsupportedOperationException();
        }
        BlockingQueue blockingQueue = (BlockingQueue)this.taskQueue;
        do {
            ScheduledFutureTask<?> scheduledFutureTask;
            if ((scheduledFutureTask = this.delayedTaskQueue.peek()) == null) {
                Runnable runnable2 = null;
                try {
                    runnable2 = (Runnable)blockingQueue.take();
                    if (runnable2 == WAKEUP_TASK) {
                        runnable2 = null;
                    }
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
                return runnable2;
            }
            long l = scheduledFutureTask.delayNanos();
            runnable = null;
            if (l > 0L) {
                try {
                    runnable = (Runnable)blockingQueue.poll(l, TimeUnit.NANOSECONDS);
                }
                catch (InterruptedException interruptedException) {
                    return null;
                }
            }
            if (runnable != null) continue;
            this.fetchFromDelayedQueue();
            runnable = (Runnable)blockingQueue.poll();
        } while (runnable == null);
        return runnable;
    }

    @Override
    @Deprecated
    public void shutdown() {
        boolean bl;
        int n;
        int n2;
        if (this.isShutdown()) {
            return;
        }
        boolean bl2 = this.inEventLoop();
        do {
            if (this.isShuttingDown()) {
                return;
            }
            bl = true;
            n2 = STATE_UPDATER.get(this);
            if (bl2) {
                n = 4;
                continue;
            }
            switch (n2) {
                case 1: 
                case 2: 
                case 3: {
                    n = 4;
                    break;
                }
                default: {
                    n = n2;
                    bl = false;
                }
            }
        } while (!STATE_UPDATER.compareAndSet(this, n2, n));
        if (n2 == 1) {
            this.thread.start();
        }
        if (bl) {
            this.wakeup(bl2);
        }
    }

    protected boolean removeTask(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("task");
        }
        return this.taskQueue.remove(runnable);
    }

    protected boolean runAllTasks(long l) {
        long l2;
        block4: {
            this.fetchFromDelayedQueue();
            Runnable runnable = this.pollTask();
            if (runnable == null) {
                return false;
            }
            long l3 = ScheduledFutureTask.nanoTime() + l;
            long l4 = 0L;
            do {
                try {
                    runnable.run();
                }
                catch (Throwable throwable) {
                    logger.warn("A task raised an exception.", throwable);
                }
                if ((++l4 & 0x3FL) == 0L && (l2 = ScheduledFutureTask.nanoTime()) >= l3) break block4;
            } while ((runnable = this.pollTask()) != null);
            l2 = ScheduledFutureTask.nanoTime();
        }
        this.lastExecutionTime = l2;
        return true;
    }

    protected boolean hasTasks() {
        assert (this.inEventLoop());
        return !this.taskQueue.isEmpty();
    }

    protected static void reject() {
        throw new RejectedExecutionException("event executor terminated");
    }

    @Override
    public boolean isTerminated() {
        return STATE_UPDATER.get(this) == 5;
    }

    @Override
    public boolean isShuttingDown() {
        return STATE_UPDATER.get(this) >= 3;
    }

    protected void interruptThread() {
        this.thread.interrupt();
    }

    protected abstract void run();

    protected boolean confirmShutdown() {
        if (!this.isShuttingDown()) {
            return false;
        }
        if (!this.inEventLoop()) {
            throw new IllegalStateException("must be invoked from an event loop");
        }
        this.cancelDelayedTasks();
        if (this.gracefulShutdownStartTime == 0L) {
            this.gracefulShutdownStartTime = ScheduledFutureTask.nanoTime();
        }
        if (this.runAllTasks() || this.runShutdownHooks()) {
            if (this.isShutdown()) {
                return true;
            }
            this.wakeup(true);
            return false;
        }
        long l = ScheduledFutureTask.nanoTime();
        if (this.isShutdown() || l - this.gracefulShutdownStartTime > this.gracefulShutdownTimeout) {
            return true;
        }
        if (l - this.lastExecutionTime <= this.gracefulShutdownQuietPeriod) {
            this.wakeup(true);
            try {
                Thread.sleep(100L);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
            return false;
        }
        return true;
    }

    @Override
    public EventExecutorGroup parent() {
        return this.parent;
    }

    private <V> ScheduledFuture<V> schedule(final ScheduledFutureTask<V> scheduledFutureTask) {
        if (scheduledFutureTask == null) {
            throw new NullPointerException("task");
        }
        if (this.inEventLoop()) {
            this.delayedTaskQueue.add(scheduledFutureTask);
        } else {
            this.execute(new Runnable(){

                @Override
                public void run() {
                    SingleThreadEventExecutor.this.delayedTaskQueue.add(scheduledFutureTask);
                }
            });
        }
        return scheduledFutureTask;
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long l, long l2, TimeUnit timeUnit) {
        if (runnable == null) {
            throw new NullPointerException("command");
        }
        if (timeUnit == null) {
            throw new NullPointerException("unit");
        }
        if (l < 0L) {
            throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", l));
        }
        if (l2 <= 0L) {
            throw new IllegalArgumentException(String.format("delay: %d (expected: > 0)", l2));
        }
        return this.schedule(new ScheduledFutureTask<Object>((EventExecutor)this, this.delayedTaskQueue, Executors.callable(runnable, null), ScheduledFutureTask.deadlineNanos(timeUnit.toNanos(l)), -timeUnit.toNanos(l2)));
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long l, long l2, TimeUnit timeUnit) {
        if (runnable == null) {
            throw new NullPointerException("command");
        }
        if (timeUnit == null) {
            throw new NullPointerException("unit");
        }
        if (l < 0L) {
            throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", l));
        }
        if (l2 <= 0L) {
            throw new IllegalArgumentException(String.format("period: %d (expected: > 0)", l2));
        }
        return this.schedule(new ScheduledFutureTask<Object>((EventExecutor)this, this.delayedTaskQueue, Executors.callable(runnable, null), ScheduledFutureTask.deadlineNanos(timeUnit.toNanos(l)), timeUnit.toNanos(l2)));
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long l, TimeUnit timeUnit) {
        if (callable == null) {
            throw new NullPointerException("callable");
        }
        if (timeUnit == null) {
            throw new NullPointerException("unit");
        }
        if (l < 0L) {
            throw new IllegalArgumentException(String.format("delay: %d (expected: >= 0)", l));
        }
        return this.schedule(new ScheduledFutureTask<V>(this, this.delayedTaskQueue, callable, ScheduledFutureTask.deadlineNanos(timeUnit.toNanos(l))));
    }

    private void startThread() {
        if (STATE_UPDATER.get(this) == 1 && STATE_UPDATER.compareAndSet(this, 1, 2)) {
            this.delayedTaskQueue.add(new ScheduledFutureTask<Object>((EventExecutor)this, this.delayedTaskQueue, Executors.callable(new PurgeTask(), null), ScheduledFutureTask.deadlineNanos(SCHEDULE_PURGE_INTERVAL), -SCHEDULE_PURGE_INTERVAL));
            this.thread.start();
        }
    }

    private boolean runShutdownHooks() {
        boolean bl = false;
        while (!this.shutdownHooks.isEmpty()) {
            ArrayList<Runnable> arrayList = new ArrayList<Runnable>(this.shutdownHooks);
            this.shutdownHooks.clear();
            for (Runnable runnable : arrayList) {
                try {
                    runnable.run();
                    bl = true;
                }
                catch (Throwable throwable) {
                    logger.warn("Shutdown hook raised an exception.", throwable);
                    bl = true;
                }
            }
        }
        if (bl) {
            this.lastExecutionTime = ScheduledFutureTask.nanoTime();
        }
        return bl;
    }

    protected boolean wakesUpForTask(Runnable runnable) {
        return true;
    }

    static {
        logger = InternalLoggerFactory.getInstance(SingleThreadEventExecutor.class);
        WAKEUP_TASK = new Runnable(){

            @Override
            public void run() {
            }
        };
        AtomicIntegerFieldUpdater<Object> atomicIntegerFieldUpdater = PlatformDependent.newAtomicIntegerFieldUpdater(SingleThreadEventExecutor.class, "state");
        if (atomicIntegerFieldUpdater == null) {
            atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(SingleThreadEventExecutor.class, "state");
        }
        STATE_UPDATER = atomicIntegerFieldUpdater;
        SCHEDULE_PURGE_INTERVAL = TimeUnit.SECONDS.toNanos(1L);
    }

    private void cancelDelayedTasks() {
        ScheduledFutureTask[] scheduledFutureTaskArray;
        if (this.delayedTaskQueue.isEmpty()) {
            return;
        }
        for (ScheduledFutureTask scheduledFutureTask : scheduledFutureTaskArray = this.delayedTaskQueue.toArray(new ScheduledFutureTask[this.delayedTaskQueue.size()])) {
            scheduledFutureTask.cancel(false);
        }
        this.delayedTaskQueue.clear();
    }

    protected long delayNanos(long l) {
        ScheduledFutureTask<?> scheduledFutureTask = this.delayedTaskQueue.peek();
        if (scheduledFutureTask == null) {
            return SCHEDULE_PURGE_INTERVAL;
        }
        return scheduledFutureTask.delayNanos(l);
    }

    @Override
    public boolean isShutdown() {
        return STATE_UPDATER.get(this) >= 4;
    }

    public final int pendingTasks() {
        return this.taskQueue.size();
    }

    private final class PurgeTask
    implements Runnable {
        private PurgeTask() {
        }

        @Override
        public void run() {
            Iterator iterator = SingleThreadEventExecutor.this.delayedTaskQueue.iterator();
            while (iterator.hasNext()) {
                ScheduledFutureTask scheduledFutureTask = (ScheduledFutureTask)iterator.next();
                if (!scheduledFutureTask.isCancelled()) continue;
                iterator.remove();
            }
        }
    }
}

