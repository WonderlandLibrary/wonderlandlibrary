/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.util;

import org.apache.http.util.TextUtils;

public class Asserts {
    public static void check(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void check(boolean expression, String message, Object ... args) {
        if (!expression) {
            throw new IllegalStateException(String.format(message, args));
        }
    }

    public static void check(boolean expression, String message, Object arg) {
        if (!expression) {
            throw new IllegalStateException(String.format(message, arg));
        }
    }

    public static void notNull(Object object, String name) {
        if (object == null) {
            throw new IllegalStateException(name + " is null");
        }
    }

    public static void notEmpty(CharSequence s2, String name) {
        if (TextUtils.isEmpty(s2)) {
            throw new IllegalStateException(name + " is empty");
        }
    }

    public static void notBlank(CharSequence s2, String name) {
        if (TextUtils.isBlank(s2)) {
            throw new IllegalStateException(name + " is blank");
        }
    }
}

