// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;
import java.util.concurrent.ScheduledFuture;

@Beta
@GwtIncompatible
public interface ListenableScheduledFuture<V> extends ScheduledFuture<V>, ListenableFuture<V>
{
}
