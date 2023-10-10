// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import com.google.errorprone.annotations.ForOverride;
import java.util.concurrent.ExecutionException;
import java.util.Set;
import java.util.logging.Level;
import java.util.Objects;
import com.google.common.collect.UnmodifiableIterator;
import java.util.concurrent.Future;
import com.google.common.base.Preconditions;
import javax.annotation.CheckForNull;
import com.google.common.collect.ImmutableCollection;
import java.util.logging.Logger;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class AggregateFuture<InputT, OutputT> extends AggregateFutureState<OutputT>
{
    private static final Logger logger;
    @CheckForNull
    private ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures;
    private final boolean allMustSucceed;
    private final boolean collectsValues;
    
    AggregateFuture(final ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures, final boolean allMustSucceed, final boolean collectsValues) {
        super(futures.size());
        this.futures = Preconditions.checkNotNull(futures);
        this.allMustSucceed = allMustSucceed;
        this.collectsValues = collectsValues;
    }
    
    @Override
    protected final void afterDone() {
        super.afterDone();
        final ImmutableCollection<? extends Future<?>> localFutures = this.futures;
        this.releaseResources(ReleaseResourcesReason.OUTPUT_FUTURE_DONE);
        if (this.isCancelled() & localFutures != null) {
            final boolean wasInterrupted = this.wasInterrupted();
            for (final Future<?> future : localFutures) {
                future.cancel(wasInterrupted);
            }
        }
    }
    
    @CheckForNull
    @Override
    protected final String pendingToString() {
        final ImmutableCollection<? extends Future<?>> localFutures = this.futures;
        if (localFutures != null) {
            final String value = String.valueOf(localFutures);
            return new StringBuilder(8 + String.valueOf(value).length()).append("futures=").append(value).toString();
        }
        return super.pendingToString();
    }
    
    final void init() {
        Objects.requireNonNull(this.futures);
        if (this.futures.isEmpty()) {
            this.handleAllCompleted();
            return;
        }
        if (this.allMustSucceed) {
            int i = 0;
            for (final ListenableFuture<? extends InputT> future : this.futures) {
                final int index = i++;
                future.addListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (future.isCancelled()) {
                                AggregateFuture.this.futures = null;
                                AggregateFuture.this.cancel(false);
                            }
                            else {
                                AggregateFuture.this.collectValueFromNonCancelledFuture(index, future);
                            }
                        }
                        finally {
                            AggregateFuture.this.decrementCountAndMaybeComplete(null);
                        }
                    }
                }, MoreExecutors.directExecutor());
            }
        }
        else {
            final ImmutableCollection<? extends Future<? extends InputT>> localFutures = this.collectsValues ? this.futures : null;
            final Runnable listener = new Runnable() {
                @Override
                public void run() {
                    AggregateFuture.this.decrementCountAndMaybeComplete(localFutures);
                }
            };
            for (final ListenableFuture<? extends InputT> future2 : this.futures) {
                future2.addListener(listener, MoreExecutors.directExecutor());
            }
        }
    }
    
    private void handleException(final Throwable throwable) {
        Preconditions.checkNotNull(throwable);
        if (this.allMustSucceed) {
            final boolean completedWithFailure = this.setException(throwable);
            if (!completedWithFailure) {
                final boolean firstTimeSeeingThisException = addCausalChain(this.getOrInitSeenExceptions(), throwable);
                if (firstTimeSeeingThisException) {
                    log(throwable);
                    return;
                }
            }
        }
        if (throwable instanceof Error) {
            log(throwable);
        }
    }
    
    private static void log(final Throwable throwable) {
        final String message = (throwable instanceof Error) ? "Input Future failed with Error" : "Got more than one input Future failure. Logging failures after the first";
        AggregateFuture.logger.log(Level.SEVERE, message, throwable);
    }
    
    @Override
    final void addInitialException(final Set<Throwable> seen) {
        Preconditions.checkNotNull(seen);
        if (!this.isCancelled()) {
            addCausalChain(seen, Objects.requireNonNull(this.tryInternalFastPathGetFailure()));
        }
    }
    
    private void collectValueFromNonCancelledFuture(final int index, final Future<? extends InputT> future) {
        try {
            this.collectOneValue(index, Futures.getDone((Future<InputT>)future));
        }
        catch (ExecutionException e) {
            this.handleException(e.getCause());
        }
        catch (Throwable t) {
            this.handleException(t);
        }
    }
    
    private void decrementCountAndMaybeComplete(@CheckForNull final ImmutableCollection<? extends Future<? extends InputT>> futuresIfNeedToCollectAtCompletion) {
        final int newRemaining = this.decrementRemainingAndGet();
        Preconditions.checkState(newRemaining >= 0, (Object)"Less than 0 remaining futures");
        if (newRemaining == 0) {
            this.processCompleted(futuresIfNeedToCollectAtCompletion);
        }
    }
    
    private void processCompleted(@CheckForNull final ImmutableCollection<? extends Future<? extends InputT>> futuresIfNeedToCollectAtCompletion) {
        if (futuresIfNeedToCollectAtCompletion != null) {
            int i = 0;
            for (final Future<? extends InputT> future : futuresIfNeedToCollectAtCompletion) {
                if (!future.isCancelled()) {
                    this.collectValueFromNonCancelledFuture(i, future);
                }
                ++i;
            }
        }
        this.clearSeenExceptions();
        this.handleAllCompleted();
        this.releaseResources(ReleaseResourcesReason.ALL_INPUT_FUTURES_PROCESSED);
    }
    
    @ForOverride
    @OverridingMethodsMustInvokeSuper
    void releaseResources(final ReleaseResourcesReason reason) {
        Preconditions.checkNotNull(reason);
        this.futures = null;
    }
    
    abstract void collectOneValue(final int p0, @ParametricNullness final InputT p1);
    
    abstract void handleAllCompleted();
    
    private static boolean addCausalChain(final Set<Throwable> seen, final Throwable param) {
        for (Throwable t = param; t != null; t = t.getCause()) {
            final boolean firstTimeSeen = seen.add(t);
            if (!firstTimeSeen) {
                return false;
            }
        }
        return true;
    }
    
    static {
        logger = Logger.getLogger(AggregateFuture.class.getName());
    }
    
    enum ReleaseResourcesReason
    {
        OUTPUT_FUTURE_DONE, 
        ALL_INPUT_FUTURES_PROCESSED;
        
        private static /* synthetic */ ReleaseResourcesReason[] $values() {
            return new ReleaseResourcesReason[] { ReleaseResourcesReason.OUTPUT_FUTURE_DONE, ReleaseResourcesReason.ALL_INPUT_FUTURES_PROCESSED };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
