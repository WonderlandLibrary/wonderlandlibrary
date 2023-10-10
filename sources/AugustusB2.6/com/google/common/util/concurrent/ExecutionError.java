// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public class ExecutionError extends Error
{
    private static final long serialVersionUID = 0L;
    
    protected ExecutionError() {
    }
    
    protected ExecutionError(@CheckForNull final String message) {
        super(message);
    }
    
    public ExecutionError(@CheckForNull final String message, @CheckForNull final Error cause) {
        super(message, cause);
    }
    
    public ExecutionError(@CheckForNull final Error cause) {
        super(cause);
    }
}
