// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.misc;

import java.util.concurrent.Executors;
import lombok.NonNull;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public class Multithreading
{
    private static final ScheduledExecutorService RUNNABLE_POOL;
    public static ExecutorService POOL;
    
    public static void schedule(final Runnable r, final long initialDelay, final long delay, final TimeUnit unit) {
        Multithreading.RUNNABLE_POOL.scheduleAtFixedRate(r, initialDelay, delay, unit);
    }
    
    public static ScheduledFuture<?> schedule(final Runnable r, final long delay, final TimeUnit unit) {
        return Multithreading.RUNNABLE_POOL.schedule(r, delay, unit);
    }
    
    public static int getTotal() {
        return ((ThreadPoolExecutor)Multithreading.POOL).getActiveCount();
    }
    
    public static void runAsync(final Runnable runnable) {
        Multithreading.POOL.execute(runnable);
    }
    
    static {
        RUNNABLE_POOL = Executors.newScheduledThreadPool(3, new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(0);
            
            @Override
            public Thread newThread(@NonNull final Runnable r) {
                if (r == null) {
                    throw new NullPointerException("r is marked non-null but is null");
                }
                return new Thread(r, "Multithreading Thread " + this.counter.incrementAndGet());
            }
        });
        Multithreading.POOL = Executors.newCachedThreadPool(new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(0);
            
            @Override
            public Thread newThread(@NonNull final Runnable r) {
                if (r == null) {
                    throw new NullPointerException("r is marked non-null but is null");
                }
                return new Thread(r, "Multithreading Thread " + this.counter.incrementAndGet());
            }
        });
    }
}
