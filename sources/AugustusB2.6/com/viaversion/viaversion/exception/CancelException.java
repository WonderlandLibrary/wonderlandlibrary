// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.exception;

import com.viaversion.viaversion.api.Via;

public class CancelException extends Exception
{
    public static final CancelException CACHED;
    
    public CancelException() {
    }
    
    public CancelException(final String message) {
        super(message);
    }
    
    public CancelException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public CancelException(final Throwable cause) {
        super(cause);
    }
    
    public CancelException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    public static CancelException generate() {
        return Via.getManager().isDebug() ? new CancelException() : CancelException.CACHED;
    }
    
    static {
        CACHED = new CancelException() {
            @Override
            public Throwable fillInStackTrace() {
                return this;
            }
        };
    }
}
