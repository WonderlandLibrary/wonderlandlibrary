// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.util.concurrent;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;
import com.google.common.annotations.Beta;

@Beta
public abstract class AbstractCheckedFuture<V, X extends Exception> extends SimpleForwardingListenableFuture<V> implements CheckedFuture<V, X>
{
    protected AbstractCheckedFuture(final ListenableFuture<V> delegate) {
        super(delegate);
    }
    
    protected abstract X mapException(final Exception p0);
    
    @Override
    public V checkedGet() throws X, Exception {
        try {
            return this.get();
        }
        catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw this.mapException(e);
        }
        catch (final CancellationException e2) {
            throw this.mapException(e2);
        }
        catch (final ExecutionException e3) {
            throw this.mapException(e3);
        }
    }
    
    @Override
    public V checkedGet(final long timeout, final TimeUnit unit) throws TimeoutException, X, Exception {
        try {
            return this.get(timeout, unit);
        }
        catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw this.mapException(e);
        }
        catch (final CancellationException e2) {
            throw this.mapException(e2);
        }
        catch (final ExecutionException e3) {
            throw this.mapException(e3);
        }
    }
}
