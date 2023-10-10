// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;

public final class NonDisposableHandle implements ChildHandle, DisposableHandle
{
    public static final NonDisposableHandle INSTANCE;
    
    @Override
    public final void dispose() {
    }
    
    @Override
    public final boolean childCancelled(final Throwable cause) {
        Intrinsics.checkParameterIsNotNull(cause, "cause");
        return false;
    }
    
    @Override
    public final String toString() {
        return "NonDisposableHandle";
    }
    
    private NonDisposableHandle() {
    }
    
    static {
        INSTANCE = new NonDisposableHandle();
    }
}
