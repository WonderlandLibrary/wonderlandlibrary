// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;

public abstract class JobCancellingNode<J extends Job> extends JobNode<J>
{
    public JobCancellingNode(final J job) {
        Intrinsics.checkParameterIsNotNull(job, "job");
        super(job);
    }
}
