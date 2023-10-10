// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.TimeoutException;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.ExecutionException;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;
import java.time.Duration;
import java.util.concurrent.Executor;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Use FluentFuture.from(Futures.immediate*Future) or SettableFuture")
@ElementTypesAreNonnullByDefault
@Beta
@GwtCompatible(emulated = true)
public abstract class FluentFuture<V> extends GwtFluentFutureCatchingSpecialization<V>
{
    FluentFuture() {
    }
    
    public static <V> FluentFuture<V> from(final ListenableFuture<V> future) {
        return (future instanceof FluentFuture) ? ((FluentFuture)future) : new ForwardingFluentFuture<V>(future);
    }
    
    @Deprecated
    public static <V> FluentFuture<V> from(final FluentFuture<V> future) {
        return Preconditions.checkNotNull(future);
    }
    
    @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
    public final <X extends Throwable> FluentFuture<V> catching(final Class<X> exceptionType, final Function<? super X, ? extends V> fallback, final Executor executor) {
        return (FluentFuture)Futures.catching((ListenableFuture<? extends V>)this, exceptionType, fallback, executor);
    }
    
    @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
    public final <X extends Throwable> FluentFuture<V> catchingAsync(final Class<X> exceptionType, final AsyncFunction<? super X, ? extends V> fallback, final Executor executor) {
        return (FluentFuture)Futures.catchingAsync((ListenableFuture<? extends V>)this, exceptionType, fallback, executor);
    }
    
    @GwtIncompatible
    public final FluentFuture<V> withTimeout(final Duration timeout, final ScheduledExecutorService scheduledExecutor) {
        return this.withTimeout(Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS, scheduledExecutor);
    }
    
    @GwtIncompatible
    public final FluentFuture<V> withTimeout(final long timeout, final TimeUnit unit, final ScheduledExecutorService scheduledExecutor) {
        return (FluentFuture)Futures.withTimeout((ListenableFuture<V>)this, timeout, unit, scheduledExecutor);
    }
    
    public final <T> FluentFuture<T> transformAsync(final AsyncFunction<? super V, T> function, final Executor executor) {
        return (FluentFuture<T>)(FluentFuture)Futures.transformAsync((ListenableFuture<Object>)this, (AsyncFunction<? super Object, ? extends T>)function, executor);
    }
    
    public final <T> FluentFuture<T> transform(final Function<? super V, T> function, final Executor executor) {
        return (FluentFuture<T>)(FluentFuture)Futures.transform((ListenableFuture<Object>)this, (Function<? super Object, ? extends T>)function, executor);
    }
    
    public final void addCallback(final FutureCallback<? super V> callback, final Executor executor) {
        Futures.addCallback((ListenableFuture<Object>)this, (FutureCallback<? super Object>)callback, executor);
    }
    
    abstract static class TrustedFuture<V> extends FluentFuture<V> implements Trusted<V>
    {
        @ParametricNullness
        @CanIgnoreReturnValue
        @Override
        public final V get() throws InterruptedException, ExecutionException {
            return super.get();
        }
        
        @ParametricNullness
        @CanIgnoreReturnValue
        @Override
        public final V get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return super.get(timeout, unit);
        }
        
        @Override
        public final boolean isDone() {
            return super.isDone();
        }
        
        @Override
        public final boolean isCancelled() {
            return super.isCancelled();
        }
        
        @Override
        public final void addListener(final Runnable listener, final Executor executor) {
            super.addListener(listener, executor);
        }
        
        @CanIgnoreReturnValue
        @Override
        public final boolean cancel(final boolean mayInterruptIfRunning) {
            return super.cancel(mayInterruptIfRunning);
        }
    }
}
