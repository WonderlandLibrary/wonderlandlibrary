/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viabackwards.utils;

import java.util.regex.Pattern;

public class ChatUtil {
    private static final Pattern UNUSED_COLOR_PATTERN = Pattern.compile("(?>(?>\u00a7[0-fk-or])*(\u00a7r|\\Z))|(?>(?>\u00a7[0-f])*(\u00a7[0-f]))");
    private static final Pattern UNUSED_COLOR_PATTERN_PREFIX = Pattern.compile("(?>(?>\u00a7[0-fk-or])*(\u00a7r))|(?>(?>\u00a7[0-f])*(\u00a7[0-f]))");

    public static String removeUnusedColor(String legacy, char defaultColor) {
        return ChatUtil.removeUnusedColor(legacy, defaultColor, false);
    }

    public static String removeUnusedColor(String legacy, char defaultColor, boolean isPrefix) {
        if (legacy == null) {
            return null;
        }
        Pattern pattern = isPrefix ? UNUSED_COLOR_PATTERN_PREFIX : UNUSED_COLOR_PATTERN;
        legacy = pattern.matcher(legacy).replaceAll("$1$2");
        StringBuilder builder = new StringBuilder();
        char last = defaultColor;
        for (int i2 = 0; i2 < legacy.length(); ++i2) {
            char current = legacy.charAt(i2);
            if (current != '\u00a7' || i2 == legacy.length() - 1) {
                builder.append(current);
                continue;
            }
            if ((current = legacy.charAt(++i2)) == last) continue;
            builder.append('\u00a7').append(current);
            last = current;
        }
        return builder.toString();
    }
}

