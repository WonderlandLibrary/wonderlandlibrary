// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface FutureCallback<V>
{
    void onSuccess(@ParametricNullness final V p0);
    
    void onFailure(final Throwable p0);
}
