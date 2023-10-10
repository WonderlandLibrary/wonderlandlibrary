// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.CoroutineContext;

public final class Unconfined extends CoroutineDispatcher
{
    public static final Unconfined INSTANCE;
    
    @Override
    public final boolean isDispatchNeeded(final CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return false;
    }
    
    @Override
    public final void dispatch(final CoroutineContext context, final Runnable block) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(block, "block");
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final String toString() {
        return "Unconfined";
    }
    
    private Unconfined() {
    }
    
    static {
        INSTANCE = new Unconfined();
    }
}
