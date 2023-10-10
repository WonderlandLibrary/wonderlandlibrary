// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface FutureCallback<V>
{
    void onSuccess(@Nullable final V p0);
    
    void onFailure(final Throwable p0);
}
