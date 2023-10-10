// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public class UncheckedExecutionException extends RuntimeException
{
    private static final long serialVersionUID = 0L;
    
    protected UncheckedExecutionException() {
    }
    
    protected UncheckedExecutionException(@CheckForNull final String message) {
        super(message);
    }
    
    public UncheckedExecutionException(@CheckForNull final String message, @CheckForNull final Throwable cause) {
        super(message, cause);
    }
    
    public UncheckedExecutionException(@CheckForNull final Throwable cause) {
        super(cause);
    }
}
