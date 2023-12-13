/*
 * Decompiled with CFR 0.150.
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

public final class GlobalEventExecutor
extends AbstractEventExecutor {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(GlobalEventExecutor.class);
    private static final int ST_NOT_STARTED = 1;
    private static final int ST_STARTED = 2;
    private static final long SCHEDULE_PURGE_INTERVAL = TimeUnit.SECONDS.toNanos(1L);
    public static final GlobalEventExecutor INSTANCE = new GlobalEventExecutor();
    final Queue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
    final Queue<ScheduledFutureTask<?>> delayedTaskQueue = new PriorityQueue();
    final ScheduledFutureTask<Void> purgeTask = new ScheduledFutureTask<Object>((EventExecutor)this, this.delayedTaskQueue, Executors.callable(new PurgeTask(), null), ScheduledFutureTask.deadlineNanos(SCHEDULE_PURGE_INTERVAL), -SCHEDULE_PURGE_INTERVAL);
    private final ThreadFactory threadFactory = new DefaultThreadFactory(this.getClass());
    private final TaskRunner taskRunner = new TaskRunner();
    private final Object stateLock = new Object();
    volatile Thread thread;
    private volatile int state = 1;
    private final Future<?> terminationFuture = new FailedFuture(this, new UnsupportedOperationException());

    private GlobalEventExecutor() {
        this.delayedTaskQueue.add(this.purgeTask);
    }

    @Override
    public EventExecutorGroup parent() {
        return null;
    }

    Runnable takeTask() {
        Runnable task;
        BlockingQueue taskQueue = (BlockingQueue)this.taskQueue;
        do {
            ScheduledFutureTask<?> delayedTask;
            if ((delayedTask = this.delayedTaskQueue.peek()) == null) {
                Runnable task2 = null;
                try {
                    task2 = (Runnable)taskQueue.take();
                }
                catch (InterruptedException e) {
                    // empty catch block
                }
                return task2;
            }
            long delayNanos = delayedTask.delayNanos();
            if (delayNanos > 0L) {
                try {
                    task = (Runnable)taskQueue.poll(delayNanos, TimeUnit.NANOSECONDS);
                }
                catch (InterruptedException e) {
                    return null;
                }
            } else {
                task = (Runnable)taskQueue.poll();
            }
            if (task != null) continue;
            this.fetchFromDelayedQueue();
            task = (Runnable)taskQueue.poll();
        } while (task == null);
        return task;
    }

    private void fetchFromDelayedQueue() {
        ScheduledFutureTask<?> delayedTask;
        long nanoTime = 0L;
        while ((delayedTask = this.delayedTaskQueue.peek()) != null) {
            if (nanoTime == 0L) {
                nanoTime = ScheduledFutureTask.nanoTime();
            }
            if (delayedTask.deadlineNanos() > nanoTime) break;
            this.delayedTaskQueue.remove();
            this.taskQueue.add(delayedTask);
        }
    }

    public int pendingTasks() {
        return this.taskQueue.size();
    }

    private void addTask(Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        this.taskQueue.add(task);
    }

    @Override
    public boolean inEventLoop(Thread thread) {
        return thread == this.thread;
    }

    @Override
    public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
        return this.terminationFuture();
    }

    @Override
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }

    @Override
    @Deprecated
    public void shutdown() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isShuttingDown() {
        return false;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
        return false;
    }

    @Override
    public void execute(Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        this.addTask(task);
        if (!this.inEventLoop()) {
            this.startThread();
        }
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        if (command == null) {
            throw new NullPointerException("command");
        }
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (delay < 0L) {
            throw new IllegalArgumentException(String.format("delay: %d (expected: >= 0)", delay));
        }
        return this.schedule(new ScheduledFutureTask<Object>((EventExecutor)this, this.delayedTaskQueue, command, null, ScheduledFutureTask.deadlineNanos(unit.toNanos(delay))));
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        if (callable == null) {
            throw new NullPointerException("callable");
        }
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (delay < 0L) {
            throw new IllegalArgumentException(String.format("delay: %d (expected: >= 0)", delay));
        }
        return this.schedule(new ScheduledFutureTask<V>(this, this.delayedTaskQueue, callable, ScheduledFutureTask.deadlineNanos(unit.toNanos(delay))));
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        if (command == null) {
            throw new NullPointerException("command");
        }
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (initialDelay < 0L) {
            throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", initialDelay));
        }
        if (period <= 0L) {
            throw new IllegalArgumentException(String.format("period: %d (expected: > 0)", period));
        }
        return this.schedule(new ScheduledFutureTask<Object>((EventExecutor)this, this.delayedTaskQueue, Executors.callable(command, null), ScheduledFutureTask.deadlineNanos(unit.toNanos(initialDelay)), unit.toNanos(period)));
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        if (command == null) {
            throw new NullPointerException("command");
        }
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (initialDelay < 0L) {
            throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", initialDelay));
        }
        if (delay <= 0L) {
            throw new IllegalArgumentException(String.format("delay: %d (expected: > 0)", delay));
        }
        return this.schedule(new ScheduledFutureTask<Object>((EventExecutor)this, this.delayedTaskQueue, Executors.callable(command, null), ScheduledFutureTask.deadlineNanos(unit.toNanos(initialDelay)), -unit.toNanos(delay)));
    }

    private <V> ScheduledFuture<V> schedule(final ScheduledFutureTask<V> task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        if (this.inEventLoop()) {
            this.delayedTaskQueue.add(task);
        } else {
            this.execute(new Runnable(){

                @Override
                public void run() {
                    GlobalEventExecutor.this.delayedTaskQueue.add(task);
                }
            });
        }
        return task;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void startThread() {
        Object object = this.stateLock;
        synchronized (object) {
            if (this.state == 1) {
                this.state = 2;
                this.thread = this.threadFactory.newThread(this.taskRunner);
                this.thread.start();
            }
        }
    }

    private final class PurgeTask
    implements Runnable {
        private PurgeTask() {
        }

        @Override
        public void run() {
            Iterator i = GlobalEventExecutor.this.delayedTaskQueue.iterator();
            while (i.hasNext()) {
                ScheduledFutureTask task = (ScheduledFutureTask)i.next();
                if (!task.isCancelled()) continue;
                i.remove();
            }
        }
    }

    final class TaskRunner
    implements Runnable {
        TaskRunner() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            while (true) {
                Runnable task;
                if ((task = GlobalEventExecutor.this.takeTask()) != null) {
                    try {
                        task.run();
                    }
                    catch (Throwable t) {
                        logger.warn("Unexpected exception from the global event executor: ", t);
                    }
                    if (task != GlobalEventExecutor.this.purgeTask) continue;
                }
                if (!GlobalEventExecutor.this.taskQueue.isEmpty() || GlobalEventExecutor.this.delayedTaskQueue.size() != 1) continue;
                Object object = GlobalEventExecutor.this.stateLock;
                synchronized (object) {
                    if (GlobalEventExecutor.this.taskQueue.isEmpty() && GlobalEventExecutor.this.delayedTaskQueue.size() == 1) {
                        GlobalEventExecutor.this.state = 1;
                        break;
                    }
                }
            }
        }
    }
}

