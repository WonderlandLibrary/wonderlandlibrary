// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.logging.Logger;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
class ImmediateFuture<V> implements ListenableFuture<V>
{
    static final ListenableFuture<?> NULL;
    private static final Logger log;
    @ParametricNullness
    private final V value;
    
    ImmediateFuture(@ParametricNullness final V value) {
        this.value = value;
    }
    
    @Override
    public void addListener(final Runnable listener, final Executor executor) {
        Preconditions.checkNotNull(listener, (Object)"Runnable was null.");
        Preconditions.checkNotNull(executor, (Object)"Executor was null.");
        try {
            executor.execute(listener);
        }
        catch (RuntimeException e) {
            final Logger log = ImmediateFuture.log;
            final Level severe = Level.SEVERE;
            final String value = String.valueOf(listener);
            final String value2 = String.valueOf(executor);
            log.log(severe, new StringBuilder(57 + String.valueOf(value).length() + String.valueOf(value2).length()).append("RuntimeException while executing runnable ").append(value).append(" with executor ").append(value2).toString(), e);
        }
    }
    
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return false;
    }
    
    @ParametricNullness
    @Override
    public V get() {
        return this.value;
    }
    
    @ParametricNullness
    @Override
    public V get(final long timeout, final TimeUnit unit) throws ExecutionException {
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
    
    @Override
    public String toString() {
        final String string = super.toString();
        final String value = String.valueOf(this.value);
        return new StringBuilder(27 + String.valueOf(string).length() + String.valueOf(value).length()).append(string).append("[status=SUCCESS, result=[").append(value).append("]]").toString();
    }
    
    static {
        NULL = new ImmediateFuture<Object>(null);
        log = Logger.getLogger(ImmediateFuture.class.getName());
    }
    
    static final class ImmediateFailedFuture<V> extends TrustedFuture<V>
    {
        ImmediateFailedFuture(final Throwable thrown) {
            this.setException(thrown);
        }
    }
    
    static final class ImmediateCancelledFuture<V> extends TrustedFuture<V>
    {
        ImmediateCancelledFuture() {
            this.cancel(false);
        }
    }
}
