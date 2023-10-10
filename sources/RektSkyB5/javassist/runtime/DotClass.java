/*
 * Decompiled with CFR 0.152.
 */
package javassist.runtime;

public class DotClass {
    public static NoClassDefFoundError fail(ClassNotFoundException e2) {
        return new NoClassDefFoundError(e2.getMessage());
    }
}

