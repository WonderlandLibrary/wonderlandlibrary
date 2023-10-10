// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.utils;

import java.util.regex.Pattern;

public class ChatUtil
{
    private static final Pattern UNUSED_COLOR_PATTERN;
    private static final Pattern UNUSED_COLOR_PATTERN_PREFIX;
    
    public static String removeUnusedColor(final String legacy, final char defaultColor) {
        return removeUnusedColor(legacy, defaultColor, false);
    }
    
    public static String removeUnusedColor(String legacy, final char defaultColor, final boolean isPrefix) {
        if (legacy == null) {
            return null;
        }
        final Pattern pattern = isPrefix ? ChatUtil.UNUSED_COLOR_PATTERN_PREFIX : ChatUtil.UNUSED_COLOR_PATTERN;
        legacy = pattern.matcher(legacy).replaceAll("$1$2");
        final StringBuilder builder = new StringBuilder();
        char last = defaultColor;
        for (int i = 0; i < legacy.length(); ++i) {
            char current = legacy.charAt(i);
            if (current != '§' || i == legacy.length() - 1) {
                builder.append(current);
            }
            else {
                current = legacy.charAt(++i);
                if (current != last) {
                    builder.append('§').append(current);
                    last = current;
                }
            }
        }
        return builder.toString();
    }
    
    static {
        UNUSED_COLOR_PATTERN = Pattern.compile("(?>(?>§[0-fk-or])*(§r|\\Z))|(?>(?>§[0-f])*(§[0-f]))");
        UNUSED_COLOR_PATTERN_PREFIX = Pattern.compile("(?>(?>§[0-fk-or])*(§r))|(?>(?>§[0-f])*(§[0-f]))");
    }
}
