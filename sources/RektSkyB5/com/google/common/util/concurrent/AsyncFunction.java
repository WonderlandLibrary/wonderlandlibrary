/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.util.concurrent;

import com.google.common.util.concurrent.ListenableFuture;

public interface AsyncFunction<I, O> {
    public ListenableFuture<O> apply(I var1) throws Exception;
}

