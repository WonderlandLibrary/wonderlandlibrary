// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.scheduling;

import kotlin.jvm.internal.Intrinsics;
import com.badlogic.gdx.graphics.Pixmap;

public final class TaskImpl extends Task
{
    private Runnable block;
    
    @Override
    public final void run() {
        try {
            this.block.run();
        }
        finally {
            this.taskContext.afterTask();
        }
    }
    
    @Override
    public final String toString() {
        return "Task[" + Pixmap.getClassSimpleName(this.block) + '@' + Pixmap.getHexAddress(this.block) + ", " + this.submissionTime + ", " + this.taskContext + ']';
    }
    
    public TaskImpl(final Runnable block, final long submissionTime, final TaskContext taskContext) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        Intrinsics.checkParameterIsNotNull(taskContext, "taskContext");
        super(submissionTime, taskContext);
        this.block = block;
    }
}
