/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.libs.javassist.tools.rmi;

public class RemoteException
extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RemoteException(String msg) {
        super(msg);
    }

    public RemoteException(Exception e) {
        super("by " + e.toString());
    }
}

