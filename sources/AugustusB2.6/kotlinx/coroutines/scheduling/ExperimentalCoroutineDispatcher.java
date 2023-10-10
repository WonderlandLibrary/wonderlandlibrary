// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.scheduling;

import java.util.concurrent.RejectedExecutionException;
import kotlinx.coroutines.DefaultExecutor;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.ExecutorCoroutineDispatcher;

public class ExperimentalCoroutineDispatcher extends ExecutorCoroutineDispatcher
{
    private CoroutineScheduler coroutineScheduler;
    private final int corePoolSize;
    private final int maxPoolSize;
    private final long idleWorkerKeepAliveNs;
    private final String schedulerName;
    
    @Override
    public final void dispatch(final CoroutineContext context, final Runnable block) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(block, "block");
        try {
            CoroutineScheduler.dispatch$default$30bf587f(this.coroutineScheduler, block, null, false, 6);
        }
        catch (RejectedExecutionException ex) {
            DefaultExecutor.INSTANCE.dispatch(context, block);
        }
    }
    
    @Override
    public void close() {
        this.coroutineScheduler.close();
    }
    
    @Override
    public String toString() {
        return super.toString() + "[scheduler = " + this.coroutineScheduler + ']';
    }
    
    public final void dispatchWithContext$kotlinx_coroutines_core(final Runnable block, final TaskContext context, final boolean fair) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        Intrinsics.checkParameterIsNotNull(context, "context");
        try {
            this.coroutineScheduler.dispatch(block, context, fair);
        }
        catch (RejectedExecutionException ex) {
            DefaultExecutor.INSTANCE.enqueue(CoroutineScheduler.createTask$kotlinx_coroutines_core(block, context));
        }
    }
    
    private ExperimentalCoroutineDispatcher(final int corePoolSize, final int maxPoolSize, final long idleWorkerKeepAliveNs, final String schedulerName) {
        Intrinsics.checkParameterIsNotNull(schedulerName, "schedulerName");
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.idleWorkerKeepAliveNs = idleWorkerKeepAliveNs;
        this.schedulerName = schedulerName;
        this.coroutineScheduler = new CoroutineScheduler(this.corePoolSize, this.maxPoolSize, this.idleWorkerKeepAliveNs, this.schedulerName);
    }
    
    private ExperimentalCoroutineDispatcher(final int corePoolSize, final int maxPoolSize, final String schedulerName) {
        Intrinsics.checkParameterIsNotNull(schedulerName, "schedulerName");
        this(corePoolSize, maxPoolSize, TasksKt.IDLE_WORKER_KEEP_ALIVE_NS, schedulerName);
    }
}
