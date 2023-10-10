// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.base.MoreObjects;
import com.google.common.util.concurrent.internal.InternalFutures;
import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Objects;
import javax.annotation.CheckForNull;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import com.google.common.base.Function;
import java.util.concurrent.Future;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;
import java.time.Duration;
import com.google.common.annotations.Beta;
import java.util.concurrent.Executor;
import java.util.concurrent.Callable;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public final class Futures extends GwtFuturesCatchingSpecialization
{
    private Futures() {
    }
    
    public static <V> ListenableFuture<V> immediateFuture(@ParametricNullness final V value) {
        if (value == null) {
            final ListenableFuture<V> typedNull = (ListenableFuture<V>)ImmediateFuture.NULL;
            return typedNull;
        }
        return new ImmediateFuture<V>(value);
    }
    
    public static ListenableFuture<Void> immediateVoidFuture() {
        return (ListenableFuture<Void>)ImmediateFuture.NULL;
    }
    
    public static <V> ListenableFuture<V> immediateFailedFuture(final Throwable throwable) {
        Preconditions.checkNotNull(throwable);
        return new ImmediateFuture.ImmediateFailedFuture<V>(throwable);
    }
    
    public static <V> ListenableFuture<V> immediateCancelledFuture() {
        return new ImmediateFuture.ImmediateCancelledFuture<V>();
    }
    
    @Beta
    public static <O> ListenableFuture<O> submit(final Callable<O> callable, final Executor executor) {
        final TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
        executor.execute(task);
        return task;
    }
    
    @Beta
    public static ListenableFuture<Void> submit(final Runnable runnable, final Executor executor) {
        final TrustedListenableFutureTask<Void> task = TrustedListenableFutureTask.create(runnable, (Void)null);
        executor.execute(task);
        return task;
    }
    
    @Beta
    public static <O> ListenableFuture<O> submitAsync(final AsyncCallable<O> callable, final Executor executor) {
        final TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
        executor.execute(task);
        return task;
    }
    
    @Beta
    @GwtIncompatible
    public static <O> ListenableFuture<O> scheduleAsync(final AsyncCallable<O> callable, final Duration delay, final ScheduledExecutorService executorService) {
        return scheduleAsync(callable, Internal.toNanosSaturated(delay), TimeUnit.NANOSECONDS, executorService);
    }
    
    @Beta
    @GwtIncompatible
    public static <O> ListenableFuture<O> scheduleAsync(final AsyncCallable<O> callable, final long delay, final TimeUnit timeUnit, final ScheduledExecutorService executorService) {
        final TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
        final Future<?> scheduled = executorService.schedule(task, delay, timeUnit);
        task.addListener(new Runnable() {
            @Override
            public void run() {
                scheduled.cancel(false);
            }
        }, MoreExecutors.directExecutor());
        return task;
    }
    
    @Beta
    @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
    public static <V, X extends Throwable> ListenableFuture<V> catching(final ListenableFuture<? extends V> input, final Class<X> exceptionType, final Function<? super X, ? extends V> fallback, final Executor executor) {
        return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
    }
    
    @Beta
    @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
    public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(final ListenableFuture<? extends V> input, final Class<X> exceptionType, final AsyncFunction<? super X, ? extends V> fallback, final Executor executor) {
        return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
    }
    
    @Beta
    @GwtIncompatible
    public static <V> ListenableFuture<V> withTimeout(final ListenableFuture<V> delegate, final Duration time, final ScheduledExecutorService scheduledExecutor) {
        return withTimeout(delegate, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS, scheduledExecutor);
    }
    
    @Beta
    @GwtIncompatible
    public static <V> ListenableFuture<V> withTimeout(final ListenableFuture<V> delegate, final long time, final TimeUnit unit, final ScheduledExecutorService scheduledExecutor) {
        if (delegate.isDone()) {
            return delegate;
        }
        return TimeoutFuture.create(delegate, time, unit, scheduledExecutor);
    }
    
    @Beta
    public static <I, O> ListenableFuture<O> transformAsync(final ListenableFuture<I> input, final AsyncFunction<? super I, ? extends O> function, final Executor executor) {
        return AbstractTransformFuture.create(input, function, executor);
    }
    
    @Beta
    public static <I, O> ListenableFuture<O> transform(final ListenableFuture<I> input, final Function<? super I, ? extends O> function, final Executor executor) {
        return AbstractTransformFuture.create(input, function, executor);
    }
    
    @Beta
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
    
    @SafeVarargs
    @Beta
    public static <V> ListenableFuture<List<V>> allAsList(final ListenableFuture<? extends V>... futures) {
        final ListenableFuture<List<V>> nonNull;
        final ListenableFuture<List<V>> nullable = nonNull = (ListenableFuture<List<V>>)new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), true);
        return nonNull;
    }
    
    @Beta
    public static <V> ListenableFuture<List<V>> allAsList(final Iterable<? extends ListenableFuture<? extends V>> futures) {
        final ListenableFuture<List<V>> nonNull;
        final ListenableFuture<List<V>> nullable = nonNull = (ListenableFuture<List<V>>)new CollectionFuture.ListFuture((ImmutableCollection<? extends ListenableFuture<?>>)ImmutableList.copyOf((Iterable<?>)futures), true);
        return nonNull;
    }
    
    @SafeVarargs
    @Beta
    public static <V> FutureCombiner<V> whenAllComplete(final ListenableFuture<? extends V>... futures) {
        return new FutureCombiner<V>(false, (ImmutableList)ImmutableList.copyOf(futures));
    }
    
    @Beta
    public static <V> FutureCombiner<V> whenAllComplete(final Iterable<? extends ListenableFuture<? extends V>> futures) {
        return new FutureCombiner<V>(false, (ImmutableList)ImmutableList.copyOf((Iterable<?>)futures));
    }
    
    @SafeVarargs
    @Beta
    public static <V> FutureCombiner<V> whenAllSucceed(final ListenableFuture<? extends V>... futures) {
        return new FutureCombiner<V>(true, (ImmutableList)ImmutableList.copyOf(futures));
    }
    
    @Beta
    public static <V> FutureCombiner<V> whenAllSucceed(final Iterable<? extends ListenableFuture<? extends V>> futures) {
        return new FutureCombiner<V>(true, (ImmutableList)ImmutableList.copyOf((Iterable<?>)futures));
    }
    
    @Beta
    public static <V> ListenableFuture<V> nonCancellationPropagating(final ListenableFuture<V> future) {
        if (future.isDone()) {
            return future;
        }
        final NonCancellationPropagatingFuture<V> output = new NonCancellationPropagatingFuture<V>(future);
        future.addListener(output, MoreExecutors.directExecutor());
        return output;
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
    public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(final Iterable<? extends ListenableFuture<? extends T>> futures) {
        final ListenableFuture<? extends T>[] copy = (ListenableFuture<? extends T>[])gwtCompatibleToArray((Iterable<? extends ListenableFuture<?>>)futures);
        final InCompletionOrderState<T> state = new InCompletionOrderState<T>((ListenableFuture[])copy);
        final ImmutableList.Builder<AbstractFuture<T>> delegatesBuilder = ImmutableList.builderWithExpectedSize(copy.length);
        for (int i = 0; i < copy.length; ++i) {
            delegatesBuilder.add(new InCompletionOrderFuture<T>((InCompletionOrderState)state));
        }
        final ImmutableList<AbstractFuture<T>> delegates = delegatesBuilder.build();
        for (int j = 0; j < copy.length; ++j) {
            final int localI = j;
            copy[j].addListener(new Runnable() {
                @Override
                public void run() {
                    state.recordInputCompletion(delegates, localI);
                }
            }, MoreExecutors.directExecutor());
        }
        final ImmutableList<ListenableFuture<T>> delegatesCast = (ImmutableList<ListenableFuture<T>>)delegates;
        return delegatesCast;
    }
    
    private static <T> ListenableFuture<? extends T>[] gwtCompatibleToArray(final Iterable<? extends ListenableFuture<? extends T>> futures) {
        Collection<ListenableFuture<? extends T>> collection;
        if (futures instanceof Collection) {
            collection = (Collection<ListenableFuture<? extends T>>)(Collection)futures;
        }
        else {
            collection = (Collection<ListenableFuture<? extends T>>)ImmutableList.copyOf((Iterable<?>)futures);
        }
        return collection.toArray(new ListenableFuture[0]);
    }
    
    public static <V> void addCallback(final ListenableFuture<V> future, final FutureCallback<? super V> callback, final Executor executor) {
        Preconditions.checkNotNull(callback);
        future.addListener(new CallbackListener<Object>(future, callback), executor);
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
    public static <V> V getDone(final Future<V> future) throws ExecutionException {
        Preconditions.checkState(future.isDone(), "Future was expected to be done: %s", future);
        return Uninterruptibles.getUninterruptibly(future);
    }
    
    @ParametricNullness
    @Beta
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static <V, X extends Exception> V getChecked(final Future<V> future, final Class<X> exceptionClass) throws X, Exception {
        return FuturesGetChecked.getChecked(future, exceptionClass);
    }
    
    @ParametricNullness
    @Beta
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static <V, X extends Exception> V getChecked(final Future<V> future, final Class<X> exceptionClass, final Duration timeout) throws X, Exception {
        return getChecked(future, exceptionClass, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
    }
    
    @ParametricNullness
    @Beta
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static <V, X extends Exception> V getChecked(final Future<V> future, final Class<X> exceptionClass, final long timeout, final TimeUnit unit) throws X, Exception {
        return FuturesGetChecked.getChecked(future, exceptionClass, timeout, unit);
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
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
    
    private static void wrapAndThrowUnchecked(final Throwable cause) {
        if (cause instanceof Error) {
            throw new ExecutionError((Error)cause);
        }
        throw new UncheckedExecutionException(cause);
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
        
        @CanIgnoreReturnValue
        public <C> ListenableFuture<C> call(final Callable<C> combiner, final Executor executor) {
            return (ListenableFuture<C>)new CombinedFuture(this.futures, this.allMustSucceed, executor, (Callable<Object>)combiner);
        }
        
        public ListenableFuture<?> run(final Runnable combiner, final Executor executor) {
            return this.call((Callable<?>)new Callable<Void>(this) {
                @CheckForNull
                @Override
                public Void call() throws Exception {
                    combiner.run();
                    return null;
                }
            }, executor);
        }
    }
    
    private static final class NonCancellationPropagatingFuture<V> extends TrustedFuture<V> implements Runnable
    {
        @CheckForNull
        private ListenableFuture<V> delegate;
        
        NonCancellationPropagatingFuture(final ListenableFuture<V> delegate) {
            this.delegate = delegate;
        }
        
        @Override
        public void run() {
            final ListenableFuture<V> localDelegate = this.delegate;
            if (localDelegate != null) {
                this.setFuture((ListenableFuture<? extends V>)localDelegate);
            }
        }
        
        @CheckForNull
        @Override
        protected String pendingToString() {
            final ListenableFuture<V> localDelegate = this.delegate;
            if (localDelegate != null) {
                final String value = String.valueOf(localDelegate);
                return new StringBuilder(11 + String.valueOf(value).length()).append("delegate=[").append(value).append("]").toString();
            }
            return null;
        }
        
        @Override
        protected void afterDone() {
            this.delegate = null;
        }
    }
    
    private static final class InCompletionOrderFuture<T> extends AbstractFuture<T>
    {
        @CheckForNull
        private InCompletionOrderState<T> state;
        
        private InCompletionOrderFuture(final InCompletionOrderState<T> state) {
            this.state = state;
        }
        
        @Override
        public boolean cancel(final boolean interruptIfRunning) {
            final InCompletionOrderState<T> localState = this.state;
            if (super.cancel(interruptIfRunning)) {
                ((InCompletionOrderState<Object>)Objects.requireNonNull(localState)).recordOutputCancellation(interruptIfRunning);
                return true;
            }
            return false;
        }
        
        @Override
        protected void afterDone() {
            this.state = null;
        }
        
        @CheckForNull
        @Override
        protected String pendingToString() {
            final InCompletionOrderState<T> localState = this.state;
            if (localState != null) {
                return new StringBuilder(49).append("inputCount=[").append(((InCompletionOrderState<Object>)localState).inputFutures.length).append("], remaining=[").append(((InCompletionOrderState<Object>)localState).incompleteOutputCount.get()).append("]").toString();
            }
            return null;
        }
    }
    
    private static final class InCompletionOrderState<T>
    {
        private boolean wasCancelled;
        private boolean shouldInterrupt;
        private final AtomicInteger incompleteOutputCount;
        private final ListenableFuture<? extends T>[] inputFutures;
        private volatile int delegateIndex;
        
        private InCompletionOrderState(final ListenableFuture<? extends T>[] inputFutures) {
            this.wasCancelled = false;
            this.shouldInterrupt = true;
            this.delegateIndex = 0;
            this.inputFutures = inputFutures;
            this.incompleteOutputCount = new AtomicInteger(inputFutures.length);
        }
        
        private void recordOutputCancellation(final boolean interruptIfRunning) {
            this.wasCancelled = true;
            if (!interruptIfRunning) {
                this.shouldInterrupt = false;
            }
            this.recordCompletion();
        }
        
        private void recordInputCompletion(final ImmutableList<AbstractFuture<T>> delegates, final int inputFutureIndex) {
            final ListenableFuture<? extends T> inputFuture = Objects.requireNonNull(this.inputFutures[inputFutureIndex]);
            this.inputFutures[inputFutureIndex] = null;
            for (int i = this.delegateIndex; i < delegates.size(); ++i) {
                if (delegates.get(i).setFuture(inputFuture)) {
                    this.recordCompletion();
                    this.delegateIndex = i + 1;
                    return;
                }
            }
            this.delegateIndex = delegates.size();
        }
        
        private void recordCompletion() {
            if (this.incompleteOutputCount.decrementAndGet() == 0 && this.wasCancelled) {
                for (final ListenableFuture<? extends T> toCancel : this.inputFutures) {
                    if (toCancel != null) {
                        toCancel.cancel(this.shouldInterrupt);
                    }
                }
            }
        }
    }
    
    private static final class CallbackListener<V> implements Runnable
    {
        final Future<V> future;
        final FutureCallback<? super V> callback;
        
        CallbackListener(final Future<V> future, final FutureCallback<? super V> callback) {
            this.future = future;
            this.callback = callback;
        }
        
        @Override
        public void run() {
            if (this.future instanceof InternalFutureFailureAccess) {
                final Throwable failure = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess)this.future);
                if (failure != null) {
                    this.callback.onFailure(failure);
                    return;
                }
            }
            V value;
            try {
                value = Futures.getDone(this.future);
            }
            catch (ExecutionException e) {
                this.callback.onFailure(e.getCause());
                return;
            }
            catch (RuntimeException | Error ex) {
                final Throwable t;
                final Throwable e2 = t;
                this.callback.onFailure(e2);
                return;
            }
            this.callback.onSuccess((Object)value);
        }
        
        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).addValue(this.callback).toString();
        }
    }
}
