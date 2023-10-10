// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist;

public class NotFoundException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public NotFoundException(final String msg) {
        super(msg);
    }
    
    public NotFoundException(final String msg, final Exception e) {
        super(msg + " because of " + e.toString());
    }
}
