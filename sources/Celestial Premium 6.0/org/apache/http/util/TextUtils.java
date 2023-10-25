/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.util;

public final class TextUtils {
    public static boolean isEmpty(CharSequence s) {
        if (s == null) {
            return true;
        }
        return s.length() == 0;
    }

    public static boolean isBlank(CharSequence s) {
        if (s == null) {
            return true;
        }
        for (int i = 0; i < s.length(); ++i) {
            if (Character.isWhitespace(s.charAt(i))) continue;
            return false;
        }
        return true;
    }
}

