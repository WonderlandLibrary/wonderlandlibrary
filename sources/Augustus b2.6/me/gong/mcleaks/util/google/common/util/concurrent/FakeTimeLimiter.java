// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import java.util.concurrent.Callable;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@CanIgnoreReturnValue
@GwtIncompatible
public final class FakeTimeLimiter implements TimeLimiter
{
    @Override
    public <T> T newProxy(final T target, final Class<T> interfaceType, final long timeoutDuration, final TimeUnit timeoutUnit) {
        Preconditions.checkNotNull(target);
        Preconditions.checkNotNull(interfaceType);
        Preconditions.checkNotNull(timeoutUnit);
        return target;
    }
    
    @Override
    public <T> T callWithTimeout(final Callable<T> callable, final long timeoutDuration, final TimeUnit timeoutUnit, final boolean amInterruptible) throws Exception {
        Preconditions.checkNotNull(timeoutUnit);
        return callable.call();
    }
}
