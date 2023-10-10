// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
import kotlin.TypeCastException;

public abstract class JobNode<J extends Job> extends CompletionHandlerBase implements DisposableHandle, Incomplete
{
    public final J job;
    
    @Override
    public final boolean isActive() {
        return true;
    }
    
    @Override
    public final NodeList getList() {
        return null;
    }
    
    @Override
    public final void dispose() {
        final Job job = this.job;
        if (job == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.JobSupport");
        }
        ((JobSupport)job).removeNode$kotlinx_coroutines_core(this);
    }
    
    public JobNode(final J job) {
        Intrinsics.checkParameterIsNotNull(job, "job");
        this.job = job;
    }
}
