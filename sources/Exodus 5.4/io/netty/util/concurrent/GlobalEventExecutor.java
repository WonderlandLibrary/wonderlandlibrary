/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.concurrent;

import io.netty.util.concurrent.AbstractEventExecutor;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.FailedFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.concurrent.ScheduledFutureTask;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class GlobalEventExecutor
extends AbstractEventExecutor {
    private final AtomicBoolean started;
    volatile Thread thread;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(GlobalEventExecutor.class);
    final ScheduledFutureTask<Void> purgeTask;
    final Queue<ScheduledFutureTask<?>> delayedTaskQueue;
    private final ThreadFactory threadFactory;
    public static final GlobalEventExecutor INSTANCE;
    private final TaskRunner taskRunner;
    final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
    private static final long SCHEDULE_PURGE_INTERVAL;
    private final Future<?> terminationFuture;

    @Override
    public boolean isTerminated() {
        return false;
    }

    static {
        SCHEDULE_PURGE_INTERVAL = TimeUnit.SECONDS.toNanos(1L);
        INSTANCE = new GlobalEventExecutor();
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
    public boolean awaitTermination(long l, TimeUnit timeUnit) {
        return false;
    }

    private GlobalEventExecutor() {
        this.delayedTaskQueue = new PriorityQueue();
        this.purgeTask = new ScheduledFutureTask<Object>((EventExecutor)this, this.delayedTaskQueue, Executors.callable(new PurgeTask(), null), ScheduledFutureTask.deadlineNanos(SCHEDULE_PURGE_INTERVAL), -SCHEDULE_PURGE_INTERVAL);
        this.threadFactory = new DefaultThreadFactory(this.getClass());
        this.taskRunner = new TaskRunner();
        this.started = new AtomicBoolean();
        this.terminationFuture = new FailedFuture(this, new UnsupportedOperationException());
        this.delayedTaskQueue.add(this.purgeTask);
    }

    private void addTask(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("task");
        }
        this.taskQueue.add(runnable);
    }

    @Override
    public void execute(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("task");
        }
        this.addTask(runnable);
        if (!this.inEventLoop()) {
            this.startThread();
        }
    }

    @Override
    public Future<?> shutdownGracefully(long l, long l2, TimeUnit timeUnit) {
        return this.terminationFuture();
    }

    @Override
    public boolean isShuttingDown() {
        return false;
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

    @Override
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }

    @Override
    public boolean inEventLoop(Thread thread) {
        return thread == this.thread;
    }

    @Override
    @Deprecated
    public void shutdown() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EventExecutorGroup parent() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    Runnable takeTask() {
        Runnable runnable;
        BlockingQueue<Runnable> blockingQueue = this.taskQueue;
        do {
            ScheduledFutureTask<?> scheduledFutureTask;
            if ((scheduledFutureTask = this.delayedTaskQueue.peek()) == null) {
                Runnable runnable2 = null;
                try {
                    runnable2 = blockingQueue.take();
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
                return runnable2;
            }
            long l = scheduledFutureTask.delayNanos();
            if (l > 0L) {
                try {
                    runnable = blockingQueue.poll(l, TimeUnit.NANOSECONDS);
                }
                catch (InterruptedException interruptedException) {
                    return null;
                }
            } else {
                runnable = (Runnable)blockingQueue.poll();
            }
            if (runnable != null) continue;
            this.fetchFromDelayedQueue();
            runnable = (Runnable)blockingQueue.poll();
        } while (runnable == null);
        return runnable;
    }

    private void startThread() {
        if (this.started.compareAndSet(false, true)) {
            Thread thread = this.threadFactory.newThread(this.taskRunner);
            thread.start();
            this.thread = thread;
        }
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

    public int pendingTasks() {
        return this.taskQueue.size();
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
                    GlobalEventExecutor.this.delayedTaskQueue.add(scheduledFutureTask);
                }
            });
        }
        return scheduledFutureTask;
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

    public boolean awaitInactivity(long l, TimeUnit timeUnit) throws InterruptedException {
        if (timeUnit == null) {
            throw new NullPointerException("unit");
        }
        Thread thread = this.thread;
        if (thread == null) {
            throw new IllegalStateException("thread was not started");
        }
        thread.join(timeUnit.toMillis(l));
        return !thread.isAlive();
    }

    final class TaskRunner
    implements Runnable {
        @Override
        public void run() {
            while (true) {
                Runnable runnable;
                if ((runnable = GlobalEventExecutor.this.takeTask()) != null) {
                    try {
                        runnable.run();
                    }
                    catch (Throwable throwable) {
                        logger.warn("Unexpected exception from the global event executor: ", throwable);
                    }
                    if (runnable != GlobalEventExecutor.this.purgeTask) continue;
                }
                if (!GlobalEventExecutor.this.taskQueue.isEmpty() || GlobalEventExecutor.this.delayedTaskQueue.size() != 1) continue;
                boolean bl = GlobalEventExecutor.this.started.compareAndSet(true, false);
                assert (bl);
                if (GlobalEventExecutor.this.taskQueue.isEmpty() && GlobalEventExecutor.this.delayedTaskQueue.size() == 1 || !GlobalEventExecutor.this.started.compareAndSet(false, true)) break;
            }
        }

        TaskRunner() {
        }
    }

    private final class PurgeTask
    implements Runnable {
        @Override
        public void run() {
            Iterator iterator = GlobalEventExecutor.this.delayedTaskQueue.iterator();
            while (iterator.hasNext()) {
                ScheduledFutureTask scheduledFutureTask = (ScheduledFutureTask)iterator.next();
                if (!scheduledFutureTask.isCancelled()) continue;
                iterator.remove();
            }
        }

        private PurgeTask() {
        }
    }
}

