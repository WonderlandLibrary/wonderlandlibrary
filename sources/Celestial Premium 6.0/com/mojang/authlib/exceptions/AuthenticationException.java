/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.authlib.exceptions;

public class AuthenticationException
extends Exception {
    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}

