// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import kotlin.ranges.RangesKt___RangesKt;

public final class DefaultExecutor extends EventLoopImplBase implements Runnable
{
    private static final long KEEP_ALIVE_NANOS;
    private static volatile Thread _thread;
    private static volatile int debugStatus;
    public static final DefaultExecutor INSTANCE;
    
    @Override
    protected final Thread getThread() {
        Thread thread;
        if ((thread = DefaultExecutor._thread) == null) {
            thread = this.createThreadSync();
        }
        return thread;
    }
    
    private static boolean isShutdownRequested() {
        final int debugStatus;
        return (debugStatus = DefaultExecutor.debugStatus) == 2 || debugStatus == 3;
    }
    
    @Override
    public final void run() {
        final ThreadLocalEventLoop instance = ThreadLocalEventLoop.INSTANCE;
        ThreadLocalEventLoop.setEventLoop$kotlinx_coroutines_core(this);
        try {
            long shutdownNanos = Long.MAX_VALUE;
            if (!this.notifyStartup()) {
                return;
            }
            while (true) {
                Thread.interrupted();
                long parkNanos;
                if ((parkNanos = this.processNextEvent()) == Long.MAX_VALUE) {
                    if (shutdownNanos == Long.MAX_VALUE) {
                        final TimeSource timeSource = null;
                        final long now = (timeSource != null) ? timeSource.nanoTime() : System.nanoTime();
                        if (shutdownNanos == Long.MAX_VALUE) {
                            shutdownNanos = now + DefaultExecutor.KEEP_ALIVE_NANOS;
                        }
                        final long tillShutdown;
                        if ((tillShutdown = shutdownNanos - now) <= 0L) {
                            return;
                        }
                        parkNanos = RangesKt___RangesKt.coerceAtMost(parkNanos, tillShutdown);
                    }
                    else {
                        parkNanos = RangesKt___RangesKt.coerceAtMost(parkNanos, DefaultExecutor.KEEP_ALIVE_NANOS);
                    }
                }
                if (parkNanos > 0L) {
                    if (isShutdownRequested()) {
                        return;
                    }
                    LockSupport.parkNanos(this, parkNanos);
                }
            }
        }
        finally {
            DefaultExecutor._thread = null;
            this.acknowledgeShutdownIfNeeded();
            if (!this.isEmpty()) {
                this.getThread();
            }
        }
    }
    
    private final synchronized Thread createThreadSync() {
        Thread thread;
        if ((thread = DefaultExecutor._thread) == null) {
            final Thread thread2;
            final Thread $this$apply = DefaultExecutor._thread = (thread2 = new Thread(this, "kotlinx.coroutines.DefaultExecutor"));
            $this$apply.setDaemon(true);
            $this$apply.start();
            thread = thread2;
        }
        return thread;
    }
    
    private final synchronized boolean notifyStartup() {
        if (isShutdownRequested()) {
            return false;
        }
        DefaultExecutor.debugStatus = 1;
        this.notifyAll();
        return true;
    }
    
    private final synchronized void acknowledgeShutdownIfNeeded() {
        if (!isShutdownRequested()) {
            return;
        }
        DefaultExecutor.debugStatus = 3;
        this.resetAll();
        this.notifyAll();
    }
    
    private DefaultExecutor() {
    }
    
    static {
        EventLoop.incrementUseCount$default(INSTANCE = new DefaultExecutor(), false, 1, null);
        final TimeUnit milliseconds = TimeUnit.MILLISECONDS;
        TimeUnit timeUnit;
        Long n;
        try {
            timeUnit = milliseconds;
            n = Long.getLong("kotlinx.coroutines.DefaultExecutor.keepAlive", 1000L);
        }
        catch (SecurityException ex) {
            timeUnit = milliseconds;
            n = 1000L;
        }
        final Long value = n;
        Intrinsics.checkExpressionValueIsNotNull(value, "try {\n            java.l\u2026AULT_KEEP_ALIVE\n        }");
        KEEP_ALIVE_NANOS = timeUnit.toNanos(value);
    }
}
