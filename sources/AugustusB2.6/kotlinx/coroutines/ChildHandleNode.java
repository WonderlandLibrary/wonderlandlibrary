// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import java.util.concurrent.CancellationException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Unit;

public final class ChildHandleNode extends JobCancellingNode<JobSupport> implements ChildHandle
{
    public final ChildJob childJob;
    
    @Override
    public final void invoke(final Throwable cause) {
        this.childJob.parentCancelled((ParentJob)this.job);
    }
    
    @Override
    public final boolean childCancelled(final Throwable cause) {
        Intrinsics.checkParameterIsNotNull(cause, "cause");
        final JobSupport jobSupport = (JobSupport)this.job;
        Intrinsics.checkParameterIsNotNull(cause, "cause");
        return cause instanceof CancellationException || jobSupport.cancelImpl$kotlinx_coroutines_core(cause);
    }
    
    @Override
    public final String toString() {
        return "ChildHandle[" + this.childJob + ']';
    }
    
    public ChildHandleNode(final JobSupport parent, final ChildJob childJob) {
        Intrinsics.checkParameterIsNotNull(parent, "parent");
        Intrinsics.checkParameterIsNotNull(childJob, "childJob");
        super(parent);
        this.childJob = childJob;
    }
}
