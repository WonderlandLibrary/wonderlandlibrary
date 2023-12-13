// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@FunctionalInterface
@GwtCompatible
public interface AsyncFunction<I, O>
{
    ListenableFuture<O> apply(@Nullable final I p0) throws Exception;
}
