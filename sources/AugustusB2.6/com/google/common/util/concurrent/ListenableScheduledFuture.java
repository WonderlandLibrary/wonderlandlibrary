// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;
import java.util.concurrent.ScheduledFuture;

@ElementTypesAreNonnullByDefault
@Beta
@GwtCompatible
public interface ListenableScheduledFuture<V> extends ScheduledFuture<V>, ListenableFuture<V>
{
}
