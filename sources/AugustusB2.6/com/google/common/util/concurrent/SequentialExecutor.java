// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.logging.Level;
import javax.annotation.CheckForNull;
import java.util.concurrent.RejectedExecutionException;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import com.google.j2objc.annotations.RetainedWith;
import com.google.errorprone.annotations.concurrent.GuardedBy;
import java.util.Deque;
import java.util.logging.Logger;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.Executor;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
final class SequentialExecutor implements Executor
{
    private static final Logger log;
    private final Executor executor;
    @GuardedBy("queue")
    private final Deque<Runnable> queue;
    @GuardedBy("queue")
    private WorkerRunningState workerRunningState;
    @GuardedBy("queue")
    private long workerRunCount;
    @RetainedWith
    private final QueueWorker worker;
    
    SequentialExecutor(final Executor executor) {
        this.queue = new ArrayDeque<Runnable>();
        this.workerRunningState = WorkerRunningState.IDLE;
        this.workerRunCount = 0L;
        this.worker = new QueueWorker();
        this.executor = Preconditions.checkNotNull(executor);
    }
    
    @Override
    public void execute(final Runnable task) {
        Preconditions.checkNotNull(task);
        final long oldRunCount;
        final Runnable submittedTask;
        synchronized (this.queue) {
            if (this.workerRunningState == WorkerRunningState.RUNNING || this.workerRunningState == WorkerRunningState.QUEUED) {
                this.queue.add(task);
                return;
            }
            oldRunCount = this.workerRunCount;
            submittedTask = new Runnable(this) {
                @Override
                public void run() {
                    task.run();
                }
                
                @Override
                public String toString() {
                    return task.toString();
                }
            };
            this.queue.add(submittedTask);
            this.workerRunningState = WorkerRunningState.QUEUING;
        }
        try {
            this.executor.execute(this.worker);
        }
        catch (RuntimeException | Error ex) {
            final Throwable t2;
            final Throwable t = t2;
            synchronized (this.queue) {
                final boolean removed = (this.workerRunningState == WorkerRunningState.IDLE || this.workerRunningState == WorkerRunningState.QUEUING) && this.queue.removeLastOccurrence(submittedTask);
                if (!(t instanceof RejectedExecutionException) || removed) {
                    throw t;
                }
            }
            return;
        }
        final boolean alreadyMarkedQueued = this.workerRunningState != WorkerRunningState.QUEUING;
        if (alreadyMarkedQueued) {
            return;
        }
        synchronized (this.queue) {
            if (this.workerRunCount == oldRunCount && this.workerRunningState == WorkerRunningState.QUEUING) {
                this.workerRunningState = WorkerRunningState.QUEUED;
            }
        }
    }
    
    @Override
    public String toString() {
        final int identityHashCode = System.identityHashCode(this);
        final String value = String.valueOf(this.executor);
        return new StringBuilder(32 + String.valueOf(value).length()).append("SequentialExecutor@").append(identityHashCode).append("{").append(value).append("}").toString();
    }
    
    static {
        log = Logger.getLogger(SequentialExecutor.class.getName());
    }
    
    enum WorkerRunningState
    {
        IDLE, 
        QUEUING, 
        QUEUED, 
        RUNNING;
        
        private static /* synthetic */ WorkerRunningState[] $values() {
            return new WorkerRunningState[] { WorkerRunningState.IDLE, WorkerRunningState.QUEUING, WorkerRunningState.QUEUED, WorkerRunningState.RUNNING };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    private final class QueueWorker implements Runnable
    {
        @CheckForNull
        Runnable task;
        
        @Override
        public void run() {
            try {
                this.workOnQueue();
            }
            catch (Error e) {
                synchronized (SequentialExecutor.this.queue) {
                    SequentialExecutor.this.workerRunningState = WorkerRunningState.IDLE;
                }
                throw e;
            }
        }
        
        private void workOnQueue() {
            boolean interruptedDuringTask = false;
            boolean hasSetRunning = false;
            try {
                while (true) {
                    synchronized (SequentialExecutor.this.queue) {
                        if (!hasSetRunning) {
                            if (SequentialExecutor.this.workerRunningState == WorkerRunningState.RUNNING) {
                                return;
                            }
                            SequentialExecutor.this.workerRunCount++;
                            SequentialExecutor.this.workerRunningState = WorkerRunningState.RUNNING;
                            hasSetRunning = true;
                        }
                        this.task = SequentialExecutor.this.queue.poll();
                        if (this.task == null) {
                            SequentialExecutor.this.workerRunningState = WorkerRunningState.IDLE;
                            return;
                        }
                    }
                    interruptedDuringTask |= Thread.interrupted();
                    try {
                        this.task.run();
                    }
                    catch (RuntimeException e) {
                        final Logger access$400 = SequentialExecutor.log;
                        final Level severe = Level.SEVERE;
                        final String value = String.valueOf(this.task);
                        access$400.log(severe, new StringBuilder(35 + String.valueOf(value).length()).append("Exception while executing runnable ").append(value).toString(), e);
                    }
                    finally {
                        this.task = null;
                    }
                }
            }
            finally {
                if (interruptedDuringTask) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        
        @Override
        public String toString() {
            final Runnable currentlyRunning = this.task;
            if (currentlyRunning != null) {
                final String value = String.valueOf(currentlyRunning);
                return new StringBuilder(34 + String.valueOf(value).length()).append("SequentialExecutorWorker{running=").append(value).append("}").toString();
            }
            final String value2 = String.valueOf(SequentialExecutor.this.workerRunningState);
            return new StringBuilder(32 + String.valueOf(value2).length()).append("SequentialExecutorWorker{state=").append(value2).append("}").toString();
        }
    }
}
