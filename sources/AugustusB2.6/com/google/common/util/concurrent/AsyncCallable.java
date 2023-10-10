// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@FunctionalInterface
@ElementTypesAreNonnullByDefault
@Beta
@GwtCompatible
public interface AsyncCallable<V>
{
    ListenableFuture<V> call() throws Exception;
}
