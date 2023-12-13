// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import java.util.logging.Level;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.ArrayDeque;
import javax.annotation.concurrent.GuardedBy;
import java.util.Deque;
import java.util.logging.Logger;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import java.util.concurrent.Executor;

@GwtIncompatible
final class SerializingExecutor implements Executor
{
    private static final Logger log;
    private final Executor executor;
    @GuardedBy("internalLock")
    private final Deque<Runnable> queue;
    @GuardedBy("internalLock")
    private boolean isWorkerRunning;
    @GuardedBy("internalLock")
    private int suspensions;
    private final Object internalLock;
    
    public SerializingExecutor(final Executor executor) {
        this.queue = new ArrayDeque<Runnable>();
        this.isWorkerRunning = false;
        this.suspensions = 0;
        this.internalLock = new Object();
        this.executor = Preconditions.checkNotNull(executor);
    }
    
    @Override
    public void execute(final Runnable task) {
        synchronized (this.internalLock) {
            this.queue.add(task);
        }
        this.startQueueWorker();
    }
    
    public void executeFirst(final Runnable task) {
        synchronized (this.internalLock) {
            this.queue.addFirst(task);
        }
        this.startQueueWorker();
    }
    
    public void suspend() {
        synchronized (this.internalLock) {
            ++this.suspensions;
        }
    }
    
    public void resume() {
        synchronized (this.internalLock) {
            Preconditions.checkState(this.suspensions > 0);
            --this.suspensions;
        }
        this.startQueueWorker();
    }
    
    private void startQueueWorker() {
        synchronized (this.internalLock) {
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
                synchronized (this.internalLock) {
                    this.isWorkerRunning = false;
                }
            }
        }
    }
    
    static {
        log = Logger.getLogger(SerializingExecutor.class.getName());
    }
    
    private final class QueueWorker implements Runnable
    {
        @Override
        public void run() {
            try {
                this.workOnQueue();
            }
            catch (Error e) {
                synchronized (SerializingExecutor.this.internalLock) {
                    SerializingExecutor.this.isWorkerRunning = false;
                }
                throw e;
            }
        }
        
        private void workOnQueue() {
            while (true) {
                Runnable task = null;
                synchronized (SerializingExecutor.this.internalLock) {
                    if (SerializingExecutor.this.suspensions == 0) {
                        task = SerializingExecutor.this.queue.poll();
                    }
                    if (task == null) {
                        SerializingExecutor.this.isWorkerRunning = false;
                        return;
                    }
                }
                try {
                    task.run();
                }
                catch (RuntimeException e) {
                    SerializingExecutor.log.log(Level.SEVERE, "Exception while executing runnable " + task, e);
                }
            }
        }
    }
}
