/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

@GwtIncompatible
final class ListenerCallQueue<L>
implements Runnable {
    private static final Logger logger = Logger.getLogger(ListenerCallQueue.class.getName());
    private final L listener;
    private final Executor executor;
    @GuardedBy(value="this")
    private final Queue<Callback<L>> waitQueue = Queues.newArrayDeque();
    @GuardedBy(value="this")
    private boolean isThreadScheduled;

    ListenerCallQueue(L listener, Executor executor) {
        this.listener = Preconditions.checkNotNull(listener);
        this.executor = Preconditions.checkNotNull(executor);
    }

    synchronized void add(Callback<L> callback) {
        this.waitQueue.add(callback);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void execute() {
        boolean scheduleTaskRunner = false;
        ListenerCallQueue listenerCallQueue = this;
        synchronized (listenerCallQueue) {
            if (!this.isThreadScheduled) {
                this.isThreadScheduled = true;
                scheduleTaskRunner = true;
            }
        }
        if (scheduleTaskRunner) {
            try {
                this.executor.execute(this);
            }
            catch (RuntimeException e) {
                ListenerCallQueue listenerCallQueue2 = this;
                synchronized (listenerCallQueue2) {
                    this.isThreadScheduled = false;
                }
                logger.log(Level.SEVERE, "Exception while running callbacks for " + this.listener + " on " + this.executor, e);
                throw e;
            }
        }
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
                    Callback<L> nextToRun;
                    ListenerCallQueue listenerCallQueue = this;
                    synchronized (listenerCallQueue) {
                        Preconditions.checkState(this.isThreadScheduled);
                        nextToRun = this.waitQueue.poll();
                        if (nextToRun == null) {
                            this.isThreadScheduled = false;
                            stillRunning = false;
                            break block18;
                        }
                    }
                    try {
                        nextToRun.call(this.listener);
                        continue block14;
                    }
                    catch (RuntimeException e) {
                        logger.log(Level.SEVERE, "Exception while executing callback: " + this.listener + "." + ((Callback)nextToRun).methodCall, e);
                        continue;
                    }
                    break;
                }
            }
            finally {
                if (stillRunning) {
                    ListenerCallQueue listenerCallQueue = this;
                    synchronized (listenerCallQueue) {
                        this.isThreadScheduled = false;
                    }
                }
            }
        }
    }

    static abstract class Callback<L> {
        private final String methodCall;

        Callback(String methodCall) {
            this.methodCall = methodCall;
        }

        abstract void call(L var1);

        void enqueueOn(Iterable<ListenerCallQueue<L>> queues) {
            for (ListenerCallQueue<L> queue : queues) {
                queue.add(this);
            }
        }
    }
}

