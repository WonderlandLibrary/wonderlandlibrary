/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.auth.exception.request;

import com.github.steveice10.mc.auth.exception.request.RequestException;

public class InvalidCredentialsException
extends RequestException {
    private static final long serialVersionUID = 1L;

    public InvalidCredentialsException() {
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCredentialsException(Throwable cause) {
        super(cause);
    }
}

