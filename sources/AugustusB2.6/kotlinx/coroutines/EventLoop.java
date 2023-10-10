// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.ArrayQueue;

public abstract class EventLoop extends CoroutineDispatcher
{
    private long useCount;
    private boolean shared;
    private ArrayQueue<DispatchedTask<?>> unconfinedQueue;
    
    public long processNextEvent() {
        if (!this.processUnconfinedEvent()) {
            return Long.MAX_VALUE;
        }
        return this.getNextTime();
    }
    
    protected boolean isEmpty() {
        return this.isUnconfinedQueueEmpty();
    }
    
    protected long getNextTime() {
        final ArrayQueue<DispatchedTask<?>> unconfinedQueue = this.unconfinedQueue;
        if (unconfinedQueue == null) {
            return Long.MAX_VALUE;
        }
        if (unconfinedQueue.isEmpty()) {
            return Long.MAX_VALUE;
        }
        return 0L;
    }
    
    public final boolean processUnconfinedEvent() {
        final ArrayQueue<DispatchedTask<?>> unconfinedQueue = this.unconfinedQueue;
        if (unconfinedQueue == null) {
            return false;
        }
        final DispatchedTask<?> dispatchedTask = unconfinedQueue.removeFirstOrNull();
        if (dispatchedTask == null) {
            return false;
        }
        dispatchedTask.run();
        return true;
    }
    
    public final void dispatchUnconfined(final DispatchedTask<?> task) {
        Intrinsics.checkParameterIsNotNull(task, "task");
        ArrayQueue<DispatchedTask<?>> unconfinedQueue;
        ArrayQueue<DispatchedTask<?>> arrayQueue;
        if ((arrayQueue = (unconfinedQueue = this.unconfinedQueue)) == null) {
            final ArrayQueue it;
            final ArrayQueue arrayQueue2 = it = new ArrayQueue();
            this.unconfinedQueue = (ArrayQueue<DispatchedTask<?>>)it;
            unconfinedQueue = (arrayQueue = (ArrayQueue<DispatchedTask<?>>)arrayQueue2);
        }
        unconfinedQueue.addLast(task);
    }
    
    public final boolean isUnconfinedLoopActive() {
        return this.useCount >= delta(true);
    }
    
    public final boolean isUnconfinedQueueEmpty() {
        final ArrayQueue<DispatchedTask<?>> unconfinedQueue = this.unconfinedQueue;
        return unconfinedQueue == null || unconfinedQueue.isEmpty();
    }
    
    private static long delta(final boolean unconfined) {
        if (unconfined) {
            return 4294967296L;
        }
        return 1L;
    }
    
    public final void incrementUseCount(final boolean unconfined) {
        this.useCount += delta(unconfined);
        if (!unconfined) {
            this.shared = true;
        }
    }
    
    public final void decrementUseCount(final boolean unconfined) {
        this.useCount -= delta(unconfined);
        if (this.useCount > 0L) {
            return;
        }
        if (DebugKt.getASSERTIONS_ENABLED() && this.useCount != 0L) {
            throw new AssertionError();
        }
        if (this.shared) {
            this.shutdown();
        }
    }
    
    protected void shutdown() {
    }
}
