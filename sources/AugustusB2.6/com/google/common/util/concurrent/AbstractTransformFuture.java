// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.errorprone.annotations.ForOverride;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class AbstractTransformFuture<I, O, F, T> extends TrustedFuture<O> implements Runnable
{
    @CheckForNull
    ListenableFuture<? extends I> inputFuture;
    @CheckForNull
    F function;
    
    static <I, O> ListenableFuture<O> create(final ListenableFuture<I> input, final AsyncFunction<? super I, ? extends O> function, final Executor executor) {
        Preconditions.checkNotNull(executor);
        final AsyncTransformFuture<I, O> output = new AsyncTransformFuture<I, O>((ListenableFuture<? extends I>)input, function);
        input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
        return (ListenableFuture<O>)output;
    }
    
    static <I, O> ListenableFuture<O> create(final ListenableFuture<I> input, final Function<? super I, ? extends O> function, final Executor executor) {
        Preconditions.checkNotNull(function);
        final TransformFuture<I, O> output = new TransformFuture<I, O>((ListenableFuture<? extends I>)input, function);
        input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
        return (ListenableFuture<O>)output;
    }
    
    AbstractTransformFuture(final ListenableFuture<? extends I> inputFuture, final F function) {
        this.inputFuture = Preconditions.checkNotNull(inputFuture);
        this.function = Preconditions.checkNotNull(function);
    }
    
    @Override
    public final void run() {
        final ListenableFuture<? extends I> localInputFuture = this.inputFuture;
        final F localFunction = this.function;
        if (this.isCancelled() | localInputFuture == null | localFunction == null) {
            return;
        }
        this.inputFuture = null;
        if (localInputFuture.isCancelled()) {
            final boolean unused = this.setFuture((ListenableFuture<? extends O>)localInputFuture);
            return;
        }
        I sourceResult;
        try {
            sourceResult = Futures.getDone((Future<I>)localInputFuture);
        }
        catch (CancellationException e4) {
            this.cancel(false);
            return;
        }
        catch (ExecutionException e) {
            this.setException(e.getCause());
            return;
        }
        catch (RuntimeException e2) {
            this.setException(e2);
            return;
        }
        catch (Error e3) {
            this.setException(e3);
            return;
        }
        T transformResult;
        try {
            transformResult = this.doTransform(localFunction, sourceResult);
        }
        catch (Throwable t) {
            this.setException(t);
            return;
        }
        finally {
            this.function = null;
        }
        this.setResult(transformResult);
    }
    
    @ParametricNullness
    @ForOverride
    abstract T doTransform(final F p0, @ParametricNullness final I p1) throws Exception;
    
    @ForOverride
    abstract void setResult(@ParametricNullness final T p0);
    
    @Override
    protected final void afterDone() {
        this.maybePropagateCancellationTo(this.inputFuture);
        this.inputFuture = null;
        this.function = null;
    }
    
    @CheckForNull
    @Override
    protected String pendingToString() {
        final ListenableFuture<? extends I> localInputFuture = this.inputFuture;
        final F localFunction = this.function;
        final String superString = super.pendingToString();
        String resultString = "";
        if (localInputFuture != null) {
            final String value = String.valueOf(localInputFuture);
            resultString = new StringBuilder(16 + String.valueOf(value).length()).append("inputFuture=[").append(value).append("], ").toString();
        }
        if (localFunction != null) {
            final String s = resultString;
            final String value2 = String.valueOf(localFunction);
            return new StringBuilder(11 + String.valueOf(s).length() + String.valueOf(value2).length()).append(s).append("function=[").append(value2).append("]").toString();
        }
        if (superString != null) {
            final String value3 = String.valueOf(resultString);
            final String value4 = String.valueOf(superString);
            return (value4.length() != 0) ? value3.concat(value4) : new String(value3);
        }
        return null;
    }
    
    private static final class AsyncTransformFuture<I, O> extends AbstractTransformFuture<I, O, AsyncFunction<? super I, ? extends O>, ListenableFuture<? extends O>>
    {
        AsyncTransformFuture(final ListenableFuture<? extends I> inputFuture, final AsyncFunction<? super I, ? extends O> function) {
            super(inputFuture, function);
        }
        
        @Override
        ListenableFuture<? extends O> doTransform(final AsyncFunction<? super I, ? extends O> function, @ParametricNullness final I input) throws Exception {
            final ListenableFuture<? extends O> outputFuture = function.apply((Object)input);
            Preconditions.checkNotNull(outputFuture, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", function);
            return outputFuture;
        }
        
        @Override
        void setResult(final ListenableFuture<? extends O> result) {
            this.setFuture((ListenableFuture<? extends O>)result);
        }
    }
    
    private static final class TransformFuture<I, O> extends AbstractTransformFuture<I, O, Function<? super I, ? extends O>, O>
    {
        TransformFuture(final ListenableFuture<? extends I> inputFuture, final Function<? super I, ? extends O> function) {
            super(inputFuture, function);
        }
        
        @ParametricNullness
        @Override
        O doTransform(final Function<? super I, ? extends O> function, @ParametricNullness final I input) {
            return (O)function.apply((Object)input);
        }
        
        @Override
        void setResult(@ParametricNullness final O result) {
            this.set((O)result);
        }
    }
}
