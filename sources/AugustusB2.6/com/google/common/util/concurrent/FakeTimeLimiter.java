// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Callable;
import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
@CanIgnoreReturnValue
@GwtIncompatible
public final class FakeTimeLimiter implements TimeLimiter
{
    @Override
    public <T> T newProxy(final T target, final Class<T> interfaceType, final long timeoutDuration, final TimeUnit timeoutUnit) {
        Preconditions.checkNotNull(target);
        Preconditions.checkNotNull(interfaceType);
        Preconditions.checkNotNull(timeoutUnit);
        return target;
    }
    
    @ParametricNullness
    @Override
    public <T> T callWithTimeout(final Callable<T> callable, final long timeoutDuration, final TimeUnit timeoutUnit) throws ExecutionException {
        Preconditions.checkNotNull(callable);
        Preconditions.checkNotNull(timeoutUnit);
        try {
            return callable.call();
        }
        catch (RuntimeException e) {
            throw new UncheckedExecutionException(e);
        }
        catch (Exception e2) {
            throw new ExecutionException(e2);
        }
        catch (Error e3) {
            throw new ExecutionError(e3);
        }
        catch (Throwable e4) {
            throw new ExecutionException(e4);
        }
    }
    
    @ParametricNullness
    @Override
    public <T> T callUninterruptiblyWithTimeout(final Callable<T> callable, final long timeoutDuration, final TimeUnit timeoutUnit) throws ExecutionException {
        return (T)this.callWithTimeout((Callable<Object>)callable, timeoutDuration, timeoutUnit);
    }
    
    @Override
    public void runWithTimeout(final Runnable runnable, final long timeoutDuration, final TimeUnit timeoutUnit) {
        Preconditions.checkNotNull(runnable);
        Preconditions.checkNotNull(timeoutUnit);
        try {
            runnable.run();
        }
        catch (RuntimeException e) {
            throw new UncheckedExecutionException(e);
        }
        catch (Error e2) {
            throw new ExecutionError(e2);
        }
        catch (Throwable e3) {
            throw new UncheckedExecutionException(e3);
        }
    }
    
    @Override
    public void runUninterruptiblyWithTimeout(final Runnable runnable, final long timeoutDuration, final TimeUnit timeoutUnit) {
        this.runWithTimeout(runnable, timeoutDuration, timeoutUnit);
    }
}
