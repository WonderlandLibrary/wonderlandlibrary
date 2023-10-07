// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.util.concurrent;

import java.util.logging.Level;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import javax.annotation.concurrent.GuardedBy;
import java.util.Queue;
import java.util.logging.Logger;
import java.util.concurrent.Executor;

final class SerializingExecutor implements Executor
{
    private static final Logger log;
    private final Executor executor;
    @GuardedBy("internalLock")
    private final Queue<Runnable> waitQueue;
    @GuardedBy("internalLock")
    private boolean isThreadScheduled;
    private final TaskRunner taskRunner;
    private final Object internalLock;
    
    public SerializingExecutor(final Executor executor) {
        this.waitQueue = new ArrayDeque<Runnable>();
        this.isThreadScheduled = false;
        this.taskRunner = new TaskRunner();
        this.internalLock = new Object() {
            @Override
            public String toString() {
                final String s = "SerializingExecutor lock: ";
                final String value = String.valueOf(super.toString());
                return (value.length() != 0) ? s.concat(value) : new String(s);
            }
        };
        Preconditions.checkNotNull(executor, (Object)"'executor' must not be null.");
        this.executor = executor;
    }
    
    @Override
    public void execute(final Runnable r) {
        Preconditions.checkNotNull(r, (Object)"'r' must not be null.");
        boolean scheduleTaskRunner = false;
        synchronized (this.internalLock) {
            this.waitQueue.add(r);
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
                    synchronized (this.internalLock) {
                        this.isThreadScheduled = false;
                    }
                }
            }
        }
    }
    
    static {
        log = Logger.getLogger(SerializingExecutor.class.getName());
    }
    
    private class TaskRunner implements Runnable
    {
        @Override
        public void run() {
            boolean stillRunning = true;
            try {
                while (true) {
                    Preconditions.checkState(SerializingExecutor.this.isThreadScheduled);
                    final Runnable nextToRun;
                    synchronized (SerializingExecutor.this.internalLock) {
                        nextToRun = SerializingExecutor.this.waitQueue.poll();
                        if (nextToRun == null) {
                            SerializingExecutor.this.isThreadScheduled = false;
                            stillRunning = false;
                            break;
                        }
                    }
                    try {
                        nextToRun.run();
                    }
                    catch (final RuntimeException e) {
                        final Logger access$400 = SerializingExecutor.log;
                        final Level severe = Level.SEVERE;
                        final String value = String.valueOf(String.valueOf(nextToRun));
                        access$400.log(severe, new StringBuilder(35 + value.length()).append("Exception while executing runnable ").append(value).toString(), e);
                    }
                }
            }
            finally {
                if (stillRunning) {
                    synchronized (SerializingExecutor.this.internalLock) {
                        SerializingExecutor.this.isThreadScheduled = false;
                    }
                }
            }
        }
    }
}
