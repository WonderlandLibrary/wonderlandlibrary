// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.concurrent.Future;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.Set;
import me.gong.mcleaks.util.google.common.collect.UnmodifiableIterator;
import me.gong.mcleaks.util.google.common.collect.ImmutableCollection;
import java.util.logging.Logger;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class AggregateFuture<InputT, OutputT> extends TrustedFuture<OutputT>
{
    private static final Logger logger;
    private RunningState runningState;
    
    @Override
    protected final void afterDone() {
        super.afterDone();
        final RunningState localRunningState = this.runningState;
        if (localRunningState != null) {
            this.runningState = null;
            final ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures = localRunningState.futures;
            final boolean wasInterrupted = this.wasInterrupted();
            if (this.wasInterrupted()) {
                localRunningState.interruptTask();
            }
            if (this.isCancelled() & futures != null) {
                for (final ListenableFuture<?> future : futures) {
                    future.cancel(wasInterrupted);
                }
            }
        }
    }
    
    final void init(final RunningState runningState) {
        (this.runningState = runningState).init();
    }
    
    private static boolean addCausalChain(final Set<Throwable> seen, Throwable t) {
        while (t != null) {
            final boolean firstTimeSeen = seen.add(t);
            if (!firstTimeSeen) {
                return false;
            }
            t = t.getCause();
        }
        return true;
    }
    
    static {
        logger = Logger.getLogger(AggregateFuture.class.getName());
    }
    
    abstract class RunningState extends AggregateFutureState implements Runnable
    {
        private ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures;
        private final boolean allMustSucceed;
        private final boolean collectsValues;
        
        RunningState(final ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures, final boolean allMustSucceed, final boolean collectsValues) {
            super(futures.size());
            this.futures = Preconditions.checkNotNull(futures);
            this.allMustSucceed = allMustSucceed;
            this.collectsValues = collectsValues;
        }
        
        @Override
        public final void run() {
            this.decrementCountAndMaybeComplete();
        }
        
        private void init() {
            if (this.futures.isEmpty()) {
                this.handleAllCompleted();
                return;
            }
            if (this.allMustSucceed) {
                int i = 0;
                for (final ListenableFuture<? extends InputT> listenable : this.futures) {
                    final int index = i++;
                    listenable.addListener(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                RunningState.this.handleOneInputDone(index, listenable);
                            }
                            finally {
                                RunningState.this.decrementCountAndMaybeComplete();
                            }
                        }
                    }, MoreExecutors.directExecutor());
                }
            }
            else {
                for (final ListenableFuture<? extends InputT> listenable2 : this.futures) {
                    listenable2.addListener(this, MoreExecutors.directExecutor());
                }
            }
        }
        
        private void handleException(final Throwable throwable) {
            Preconditions.checkNotNull(throwable);
            boolean completedWithFailure = false;
            boolean firstTimeSeeingThisException = true;
            if (this.allMustSucceed) {
                completedWithFailure = AggregateFuture.this.setException(throwable);
                if (completedWithFailure) {
                    this.releaseResourcesAfterFailure();
                }
                else {
                    firstTimeSeeingThisException = addCausalChain(this.getOrInitSeenExceptions(), throwable);
                }
            }
            if (throwable instanceof Error | (this.allMustSucceed & !completedWithFailure & firstTimeSeeingThisException)) {
                final String message = (throwable instanceof Error) ? "Input Future failed with Error" : "Got more than one input Future failure. Logging failures after the first";
                AggregateFuture.logger.log(Level.SEVERE, message, throwable);
            }
        }
        
        @Override
        final void addInitialException(final Set<Throwable> seen) {
            if (!AggregateFuture.this.isCancelled()) {
                addCausalChain(seen, AggregateFuture.this.trustedGetException());
            }
        }
        
        private void handleOneInputDone(final int index, final Future<? extends InputT> future) {
            Preconditions.checkState(this.allMustSucceed || !AggregateFuture.this.isDone() || AggregateFuture.this.isCancelled(), (Object)"Future was done before all dependencies completed");
            try {
                Preconditions.checkState(future.isDone(), (Object)"Tried to set value from future which is not done");
                if (this.allMustSucceed) {
                    if (future.isCancelled()) {
                        AggregateFuture.this.runningState = null;
                        AggregateFuture.this.cancel(false);
                    }
                    else {
                        final InputT result = Futures.getDone((Future<InputT>)future);
                        if (this.collectsValues) {
                            this.collectOneValue(this.allMustSucceed, index, result);
                        }
                    }
                }
                else if (this.collectsValues && !future.isCancelled()) {
                    this.collectOneValue(this.allMustSucceed, index, Futures.getDone((Future<InputT>)future));
                }
            }
            catch (ExecutionException e) {
                this.handleException(e.getCause());
            }
            catch (Throwable t) {
                this.handleException(t);
            }
        }
        
        private void decrementCountAndMaybeComplete() {
            final int newRemaining = this.decrementRemainingAndGet();
            Preconditions.checkState(newRemaining >= 0, (Object)"Less than 0 remaining futures");
            if (newRemaining == 0) {
                this.processCompleted();
            }
        }
        
        private void processCompleted() {
            if (this.collectsValues & !this.allMustSucceed) {
                int i = 0;
                for (final ListenableFuture<? extends InputT> listenable : this.futures) {
                    this.handleOneInputDone(i++, listenable);
                }
            }
            this.handleAllCompleted();
        }
        
        void releaseResourcesAfterFailure() {
            this.futures = null;
        }
        
        abstract void collectOneValue(final boolean p0, final int p1, @Nullable final InputT p2);
        
        abstract void handleAllCompleted();
        
        void interruptTask() {
        }
    }
}
