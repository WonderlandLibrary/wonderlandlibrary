/*
 * Decompiled with CFR 0.150.
 */
package com.sun.pisces;

import com.sun.pisces.AbstractSurface;
import java.nio.IntBuffer;

public final class JavaSurface
extends AbstractSurface {
    private IntBuffer dataBuffer;
    private int[] dataInt;

    public JavaSurface(int[] arrn, int n, int n2, int n3) {
        super(n2, n3);
        if (arrn.length / n2 < n3) {
            throw new IllegalArgumentException("width(=" + n2 + ") * height(=" + n3 + ") is greater than dataInt.length(=" + arrn.length + ")");
        }
        this.dataInt = arrn;
        this.dataBuffer = IntBuffer.wrap(this.dataInt);
        this.initialize(n, n2, n3);
        this.addDisposerRecord();
    }

    public IntBuffer getDataIntBuffer() {
        return this.dataBuffer;
    }

    private native void initialize(int var1, int var2, int var3);
}

