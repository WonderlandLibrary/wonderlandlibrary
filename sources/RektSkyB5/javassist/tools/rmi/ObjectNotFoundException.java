/*
 * Decompiled with CFR 0.152.
 */
package javassist.tools.rmi;

public class ObjectNotFoundException
extends Exception {
    private static final long serialVersionUID = 1L;

    public ObjectNotFoundException(String name) {
        super(name + " is not exported");
    }

    public ObjectNotFoundException(String name, Exception e2) {
        super(name + " because of " + e2.toString());
    }
}

