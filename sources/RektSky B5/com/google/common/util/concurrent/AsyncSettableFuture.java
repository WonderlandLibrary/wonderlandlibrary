/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.ForwardingListenableFuture;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import javax.annotation.Nullable;

final class AsyncSettableFuture<V>
extends ForwardingListenableFuture<V> {
    private final NestedFuture<V> nested = new NestedFuture();
    private final ListenableFuture<V> dereferenced = Futures.dereference(this.nested);

    public static <V> AsyncSettableFuture<V> create() {
        return new AsyncSettableFuture<V>();
    }

    private AsyncSettableFuture() {
    }

    @Override
    protected ListenableFuture<V> delegate() {
        return this.dereferenced;
    }

    public boolean setFuture(ListenableFuture<? extends V> future) {
        return this.nested.setFuture(Preconditions.checkNotNull(future));
    }

    public boolean setValue(@Nullable V value) {
        return this.setFuture(Futures.immediateFuture(value));
    }

    public boolean setException(Throwable exception) {
        return this.setFuture(Futures.immediateFailedFuture(exception));
    }

    public boolean isSet() {
        return this.nested.isDone();
    }

    private static final class NestedFuture<V>
    extends AbstractFuture<ListenableFuture<? extends V>> {
        private NestedFuture() {
        }

        boolean setFuture(ListenableFuture<? extends V> value) {
            boolean result = this.set(value);
            if (this.isCancelled()) {
                value.cancel(this.wasInterrupted());
            }
            return result;
        }
    }
}

