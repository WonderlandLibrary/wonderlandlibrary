// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;

public final class ThreadLocalEventLoop
{
    private static final ThreadLocal<EventLoop> ref;
    public static final ThreadLocalEventLoop INSTANCE;
    
    public static EventLoop getEventLoop$kotlinx_coroutines_core() {
        EventLoop eventLoop;
        if ((eventLoop = ThreadLocalEventLoop.ref.get()) == null) {
            final Thread currentThread = Thread.currentThread();
            Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
            final EventLoop it = new BlockingEventLoop(currentThread);
            ThreadLocalEventLoop.ref.set(it);
            eventLoop = it;
        }
        return eventLoop;
    }
    
    public static EventLoop currentOrNull$kotlinx_coroutines_core() {
        return ThreadLocalEventLoop.ref.get();
    }
    
    public static void resetEventLoop$kotlinx_coroutines_core() {
        ThreadLocalEventLoop.ref.set(null);
    }
    
    public static void setEventLoop$kotlinx_coroutines_core(final EventLoop eventLoop) {
        Intrinsics.checkParameterIsNotNull(eventLoop, "eventLoop");
        ThreadLocalEventLoop.ref.set(eventLoop);
    }
    
    private ThreadLocalEventLoop() {
    }
    
    static {
        INSTANCE = new ThreadLocalEventLoop();
        ref = new ThreadLocal<EventLoop>();
    }
}
