// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public class UncheckedTimeoutException extends RuntimeException
{
    private static final long serialVersionUID = 0L;
    
    public UncheckedTimeoutException() {
    }
    
    public UncheckedTimeoutException(@CheckForNull final String message) {
        super(message);
    }
    
    public UncheckedTimeoutException(@CheckForNull final Throwable cause) {
        super(cause);
    }
    
    public UncheckedTimeoutException(@CheckForNull final String message, @CheckForNull final Throwable cause) {
        super(message, cause);
    }
}
