// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.Map;
import java.io.Closeable;
import java.util.IdentityHashMap;
import java.util.Iterator;
import com.google.common.collect.ImmutableList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import com.google.j2objc.annotations.RetainedWith;
import com.google.common.annotations.VisibleForTesting;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionException;
import com.google.common.base.MoreObjects;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.logging.Level;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import java.util.concurrent.Callable;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import javax.annotation.CheckForNull;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Use ClosingFuture.from(Futures.immediate*Future)")
@ElementTypesAreNonnullByDefault
@Beta
public final class ClosingFuture<V>
{
    private static final Logger logger;
    private final AtomicReference<State> state;
    private final CloseableList closeables;
    private final FluentFuture<V> future;
    
    public static <V> ClosingFuture<V> submit(final ClosingCallable<V> callable, final Executor executor) {
        return new ClosingFuture<V>(callable, executor);
    }
    
    public static <V> ClosingFuture<V> submitAsync(final AsyncClosingCallable<V> callable, final Executor executor) {
        return new ClosingFuture<V>(callable, executor);
    }
    
    public static <V> ClosingFuture<V> from(final ListenableFuture<V> future) {
        return new ClosingFuture<V>(future);
    }
    
    @Deprecated
    public static <C extends java.lang.Object> ClosingFuture<C> eventuallyClosing(final ListenableFuture<C> future, final Executor closingExecutor) {
        Preconditions.checkNotNull(closingExecutor);
        final ClosingFuture<C> closingFuture = new ClosingFuture<C>(Futures.nonCancellationPropagating(future));
        Futures.addCallback(future, (FutureCallback<? super C>)new FutureCallback<AutoCloseable>() {
            @Override
            public void onSuccess(@CheckForNull final AutoCloseable result) {
                closingFuture.closeables.closer.eventuallyClose(result, closingExecutor);
            }
            
            @Override
            public void onFailure(final Throwable t) {
            }
        }, MoreExecutors.directExecutor());
        return closingFuture;
    }
    
    public static Combiner whenAllComplete(final Iterable<? extends ClosingFuture<?>> futures) {
        return new Combiner(false, (Iterable)futures);
    }
    
    public static Combiner whenAllComplete(final ClosingFuture<?> future1, final ClosingFuture<?>... moreFutures) {
        return whenAllComplete(Lists.asList(future1, moreFutures));
    }
    
    public static Combiner whenAllSucceed(final Iterable<? extends ClosingFuture<?>> futures) {
        return new Combiner(true, (Iterable)futures);
    }
    
    public static <V1, V2> Combiner2<V1, V2> whenAllSucceed(final ClosingFuture<V1> future1, final ClosingFuture<V2> future2) {
        return new Combiner2<V1, V2>((ClosingFuture)future1, (ClosingFuture)future2);
    }
    
    public static <V1, V2, V3> Combiner3<V1, V2, V3> whenAllSucceed(final ClosingFuture<V1> future1, final ClosingFuture<V2> future2, final ClosingFuture<V3> future3) {
        return new Combiner3<V1, V2, V3>((ClosingFuture)future1, (ClosingFuture)future2, (ClosingFuture)future3);
    }
    
    public static <V1, V2, V3, V4> Combiner4<V1, V2, V3, V4> whenAllSucceed(final ClosingFuture<V1> future1, final ClosingFuture<V2> future2, final ClosingFuture<V3> future3, final ClosingFuture<V4> future4) {
        return new Combiner4<V1, V2, V3, V4>((ClosingFuture)future1, (ClosingFuture)future2, (ClosingFuture)future3, (ClosingFuture)future4);
    }
    
    public static <V1, V2, V3, V4, V5> Combiner5<V1, V2, V3, V4, V5> whenAllSucceed(final ClosingFuture<V1> future1, final ClosingFuture<V2> future2, final ClosingFuture<V3> future3, final ClosingFuture<V4> future4, final ClosingFuture<V5> future5) {
        return new Combiner5<V1, V2, V3, V4, V5>((ClosingFuture)future1, (ClosingFuture)future2, (ClosingFuture)future3, (ClosingFuture)future4, (ClosingFuture)future5);
    }
    
    public static Combiner whenAllSucceed(final ClosingFuture<?> future1, final ClosingFuture<?> future2, final ClosingFuture<?> future3, final ClosingFuture<?> future4, final ClosingFuture<?> future5, final ClosingFuture<?> future6, final ClosingFuture<?>... moreFutures) {
        return whenAllSucceed(FluentIterable.of(future1, future2, future3, future4, future5, future6).append(moreFutures));
    }
    
    private ClosingFuture(final ListenableFuture<V> future) {
        this.state = new AtomicReference<State>(State.OPEN);
        this.closeables = new CloseableList();
        this.future = FluentFuture.from(future);
    }
    
    private ClosingFuture(final ClosingCallable<V> callable, final Executor executor) {
        this.state = new AtomicReference<State>(State.OPEN);
        this.closeables = new CloseableList();
        Preconditions.checkNotNull(callable);
        final TrustedListenableFutureTask<V> task = TrustedListenableFutureTask.create((Callable<V>)new Callable<V>() {
            @ParametricNullness
            @Override
            public V call() throws Exception {
                return callable.call(ClosingFuture.this.closeables.closer);
            }
            
            @Override
            public String toString() {
                return callable.toString();
            }
        });
        executor.execute(task);
        this.future = task;
    }
    
    private ClosingFuture(final AsyncClosingCallable<V> callable, final Executor executor) {
        this.state = new AtomicReference<State>(State.OPEN);
        this.closeables = new CloseableList();
        Preconditions.checkNotNull(callable);
        final TrustedListenableFutureTask<V> task = TrustedListenableFutureTask.create((AsyncCallable<V>)new AsyncCallable<V>() {
            @Override
            public ListenableFuture<V> call() throws Exception {
                final CloseableList newCloseables = new CloseableList();
                try {
                    final ClosingFuture<V> closingFuture = callable.call(newCloseables.closer);
                    closingFuture.becomeSubsumedInto(ClosingFuture.this.closeables);
                    return closingFuture.future;
                }
                finally {
                    ClosingFuture.this.closeables.add(newCloseables, MoreExecutors.directExecutor());
                }
            }
            
            @Override
            public String toString() {
                return callable.toString();
            }
        });
        executor.execute(task);
        this.future = task;
    }
    
    public ListenableFuture<?> statusFuture() {
        return Futures.nonCancellationPropagating((ListenableFuture<?>)this.future.transform((Function<? super V, Object>)Functions.constant((T)null), MoreExecutors.directExecutor()));
    }
    
    public <U> ClosingFuture<U> transform(final ClosingFunction<? super V, U> function, final Executor executor) {
        Preconditions.checkNotNull(function);
        final AsyncFunction<V, U> applyFunction = new AsyncFunction<V, U>() {
            @Override
            public ListenableFuture<U> apply(final V input) throws Exception {
                return ClosingFuture.this.closeables.applyClosingFunction(function, input);
            }
            
            @Override
            public String toString() {
                return function.toString();
            }
        };
        return this.derive((FluentFuture<U>)this.future.transformAsync((AsyncFunction<? super V, U>)applyFunction, executor));
    }
    
    public <U> ClosingFuture<U> transformAsync(final AsyncClosingFunction<? super V, U> function, final Executor executor) {
        Preconditions.checkNotNull(function);
        final AsyncFunction<V, U> applyFunction = new AsyncFunction<V, U>() {
            @Override
            public ListenableFuture<U> apply(final V input) throws Exception {
                return (ListenableFuture<U>)ClosingFuture.this.closeables.applyAsyncClosingFunction((AsyncClosingFunction<V, Object>)function, input);
            }
            
            @Override
            public String toString() {
                return function.toString();
            }
        };
        return this.derive((FluentFuture<U>)this.future.transformAsync((AsyncFunction<? super V, U>)applyFunction, executor));
    }
    
    public static <V, U> AsyncClosingFunction<V, U> withoutCloser(final AsyncFunction<V, U> function) {
        Preconditions.checkNotNull(function);
        return new AsyncClosingFunction<V, U>() {
            @Override
            public ClosingFuture<U> apply(final DeferredCloser closer, final V input) throws Exception {
                return ClosingFuture.from(function.apply(input));
            }
        };
    }
    
    public <X extends Throwable> ClosingFuture<V> catching(final Class<X> exceptionType, final ClosingFunction<? super X, ? extends V> fallback, final Executor executor) {
        return this.catchingMoreGeneric((Class<Throwable>)exceptionType, (ClosingFunction<? super Throwable, ? extends V>)fallback, executor);
    }
    
    private <X extends Throwable, W extends V> ClosingFuture<V> catchingMoreGeneric(final Class<X> exceptionType, final ClosingFunction<? super X, W> fallback, final Executor executor) {
        Preconditions.checkNotNull(fallback);
        final AsyncFunction<X, W> applyFallback = new AsyncFunction<X, W>() {
            @Override
            public ListenableFuture<W> apply(final X exception) throws Exception {
                return ClosingFuture.this.closeables.applyClosingFunction(fallback, exception);
            }
            
            @Override
            public String toString() {
                return fallback.toString();
            }
        };
        return this.derive(this.future.catchingAsync(exceptionType, (AsyncFunction<? super X, ? extends V>)applyFallback, executor));
    }
    
    public <X extends Throwable> ClosingFuture<V> catchingAsync(final Class<X> exceptionType, final AsyncClosingFunction<? super X, ? extends V> fallback, final Executor executor) {
        return this.catchingAsyncMoreGeneric((Class<Throwable>)exceptionType, (AsyncClosingFunction<? super Throwable, ? extends V>)fallback, executor);
    }
    
    private <X extends Throwable, W extends V> ClosingFuture<V> catchingAsyncMoreGeneric(final Class<X> exceptionType, final AsyncClosingFunction<? super X, W> fallback, final Executor executor) {
        Preconditions.checkNotNull(fallback);
        final AsyncFunction<X, W> asyncFunction = new AsyncFunction<X, W>() {
            @Override
            public ListenableFuture<W> apply(final X exception) throws Exception {
                return (ListenableFuture<W>)ClosingFuture.this.closeables.applyAsyncClosingFunction((AsyncClosingFunction<X, Object>)fallback, exception);
            }
            
            @Override
            public String toString() {
                return fallback.toString();
            }
        };
        return this.derive(this.future.catchingAsync(exceptionType, (AsyncFunction<? super X, ? extends V>)asyncFunction, executor));
    }
    
    public FluentFuture<V> finishToFuture() {
        if (this.compareAndUpdateState(State.OPEN, State.WILL_CLOSE)) {
            ClosingFuture.logger.log(Level.FINER, "will close {0}", this);
            this.future.addListener(new Runnable() {
                @Override
                public void run() {
                    ClosingFuture.this.checkAndUpdateState(State.WILL_CLOSE, State.CLOSING);
                    ClosingFuture.this.close();
                    ClosingFuture.this.checkAndUpdateState(State.CLOSING, State.CLOSED);
                }
            }, MoreExecutors.directExecutor());
        }
        else {
            switch (this.state.get()) {
                case SUBSUMED: {
                    throw new IllegalStateException("Cannot call finishToFuture() after deriving another step");
                }
                case WILL_CREATE_VALUE_AND_CLOSER: {
                    throw new IllegalStateException("Cannot call finishToFuture() after calling finishToValueAndCloser()");
                }
                case WILL_CLOSE:
                case CLOSING:
                case CLOSED: {
                    throw new IllegalStateException("Cannot call finishToFuture() twice");
                }
                case OPEN: {
                    throw new AssertionError();
                }
            }
        }
        return this.future;
    }
    
    public void finishToValueAndCloser(final ValueAndCloserConsumer<? super V> consumer, final Executor executor) {
        Preconditions.checkNotNull(consumer);
        if (this.compareAndUpdateState(State.OPEN, State.WILL_CREATE_VALUE_AND_CLOSER)) {
            this.future.addListener(new Runnable() {
                @Override
                public void run() {
                    provideValueAndCloser(consumer, (ClosingFuture<Object>)ClosingFuture.this);
                }
            }, executor);
            return;
        }
        switch (this.state.get()) {
            case SUBSUMED: {
                throw new IllegalStateException("Cannot call finishToValueAndCloser() after deriving another step");
            }
            case WILL_CLOSE:
            case CLOSING:
            case CLOSED: {
                throw new IllegalStateException("Cannot call finishToValueAndCloser() after calling finishToFuture()");
            }
            case WILL_CREATE_VALUE_AND_CLOSER: {
                throw new IllegalStateException("Cannot call finishToValueAndCloser() twice");
            }
            default: {
                throw new AssertionError(this.state);
            }
        }
    }
    
    private static <C, V extends C> void provideValueAndCloser(final ValueAndCloserConsumer<C> consumer, final ClosingFuture<V> closingFuture) {
        consumer.accept(new ValueAndCloser<C>((ClosingFuture<? extends C>)closingFuture));
    }
    
    @CanIgnoreReturnValue
    public boolean cancel(final boolean mayInterruptIfRunning) {
        ClosingFuture.logger.log(Level.FINER, "cancelling {0}", this);
        final boolean cancelled = this.future.cancel(mayInterruptIfRunning);
        if (cancelled) {
            this.close();
        }
        return cancelled;
    }
    
    private void close() {
        ClosingFuture.logger.log(Level.FINER, "closing {0}", this);
        this.closeables.close();
    }
    
    private <U> ClosingFuture<U> derive(final FluentFuture<U> future) {
        final ClosingFuture<U> derived = new ClosingFuture<U>(future);
        this.becomeSubsumedInto(derived.closeables);
        return derived;
    }
    
    private void becomeSubsumedInto(final CloseableList otherCloseables) {
        this.checkAndUpdateState(State.OPEN, State.SUBSUMED);
        otherCloseables.add(this.closeables, MoreExecutors.directExecutor());
    }
    
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("state", this.state.get()).addValue(this.future).toString();
    }
    
    @Override
    protected void finalize() {
        if (this.state.get().equals(State.OPEN)) {
            ClosingFuture.logger.log(Level.SEVERE, "Uh oh! An open ClosingFuture has leaked and will close: {0}", this);
            this.finishToFuture();
        }
    }
    
    private static void closeQuietly(@CheckForNull final AutoCloseable closeable, final Executor executor) {
        if (closeable == null) {
            return;
        }
        try {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        closeable.close();
                    }
                    catch (Exception e) {
                        ClosingFuture.logger.log(Level.WARNING, "thrown by close()", e);
                    }
                }
            });
        }
        catch (RejectedExecutionException e) {
            if (ClosingFuture.logger.isLoggable(Level.WARNING)) {
                ClosingFuture.logger.log(Level.WARNING, String.format("while submitting close to %s; will close inline", executor), e);
            }
            closeQuietly(closeable, MoreExecutors.directExecutor());
        }
    }
    
    private void checkAndUpdateState(final State oldState, final State newState) {
        Preconditions.checkState(this.compareAndUpdateState(oldState, newState), "Expected state to be %s, but it was %s", oldState, newState);
    }
    
    private boolean compareAndUpdateState(final State oldState, final State newState) {
        return this.state.compareAndSet(oldState, newState);
    }
    
    @VisibleForTesting
    CountDownLatch whenClosedCountDown() {
        return this.closeables.whenClosedCountDown();
    }
    
    static {
        logger = Logger.getLogger(ClosingFuture.class.getName());
    }
    
    public static final class DeferredCloser
    {
        @RetainedWith
        private final CloseableList list;
        
        DeferredCloser(final CloseableList list) {
            this.list = list;
        }
        
        @ParametricNullness
        @CanIgnoreReturnValue
        public <C extends java.lang.Object> C eventuallyClose(@ParametricNullness final C closeable, final Executor closingExecutor) {
            Preconditions.checkNotNull(closingExecutor);
            if (closeable != null) {
                this.list.add((AutoCloseable)closeable, closingExecutor);
            }
            return closeable;
        }
    }
    
    public static final class ValueAndCloser<V>
    {
        private final ClosingFuture<? extends V> closingFuture;
        
        ValueAndCloser(final ClosingFuture<? extends V> closingFuture) {
            this.closingFuture = Preconditions.checkNotNull(closingFuture);
        }
        
        @ParametricNullness
        public V get() throws ExecutionException {
            return Futures.getDone((Future<V>)((ClosingFuture<Object>)this.closingFuture).future);
        }
        
        public void closeAsync() {
            ((ClosingFuture<Object>)this.closingFuture).close();
        }
    }
    
    public static final class Peeker
    {
        private final ImmutableList<ClosingFuture<?>> futures;
        private volatile boolean beingCalled;
        
        private Peeker(final ImmutableList<ClosingFuture<?>> futures) {
            this.futures = Preconditions.checkNotNull(futures);
        }
        
        @ParametricNullness
        public final <D> D getDone(final ClosingFuture<D> closingFuture) throws ExecutionException {
            Preconditions.checkState(this.beingCalled);
            Preconditions.checkArgument(this.futures.contains(closingFuture));
            return Futures.getDone((Future<D>)((ClosingFuture<Object>)closingFuture).future);
        }
        
        @ParametricNullness
        private <V> V call(final Combiner.CombiningCallable<V> combiner, final CloseableList closeables) throws Exception {
            this.beingCalled = true;
            final CloseableList newCloseables = new CloseableList();
            try {
                return combiner.call(newCloseables.closer, this);
            }
            finally {
                closeables.add(newCloseables, MoreExecutors.directExecutor());
                this.beingCalled = false;
            }
        }
        
        private <V> FluentFuture<V> callAsync(final Combiner.AsyncCombiningCallable<V> combiner, final CloseableList closeables) throws Exception {
            this.beingCalled = true;
            final CloseableList newCloseables = new CloseableList();
            try {
                final ClosingFuture<V> closingFuture = combiner.call(newCloseables.closer, this);
                ((ClosingFuture<Object>)closingFuture).becomeSubsumedInto(closeables);
                return (FluentFuture<V>)((ClosingFuture<Object>)closingFuture).future;
            }
            finally {
                closeables.add(newCloseables, MoreExecutors.directExecutor());
                this.beingCalled = false;
            }
        }
    }
    
    @DoNotMock("Use ClosingFuture.whenAllSucceed() or .whenAllComplete() instead.")
    public static class Combiner
    {
        private final CloseableList closeables;
        private final boolean allMustSucceed;
        protected final ImmutableList<ClosingFuture<?>> inputs;
        private static final Function<ClosingFuture<?>, FluentFuture<?>> INNER_FUTURE;
        
        private Combiner(final boolean allMustSucceed, final Iterable<? extends ClosingFuture<?>> inputs) {
            this.closeables = new CloseableList();
            this.allMustSucceed = allMustSucceed;
            this.inputs = ImmutableList.copyOf(inputs);
            for (final ClosingFuture<?> input : inputs) {
                ((ClosingFuture<Object>)input).becomeSubsumedInto(this.closeables);
            }
        }
        
        public <V> ClosingFuture<V> call(final CombiningCallable<V> combiningCallable, final Executor executor) {
            final Callable<V> callable = new Callable<V>() {
                @ParametricNullness
                @Override
                public V call() throws Exception {
                    return (V)new Peeker((ImmutableList)Combiner.this.inputs).call((CombiningCallable<Object>)combiningCallable, Combiner.this.closeables);
                }
                
                @Override
                public String toString() {
                    return combiningCallable.toString();
                }
            };
            final ClosingFuture<V> derived = new ClosingFuture<V>(this.futureCombiner().call(callable, executor), null);
            ((ClosingFuture<Object>)derived).closeables.add(this.closeables, MoreExecutors.directExecutor());
            return derived;
        }
        
        public <V> ClosingFuture<V> callAsync(final AsyncCombiningCallable<V> combiningCallable, final Executor executor) {
            final AsyncCallable<V> asyncCallable = new AsyncCallable<V>() {
                @Override
                public ListenableFuture<V> call() throws Exception {
                    return (ListenableFuture<V>)new Peeker((ImmutableList)Combiner.this.inputs).callAsync((AsyncCombiningCallable<Object>)combiningCallable, Combiner.this.closeables);
                }
                
                @Override
                public String toString() {
                    return combiningCallable.toString();
                }
            };
            final ClosingFuture<V> derived = new ClosingFuture<V>(this.futureCombiner().callAsync(asyncCallable, executor), null);
            ((ClosingFuture<Object>)derived).closeables.add(this.closeables, MoreExecutors.directExecutor());
            return derived;
        }
        
        private Futures.FutureCombiner<Object> futureCombiner() {
            return this.allMustSucceed ? Futures.whenAllSucceed((Iterable<? extends ListenableFuture<?>>)this.inputFutures()) : Futures.whenAllComplete((Iterable<? extends ListenableFuture<?>>)this.inputFutures());
        }
        
        private ImmutableList<FluentFuture<?>> inputFutures() {
            return FluentIterable.from(this.inputs).transform(Combiner.INNER_FUTURE).toList();
        }
        
        static {
            INNER_FUTURE = new Function<ClosingFuture<?>, FluentFuture<?>>() {
                @Override
                public FluentFuture<?> apply(final ClosingFuture<?> future) {
                    return ((ClosingFuture<Object>)future).future;
                }
            };
        }
        
        @FunctionalInterface
        public interface AsyncCombiningCallable<V>
        {
            ClosingFuture<V> call(final DeferredCloser p0, final Peeker p1) throws Exception;
        }
        
        @FunctionalInterface
        public interface CombiningCallable<V>
        {
            @ParametricNullness
            V call(final DeferredCloser p0, final Peeker p1) throws Exception;
        }
    }
    
    public static final class Combiner2<V1, V2> extends Combiner
    {
        private final ClosingFuture<V1> future1;
        private final ClosingFuture<V2> future2;
        
        private Combiner2(final ClosingFuture<V1> future1, final ClosingFuture<V2> future2) {
            super(true, (Iterable)ImmutableList.of(future1, (ClosingFuture<V1>)future2));
            this.future1 = future1;
            this.future2 = future2;
        }
        
        public <U> ClosingFuture<U> call(final ClosingFunction2<V1, V2, U> function, final Executor executor) {
            return this.call((CombiningCallable<U>)new CombiningCallable<U>() {
                @ParametricNullness
                @Override
                public U call(final DeferredCloser closer, final Peeker peeker) throws Exception {
                    return function.apply(closer, peeker.getDone((ClosingFuture<Object>)Combiner2.this.future1), peeker.getDone((ClosingFuture<Object>)Combiner2.this.future2));
                }
                
                @Override
                public String toString() {
                    return function.toString();
                }
            }, executor);
        }
        
        public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction2<V1, V2, U> function, final Executor executor) {
            return this.callAsync((AsyncCombiningCallable<U>)new AsyncCombiningCallable<U>() {
                @Override
                public ClosingFuture<U> call(final DeferredCloser closer, final Peeker peeker) throws Exception {
                    return function.apply(closer, peeker.getDone((ClosingFuture<Object>)Combiner2.this.future1), peeker.getDone((ClosingFuture<Object>)Combiner2.this.future2));
                }
                
                @Override
                public String toString() {
                    return function.toString();
                }
            }, executor);
        }
        
        @FunctionalInterface
        public interface AsyncClosingFunction2<V1, V2, U>
        {
            ClosingFuture<U> apply(final DeferredCloser p0, @ParametricNullness final V1 p1, @ParametricNullness final V2 p2) throws Exception;
        }
        
        @FunctionalInterface
        public interface ClosingFunction2<V1, V2, U>
        {
            @ParametricNullness
            U apply(final DeferredCloser p0, @ParametricNullness final V1 p1, @ParametricNullness final V2 p2) throws Exception;
        }
    }
    
    public static final class Combiner3<V1, V2, V3> extends Combiner
    {
        private final ClosingFuture<V1> future1;
        private final ClosingFuture<V2> future2;
        private final ClosingFuture<V3> future3;
        
        private Combiner3(final ClosingFuture<V1> future1, final ClosingFuture<V2> future2, final ClosingFuture<V3> future3) {
            super(true, (Iterable)ImmutableList.of(future1, (ClosingFuture<V1>)future2, (ClosingFuture<V1>)future3));
            this.future1 = future1;
            this.future2 = future2;
            this.future3 = future3;
        }
        
        public <U> ClosingFuture<U> call(final ClosingFunction3<V1, V2, V3, U> function, final Executor executor) {
            return this.call((CombiningCallable<U>)new CombiningCallable<U>() {
                @ParametricNullness
                @Override
                public U call(final DeferredCloser closer, final Peeker peeker) throws Exception {
                    return function.apply(closer, peeker.getDone((ClosingFuture<Object>)Combiner3.this.future1), peeker.getDone((ClosingFuture<Object>)Combiner3.this.future2), peeker.getDone((ClosingFuture<Object>)Combiner3.this.future3));
                }
                
                @Override
                public String toString() {
                    return function.toString();
                }
            }, executor);
        }
        
        public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction3<V1, V2, V3, U> function, final Executor executor) {
            return this.callAsync((AsyncCombiningCallable<U>)new AsyncCombiningCallable<U>() {
                @Override
                public ClosingFuture<U> call(final DeferredCloser closer, final Peeker peeker) throws Exception {
                    return function.apply(closer, peeker.getDone((ClosingFuture<Object>)Combiner3.this.future1), peeker.getDone((ClosingFuture<Object>)Combiner3.this.future2), peeker.getDone((ClosingFuture<Object>)Combiner3.this.future3));
                }
                
                @Override
                public String toString() {
                    return function.toString();
                }
            }, executor);
        }
        
        @FunctionalInterface
        public interface AsyncClosingFunction3<V1, V2, V3, U>
        {
            ClosingFuture<U> apply(final DeferredCloser p0, @ParametricNullness final V1 p1, @ParametricNullness final V2 p2, @ParametricNullness final V3 p3) throws Exception;
        }
        
        @FunctionalInterface
        public interface ClosingFunction3<V1, V2, V3, U>
        {
            @ParametricNullness
            U apply(final DeferredCloser p0, @ParametricNullness final V1 p1, @ParametricNullness final V2 p2, @ParametricNullness final V3 p3) throws Exception;
        }
    }
    
    public static final class Combiner4<V1, V2, V3, V4> extends Combiner
    {
        private final ClosingFuture<V1> future1;
        private final ClosingFuture<V2> future2;
        private final ClosingFuture<V3> future3;
        private final ClosingFuture<V4> future4;
        
        private Combiner4(final ClosingFuture<V1> future1, final ClosingFuture<V2> future2, final ClosingFuture<V3> future3, final ClosingFuture<V4> future4) {
            super(true, (Iterable)ImmutableList.of(future1, (ClosingFuture<V1>)future2, (ClosingFuture<V1>)future3, (ClosingFuture<V1>)future4));
            this.future1 = future1;
            this.future2 = future2;
            this.future3 = future3;
            this.future4 = future4;
        }
        
        public <U> ClosingFuture<U> call(final ClosingFunction4<V1, V2, V3, V4, U> function, final Executor executor) {
            return this.call((CombiningCallable<U>)new CombiningCallable<U>() {
                @ParametricNullness
                @Override
                public U call(final DeferredCloser closer, final Peeker peeker) throws Exception {
                    return function.apply(closer, peeker.getDone((ClosingFuture<Object>)Combiner4.this.future1), peeker.getDone((ClosingFuture<Object>)Combiner4.this.future2), peeker.getDone((ClosingFuture<Object>)Combiner4.this.future3), peeker.getDone((ClosingFuture<Object>)Combiner4.this.future4));
                }
                
                @Override
                public String toString() {
                    return function.toString();
                }
            }, executor);
        }
        
        public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction4<V1, V2, V3, V4, U> function, final Executor executor) {
            return this.callAsync((AsyncCombiningCallable<U>)new AsyncCombiningCallable<U>() {
                @Override
                public ClosingFuture<U> call(final DeferredCloser closer, final Peeker peeker) throws Exception {
                    return function.apply(closer, peeker.getDone((ClosingFuture<Object>)Combiner4.this.future1), peeker.getDone((ClosingFuture<Object>)Combiner4.this.future2), peeker.getDone((ClosingFuture<Object>)Combiner4.this.future3), peeker.getDone((ClosingFuture<Object>)Combiner4.this.future4));
                }
                
                @Override
                public String toString() {
                    return function.toString();
                }
            }, executor);
        }
        
        @FunctionalInterface
        public interface AsyncClosingFunction4<V1, V2, V3, V4, U>
        {
            ClosingFuture<U> apply(final DeferredCloser p0, @ParametricNullness final V1 p1, @ParametricNullness final V2 p2, @ParametricNullness final V3 p3, @ParametricNullness final V4 p4) throws Exception;
        }
        
        @FunctionalInterface
        public interface ClosingFunction4<V1, V2, V3, V4, U>
        {
            @ParametricNullness
            U apply(final DeferredCloser p0, @ParametricNullness final V1 p1, @ParametricNullness final V2 p2, @ParametricNullness final V3 p3, @ParametricNullness final V4 p4) throws Exception;
        }
    }
    
    public static final class Combiner5<V1, V2, V3, V4, V5> extends Combiner
    {
        private final ClosingFuture<V1> future1;
        private final ClosingFuture<V2> future2;
        private final ClosingFuture<V3> future3;
        private final ClosingFuture<V4> future4;
        private final ClosingFuture<V5> future5;
        
        private Combiner5(final ClosingFuture<V1> future1, final ClosingFuture<V2> future2, final ClosingFuture<V3> future3, final ClosingFuture<V4> future4, final ClosingFuture<V5> future5) {
            super(true, (Iterable)ImmutableList.of(future1, (ClosingFuture<V1>)future2, (ClosingFuture<V1>)future3, (ClosingFuture<V1>)future4, (ClosingFuture<V1>)future5));
            this.future1 = future1;
            this.future2 = future2;
            this.future3 = future3;
            this.future4 = future4;
            this.future5 = future5;
        }
        
        public <U> ClosingFuture<U> call(final ClosingFunction5<V1, V2, V3, V4, V5, U> function, final Executor executor) {
            return this.call((CombiningCallable<U>)new CombiningCallable<U>() {
                @ParametricNullness
                @Override
                public U call(final DeferredCloser closer, final Peeker peeker) throws Exception {
                    return function.apply(closer, peeker.getDone((ClosingFuture<Object>)Combiner5.this.future1), peeker.getDone((ClosingFuture<Object>)Combiner5.this.future2), peeker.getDone((ClosingFuture<Object>)Combiner5.this.future3), peeker.getDone((ClosingFuture<Object>)Combiner5.this.future4), peeker.getDone((ClosingFuture<Object>)Combiner5.this.future5));
                }
                
                @Override
                public String toString() {
                    return function.toString();
                }
            }, executor);
        }
        
        public <U> ClosingFuture<U> callAsync(final AsyncClosingFunction5<V1, V2, V3, V4, V5, U> function, final Executor executor) {
            return this.callAsync((AsyncCombiningCallable<U>)new AsyncCombiningCallable<U>() {
                @Override
                public ClosingFuture<U> call(final DeferredCloser closer, final Peeker peeker) throws Exception {
                    return function.apply(closer, peeker.getDone((ClosingFuture<Object>)Combiner5.this.future1), peeker.getDone((ClosingFuture<Object>)Combiner5.this.future2), peeker.getDone((ClosingFuture<Object>)Combiner5.this.future3), peeker.getDone((ClosingFuture<Object>)Combiner5.this.future4), peeker.getDone((ClosingFuture<Object>)Combiner5.this.future5));
                }
                
                @Override
                public String toString() {
                    return function.toString();
                }
            }, executor);
        }
        
        @FunctionalInterface
        public interface AsyncClosingFunction5<V1, V2, V3, V4, V5, U>
        {
            ClosingFuture<U> apply(final DeferredCloser p0, @ParametricNullness final V1 p1, @ParametricNullness final V2 p2, @ParametricNullness final V3 p3, @ParametricNullness final V4 p4, @ParametricNullness final V5 p5) throws Exception;
        }
        
        @FunctionalInterface
        public interface ClosingFunction5<V1, V2, V3, V4, V5, U>
        {
            @ParametricNullness
            U apply(final DeferredCloser p0, @ParametricNullness final V1 p1, @ParametricNullness final V2 p2, @ParametricNullness final V3 p3, @ParametricNullness final V4 p4, @ParametricNullness final V5 p5) throws Exception;
        }
    }
    
    private static final class CloseableList extends IdentityHashMap<AutoCloseable, Executor> implements Closeable
    {
        private final DeferredCloser closer;
        private volatile boolean closed;
        @CheckForNull
        private volatile CountDownLatch whenClosed;
        
        private CloseableList() {
            this.closer = new DeferredCloser(this);
        }
        
         <V, U> ListenableFuture<U> applyClosingFunction(final ClosingFunction<? super V, U> transformation, @ParametricNullness final V input) throws Exception {
            final CloseableList newCloseables = new CloseableList();
            try {
                return Futures.immediateFuture(transformation.apply(newCloseables.closer, (Object)input));
            }
            finally {
                this.add(newCloseables, MoreExecutors.directExecutor());
            }
        }
        
         <V, U> FluentFuture<U> applyAsyncClosingFunction(final AsyncClosingFunction<V, U> transformation, @ParametricNullness final V input) throws Exception {
            final CloseableList newCloseables = new CloseableList();
            try {
                final ClosingFuture<U> closingFuture = transformation.apply(newCloseables.closer, input);
                ((ClosingFuture<Object>)closingFuture).becomeSubsumedInto(newCloseables);
                return (FluentFuture<U>)((ClosingFuture<Object>)closingFuture).future;
            }
            finally {
                this.add(newCloseables, MoreExecutors.directExecutor());
            }
        }
        
        @Override
        public void close() {
            if (this.closed) {
                return;
            }
            synchronized (this) {
                if (this.closed) {
                    return;
                }
                this.closed = true;
            }
            for (final Map.Entry<AutoCloseable, Executor> entry : this.entrySet()) {
                closeQuietly(entry.getKey(), entry.getValue());
            }
            this.clear();
            if (this.whenClosed != null) {
                this.whenClosed.countDown();
            }
        }
        
        void add(@CheckForNull final AutoCloseable closeable, final Executor executor) {
            Preconditions.checkNotNull(executor);
            if (closeable == null) {
                return;
            }
            synchronized (this) {
                if (!this.closed) {
                    this.put(closeable, executor);
                    return;
                }
            }
            closeQuietly(closeable, executor);
        }
        
        CountDownLatch whenClosedCountDown() {
            if (this.closed) {
                return new CountDownLatch(0);
            }
            synchronized (this) {
                if (this.closed) {
                    return new CountDownLatch(0);
                }
                Preconditions.checkState(this.whenClosed == null);
                return this.whenClosed = new CountDownLatch(1);
            }
        }
    }
    
    enum State
    {
        OPEN, 
        SUBSUMED, 
        WILL_CLOSE, 
        CLOSING, 
        CLOSED, 
        WILL_CREATE_VALUE_AND_CLOSER;
        
        private static /* synthetic */ State[] $values() {
            return new State[] { State.OPEN, State.SUBSUMED, State.WILL_CLOSE, State.CLOSING, State.CLOSED, State.WILL_CREATE_VALUE_AND_CLOSER };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    @FunctionalInterface
    public interface ClosingFunction<T, U>
    {
        @ParametricNullness
        U apply(final DeferredCloser p0, @ParametricNullness final T p1) throws Exception;
    }
    
    @FunctionalInterface
    public interface AsyncClosingFunction<T, U>
    {
        ClosingFuture<U> apply(final DeferredCloser p0, @ParametricNullness final T p1) throws Exception;
    }
    
    @FunctionalInterface
    public interface ValueAndCloserConsumer<V>
    {
        void accept(final ValueAndCloser<V> p0);
    }
    
    @FunctionalInterface
    public interface AsyncClosingCallable<V>
    {
        ClosingFuture<V> call(final DeferredCloser p0) throws Exception;
    }
    
    @FunctionalInterface
    public interface ClosingCallable<V>
    {
        @ParametricNullness
        V call(final DeferredCloser p0) throws Exception;
    }
}
