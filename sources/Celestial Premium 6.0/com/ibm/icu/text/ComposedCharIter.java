/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.impl.Normalizer2Impl;

public final class ComposedCharIter {
    public static final char DONE = '\uffff';
    private final Normalizer2Impl n2impl;
    private String decompBuf;
    private int curChar = 0;
    private int nextChar = -1;

    public ComposedCharIter() {
        this(false, 0);
    }

    public ComposedCharIter(boolean compat, int options) {
        this.n2impl = compat ? Norm2AllModes.getNFKCInstance().impl : Norm2AllModes.getNFCInstance().impl;
    }

    public boolean hasNext() {
        if (this.nextChar == -1) {
            this.findNextChar();
        }
        return this.nextChar != -1;
    }

    public char next() {
        if (this.nextChar == -1) {
            this.findNextChar();
        }
        this.curChar = this.nextChar;
        this.nextChar = -1;
        return (char)this.curChar;
    }

    public String decomposition() {
        if (this.decompBuf != null) {
            return this.decompBuf;
        }
        return "";
    }

    private void findNextChar() {
        int c;
        block2: {
            this.decompBuf = null;
            for (c = this.curChar + 1; c < 65535; ++c) {
                this.decompBuf = this.n2impl.getDecomposition(c);
                if (this.decompBuf == null) {
                    continue;
                }
                break block2;
            }
            c = -1;
        }
        this.nextChar = c;
    }
}

