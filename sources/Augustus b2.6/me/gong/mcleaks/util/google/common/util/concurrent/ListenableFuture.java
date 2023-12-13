// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import java.util.concurrent.Executor;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import java.util.concurrent.Future;

@GwtCompatible
public interface ListenableFuture<V> extends Future<V>
{
    void addListener(final Runnable p0, final Executor p1);
}
