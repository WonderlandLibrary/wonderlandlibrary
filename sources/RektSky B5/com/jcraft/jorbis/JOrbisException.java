/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

public class JOrbisException
extends Exception {
    private static final long serialVersionUID = 1L;

    public JOrbisException() {
    }

    public JOrbisException(String s2) {
        super("JOrbis: " + s2);
    }
}

