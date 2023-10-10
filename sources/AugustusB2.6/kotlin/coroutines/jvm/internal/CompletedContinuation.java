// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.jvm.internal;

import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.Continuation;

public final class CompletedContinuation implements Continuation<Object>
{
    public static final CompletedContinuation INSTANCE;
    
    @Override
    public final CoroutineContext getContext() {
        throw new IllegalStateException("This continuation is already complete".toString());
    }
    
    @Override
    public final void resumeWith(final Object result) {
        throw new IllegalStateException("This continuation is already complete".toString());
    }
    
    @Override
    public final String toString() {
        return "This continuation is already complete";
    }
    
    private CompletedContinuation() {
    }
    
    static {
        INSTANCE = new CompletedContinuation();
    }
}
