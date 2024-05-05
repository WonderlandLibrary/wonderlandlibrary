/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni.encoding;

public final class ObjPtr<T> {
    public T p;

    public ObjPtr() {
        this(null);
    }

    public ObjPtr(T p) {
        this.p = p;
    }
}

