/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.util;

public final class TextUtils {
    public static boolean isEmpty(CharSequence s2) {
        if (s2 == null) {
            return true;
        }
        return s2.length() == 0;
    }

    public static boolean isBlank(CharSequence s2) {
        if (s2 == null) {
            return true;
        }
        for (int i2 = 0; i2 < s2.length(); ++i2) {
            if (Character.isWhitespace(s2.charAt(i2))) continue;
            return false;
        }
        return true;
    }

    public static boolean containsBlanks(CharSequence s2) {
        if (s2 == null) {
            return false;
        }
        for (int i2 = 0; i2 < s2.length(); ++i2) {
            if (!Character.isWhitespace(s2.charAt(i2))) continue;
            return true;
        }
        return false;
    }
}

