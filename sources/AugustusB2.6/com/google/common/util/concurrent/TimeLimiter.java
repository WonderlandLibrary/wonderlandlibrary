// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.Callable;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Use FakeTimeLimiter")
@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public interface TimeLimiter
{
     <T> T newProxy(final T p0, final Class<T> p1, final long p2, final TimeUnit p3);
    
    default <T> T newProxy(final T target, final Class<T> interfaceType, final Duration timeout) {
        return this.newProxy(target, interfaceType, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
    }
    
    @CanIgnoreReturnValue
     <T> T callWithTimeout(final Callable<T> p0, final long p1, final TimeUnit p2) throws TimeoutException, InterruptedException, ExecutionException;
    
    @CanIgnoreReturnValue
    default <T> T callWithTimeout(final Callable<T> callable, final Duration timeout) throws TimeoutException, InterruptedException, ExecutionException {
        return this.callWithTimeout(callable, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
    }
    
    @CanIgnoreReturnValue
     <T> T callUninterruptiblyWithTimeout(final Callable<T> p0, final long p1, final TimeUnit p2) throws TimeoutException, ExecutionException;
    
    @CanIgnoreReturnValue
    default <T> T callUninterruptiblyWithTimeout(final Callable<T> callable, final Duration timeout) throws TimeoutException, ExecutionException {
        return this.callUninterruptiblyWithTimeout(callable, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
    }
    
    void runWithTimeout(final Runnable p0, final long p1, final TimeUnit p2) throws TimeoutException, InterruptedException;
    
    default void runWithTimeout(final Runnable runnable, final Duration timeout) throws TimeoutException, InterruptedException {
        this.runWithTimeout(runnable, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
    }
    
    void runUninterruptiblyWithTimeout(final Runnable p0, final long p1, final TimeUnit p2) throws TimeoutException;
    
    default void runUninterruptiblyWithTimeout(final Runnable runnable, final Duration timeout) throws TimeoutException {
        this.runUninterruptiblyWithTimeout(runnable, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
    }
}
