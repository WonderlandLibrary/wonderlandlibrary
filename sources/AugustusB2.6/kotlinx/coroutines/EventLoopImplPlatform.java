// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
import java.util.concurrent.locks.LockSupport;

public abstract class EventLoopImplPlatform extends EventLoop
{
    protected abstract Thread getThread();
    
    protected final void unpark() {
        final Thread thread = this.getThread();
        if (Thread.currentThread() != thread) {
            LockSupport.unpark(thread);
        }
    }
    
    protected final void reschedule(final long now, final EventLoopImplBase.DelayedTask delayedTask) {
        Intrinsics.checkParameterIsNotNull(delayedTask, "delayedTask");
        if (DebugKt.getASSERTIONS_ENABLED() && this == DefaultExecutor.INSTANCE) {
            throw new AssertionError();
        }
        DefaultExecutor.INSTANCE.schedule(now, delayedTask);
    }
}
