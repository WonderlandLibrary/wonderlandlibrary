// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.errorprone.annotations.ForOverride;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import com.google.common.util.concurrent.internal.InternalFutures;
import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import com.google.common.base.Function;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class AbstractCatchingFuture<V, X extends Throwable, F, T> extends TrustedFuture<V> implements Runnable
{
    @CheckForNull
    ListenableFuture<? extends V> inputFuture;
    @CheckForNull
    Class<X> exceptionType;
    @CheckForNull
    F fallback;
    
    static <V, X extends Throwable> ListenableFuture<V> create(final ListenableFuture<? extends V> input, final Class<X> exceptionType, final Function<? super X, ? extends V> fallback, final Executor executor) {
        final CatchingFuture<V, X> future = new CatchingFuture<V, X>(input, exceptionType, fallback);
        input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
        return (ListenableFuture<V>)future;
    }
    
    static <X extends Throwable, V> ListenableFuture<V> create(final ListenableFuture<? extends V> input, final Class<X> exceptionType, final AsyncFunction<? super X, ? extends V> fallback, final Executor executor) {
        final AsyncCatchingFuture<V, X> future = new AsyncCatchingFuture<V, X>(input, exceptionType, fallback);
        input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
        return (ListenableFuture<V>)future;
    }
    
    AbstractCatchingFuture(final ListenableFuture<? extends V> inputFuture, final Class<X> exceptionType, final F fallback) {
        this.inputFuture = Preconditions.checkNotNull(inputFuture);
        this.exceptionType = Preconditions.checkNotNull(exceptionType);
        this.fallback = Preconditions.checkNotNull(fallback);
    }
    
    @Override
    public final void run() {
        final ListenableFuture<? extends V> localInputFuture = this.inputFuture;
        final Class<X> localExceptionType = this.exceptionType;
        final F localFallback = this.fallback;
        if ((localInputFuture == null | localExceptionType == null | localFallback == null) || this.isCancelled()) {
            return;
        }
        this.inputFuture = null;
        V sourceResult = null;
        Throwable throwable = null;
        try {
            if (localInputFuture instanceof InternalFutureFailureAccess) {
                throwable = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess)localInputFuture);
            }
            if (throwable == null) {
                sourceResult = Futures.getDone((Future<V>)localInputFuture);
            }
        }
        catch (ExecutionException e) {
            throwable = e.getCause();
            if (throwable == null) {
                final String value = String.valueOf(localInputFuture.getClass());
                final String value2 = String.valueOf(e.getClass());
                throwable = new NullPointerException(new StringBuilder(35 + String.valueOf(value).length() + String.valueOf(value2).length()).append("Future type ").append(value).append(" threw ").append(value2).append(" without a cause").toString());
            }
        }
        catch (Throwable e2) {
            throwable = e2;
        }
        if (throwable == null) {
            this.set(NullnessCasts.uncheckedCastNullableTToT(sourceResult));
            return;
        }
        if (!Platform.isInstanceOfThrowableClass(throwable, localExceptionType)) {
            this.setFuture(localInputFuture);
            return;
        }
        final X castThrowable = (X)throwable;
        T fallbackResult;
        try {
            fallbackResult = this.doFallback(localFallback, castThrowable);
        }
        catch (Throwable t) {
            this.setException(t);
            return;
        }
        finally {
            this.exceptionType = null;
            this.fallback = null;
        }
        this.setResult(fallbackResult);
    }
    
    @CheckForNull
    @Override
    protected String pendingToString() {
        final ListenableFuture<? extends V> localInputFuture = this.inputFuture;
        final Class<X> localExceptionType = this.exceptionType;
        final F localFallback = this.fallback;
        final String superString = super.pendingToString();
        String resultString = "";
        if (localInputFuture != null) {
            final String value = String.valueOf(localInputFuture);
            resultString = new StringBuilder(16 + String.valueOf(value).length()).append("inputFuture=[").append(value).append("], ").toString();
        }
        if (localExceptionType != null && localFallback != null) {
            final String s = resultString;
            final String value2 = String.valueOf(localExceptionType);
            final String value3 = String.valueOf(localFallback);
            return new StringBuilder(29 + String.valueOf(s).length() + String.valueOf(value2).length() + String.valueOf(value3).length()).append(s).append("exceptionType=[").append(value2).append("], fallback=[").append(value3).append("]").toString();
        }
        if (superString != null) {
            final String value4 = String.valueOf(resultString);
            final String value5 = String.valueOf(superString);
            return (value5.length() != 0) ? value4.concat(value5) : new String(value4);
        }
        return null;
    }
    
    @ParametricNullness
    @ForOverride
    abstract T doFallback(final F p0, final X p1) throws Exception;
    
    @ForOverride
    abstract void setResult(@ParametricNullness final T p0);
    
    @Override
    protected final void afterDone() {
        this.maybePropagateCancellationTo(this.inputFuture);
        this.inputFuture = null;
        this.exceptionType = null;
        this.fallback = null;
    }
    
    private static final class AsyncCatchingFuture<V, X extends Throwable> extends AbstractCatchingFuture<V, X, AsyncFunction<? super X, ? extends V>, ListenableFuture<? extends V>>
    {
        AsyncCatchingFuture(final ListenableFuture<? extends V> input, final Class<X> exceptionType, final AsyncFunction<? super X, ? extends V> fallback) {
            super(input, exceptionType, fallback);
        }
        
        @Override
        ListenableFuture<? extends V> doFallback(final AsyncFunction<? super X, ? extends V> fallback, final X cause) throws Exception {
            final ListenableFuture<? extends V> replacement = fallback.apply((Object)cause);
            Preconditions.checkNotNull(replacement, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", fallback);
            return replacement;
        }
        
        @Override
        void setResult(final ListenableFuture<? extends V> result) {
            this.setFuture((ListenableFuture<? extends V>)result);
        }
    }
    
    private static final class CatchingFuture<V, X extends Throwable> extends AbstractCatchingFuture<V, X, Function<? super X, ? extends V>, V>
    {
        CatchingFuture(final ListenableFuture<? extends V> input, final Class<X> exceptionType, final Function<? super X, ? extends V> fallback) {
            super(input, exceptionType, fallback);
        }
        
        @ParametricNullness
        @Override
        V doFallback(final Function<? super X, ? extends V> fallback, final X cause) throws Exception {
            return (V)fallback.apply((Object)cause);
        }
        
        @Override
        void setResult(@ParametricNullness final V result) {
            this.set((V)result);
        }
    }
}
