// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.scheduling;

import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import kotlinx.coroutines.ExecutorCoroutineDispatcher;

final class LimitingDispatcher extends ExecutorCoroutineDispatcher implements Executor, TaskContext
{
    private final ConcurrentLinkedQueue<Runnable> queue;
    private volatile int inFlightTasks;
    private static final AtomicIntegerFieldUpdater inFlightTasks$FU;
    private final ExperimentalCoroutineDispatcher dispatcher;
    private final int parallelism;
    private final TaskMode taskMode;
    
    @Override
    public final void execute(final Runnable command) {
        Intrinsics.checkParameterIsNotNull(command, "command");
        this.dispatch(command, false);
    }
    
    @Override
    public final void close() {
        throw new IllegalStateException("Close cannot be invoked on LimitingBlockingDispatcher".toString());
    }
    
    @Override
    public final void dispatch(final CoroutineContext context, final Runnable block) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(block, "block");
        this.dispatch(block, false);
    }
    
    private final void dispatch(Runnable block, final boolean fair) {
        block = block;
        while (LimitingDispatcher.inFlightTasks$FU.incrementAndGet(this) > this.parallelism) {
            this.queue.add(block);
            if (LimitingDispatcher.inFlightTasks$FU.decrementAndGet(this) >= this.parallelism) {
                return;
            }
            final Runnable runnable = this.queue.poll();
            if (runnable == null) {
                return;
            }
            block = runnable;
        }
        this.dispatcher.dispatchWithContext$kotlinx_coroutines_core(block, this, fair);
    }
    
    @Override
    public final String toString() {
        return super.toString() + "[dispatcher = " + this.dispatcher + ']';
    }
    
    @Override
    public final void afterTask() {
        Runnable next;
        if ((next = this.queue.poll()) != null) {
            this.dispatcher.dispatchWithContext$kotlinx_coroutines_core(next, this, true);
            return;
        }
        LimitingDispatcher.inFlightTasks$FU.decrementAndGet(this);
        final Runnable runnable = this.queue.poll();
        if (runnable == null) {
            return;
        }
        next = runnable;
        this.dispatch(next, true);
    }
    
    @Override
    public final TaskMode getTaskMode() {
        return this.taskMode;
    }
    
    public LimitingDispatcher(final ExperimentalCoroutineDispatcher dispatcher, final int parallelism, final TaskMode taskMode) {
        Intrinsics.checkParameterIsNotNull(dispatcher, "dispatcher");
        Intrinsics.checkParameterIsNotNull(taskMode, "taskMode");
        this.dispatcher = dispatcher;
        this.parallelism = parallelism;
        this.taskMode = taskMode;
        this.queue = new ConcurrentLinkedQueue<Runnable>();
        this.inFlightTasks = 0;
    }
    
    static {
        inFlightTasks$FU = AtomicIntegerFieldUpdater.newUpdater(LimitingDispatcher.class, "inFlightTasks");
    }
}
