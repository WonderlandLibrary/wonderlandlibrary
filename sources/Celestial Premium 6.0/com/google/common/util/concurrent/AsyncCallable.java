/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.util.concurrent.ListenableFuture;

@Beta
@GwtCompatible
public interface AsyncCallable<V> {
    public ListenableFuture<V> call() throws Exception;
}

