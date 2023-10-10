// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.Objects;
import javax.annotation.CheckForNull;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
public final class ExecutionSequencer
{
    private final AtomicReference<ListenableFuture<Void>> ref;
    private ThreadConfinedTaskQueue latestTaskQueue;
    
    private ExecutionSequencer() {
        this.ref = new AtomicReference<ListenableFuture<Void>>(Futures.immediateVoidFuture());
        this.latestTaskQueue = new ThreadConfinedTaskQueue();
    }
    
    public static ExecutionSequencer create() {
        return new ExecutionSequencer();
    }
    
    public <T> ListenableFuture<T> submit(final Callable<T> callable, final Executor executor) {
        Preconditions.checkNotNull(callable);
        Preconditions.checkNotNull(executor);
        return this.submitAsync((AsyncCallable<T>)new AsyncCallable<T>(this) {
            @Override
            public ListenableFuture<T> call() throws Exception {
                return Futures.immediateFuture(callable.call());
            }
            
            @Override
            public String toString() {
                return callable.toString();
            }
        }, executor);
    }
    
    public <T> ListenableFuture<T> submitAsync(final AsyncCallable<T> callable, final Executor executor) {
        Preconditions.checkNotNull(callable);
        Preconditions.checkNotNull(executor);
        final TaskNonReentrantExecutor taskExecutor = new TaskNonReentrantExecutor(executor, this);
        final AsyncCallable<T> task = new AsyncCallable<T>(this) {
            @Override
            public ListenableFuture<T> call() throws Exception {
                if (!taskExecutor.trySetStarted()) {
                    return Futures.immediateCancelledFuture();
                }
                return callable.call();
            }
            
            @Override
            public String toString() {
                return callable.toString();
            }
        };
        final SettableFuture<Void> newFuture = SettableFuture.create();
        final ListenableFuture<Void> oldFuture = this.ref.getAndSet(newFuture);
        final TrustedListenableFutureTask<T> taskFuture = TrustedListenableFutureTask.create(task);
        oldFuture.addListener(taskFuture, taskExecutor);
        final ListenableFuture<T> outputFuture = Futures.nonCancellationPropagating(taskFuture);
        final Runnable listener = new Runnable(this) {
            @Override
            public void run() {
                if (taskFuture.isDone()) {
                    newFuture.setFuture(oldFuture);
                }
                else if (outputFuture.isCancelled() && taskExecutor.trySetCancelled()) {
                    taskFuture.cancel(false);
                }
            }
        };
        outputFuture.addListener(listener, MoreExecutors.directExecutor());
        taskFuture.addListener(listener, MoreExecutors.directExecutor());
        return outputFuture;
    }
    
    private static final class ThreadConfinedTaskQueue
    {
        @CheckForNull
        Thread thread;
        @CheckForNull
        Runnable nextTask;
        @CheckForNull
        Executor nextExecutor;
    }
    
    enum RunningState
    {
        NOT_RUN, 
        CANCELLED, 
        STARTED;
        
        private static /* synthetic */ RunningState[] $values() {
            return new RunningState[] { RunningState.NOT_RUN, RunningState.CANCELLED, RunningState.STARTED };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    private static final class TaskNonReentrantExecutor extends AtomicReference<RunningState> implements Executor, Runnable
    {
        @CheckForNull
        ExecutionSequencer sequencer;
        @CheckForNull
        Executor delegate;
        @CheckForNull
        Runnable task;
        @CheckForNull
        Thread submitting;
        
        private TaskNonReentrantExecutor(final Executor delegate, final ExecutionSequencer sequencer) {
            super(RunningState.NOT_RUN);
            this.delegate = delegate;
            this.sequencer = sequencer;
        }
        
        @Override
        public void execute(final Runnable task) {
            if (this.get() == RunningState.CANCELLED) {
                this.delegate = null;
                this.sequencer = null;
                return;
            }
            this.submitting = Thread.currentThread();
            try {
                final ThreadConfinedTaskQueue submittingTaskQueue = Objects.requireNonNull(this.sequencer).latestTaskQueue;
                if (submittingTaskQueue.thread == this.submitting) {
                    this.sequencer = null;
                    Preconditions.checkState(submittingTaskQueue.nextTask == null);
                    submittingTaskQueue.nextTask = task;
                    submittingTaskQueue.nextExecutor = Objects.requireNonNull(this.delegate);
                    this.delegate = null;
                }
                else {
                    final Executor localDelegate = Objects.requireNonNull(this.delegate);
                    this.delegate = null;
                    this.task = task;
                    localDelegate.execute(this);
                }
            }
            finally {
                this.submitting = null;
            }
        }
        
        @Override
        public void run() {
            final Thread currentThread = Thread.currentThread();
            if (currentThread != this.submitting) {
                final Runnable localTask = Objects.requireNonNull(this.task);
                this.task = null;
                localTask.run();
                return;
            }
            final ThreadConfinedTaskQueue executingTaskQueue = new ThreadConfinedTaskQueue();
            executingTaskQueue.thread = currentThread;
            Objects.requireNonNull(this.sequencer).latestTaskQueue = executingTaskQueue;
            this.sequencer = null;
            try {
                final Runnable localTask2 = Objects.requireNonNull(this.task);
                this.task = null;
                localTask2.run();
                Runnable queuedTask;
                Executor queuedExecutor;
                while ((queuedTask = executingTaskQueue.nextTask) != null & (queuedExecutor = executingTaskQueue.nextExecutor) != null) {
                    executingTaskQueue.nextTask = null;
                    executingTaskQueue.nextExecutor = null;
                    queuedExecutor.execute(queuedTask);
                }
            }
            finally {
                executingTaskQueue.thread = null;
            }
        }
        
        private boolean trySetStarted() {
            return this.compareAndSet(RunningState.NOT_RUN, RunningState.STARTED);
        }
        
        private boolean trySetCancelled() {
            return this.compareAndSet(RunningState.NOT_RUN, RunningState.CANCELLED);
        }
    }
}
