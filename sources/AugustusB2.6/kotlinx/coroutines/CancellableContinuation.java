// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.coroutines.Continuation;

public interface CancellableContinuation<T> extends Continuation<T>
{
    boolean isCompleted();
    
    Object tryResumeWithException(final Throwable p0);
    
    void completeResume(final Object p0);
    
    void invokeOnCancellation(final Function1<? super Throwable, Unit> p0);
}
