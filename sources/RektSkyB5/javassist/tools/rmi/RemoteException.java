/*
 * Decompiled with CFR 0.152.
 */
package javassist.tools.rmi;

public class RemoteException
extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RemoteException(String msg) {
        super(msg);
    }

    public RemoteException(Exception e2) {
        super("by " + e2.toString());
    }
}

