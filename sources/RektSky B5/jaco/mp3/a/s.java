/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a;

import jaco.mp3.a.q;

public final class s
extends q {
    private int a;

    public s(String string, Throwable throwable) {
        super(string, throwable);
        this.a = 256;
    }

    public s(int n2, Throwable throwable) {
        int n3 = n2;
        this("Bitstream errorcode " + Integer.toHexString(n3), throwable);
        this.a = n2;
    }

    public final int a() {
        return this.a;
    }
}

