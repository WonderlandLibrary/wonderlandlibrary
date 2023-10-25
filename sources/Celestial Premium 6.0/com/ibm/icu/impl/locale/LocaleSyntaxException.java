/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl.locale;

public class LocaleSyntaxException
extends Exception {
    private static final long serialVersionUID = 1L;
    private int _index = -1;

    public LocaleSyntaxException(String msg) {
        this(msg, 0);
    }

    public LocaleSyntaxException(String msg, int errorIndex) {
        super(msg);
        this._index = errorIndex;
    }

    public int getErrorIndex() {
        return this._index;
    }
}

