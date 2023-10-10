/*
 * Decompiled with CFR 0.152.
 */
package joptsimple;

public class ValueConversionException
extends RuntimeException {
    private static final long serialVersionUID = -1L;

    public ValueConversionException(String message) {
        this(message, null);
    }

    public ValueConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}

