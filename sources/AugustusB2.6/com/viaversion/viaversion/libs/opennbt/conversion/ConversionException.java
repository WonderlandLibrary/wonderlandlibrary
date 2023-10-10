// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.conversion;

public class ConversionException extends RuntimeException
{
    private static final long serialVersionUID = -2022049594558041160L;
    
    public ConversionException() {
    }
    
    public ConversionException(final String message) {
        super(message);
    }
    
    public ConversionException(final Throwable cause) {
        super(cause);
    }
    
    public ConversionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
