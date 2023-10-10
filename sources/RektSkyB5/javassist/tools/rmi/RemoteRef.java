/*
 * Decompiled with CFR 0.152.
 */
package javassist.tools.rmi;

import java.io.Serializable;

public class RemoteRef
implements Serializable {
    private static final long serialVersionUID = 1L;
    public int oid;
    public String classname;

    public RemoteRef(int i2) {
        this.oid = i2;
        this.classname = null;
    }

    public RemoteRef(int i2, String name) {
        this.oid = i2;
        this.classname = name;
    }
}

