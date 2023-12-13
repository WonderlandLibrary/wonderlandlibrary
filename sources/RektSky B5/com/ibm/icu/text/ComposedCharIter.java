/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.impl.Normalizer2Impl;

@Deprecated
public final class ComposedCharIter {
    @Deprecated
    public static final char DONE = '\uffff';
    private final Normalizer2Impl n2impl;
    private String decompBuf;
    private int curChar = 0;
    private int nextChar = -1;

    @Deprecated
    public ComposedCharIter() {
        this(false, 0);
    }

    @Deprecated
    public ComposedCharIter(boolean compat, int options) {
        this.n2impl = compat ? Norm2AllModes.getNFKCInstance().impl : Norm2AllModes.getNFCInstance().impl;
    }

    @Deprecated
    public boolean hasNext() {
        if (this.nextChar == -1) {
            this.findNextChar();
        }
        return this.nextChar != -1;
    }

    @Deprecated
    public char next() {
        if (this.nextChar == -1) {
            this.findNextChar();
        }
        this.curChar = this.nextChar;
        this.nextChar = -1;
        return (char)this.curChar;
    }

    @Deprecated
    public String decomposition() {
        if (this.decompBuf != null) {
            return this.decompBuf;
        }
        return "";
    }

    private void findNextChar() {
        int c2;
        block2: {
            this.decompBuf = null;
            for (c2 = this.curChar + 1; c2 < 65535; ++c2) {
                this.decompBuf = this.n2impl.getDecomposition(c2);
                if (this.decompBuf == null) {
                    continue;
                }
                break block2;
            }
            c2 = -1;
        }
        this.nextChar = c2;
    }
}

