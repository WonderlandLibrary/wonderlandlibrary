// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.CoroutineContext;

class DeferredCoroutine<T> extends AbstractCoroutine<T> implements Deferred<T>
{
    @Override
    public final T getCompleted() {
        final Object state$kotlinx_coroutines_core;
        final boolean b;
        if (!(b = !((state$kotlinx_coroutines_core = this.getState$kotlinx_coroutines_core()) instanceof Incomplete))) {
            throw new IllegalStateException("This job has not completed yet".toString());
        }
        if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            throw ((CompletedExceptionally)state$kotlinx_coroutines_core).cause;
        }
        return (T)JobSupportKt.unboxState(state$kotlinx_coroutines_core);
    }
    
    public DeferredCoroutine(final CoroutineContext parentContext, final boolean active) {
        Intrinsics.checkParameterIsNotNull(parentContext, "parentContext");
        super(parentContext, active);
    }
}
