// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import me.gong.mcleaks.util.google.errorprone.annotations.ForOverride;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import me.gong.mcleaks.util.google.common.base.Function;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class AbstractCatchingFuture<V, X extends Throwable, F, T> extends TrustedFuture<V> implements Runnable
{
    @Nullable
    ListenableFuture<? extends V> inputFuture;
    @Nullable
    Class<X> exceptionType;
    @Nullable
    F fallback;
    
    static <X extends Throwable, V> ListenableFuture<V> create(final ListenableFuture<? extends V> input, final Class<X> exceptionType, final Function<? super X, ? extends V> fallback) {
        final CatchingFuture<V, X> future = new CatchingFuture<V, X>(input, exceptionType, fallback);
        input.addListener(future, MoreExecutors.directExecutor());
        return (ListenableFuture<V>)future;
    }
    
    static <V, X extends Throwable> ListenableFuture<V> create(final ListenableFuture<? extends V> input, final Class<X> exceptionType, final Function<? super X, ? extends V> fallback, final Executor executor) {
        final CatchingFuture<V, X> future = new CatchingFuture<V, X>(input, exceptionType, fallback);
        input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
        return (ListenableFuture<V>)future;
    }
    
    static <X extends Throwable, V> ListenableFuture<V> create(final ListenableFuture<? extends V> input, final Class<X> exceptionType, final AsyncFunction<? super X, ? extends V> fallback) {
        final AsyncCatchingFuture<V, X> future = new AsyncCatchingFuture<V, X>(input, exceptionType, fallback);
        input.addListener(future, MoreExecutors.directExecutor());
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
        if (localInputFuture == null | localExceptionType == null | localFallback == null | this.isCancelled()) {
            return;
        }
        this.inputFuture = null;
        this.exceptionType = null;
        this.fallback = null;
        V sourceResult = null;
        Throwable throwable = null;
        try {
            sourceResult = Futures.getDone((Future<V>)localInputFuture);
        }
        catch (ExecutionException e) {
            throwable = Preconditions.checkNotNull(e.getCause());
        }
        catch (Throwable e2) {
            throwable = e2;
        }
        if (throwable == null) {
            this.set(sourceResult);
            return;
        }
        if (!Platform.isInstanceOfThrowableClass(throwable, localExceptionType)) {
            this.setException(throwable);
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
        this.setResult(fallbackResult);
    }
    
    @Nullable
    @ForOverride
    abstract T doFallback(final F p0, final X p1) throws Exception;
    
    @ForOverride
    abstract void setResult(@Nullable final T p0);
    
    @Override
    protected final void afterDone() {
        this.maybePropagateCancellation(this.inputFuture);
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
            Preconditions.checkNotNull(replacement, (Object)"AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)?");
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
        
        @Nullable
        @Override
        V doFallback(final Function<? super X, ? extends V> fallback, final X cause) throws Exception {
            return (V)fallback.apply((Object)cause);
        }
        
        @Override
        void setResult(@Nullable final V result) {
            this.set((V)result);
        }
    }
}
