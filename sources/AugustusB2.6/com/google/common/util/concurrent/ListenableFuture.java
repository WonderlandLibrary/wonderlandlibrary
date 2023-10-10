// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.Executor;
import com.google.errorprone.annotations.DoNotMock;
import java.util.concurrent.Future;

@DoNotMock("Use the methods in Futures (like immediateFuture) or SettableFuture")
public interface ListenableFuture<V> extends Future<V>
{
    void addListener(final Runnable p0, final Executor p1);
}
