// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public class VerifyException extends RuntimeException
{
    public VerifyException() {
    }
    
    public VerifyException(@CheckForNull final String message) {
        super(message);
    }
    
    public VerifyException(@CheckForNull final Throwable cause) {
        super(cause);
    }
    
    public VerifyException(@CheckForNull final String message, @CheckForNull final Throwable cause) {
        super(message, cause);
    }
}
