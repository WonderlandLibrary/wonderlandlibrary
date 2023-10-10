// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent.internal;

public final class InternalFutures
{
    public static Throwable tryInternalFastPathGetFailure(final InternalFutureFailureAccess future) {
        return future.tryInternalFastPathGetFailure();
    }
    
    private InternalFutures() {
    }
}
