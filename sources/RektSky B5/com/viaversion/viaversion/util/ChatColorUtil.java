/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.util;

import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import java.util.regex.Pattern;

public class ChatColorUtil {
    public static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx";
    public static final char COLOR_CHAR = '\u00a7';
    public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)\u00a7[0-9A-FK-ORX]");
    private static final Int2IntMap COLOR_ORDINALS = new Int2IntOpenHashMap();
    private static int ordinalCounter;

    public static boolean isColorCode(char c2) {
        return COLOR_ORDINALS.containsKey(c2);
    }

    public static int getColorOrdinal(char c2) {
        return COLOR_ORDINALS.getOrDefault(c2, -1);
    }

    public static String translateAlternateColorCodes(String s2) {
        char[] chars = s2.toCharArray();
        for (int i2 = 0; i2 < chars.length - 1; ++i2) {
            if (chars[i2] != '&' || ALL_CODES.indexOf(chars[i2 + 1]) <= -1) continue;
            chars[i2] = 167;
            chars[i2 + 1] = Character.toLowerCase(chars[i2 + 1]);
        }
        return new String(chars);
    }

    public static String stripColor(String input) {
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    private static void addColorOrdinal(int from, int to) {
        for (int c2 = from; c2 <= to; ++c2) {
            ChatColorUtil.addColorOrdinal(c2);
        }
    }

    private static void addColorOrdinal(int colorChar) {
        COLOR_ORDINALS.put(colorChar, ordinalCounter++);
    }

    static {
        ChatColorUtil.addColorOrdinal(48, 57);
        ChatColorUtil.addColorOrdinal(97, 102);
        ChatColorUtil.addColorOrdinal(107, 111);
        ChatColorUtil.addColorOrdinal(114);
    }
}

