// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import java.util.concurrent.Callable;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.gong.mcleaks.util.google.common.collect.Queues;
import me.gong.mcleaks.util.google.common.collect.ImmutableCollection;
import me.gong.mcleaks.util.google.common.collect.ImmutableList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.base.Function;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@GwtCompatible(emulated = true)
public final class Futures extends GwtFuturesCatchingSpecialization
{
    private static final AsyncFunction<ListenableFuture<Object>, Object> DEREFERENCER;
    
    private Futures() {
    }
    
    @GwtIncompatible
    public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(final ListenableFuture<V> future, final Function<? super Exception, X> mapper) {
        return new MappingCheckedFuture<V, X>(Preconditions.checkNotNull(future), mapper);
    }
    
    public static <V> ListenableFuture<V> immediateFuture(@Nullable final V value) {
        if (value == null) {
            final ListenableFuture<V> typedNull = (ListenableFuture<V>)ImmediateFuture.ImmediateSuccessfulFuture.NULL;
            return typedNull;
        }
        return new ImmediateFuture.ImmediateSuccessfulFuture<V>(value);
    }
    
    @GwtIncompatible
    public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(@Nullable final V value) {
        return new ImmediateFuture.ImmediateSuccessfulCheckedFuture<V, X>(value);
    }
    
    public static <V> ListenableFuture<V> immediateFailedFuture(final Throwable throwable) {
        Preconditions.checkNotNull(throwable);
        return new ImmediateFuture.ImmediateFailedFuture<V>(throwable);
    }
    
    public static <V> ListenableFuture<V> immediateCancelledFuture() {
        return new ImmediateFuture.ImmediateCancelledFuture<V>();
    }
    
    @GwtIncompatible
    public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(final X exception) {
        Preconditions.checkNotNull(exception);
        return new ImmediateFuture.ImmediateFailedCheckedFuture<V, X>(exception);
    }
    
    @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
    public static <V, X extends Throwable> ListenableFuture<V> catching(final ListenableFuture<? extends V> input, final Class<X> exceptionType, final Function<? super X, ? extends V> fallback) {
        return AbstractCatchingFuture.create(input, exceptionType, fallback);
    }
    
    @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
    public static <V, X extends Throwable> ListenableFuture<V> catching(final ListenableFuture<? extends V> input, final Class<X> exceptionType, final Function<? super X, ? extends V> fallback, final Executor executor) {
        return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
    }
    
    @CanIgnoreReturnValue
    @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
    public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(final ListenableFuture<? extends V> input, final Class<X> exceptionType, final AsyncFunction<? super X, ? extends V> fallback) {
        return AbstractCatchingFuture.create(input, exceptionType, fallback);
    }
    
    @CanIgnoreReturnValue
    @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
    public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(final ListenableFuture<? extends V> input, final Class<X> exceptionType, final AsyncFunction<? super X, ? extends V> fallback, final Executor executor) {
        return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
    }
    
    @GwtIncompatible
    public static <V> ListenableFuture<V> withTimeout(final ListenableFuture<V> delegate, final long time, final TimeUnit unit, final ScheduledExecutorService scheduledExecutor) {
        return TimeoutFuture.create(delegate, time, unit, scheduledExecutor);
    }
    
    public static <I, O> ListenableFuture<O> transformAsync(final ListenableFuture<I> input, final AsyncFunction<? super I, ? extends O> function) {
        return AbstractTransformFuture.create(input, function);
    }
    
    public static <I, O> ListenableFuture<O> transformAsync(final ListenableFuture<I> input, final AsyncFunction<? super I, ? extends O> function, final Executor executor) {
        return AbstractTransformFuture.create(input, function, executor);
    }
    
    public static <I, O> ListenableFuture<O> transform(final ListenableFuture<I> input, final Function<? super I, ? extends O> function) {
        return AbstractTransformFuture.create(input, function);
    }
    
    public static <I, O> ListenableFuture<O> transform(final ListenableFuture<I> input, final Function<? super I, ? extends O> function, final Executor executor) {
        return AbstractTransformFuture.create(input, function, executor);
    }
    
    @GwtIncompatible
    public static <I, O> Future<O> lazyTransform(final Future<I> input, final Function<? super I, ? extends O> function) {
        Preconditions.checkNotNull(input);
        Preconditions.checkNotNull(function);
        return new Future<O>() {
            @Override
            public boolean cancel(final boolean mayInterruptIfRunning) {
                return input.cancel(mayInterruptIfRunning);
            }
            
            @Override
            public boolean isCancelled() {
                return input.isCancelled();
            }
            
            @Override
            public boolean isDone() {
                return input.isDone();
            }
            
            @Override
            public O get() throws InterruptedException, ExecutionException {
                return this.applyTransformation(input.get());
            }
            
            @Override
            public O get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return this.applyTransformation(input.get(timeout, unit));
            }
            
            private O applyTransformation(final I input) throws ExecutionException {
                try {
                    return function.apply(input);
                }
                catch (Throwable t) {
                    throw new ExecutionException(t);
                }
            }
        };
    }
    
    public static <V> ListenableFuture<V> dereference(final ListenableFuture<? extends ListenableFuture<? extends V>> nested) {
        return transformAsync(nested, (AsyncFunction<? super ListenableFuture<? extends V>, ? extends V>)Futures.DEREFERENCER);
    }
    
    @SafeVarargs
    @Beta
    public static <V> ListenableFuture<List<V>> allAsList(final ListenableFuture<? extends V>... futures) {
        return (ListenableFuture<List<V>>)new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), true);
    }
    
    @Beta
    public static <V> ListenableFuture<List<V>> allAsList(final Iterable<? extends ListenableFuture<? extends V>> futures) {
        return (ListenableFuture<List<V>>)new CollectionFuture.ListFuture((ImmutableCollection<? extends ListenableFuture<?>>)ImmutableList.copyOf((Iterable<?>)futures), true);
    }
    
    @SafeVarargs
    public static <V> FutureCombiner<V> whenAllComplete(final ListenableFuture<? extends V>... futures) {
        return new FutureCombiner<V>(false, (ImmutableList)ImmutableList.copyOf(futures));
    }
    
    public static <V> FutureCombiner<V> whenAllComplete(final Iterable<? extends ListenableFuture<? extends V>> futures) {
        return new FutureCombiner<V>(false, (ImmutableList)ImmutableList.copyOf((Iterable<?>)futures));
    }
    
    @SafeVarargs
    public static <V> FutureCombiner<V> whenAllSucceed(final ListenableFuture<? extends V>... futures) {
        return new FutureCombiner<V>(true, (ImmutableList)ImmutableList.copyOf(futures));
    }
    
    public static <V> FutureCombiner<V> whenAllSucceed(final Iterable<? extends ListenableFuture<? extends V>> futures) {
        return new FutureCombiner<V>(true, (ImmutableList)ImmutableList.copyOf((Iterable<?>)futures));
    }
    
    public static <V> ListenableFuture<V> nonCancellationPropagating(final ListenableFuture<V> future) {
        if (future.isDone()) {
            return future;
        }
        return new NonCancellationPropagatingFuture<V>(future);
    }
    
    @SafeVarargs
    @Beta
    public static <V> ListenableFuture<List<V>> successfulAsList(final ListenableFuture<? extends V>... futures) {
        return (ListenableFuture<List<V>>)new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), false);
    }
    
    @Beta
    public static <V> ListenableFuture<List<V>> successfulAsList(final Iterable<? extends ListenableFuture<? extends V>> futures) {
        return (ListenableFuture<List<V>>)new CollectionFuture.ListFuture((ImmutableCollection<? extends ListenableFuture<?>>)ImmutableList.copyOf((Iterable<?>)futures), false);
    }
    
    @Beta
    @GwtIncompatible
    public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(final Iterable<? extends ListenableFuture<? extends T>> futures) {
        final ConcurrentLinkedQueue<SettableFuture<T>> delegates = Queues.newConcurrentLinkedQueue();
        final ImmutableList.Builder<ListenableFuture<T>> listBuilder = ImmutableList.builder();
        final SerializingExecutor executor = new SerializingExecutor(MoreExecutors.directExecutor());
        for (final ListenableFuture<? extends T> future : futures) {
            final SettableFuture<T> delegate = SettableFuture.create();
            delegates.add(delegate);
            future.addListener(new Runnable() {
                @Override
                public void run() {
                    ((SettableFuture)delegates.remove()).setFuture(future);
                }
            }, executor);
            listBuilder.add(delegate);
        }
        return listBuilder.build();
    }
    
    public static <V> void addCallback(final ListenableFuture<V> future, final FutureCallback<? super V> callback) {
        addCallback(future, callback, MoreExecutors.directExecutor());
    }
    
    public static <V> void addCallback(final ListenableFuture<V> future, final FutureCallback<? super V> callback, final Executor executor) {
        Preconditions.checkNotNull(callback);
        final Runnable callbackListener = new Runnable() {
            @Override
            public void run() {
                V value;
                try {
                    value = Futures.getDone(future);
                }
                catch (ExecutionException e) {
                    callback.onFailure(e.getCause());
                    return;
                }
                catch (RuntimeException e2) {
                    callback.onFailure(e2);
                    return;
                }
                catch (Error e3) {
                    callback.onFailure(e3);
                    return;
                }
                callback.onSuccess(value);
            }
        };
        future.addListener(callbackListener, executor);
    }
    
    @CanIgnoreReturnValue
    public static <V> V getDone(final Future<V> future) throws ExecutionException {
        Preconditions.checkState(future.isDone(), "Future was expected to be done: %s", future);
        return Uninterruptibles.getUninterruptibly(future);
    }
    
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static <V, X extends Exception> V getChecked(final Future<V> future, final Class<X> exceptionClass) throws X, Exception {
        return FuturesGetChecked.getChecked(future, exceptionClass);
    }
    
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static <V, X extends Exception> V getChecked(final Future<V> future, final Class<X> exceptionClass, final long timeout, final TimeUnit unit) throws X, Exception {
        return FuturesGetChecked.getChecked(future, exceptionClass, timeout, unit);
    }
    
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static <V> V getUnchecked(final Future<V> future) {
        Preconditions.checkNotNull(future);
        try {
            return Uninterruptibles.getUninterruptibly(future);
        }
        catch (ExecutionException e) {
            wrapAndThrowUnchecked(e.getCause());
            throw new AssertionError();
        }
    }
    
    @GwtIncompatible
    private static void wrapAndThrowUnchecked(final Throwable cause) {
        if (cause instanceof Error) {
            throw new ExecutionError((Error)cause);
        }
        throw new UncheckedExecutionException(cause);
    }
    
    static {
        DEREFERENCER = new AsyncFunction<ListenableFuture<Object>, Object>() {
            @Override
            public ListenableFuture<Object> apply(final ListenableFuture<Object> input) {
                return input;
            }
        };
    }
    
    @Beta
    @CanIgnoreReturnValue
    @GwtCompatible
    public static final class FutureCombiner<V>
    {
        private final boolean allMustSucceed;
        private final ImmutableList<ListenableFuture<? extends V>> futures;
        
        private FutureCombiner(final boolean allMustSucceed, final ImmutableList<ListenableFuture<? extends V>> futures) {
            this.allMustSucceed = allMustSucceed;
            this.futures = futures;
        }
        
        public <C> ListenableFuture<C> callAsync(final AsyncCallable<C> combiner, final Executor executor) {
            return (ListenableFuture<C>)new CombinedFuture(this.futures, this.allMustSucceed, executor, (AsyncCallable<Object>)combiner);
        }
        
        public <C> ListenableFuture<C> callAsync(final AsyncCallable<C> combiner) {
            return this.callAsync(combiner, MoreExecutors.directExecutor());
        }
        
        @CanIgnoreReturnValue
        public <C> ListenableFuture<C> call(final Callable<C> combiner, final Executor executor) {
            return (ListenableFuture<C>)new CombinedFuture(this.futures, this.allMustSucceed, executor, (Callable<Object>)combiner);
        }
        
        @CanIgnoreReturnValue
        public <C> ListenableFuture<C> call(final Callable<C> combiner) {
            return this.call(combiner, MoreExecutors.directExecutor());
        }
    }
    
    private static final class NonCancellationPropagatingFuture<V> extends TrustedFuture<V>
    {
        NonCancellationPropagatingFuture(final ListenableFuture<V> delegate) {
            delegate.addListener(new Runnable() {
                @Override
                public void run() {
                    NonCancellationPropagatingFuture.this.setFuture(delegate);
                }
            }, MoreExecutors.directExecutor());
        }
    }
    
    @GwtIncompatible
    private static class MappingCheckedFuture<V, X extends Exception> extends AbstractCheckedFuture<V, X>
    {
        final Function<? super Exception, X> mapper;
        
        MappingCheckedFuture(final ListenableFuture<V> delegate, final Function<? super Exception, X> mapper) {
            super(delegate);
            this.mapper = Preconditions.checkNotNull(mapper);
        }
        
        @Override
        protected X mapException(final Exception e) {
            return this.mapper.apply(e);
        }
    }
}
