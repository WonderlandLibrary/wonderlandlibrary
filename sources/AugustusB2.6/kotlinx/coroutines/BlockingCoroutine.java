// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;
import java.util.concurrent.locks.LockSupport;
import kotlin.jvm.internal.Intrinsics;

final class BlockingCoroutine<T> extends AbstractCoroutine<T>
{
    private final Thread blockedThread;
    private final EventLoop eventLoop;
    
    @Override
    protected final boolean isScopedCoroutine() {
        return true;
    }
    
    @Override
    protected final void afterCompletionInternal$4cfcfd12() {
        if (Intrinsics.areEqual(Thread.currentThread(), this.blockedThread) ^ true) {
            LockSupport.unpark(this.blockedThread);
        }
    }
    
    public final T joinBlocking() {
        try {
            final EventLoop eventLoop = this.eventLoop;
            if (eventLoop != null) {
                EventLoop.incrementUseCount$default(eventLoop, false, 1, null);
            }
            try {
                while (!Thread.interrupted()) {
                    final EventLoop eventLoop2 = this.eventLoop;
                    final long parkNanos = (eventLoop2 != null) ? eventLoop2.processNextEvent() : Long.MAX_VALUE;
                    if (this.isCompleted()) {}
                    LockSupport.parkNanos(this, parkNanos);
                }
                final InterruptedException it = new InterruptedException();
                this.cancelImpl$kotlinx_coroutines_core(it);
                throw it;
            }
            finally {
                final EventLoop eventLoop3 = this.eventLoop;
                if (eventLoop3 != null) {
                    EventLoop.decrementUseCount$default(eventLoop3, false, 1, null);
                }
            }
        }
        finally {}
        Object unboxState;
        Object state;
        if (!((state = (unboxState = JobSupportKt.unboxState(this.getState$kotlinx_coroutines_core()))) instanceof CompletedExceptionally)) {
            unboxState = null;
        }
        final CompletedExceptionally completedExceptionally = (CompletedExceptionally)unboxState;
        if (completedExceptionally != null) {
            state = completedExceptionally;
            throw completedExceptionally.cause;
        }
        return (T)state;
    }
    
    public BlockingCoroutine(final CoroutineContext parentContext, final Thread blockedThread, final EventLoop eventLoop) {
        Intrinsics.checkParameterIsNotNull(parentContext, "parentContext");
        Intrinsics.checkParameterIsNotNull(blockedThread, "blockedThread");
        super(parentContext, true);
        this.blockedThread = blockedThread;
        this.eventLoop = eventLoop;
    }
}
