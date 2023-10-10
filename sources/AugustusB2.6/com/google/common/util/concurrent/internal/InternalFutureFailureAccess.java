// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent.internal;

public abstract class InternalFutureFailureAccess
{
    protected InternalFutureFailureAccess() {
    }
    
    protected abstract Throwable tryInternalFastPathGetFailure();
}
