/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.BaseException;

public class UnsupportedTagException
extends BaseException {
    private static final long serialVersionUID = 1L;

    public UnsupportedTagException() {
    }

    public UnsupportedTagException(String string) {
        super(string);
    }

    public UnsupportedTagException(String string, Throwable throwable) {
        super(string, throwable);
    }
}

