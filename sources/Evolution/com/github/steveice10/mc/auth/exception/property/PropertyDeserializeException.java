/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.auth.exception.property;

import com.github.steveice10.mc.auth.exception.property.PropertyException;

public class PropertyDeserializeException
extends PropertyException {
    private static final long serialVersionUID = 1L;

    public PropertyDeserializeException() {
    }

    public PropertyDeserializeException(String message) {
        super(message);
    }

    public PropertyDeserializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyDeserializeException(Throwable cause) {
        super(cause);
    }
}

