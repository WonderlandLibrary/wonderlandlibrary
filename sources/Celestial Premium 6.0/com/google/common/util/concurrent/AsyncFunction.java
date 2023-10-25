/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.util.concurrent.ListenableFuture;
import javax.annotation.Nullable;

@FunctionalInterface
@GwtCompatible
public interface AsyncFunction<I, O> {
    public ListenableFuture<O> apply(@Nullable I var1) throws Exception;
}

