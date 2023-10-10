/*
 * Decompiled with CFR 0.152.
 */
package javassist.tools.reflect;

public class CannotCreateException
extends Exception {
    private static final long serialVersionUID = 1L;

    public CannotCreateException(String s2) {
        super(s2);
    }

    public CannotCreateException(Exception e2) {
        super("by " + e2.toString());
    }
}

