/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a;

import jaco.mp3.a.q;

public final class u
extends q {
    private u(String string, Throwable throwable) {
        super(string, throwable);
    }

    public u(int n2, Throwable throwable) {
        this("Decoder errorcode " + Integer.toHexString(n2), throwable);
    }
}

