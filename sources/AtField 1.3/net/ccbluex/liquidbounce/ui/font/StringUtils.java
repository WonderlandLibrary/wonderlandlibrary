/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.font;

import java.util.concurrent.ThreadLocalRandom;

public class StringUtils {
    public static String randomStringDefault(int n) {
        return StringUtils.randomString("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_", n);
    }

    public static String filterEmoji(String string) {
        if (string == null) {
            return string;
        }
        StringBuilder stringBuilder = null;
        int n = string.length();
        for (int i = 0; i < n; ++i) {
            char c = string.charAt(i);
            if (c == '\u0000') continue;
            if (stringBuilder == null) {
                stringBuilder = new StringBuilder(string.length());
            }
            stringBuilder.append(c);
        }
        if (stringBuilder == null) {
            return string;
        }
        if (stringBuilder.length() == n) {
            return string;
        }
        return stringBuilder.toString();
    }

    public static String randomString(String string, int n) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            stringBuilder.append(string.charAt(ThreadLocalRandom.current().nextInt(0, string.length() - 1)));
        }
        return stringBuilder.toString();
    }
}

