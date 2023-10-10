// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.util;

import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;
import java.util.regex.Pattern;

public class ChatColorUtil
{
    public static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx";
    public static final char COLOR_CHAR = '§';
    public static final Pattern STRIP_COLOR_PATTERN;
    private static final Int2IntMap COLOR_ORDINALS;
    private static int ordinalCounter;
    
    public static boolean isColorCode(final char c) {
        return ChatColorUtil.COLOR_ORDINALS.containsKey(c);
    }
    
    public static int getColorOrdinal(final char c) {
        return ChatColorUtil.COLOR_ORDINALS.getOrDefault(c, -1);
    }
    
    public static String translateAlternateColorCodes(final String s) {
        final char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length - 1; ++i) {
            if (chars[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(chars[i + 1]) > -1) {
                chars[i] = '§';
                chars[i + 1] = Character.toLowerCase(chars[i + 1]);
            }
        }
        return new String(chars);
    }
    
    public static String stripColor(final String input) {
        return ChatColorUtil.STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
    
    private static void addColorOrdinal(final int from, final int to) {
        for (int c = from; c <= to; ++c) {
            addColorOrdinal(c);
        }
    }
    
    private static void addColorOrdinal(final int colorChar) {
        ChatColorUtil.COLOR_ORDINALS.put(colorChar, ChatColorUtil.ordinalCounter++);
    }
    
    static {
        STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-ORX]");
        COLOR_ORDINALS = new Int2IntOpenHashMap();
        addColorOrdinal(48, 57);
        addColorOrdinal(97, 102);
        addColorOrdinal(107, 111);
        addColorOrdinal(114);
    }
}
