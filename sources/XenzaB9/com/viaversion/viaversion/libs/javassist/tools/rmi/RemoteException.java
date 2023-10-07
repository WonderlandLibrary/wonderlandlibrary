// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.javassist.tools.rmi;

public class RemoteException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public RemoteException(final String msg) {
        super(msg);
    }
    
    public RemoteException(final Exception e) {
        super("by " + e.toString());
    }
}
