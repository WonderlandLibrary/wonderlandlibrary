// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import java.util.concurrent.CancellationException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.Continuation;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public final class CancelledContinuation extends CompletedExceptionally
{
    private volatile int _resumed;
    private static final AtomicIntegerFieldUpdater _resumed$FU;
    
    public final boolean makeResumed() {
        return CancelledContinuation._resumed$FU.compareAndSet(this, 0, 1);
    }
    
    public CancelledContinuation(final Continuation<?> continuation, final Throwable cause, final boolean handled) {
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        Throwable cause2 = cause;
        if (cause == null) {
            cause2 = new CancellationException("Continuation " + continuation + " was cancelled normally");
        }
        super(cause2, handled);
        this._resumed = 0;
    }
    
    static {
        _resumed$FU = AtomicIntegerFieldUpdater.newUpdater(CancelledContinuation.class, "_resumed");
    }
}
