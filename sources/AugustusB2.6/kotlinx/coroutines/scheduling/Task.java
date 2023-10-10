// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.scheduling;

import kotlin.jvm.internal.Intrinsics;

public abstract class Task implements Runnable
{
    public long submissionTime;
    public TaskContext taskContext;
    
    public final TaskMode getMode() {
        return this.taskContext.getTaskMode();
    }
    
    public Task(final long submissionTime, final TaskContext taskContext) {
        Intrinsics.checkParameterIsNotNull(taskContext, "taskContext");
        this.submissionTime = submissionTime;
        this.taskContext = taskContext;
    }
    
    public Task() {
        this(0L, NonBlockingContext.INSTANCE);
    }
}
