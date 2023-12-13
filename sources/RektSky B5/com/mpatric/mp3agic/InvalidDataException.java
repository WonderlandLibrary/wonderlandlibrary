/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.BaseException;

public class InvalidDataException
extends BaseException {
    private static final long serialVersionUID = 1L;

    public InvalidDataException() {
    }

    public InvalidDataException(String string) {
        super(string);
    }

    public InvalidDataException(String string, Throwable throwable) {
        super(string, throwable);
    }
}

