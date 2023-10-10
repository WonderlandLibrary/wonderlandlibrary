// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;

public final class BlockingEventLoop extends EventLoopImplBase
{
    private final Thread thread;
    
    @Override
    protected final Thread getThread() {
        return this.thread;
    }
    
    public BlockingEventLoop(final Thread thread) {
        Intrinsics.checkParameterIsNotNull(thread, "thread");
        this.thread = thread;
    }
}
