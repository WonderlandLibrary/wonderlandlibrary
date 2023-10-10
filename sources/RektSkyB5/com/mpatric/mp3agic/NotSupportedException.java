/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.BaseException;

public class NotSupportedException
extends BaseException {
    private static final long serialVersionUID = 1L;

    public NotSupportedException() {
    }

    public NotSupportedException(String string) {
        super(string);
    }

    public NotSupportedException(String string, Throwable throwable) {
        super(string, throwable);
    }
}

