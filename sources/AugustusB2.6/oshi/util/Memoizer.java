// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Memoizer
{
    private static final Supplier<Long> DEFAULT_EXPIRATION_NANOS;
    
    private Memoizer() {
    }
    
    private static long queryExpirationConfig() {
        return TimeUnit.MILLISECONDS.toNanos(GlobalConfig.get("oshi.util.memoizer.expiration", 300));
    }
    
    public static long defaultExpiration() {
        return Memoizer.DEFAULT_EXPIRATION_NANOS.get();
    }
    
    public static <T> Supplier<T> memoize(final Supplier<T> original, final long ttlNanos) {
        return new Supplier<T>() {
            final Supplier<T> delegate = original;
            volatile T value;
            volatile long expirationNanos;
            
            @Override
            public T get() {
                long nanos = this.expirationNanos;
                final long now = System.nanoTime();
                if (nanos == 0L || (ttlNanos >= 0L && now - nanos >= 0L)) {
                    synchronized (this) {
                        if (nanos == this.expirationNanos) {
                            final T t = this.delegate.get();
                            this.value = t;
                            nanos = now + ttlNanos;
                            this.expirationNanos = ((nanos == 0L) ? 1L : nanos);
                            return t;
                        }
                    }
                }
                return this.value;
            }
        };
    }
    
    public static <T> Supplier<T> memoize(final Supplier<T> original) {
        return memoize(original, -1L);
    }
    
    static {
        DEFAULT_EXPIRATION_NANOS = memoize(Memoizer::queryExpirationConfig, TimeUnit.MINUTES.toNanos(1L));
    }
}
