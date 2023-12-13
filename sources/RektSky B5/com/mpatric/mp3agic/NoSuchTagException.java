/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.BaseException;

public class NoSuchTagException
extends BaseException {
    private static final long serialVersionUID = 1L;

    public NoSuchTagException() {
    }

    public NoSuchTagException(String string) {
        super(string);
    }

    public NoSuchTagException(String string, Throwable throwable) {
        super(string, throwable);
    }
}

