/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.auth.exception.profile;

import com.github.steveice10.mc.auth.exception.profile.ProfileException;

public class ProfileNotFoundException
extends ProfileException {
    private static final long serialVersionUID = 1L;

    public ProfileNotFoundException() {
    }

    public ProfileNotFoundException(String message) {
        super(message);
    }

    public ProfileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfileNotFoundException(Throwable cause) {
        super(cause);
    }
}

