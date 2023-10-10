/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.concurrent.GuardedBy
 */
package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

final class SerializingExecutor
implements Executor {
    private static final Logger log = Logger.getLogger(SerializingExecutor.class.getName());
    private final Executor executor;
    @GuardedBy(value="internalLock")
    private final Queue<Runnable> waitQueue = new ArrayDeque<Runnable>();
    @GuardedBy(value="internalLock")
    private boolean isThreadScheduled = false;
    private final TaskRunner taskRunner = new TaskRunner();
    private final Object internalLock = new Object(){

        public String toString() {
            return "SerializingExecutor lock: " + super.toString();
        }
    };

    public SerializingExecutor(Executor executor) {
        Preconditions.checkNotNull(executor, "'executor' must not be null.");
        this.executor = executor;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void execute(Runnable r2) {
        Preconditions.checkNotNull(r2, "'r' must not be null.");
        boolean scheduleTaskRunner = false;
        Object object = this.internalLock;
        synchronized (object) {
            this.waitQueue.add(r2);
            if (!this.isThreadScheduled) {
                this.isThreadScheduled = true;
                scheduleTaskRunner = true;
            }
        }
        if (scheduleTaskRunner) {
            boolean threw = true;
            try {
                this.executor.execute(this.taskRunner);
                threw = false;
            }
            finally {
                if (threw) {
                    Object object2 = this.internalLock;
                    synchronized (object2) {
                        this.isThreadScheduled = false;
                    }
                }
            }
        }
    }

    private class TaskRunner
    implements Runnable {
        private TaskRunner() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            block18: {
                boolean stillRunning = true;
                block14: while (true) {
                    while (true) {
                        Runnable nextToRun;
                        Preconditions.checkState(SerializingExecutor.this.isThreadScheduled);
                        Object object = SerializingExecutor.this.internalLock;
                        synchronized (object) {
                            nextToRun = (Runnable)SerializingExecutor.this.waitQueue.poll();
                            if (nextToRun == null) {
                                SerializingExecutor.this.isThreadScheduled = false;
                                stillRunning = false;
                                break block18;
                            }
                        }
                        try {
                            nextToRun.run();
                            continue block14;
                        }
                        catch (RuntimeException e2) {
                            log.log(Level.SEVERE, "Exception while executing runnable " + nextToRun, e2);
                            continue;
                        }
                        break;
                    }
                }
                finally {
                    if (stillRunning) {
                        Object object = SerializingExecutor.this.internalLock;
                        synchronized (object) {
                            SerializingExecutor.this.isThreadScheduled = false;
                        }
                    }
                }
            }
        }
    }
}

