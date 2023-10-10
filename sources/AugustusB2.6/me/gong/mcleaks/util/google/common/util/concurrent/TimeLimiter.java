// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public interface TimeLimiter
{
     <T> T newProxy(final T p0, final Class<T> p1, final long p2, final TimeUnit p3);
    
    @CanIgnoreReturnValue
     <T> T callWithTimeout(final Callable<T> p0, final long p1, final TimeUnit p2, final boolean p3) throws Exception;
}
