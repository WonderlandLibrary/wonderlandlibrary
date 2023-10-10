// 
// Decompiled by Procyon v0.5.36
// 

package com.thealtening.api;

public final class TheAlteningException extends RuntimeException
{
    public TheAlteningException(final String errorType, final String shortDescription) {
        super(String.format("[%s]: %s", errorType, shortDescription));
    }
    
    public TheAlteningException(final String errorType, final Throwable cause) {
        super(errorType, cause);
    }
}
