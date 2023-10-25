/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio;

import java.io.IOException;

public class ImageStorageException
extends IOException {
    private static final long serialVersionUID = 1L;

    public ImageStorageException(String string) {
        super(string);
    }

    public ImageStorageException(String string, Throwable throwable) {
        super(string);
        this.initCause(throwable);
    }
}

