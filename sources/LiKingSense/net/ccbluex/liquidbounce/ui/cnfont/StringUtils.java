/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.cnfont;

import java.util.concurrent.ThreadLocalRandom;

public class StringUtils {
    public static String randomStringDefault(int length) {
        return StringUtils.randomString("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_", length);
    }

    public static String randomString(String pool, int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            builder.append(pool.charAt(ThreadLocalRandom.current().nextInt(0, pool.length() - 1)));
        }
        return builder.toString();
    }

    public static boolean isBlank(String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0; i < s.length(); ++i) {
            if (Character.isWhitespace(s.charAt(i))) continue;
            return false;
        }
        return true;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return codePoint == '\u0000' || codePoint == '\t' || codePoint == '\n' || codePoint == '\r' || codePoint >= ' ' && codePoint <= '\ud7ff' || codePoint >= '\ue000' && codePoint <= '\ufffd' || codePoint >= '\u10000' && codePoint <= '\u10ffff';
    }

    public static String filterEmoji(String source) {
        if (StringUtils.isBlank(source)) {
            return source;
        }
        StringBuilder buf = null;
        int len = source.length();
        for (int i = 0; i < len; ++i) {
            char codePoint = source.charAt(i);
            if (!StringUtils.isEmojiCharacter(codePoint)) continue;
            if (buf == null) {
                buf = new StringBuilder(source.length());
            }
            buf.append(codePoint);
        }
        if (buf == null) {
            return source;
        }
        if (buf.length() == len) {
            return source;
        }
        return buf.toString();
    }
}

