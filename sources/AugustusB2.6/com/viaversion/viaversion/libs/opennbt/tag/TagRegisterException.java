// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.tag;

public class TagRegisterException extends RuntimeException
{
    private static final long serialVersionUID = -2022049594558041160L;
    
    public TagRegisterException() {
    }
    
    public TagRegisterException(final String message) {
        super(message);
    }
    
    public TagRegisterException(final Throwable cause) {
        super(cause);
    }
    
    public TagRegisterException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
