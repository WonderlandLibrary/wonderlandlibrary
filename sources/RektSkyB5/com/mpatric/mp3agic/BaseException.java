/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

public class BaseException
extends Exception {
    private static final long serialVersionUID = 1L;

    public BaseException() {
    }

    public BaseException(String string) {
        super(string);
    }

    public BaseException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public String getDetailedMessage() {
        Throwable throwable = this;
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            stringBuilder.append('[');
            stringBuilder.append(throwable.getClass().getName());
            if (throwable.getMessage() != null && throwable.getMessage().length() > 0) {
                stringBuilder.append(": ");
                stringBuilder.append(throwable.getMessage());
            }
            stringBuilder.append(']');
            throwable = throwable.getCause();
            if (throwable == null) break;
            stringBuilder.append(" caused by ");
        }
        return stringBuilder.toString();
    }
}

