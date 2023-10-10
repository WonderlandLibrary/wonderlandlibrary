/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.util.concurrent;

import com.google.common.util.concurrent.AbstractFuture;
import javax.annotation.Nullable;

public final class SettableFuture<V>
extends AbstractFuture<V> {
    public static <V> SettableFuture<V> create() {
        return new SettableFuture<V>();
    }

    private SettableFuture() {
    }

    @Override
    public boolean set(@Nullable V value) {
        return super.set(value);
    }

    @Override
    public boolean setException(Throwable throwable) {
        return super.setException(throwable);
    }
}

