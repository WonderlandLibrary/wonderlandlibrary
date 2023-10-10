// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.Future;
import com.google.common.base.Preconditions;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
final class TimeoutFuture<V> extends TrustedFuture<V>
{
    @CheckForNull
    private ListenableFuture<V> delegateRef;
    @CheckForNull
    private ScheduledFuture<?> timer;
    
    static <V> ListenableFuture<V> create(final ListenableFuture<V> delegate, final long time, final TimeUnit unit, final ScheduledExecutorService scheduledExecutor) {
        final TimeoutFuture<V> result = new TimeoutFuture<V>(delegate);
        final Fire<V> fire = new Fire<V>(result);
        result.timer = scheduledExecutor.schedule(fire, time, unit);
        delegate.addListener(fire, MoreExecutors.directExecutor());
        return result;
    }
    
    private TimeoutFuture(final ListenableFuture<V> delegate) {
        this.delegateRef = Preconditions.checkNotNull(delegate);
    }
    
    @CheckForNull
    @Override
    protected String pendingToString() {
        final ListenableFuture<? extends V> localInputFuture = (ListenableFuture<? extends V>)this.delegateRef;
        final ScheduledFuture<?> localTimer = this.timer;
        if (localInputFuture != null) {
            final String value = String.valueOf(localInputFuture);
            String message = new StringBuilder(14 + String.valueOf(value).length()).append("inputFuture=[").append(value).append("]").toString();
            if (localTimer != null) {
                final long delay = localTimer.getDelay(TimeUnit.MILLISECONDS);
                if (delay > 0L) {
                    final String value2 = String.valueOf(message);
                    message = new StringBuilder(43 + String.valueOf(value2).length()).append(value2).append(", remaining delay=[").append(delay).append(" ms]").toString();
                }
            }
            return message;
        }
        return null;
    }
    
    @Override
    protected void afterDone() {
        this.maybePropagateCancellationTo(this.delegateRef);
        final Future<?> localTimer = this.timer;
        if (localTimer != null) {
            localTimer.cancel(false);
        }
        this.delegateRef = null;
        this.timer = null;
    }
    
    private static final class Fire<V> implements Runnable
    {
        @CheckForNull
        TimeoutFuture<V> timeoutFutureRef;
        
        Fire(final TimeoutFuture<V> timeoutFuture) {
            this.timeoutFutureRef = timeoutFuture;
        }
        
        @Override
        public void run() {
            final TimeoutFuture<V> timeoutFuture = this.timeoutFutureRef;
            if (timeoutFuture == null) {
                return;
            }
            final ListenableFuture<V> delegate = (ListenableFuture<V>)((TimeoutFuture<Object>)timeoutFuture).delegateRef;
            if (delegate == null) {
                return;
            }
            this.timeoutFutureRef = null;
            if (delegate.isDone()) {
                timeoutFuture.setFuture((ListenableFuture<?>)delegate);
            }
            else {
                try {
                    final ScheduledFuture<?> timer = ((TimeoutFuture<Object>)timeoutFuture).timer;
                    ((TimeoutFuture<Object>)timeoutFuture).timer = null;
                    String message = "Timed out";
                    try {
                        if (timer != null) {
                            final long overDelayMs = Math.abs(timer.getDelay(TimeUnit.MILLISECONDS));
                            if (overDelayMs > 10L) {
                                final String value = String.valueOf(message);
                                message = new StringBuilder(66 + String.valueOf(value).length()).append(value).append(" (timeout delayed by ").append(overDelayMs).append(" ms after scheduled time)").toString();
                            }
                        }
                        final String value2 = String.valueOf(message);
                        final String value3 = String.valueOf(delegate);
                        message = new StringBuilder(2 + String.valueOf(value2).length() + String.valueOf(value3).length()).append(value2).append(": ").append(value3).toString();
                    }
                    finally {
                        timeoutFuture.setException(new TimeoutFutureException(message));
                    }
                }
                finally {
                    delegate.cancel(true);
                }
            }
        }
    }
    
    private static final class TimeoutFutureException extends TimeoutException
    {
        private TimeoutFutureException(final String message) {
            super(message);
        }
        
        @Override
        public synchronized Throwable fillInStackTrace() {
            this.setStackTrace(new StackTraceElement[0]);
            return this;
        }
    }
}
