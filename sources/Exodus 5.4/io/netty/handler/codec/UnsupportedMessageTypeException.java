/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec;

import io.netty.handler.codec.CodecException;

public class UnsupportedMessageTypeException
extends CodecException {
    private static final long serialVersionUID = 2799598826487038726L;

    public UnsupportedMessageTypeException(Throwable throwable) {
        super(throwable);
    }

    public UnsupportedMessageTypeException() {
    }

    public UnsupportedMessageTypeException(Object object, Class<?> ... classArray) {
        super(UnsupportedMessageTypeException.message(object == null ? "null" : object.getClass().getName(), classArray));
    }

    private static String message(String string, Class<?> ... classArray) {
        StringBuilder stringBuilder = new StringBuilder(string);
        if (classArray != null && classArray.length > 0) {
            Class<?> clazz;
            stringBuilder.append(" (expected: ").append(classArray[0].getName());
            for (int i = 1; i < classArray.length && (clazz = classArray[i]) != null; ++i) {
                stringBuilder.append(", ").append(clazz.getName());
            }
            stringBuilder.append(')');
        }
        return stringBuilder.toString();
    }

    public UnsupportedMessageTypeException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public UnsupportedMessageTypeException(String string) {
        super(string);
    }
}

