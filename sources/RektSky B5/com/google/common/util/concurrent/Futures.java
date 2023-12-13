/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AbstractCheckedFuture;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.AsyncSettableFuture;
import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.FutureFallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SerializingExecutor;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.common.util.concurrent.Uninterruptibles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

@Beta
public final class Futures {
    private static final AsyncFunction<ListenableFuture<Object>, Object> DEREFERENCER = new AsyncFunction<ListenableFuture<Object>, Object>(){

        @Override
        public ListenableFuture<Object> apply(ListenableFuture<Object> input) {
            return input;
        }
    };
    private static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST = Ordering.natural().onResultOf(new Function<Constructor<?>, Boolean>(){

        @Override
        public Boolean apply(Constructor<?> input) {
            return Arrays.asList(input.getParameterTypes()).contains(String.class);
        }
    }).reverse();

    private Futures() {
    }

    public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(ListenableFuture<V> future, Function<Exception, X> mapper) {
        return new MappingCheckedFuture<V, X>(Preconditions.checkNotNull(future), mapper);
    }

    public static <V> ListenableFuture<V> immediateFuture(@Nullable V value) {
        return new ImmediateSuccessfulFuture<V>(value);
    }

    public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(@Nullable V value) {
        return new ImmediateSuccessfulCheckedFuture(value);
    }

    public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable) {
        Preconditions.checkNotNull(throwable);
        return new ImmediateFailedFuture(throwable);
    }

    public static <V> ListenableFuture<V> immediateCancelledFuture() {
        return new ImmediateCancelledFuture();
    }

    public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(X exception) {
        Preconditions.checkNotNull(exception);
        return new ImmediateFailedCheckedFuture(exception);
    }

    public static <V> ListenableFuture<V> withFallback(ListenableFuture<? extends V> input, FutureFallback<? extends V> fallback) {
        return Futures.withFallback(input, fallback, MoreExecutors.sameThreadExecutor());
    }

    public static <V> ListenableFuture<V> withFallback(ListenableFuture<? extends V> input, FutureFallback<? extends V> fallback, Executor executor) {
        Preconditions.checkNotNull(fallback);
        return new FallbackFuture<V>(input, fallback, executor);
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function) {
        return Futures.transform(input, function, (Executor)MoreExecutors.sameThreadExecutor());
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
        ChainingListenableFuture output = new ChainingListenableFuture(function, input);
        input.addListener(output, executor);
        return output;
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function) {
        return Futures.transform(input, function, (Executor)MoreExecutors.sameThreadExecutor());
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, final Function<? super I, ? extends O> function, Executor executor) {
        Preconditions.checkNotNull(function);
        AsyncFunction wrapperFunction = new AsyncFunction<I, O>(){

            @Override
            public ListenableFuture<O> apply(I input) {
                Object output = function.apply(input);
                return Futures.immediateFuture(output);
            }
        };
        return Futures.transform(input, wrapperFunction, executor);
    }

    public static <I, O> Future<O> lazyTransform(final Future<I> input, final Function<? super I, ? extends O> function) {
        Preconditions.checkNotNull(input);
        Preconditions.checkNotNull(function);
        return new Future<O>(){

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
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
            public O get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return this.applyTransformation(input.get(timeout, unit));
            }

            private O applyTransformation(I input2) throws ExecutionException {
                try {
                    return function.apply(input2);
                }
                catch (Throwable t2) {
                    throw new ExecutionException(t2);
                }
            }
        };
    }

    public static <V> ListenableFuture<V> dereference(ListenableFuture<? extends ListenableFuture<? extends V>> nested) {
        return Futures.transform(nested, DEREFERENCER);
    }

    @Beta
    public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V> ... futures) {
        return Futures.listFuture(ImmutableList.copyOf(futures), true, MoreExecutors.sameThreadExecutor());
    }

    @Beta
    public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
        return Futures.listFuture(ImmutableList.copyOf(futures), true, MoreExecutors.sameThreadExecutor());
    }

    public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> future) {
        return new NonCancellationPropagatingFuture<V>(future);
    }

    @Beta
    public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V> ... futures) {
        return Futures.listFuture(ImmutableList.copyOf(futures), false, MoreExecutors.sameThreadExecutor());
    }

    @Beta
    public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
        return Futures.listFuture(ImmutableList.copyOf(futures), false, MoreExecutors.sameThreadExecutor());
    }

    @Beta
    public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(Iterable<? extends ListenableFuture<? extends T>> futures) {
        final ConcurrentLinkedQueue delegates = Queues.newConcurrentLinkedQueue();
        ImmutableList.Builder listBuilder = ImmutableList.builder();
        SerializingExecutor executor = new SerializingExecutor(MoreExecutors.sameThreadExecutor());
        for (final ListenableFuture<T> future : futures) {
            AsyncSettableFuture delegate = AsyncSettableFuture.create();
            delegates.add(delegate);
            future.addListener(new Runnable(){

                @Override
                public void run() {
                    ((AsyncSettableFuture)delegates.remove()).setFuture(future);
                }
            }, executor);
            listBuilder.add(delegate);
        }
        return listBuilder.build();
    }

    public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback) {
        Futures.addCallback(future, callback, MoreExecutors.sameThreadExecutor());
    }

    public static <V> void addCallback(final ListenableFuture<V> future, final FutureCallback<? super V> callback, Executor executor) {
        Preconditions.checkNotNull(callback);
        Runnable callbackListener = new Runnable(){

            @Override
            public void run() {
                Object value;
                try {
                    value = Uninterruptibles.getUninterruptibly(future);
                }
                catch (ExecutionException e2) {
                    callback.onFailure(e2.getCause());
                    return;
                }
                catch (RuntimeException e3) {
                    callback.onFailure(e3);
                    return;
                }
                catch (Error e4) {
                    callback.onFailure(e4);
                    return;
                }
                callback.onSuccess(value);
            }
        };
        future.addListener(callbackListener, executor);
    }

    public static <V, X extends Exception> V get(Future<V> future, Class<X> exceptionClass) throws X {
        Preconditions.checkNotNull(future);
        Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(exceptionClass), "Futures.get exception type (%s) must not be a RuntimeException", exceptionClass);
        try {
            return future.get();
        }
        catch (InterruptedException e2) {
            Thread.currentThread().interrupt();
            throw Futures.newWithCause(exceptionClass, e2);
        }
        catch (ExecutionException e3) {
            Futures.wrapAndThrowExceptionOrError(e3.getCause(), exceptionClass);
            throw new AssertionError();
        }
    }

    public static <V, X extends Exception> V get(Future<V> future, long timeout, TimeUnit unit, Class<X> exceptionClass) throws X {
        Preconditions.checkNotNull(future);
        Preconditions.checkNotNull(unit);
        Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(exceptionClass), "Futures.get exception type (%s) must not be a RuntimeException", exceptionClass);
        try {
            return future.get(timeout, unit);
        }
        catch (InterruptedException e2) {
            Thread.currentThread().interrupt();
            throw Futures.newWithCause(exceptionClass, e2);
        }
        catch (TimeoutException e3) {
            throw Futures.newWithCause(exceptionClass, e3);
        }
        catch (ExecutionException e4) {
            Futures.wrapAndThrowExceptionOrError(e4.getCause(), exceptionClass);
            throw new AssertionError();
        }
    }

    private static <X extends Exception> void wrapAndThrowExceptionOrError(Throwable cause, Class<X> exceptionClass) throws X {
        if (cause instanceof Error) {
            throw new ExecutionError((Error)cause);
        }
        if (cause instanceof RuntimeException) {
            throw new UncheckedExecutionException(cause);
        }
        throw Futures.newWithCause(exceptionClass, cause);
    }

    public static <V> V getUnchecked(Future<V> future) {
        Preconditions.checkNotNull(future);
        try {
            return Uninterruptibles.getUninterruptibly(future);
        }
        catch (ExecutionException e2) {
            Futures.wrapAndThrowUnchecked(e2.getCause());
            throw new AssertionError();
        }
    }

    private static void wrapAndThrowUnchecked(Throwable cause) {
        if (cause instanceof Error) {
            throw new ExecutionError((Error)cause);
        }
        throw new UncheckedExecutionException(cause);
    }

    private static <X extends Exception> X newWithCause(Class<X> exceptionClass, Throwable cause) {
        List<Constructor<X>> constructors = Arrays.asList(exceptionClass.getConstructors());
        for (Constructor<X> constructor : Futures.preferringStrings(constructors)) {
            Exception instance = (Exception)Futures.newFromConstructor(constructor, cause);
            if (instance == null) continue;
            if (instance.getCause() == null) {
                instance.initCause(cause);
            }
            return (X)instance;
        }
        throw new IllegalArgumentException("No appropriate constructor for exception of type " + exceptionClass + " in response to chained exception", cause);
    }

    private static <X extends Exception> List<Constructor<X>> preferringStrings(List<Constructor<X>> constructors) {
        return WITH_STRING_PARAM_FIRST.sortedCopy(constructors);
    }

    @Nullable
    private static <X> X newFromConstructor(Constructor<X> constructor, Throwable cause) {
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] params = new Object[paramTypes.length];
        for (int i2 = 0; i2 < paramTypes.length; ++i2) {
            Class<?> paramType = paramTypes[i2];
            if (paramType.equals(String.class)) {
                params[i2] = cause.toString();
                continue;
            }
            if (paramType.equals(Throwable.class)) {
                params[i2] = cause;
                continue;
            }
            return null;
        }
        try {
            return constructor.newInstance(params);
        }
        catch (IllegalArgumentException e2) {
            return null;
        }
        catch (InstantiationException e3) {
            return null;
        }
        catch (IllegalAccessException e4) {
            return null;
        }
        catch (InvocationTargetException e5) {
            return null;
        }
    }

    private static <V> ListenableFuture<List<V>> listFuture(ImmutableList<ListenableFuture<? extends V>> futures, boolean allMustSucceed, Executor listenerExecutor) {
        return new CombinedFuture(futures, allMustSucceed, listenerExecutor, new FutureCombiner<V, List<V>>(){

            @Override
            public List<V> combine(List<Optional<V>> values) {
                ArrayList<Object> result = Lists.newArrayList();
                for (Optional element : values) {
                    result.add(element != null ? (Object)element.orNull() : null);
                }
                return Collections.unmodifiableList(result);
            }
        });
    }

    private static class MappingCheckedFuture<V, X extends Exception>
    extends AbstractCheckedFuture<V, X> {
        final Function<Exception, X> mapper;

        MappingCheckedFuture(ListenableFuture<V> delegate, Function<Exception, X> mapper) {
            super(delegate);
            this.mapper = Preconditions.checkNotNull(mapper);
        }

        @Override
        protected X mapException(Exception e2) {
            return (X)((Exception)this.mapper.apply(e2));
        }
    }

    private static class CombinedFuture<V, C>
    extends AbstractFuture<C> {
        private static final Logger logger = Logger.getLogger(CombinedFuture.class.getName());
        ImmutableCollection<? extends ListenableFuture<? extends V>> futures;
        final boolean allMustSucceed;
        final AtomicInteger remaining;
        FutureCombiner<V, C> combiner;
        List<Optional<V>> values;
        final Object seenExceptionsLock = new Object();
        Set<Throwable> seenExceptions;

        CombinedFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed, Executor listenerExecutor, FutureCombiner<V, C> combiner) {
            this.futures = futures;
            this.allMustSucceed = allMustSucceed;
            this.remaining = new AtomicInteger(futures.size());
            this.combiner = combiner;
            this.values = Lists.newArrayListWithCapacity(futures.size());
            this.init(listenerExecutor);
        }

        protected void init(Executor listenerExecutor) {
            this.addListener(new Runnable(){

                @Override
                public void run() {
                    if (CombinedFuture.this.isCancelled()) {
                        for (ListenableFuture listenableFuture : CombinedFuture.this.futures) {
                            listenableFuture.cancel(CombinedFuture.this.wasInterrupted());
                        }
                    }
                    CombinedFuture.this.futures = null;
                    CombinedFuture.this.values = null;
                    CombinedFuture.this.combiner = null;
                }
            }, MoreExecutors.sameThreadExecutor());
            if (this.futures.isEmpty()) {
                this.set(this.combiner.combine(ImmutableList.of()));
                return;
            }
            for (int i2 = 0; i2 < this.futures.size(); ++i2) {
                this.values.add(null);
            }
            int i2 = 0;
            for (final ListenableFuture listenableFuture : this.futures) {
                final int index = i2++;
                listenableFuture.addListener(new Runnable(){

                    @Override
                    public void run() {
                        CombinedFuture.this.setOneValue(index, listenableFuture);
                    }
                }, listenerExecutor);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void setExceptionAndMaybeLog(Throwable throwable) {
            boolean visibleFromOutputFuture = false;
            boolean firstTimeSeeingThisException = true;
            if (this.allMustSucceed) {
                visibleFromOutputFuture = super.setException(throwable);
                Object object = this.seenExceptionsLock;
                synchronized (object) {
                    if (this.seenExceptions == null) {
                        this.seenExceptions = Sets.newHashSet();
                    }
                    firstTimeSeeingThisException = this.seenExceptions.add(throwable);
                }
            }
            if (throwable instanceof Error || this.allMustSucceed && !visibleFromOutputFuture && firstTimeSeeingThisException) {
                logger.log(Level.SEVERE, "input future failed.", throwable);
            }
        }

        /*
         * Exception decompiling
         */
        private void setOneValue(int index, Future<? extends V> future) {
            /*
             * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
             * 
             * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
             *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
             *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:923)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
             *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
             *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
             *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
             *     at org.benf.cfr.reader.Main.main(Main.java:54)
             */
            throw new IllegalStateException("Decompilation failed");
        }
    }

    private static interface FutureCombiner<V, C> {
        public C combine(List<Optional<V>> var1);
    }

    private static class NonCancellationPropagatingFuture<V>
    extends AbstractFuture<V> {
        NonCancellationPropagatingFuture(final ListenableFuture<V> delegate) {
            Preconditions.checkNotNull(delegate);
            Futures.addCallback(delegate, new FutureCallback<V>(){

                @Override
                public void onSuccess(V result) {
                    NonCancellationPropagatingFuture.this.set(result);
                }

                @Override
                public void onFailure(Throwable t2) {
                    if (delegate.isCancelled()) {
                        NonCancellationPropagatingFuture.this.cancel(false);
                    } else {
                        NonCancellationPropagatingFuture.this.setException(t2);
                    }
                }
            }, MoreExecutors.sameThreadExecutor());
        }
    }

    private static class ChainingListenableFuture<I, O>
    extends AbstractFuture<O>
    implements Runnable {
        private AsyncFunction<? super I, ? extends O> function;
        private ListenableFuture<? extends I> inputFuture;
        private volatile ListenableFuture<? extends O> outputFuture;
        private final CountDownLatch outputCreated = new CountDownLatch(1);

        private ChainingListenableFuture(AsyncFunction<? super I, ? extends O> function, ListenableFuture<? extends I> inputFuture) {
            this.function = Preconditions.checkNotNull(function);
            this.inputFuture = Preconditions.checkNotNull(inputFuture);
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            if (super.cancel(mayInterruptIfRunning)) {
                this.cancel(this.inputFuture, mayInterruptIfRunning);
                this.cancel(this.outputFuture, mayInterruptIfRunning);
                return true;
            }
            return false;
        }

        private void cancel(@Nullable Future<?> future, boolean mayInterruptIfRunning) {
            if (future != null) {
                future.cancel(mayInterruptIfRunning);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         * Loose catch block
         */
        @Override
        public void run() {
            block14: {
                try {
                    I sourceResult;
                    try {
                        sourceResult = Uninterruptibles.getUninterruptibly(this.inputFuture);
                    }
                    catch (CancellationException e2) {
                        this.cancel(false);
                        this.function = null;
                        this.inputFuture = null;
                        this.outputCreated.countDown();
                        return;
                    }
                    catch (ExecutionException e3) {
                        this.setException(e3.getCause());
                        this.function = null;
                        this.inputFuture = null;
                        this.outputCreated.countDown();
                        return;
                    }
                    this.outputFuture = Preconditions.checkNotNull(this.function.apply(sourceResult), "AsyncFunction may not return null.");
                    final ListenableFuture<O> outputFuture = this.outputFuture;
                    if (this.isCancelled()) {
                        outputFuture.cancel(this.wasInterrupted());
                        this.outputFuture = null;
                        return;
                    }
                    outputFuture.addListener(new Runnable(){

                        /*
                         * WARNING - Removed try catching itself - possible behaviour change.
                         */
                        @Override
                        public void run() {
                            try {
                                ChainingListenableFuture.this.set(Uninterruptibles.getUninterruptibly(outputFuture));
                            }
                            catch (CancellationException e2) {
                                ChainingListenableFuture.this.cancel(false);
                                return;
                            }
                            catch (ExecutionException e3) {
                                ChainingListenableFuture.this.setException(e3.getCause());
                            }
                            finally {
                                ChainingListenableFuture.this.outputFuture = null;
                            }
                        }
                    }, MoreExecutors.sameThreadExecutor());
                    break block14;
                    {
                        catch (UndeclaredThrowableException e4) {
                            this.setException(e4.getCause());
                            break block14;
                        }
                        catch (Throwable t2) {
                            this.setException(t2);
                            break block14;
                        }
                        catch (Throwable throwable) {
                            throw throwable;
                        }
                    }
                }
                finally {
                    this.function = null;
                    this.inputFuture = null;
                    this.outputCreated.countDown();
                }
            }
        }
    }

    private static class FallbackFuture<V>
    extends AbstractFuture<V> {
        private volatile ListenableFuture<? extends V> running;

        FallbackFuture(ListenableFuture<? extends V> input, final FutureFallback<? extends V> fallback, Executor executor) {
            this.running = input;
            Futures.addCallback(this.running, new FutureCallback<V>(){

                @Override
                public void onSuccess(V value) {
                    FallbackFuture.this.set(value);
                }

                @Override
                public void onFailure(Throwable t2) {
                    if (FallbackFuture.this.isCancelled()) {
                        return;
                    }
                    try {
                        FallbackFuture.this.running = fallback.create(t2);
                        if (FallbackFuture.this.isCancelled()) {
                            FallbackFuture.this.running.cancel(FallbackFuture.this.wasInterrupted());
                            return;
                        }
                        Futures.addCallback(FallbackFuture.this.running, new FutureCallback<V>(){

                            @Override
                            public void onSuccess(V value) {
                                FallbackFuture.this.set(value);
                            }

                            @Override
                            public void onFailure(Throwable t2) {
                                if (FallbackFuture.this.running.isCancelled()) {
                                    FallbackFuture.this.cancel(false);
                                } else {
                                    FallbackFuture.this.setException(t2);
                                }
                            }
                        }, MoreExecutors.sameThreadExecutor());
                    }
                    catch (Throwable e2) {
                        FallbackFuture.this.setException(e2);
                    }
                }
            }, executor);
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            if (super.cancel(mayInterruptIfRunning)) {
                this.running.cancel(mayInterruptIfRunning);
                return true;
            }
            return false;
        }
    }

    private static class ImmediateFailedCheckedFuture<V, X extends Exception>
    extends ImmediateFuture<V>
    implements CheckedFuture<V, X> {
        private final X thrown;

        ImmediateFailedCheckedFuture(X thrown) {
            this.thrown = thrown;
        }

        @Override
        public V get() throws ExecutionException {
            throw new ExecutionException((Throwable)this.thrown);
        }

        @Override
        public V checkedGet() throws X {
            throw this.thrown;
        }

        @Override
        public V checkedGet(long timeout, TimeUnit unit) throws X {
            Preconditions.checkNotNull(unit);
            throw this.thrown;
        }
    }

    private static class ImmediateCancelledFuture<V>
    extends ImmediateFuture<V> {
        private final CancellationException thrown = new CancellationException("Immediate cancelled future.");

        ImmediateCancelledFuture() {
        }

        @Override
        public boolean isCancelled() {
            return true;
        }

        @Override
        public V get() {
            throw AbstractFuture.cancellationExceptionWithCause("Task was cancelled.", this.thrown);
        }
    }

    private static class ImmediateFailedFuture<V>
    extends ImmediateFuture<V> {
        private final Throwable thrown;

        ImmediateFailedFuture(Throwable thrown) {
            this.thrown = thrown;
        }

        @Override
        public V get() throws ExecutionException {
            throw new ExecutionException(this.thrown);
        }
    }

    private static class ImmediateSuccessfulCheckedFuture<V, X extends Exception>
    extends ImmediateFuture<V>
    implements CheckedFuture<V, X> {
        @Nullable
        private final V value;

        ImmediateSuccessfulCheckedFuture(@Nullable V value) {
            this.value = value;
        }

        @Override
        public V get() {
            return this.value;
        }

        @Override
        public V checkedGet() {
            return this.value;
        }

        @Override
        public V checkedGet(long timeout, TimeUnit unit) {
            Preconditions.checkNotNull(unit);
            return this.value;
        }
    }

    private static class ImmediateSuccessfulFuture<V>
    extends ImmediateFuture<V> {
        @Nullable
        private final V value;

        ImmediateSuccessfulFuture(@Nullable V value) {
            this.value = value;
        }

        @Override
        public V get() {
            return this.value;
        }
    }

    private static abstract class ImmediateFuture<V>
    implements ListenableFuture<V> {
        private static final Logger log = Logger.getLogger(ImmediateFuture.class.getName());

        private ImmediateFuture() {
        }

        @Override
        public void addListener(Runnable listener, Executor executor) {
            Preconditions.checkNotNull(listener, "Runnable was null.");
            Preconditions.checkNotNull(executor, "Executor was null.");
            try {
                executor.execute(listener);
            }
            catch (RuntimeException e2) {
                log.log(Level.SEVERE, "RuntimeException while executing runnable " + listener + " with executor " + executor, e2);
            }
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public abstract V get() throws ExecutionException;

        @Override
        public V get(long timeout, TimeUnit unit) throws ExecutionException {
            Preconditions.checkNotNull(unit);
            return this.get();
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return true;
        }
    }
}

