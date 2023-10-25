/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.auth.exception.request;

import com.github.steveice10.mc.auth.exception.request.RequestException;

public class ServiceUnavailableException
extends RequestException {
    private static final long serialVersionUID = 1L;

    public ServiceUnavailableException() {
    }

    public ServiceUnavailableException(String message) {
        super(message);
    }

    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceUnavailableException(Throwable cause) {
        super(cause);
    }
}

