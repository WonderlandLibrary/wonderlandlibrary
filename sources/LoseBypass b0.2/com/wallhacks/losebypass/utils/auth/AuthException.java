/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils.auth;

public class AuthException
extends Exception {
    private static final long serialVersionUID = 1L;
    private String text;

    public AuthException(String s) {
        super(s);
        this.text = s;
    }

    public String getText() {
        return this.text;
    }
}

