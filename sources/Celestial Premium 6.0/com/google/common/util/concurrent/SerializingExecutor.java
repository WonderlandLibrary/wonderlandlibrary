/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

@GwtIncompatible
final class SerializingExecutor
implements Executor {
    private static final Logger log = Logger.getLogger(SerializingExecutor.class.getName());
    private final Executor executor;
    @GuardedBy(value="internalLock")
    private final Deque<Runnable> queue = new ArrayDeque<Runnable>();
    @GuardedBy(value="internalLock")
    private boolean isWorkerRunning = false;
    @GuardedBy(value="internalLock")
    private int suspensions = 0;
    private final Object internalLock = new Object();

    public SerializingExecutor(Executor executor) {
        this.executor = Preconditions.checkNotNull(executor);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void execute(Runnable task) {
        Object object = this.internalLock;
        synchronized (object) {
            this.queue.add(task);
        }
        this.startQueueWorker();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void executeFirst(Runnable task) {
        Object object = this.internalLock;
        synchronized (object) {
            this.queue.addFirst(task);
        }
        this.startQueueWorker();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void suspend() {
        Object object = this.internalLock;
        synchronized (object) {
            ++this.suspensions;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void resume() {
        Object object = this.internalLock;
        synchronized (object) {
            Preconditions.checkState(this.suspensions > 0);
            --this.suspensions;
        }
        this.startQueueWorker();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void startQueueWorker() {
        Object object = this.internalLock;
        synchronized (object) {
            if (this.queue.peek() == null) {
                return;
            }
            if (this.suspensions > 0) {
                return;
            }
            if (this.isWorkerRunning) {
                return;
            }
            this.isWorkerRunning = true;
        }
        boolean executionRejected = true;
        try {
            this.executor.execute(new QueueWorker());
            executionRejected = false;
        }
        finally {
            if (executionRejected) {
                Object object2 = this.internalLock;
                synchronized (object2) {
                    this.isWorkerRunning = false;
                }
            }
        }
    }

    private final class QueueWorker
    implements Runnable {
        private QueueWorker() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            try {
                this.workOnQueue();
            }
            catch (Error e) {
                Object object = SerializingExecutor.this.internalLock;
                synchronized (object) {
                    SerializingExecutor.this.isWorkerRunning = false;
                }
                throw e;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void workOnQueue() {
            while (true) {
                Runnable task = null;
                Object object = SerializingExecutor.this.internalLock;
                synchronized (object) {
                    if (SerializingExecutor.this.suspensions == 0) {
                        task = (Runnable)SerializingExecutor.this.queue.poll();
                    }
                    if (task == null) {
                        SerializingExecutor.this.isWorkerRunning = false;
                        return;
                    }
                }
                try {
                    task.run();
                    continue;
                }
                catch (RuntimeException e) {
                    log.log(Level.SEVERE, "Exception while executing runnable " + task, e);
                    continue;
                }
                break;
            }
        }
    }
}

