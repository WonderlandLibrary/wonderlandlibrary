// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@CanIgnoreReturnValue
@GwtCompatible
public interface CheckedFuture<V, X extends Exception> extends ListenableFuture<V>
{
    V checkedGet() throws X, Exception;
    
    V checkedGet(final long p0, final TimeUnit p1) throws TimeoutException, X, Exception;
}
