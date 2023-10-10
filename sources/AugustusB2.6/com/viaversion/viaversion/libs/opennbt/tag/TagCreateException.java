// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.opennbt.tag;

public class TagCreateException extends Exception
{
    private static final long serialVersionUID = -2022049594558041160L;
    
    public TagCreateException() {
    }
    
    public TagCreateException(final String message) {
        super(message);
    }
    
    public TagCreateException(final Throwable cause) {
        super(cause);
    }
    
    public TagCreateException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
