// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
import kotlin.Unit;

public final class ChildContinuation extends JobCancellingNode<Job>
{
    private CancellableContinuationImpl<?> child;
    
    @Override
    public final void invoke(final Throwable cause) {
        this.child.cancel(CancellableContinuationImpl.getContinuationCancellationCause(this.job));
    }
    
    @Override
    public final String toString() {
        return "ChildContinuation[" + this.child + ']';
    }
    
    public ChildContinuation(final Job parent, final CancellableContinuationImpl<?> child) {
        Intrinsics.checkParameterIsNotNull(parent, "parent");
        Intrinsics.checkParameterIsNotNull(child, "child");
        super(parent);
        this.child = child;
    }
}
