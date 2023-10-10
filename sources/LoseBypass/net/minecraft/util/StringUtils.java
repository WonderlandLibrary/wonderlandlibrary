/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import java.util.regex.Pattern;

public class StringUtils {
    private static final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");

    public static String ticksToElapsedTime(int ticks) {
        String string;
        int i = ticks / 20;
        int j = i / 60;
        if ((i %= 60) < 10) {
            string = j + ":0" + i;
            return string;
        }
        string = j + ":" + i;
        return string;
    }

    public static String stripControlCodes(String p_76338_0_) {
        return patternControlCode.matcher(p_76338_0_).replaceAll("");
    }

    public static boolean isNullOrEmpty(String string) {
        return org.apache.commons.lang3.StringUtils.isEmpty(string);
    }
}

