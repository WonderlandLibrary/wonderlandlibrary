/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.tools.rmi;

import com.viaversion.viaversion.libs.javassist.tools.rmi.ObjectImporter;
import com.viaversion.viaversion.libs.javassist.tools.rmi.RemoteException;

public class Sample {
    private ObjectImporter importer;
    private int objectId;

    public Object forward(Object[] args2, int identifier) {
        return this.importer.call(this.objectId, identifier, args2);
    }

    public static Object forwardStatic(Object[] args2, int identifier) throws RemoteException {
        throw new RemoteException("cannot call a static method.");
    }
}

