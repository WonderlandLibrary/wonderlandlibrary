// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@GwtCompatible
public interface AsyncCallable<V>
{
    ListenableFuture<V> call() throws Exception;
}
