/*
 * Decompiled with CFR 0.152.
 */
package com.thealtening.api.utils.exceptions;

public class TheAlteningException
extends RuntimeException {
    public TheAlteningException(String error, String errorMessage) {
        super(String.format("[%s]: %s", error, errorMessage));
    }
}

