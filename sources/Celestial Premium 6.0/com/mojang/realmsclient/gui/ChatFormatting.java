/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public enum ChatFormatting {
    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_AQUA('3'),
    DARK_RED('4'),
    DARK_PURPLE('5'),
    GOLD('6'),
    GRAY('7'),
    DARK_GRAY('8'),
    BLUE('9'),
    GREEN('a'),
    AQUA('b'),
    RED('c'),
    LIGHT_PURPLE('d'),
    YELLOW('e'),
    WHITE('f'),
    OBFUSCATED('k', true),
    BOLD('l', true),
    STRIKETHROUGH('m', true),
    UNDERLINE('n', true),
    ITALIC('o', true),
    RESET('r');

    public static final char PREFIX_CODE = '\u00a7';
    private static final Map<Character, ChatFormatting> FORMATTING_BY_CHAR;
    private static final Map<String, ChatFormatting> FORMATTING_BY_NAME;
    private static final Pattern STRIP_FORMATTING_PATTERN;
    private final char code;
    private final boolean isFormat;
    private final String toString;

    private ChatFormatting(char code) {
        this(code, false);
    }

    private ChatFormatting(char code, boolean isFormat) {
        this.code = code;
        this.isFormat = isFormat;
        this.toString = "\u00a7" + code;
    }

    public char getChar() {
        return this.code;
    }

    public boolean isFormat() {
        return this.isFormat;
    }

    public boolean isColor() {
        return !this.isFormat && this != RESET;
    }

    public String getName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public String toString() {
        return this.toString;
    }

    public static String stripFormatting(String input) {
        return input == null ? null : STRIP_FORMATTING_PATTERN.matcher(input).replaceAll("");
    }

    public static ChatFormatting getByChar(char code) {
        return FORMATTING_BY_CHAR.get(Character.valueOf(code));
    }

    public static ChatFormatting getByName(String name) {
        if (name == null) {
            return null;
        }
        return FORMATTING_BY_NAME.get(name.toLowerCase(Locale.ROOT));
    }

    public static Collection<String> getNames(boolean getColors, boolean getFormats) {
        ArrayList<String> result = new ArrayList<String>();
        for (ChatFormatting format : ChatFormatting.values()) {
            if (format.isColor() && !getColors || format.isFormat() && !getFormats) continue;
            result.add(format.getName());
        }
        return result;
    }

    static {
        FORMATTING_BY_CHAR = new HashMap<Character, ChatFormatting>();
        FORMATTING_BY_NAME = new HashMap<String, ChatFormatting>();
        STRIP_FORMATTING_PATTERN = Pattern.compile("(?i)\u00a7[0-9A-FK-OR]");
        for (ChatFormatting format : ChatFormatting.values()) {
            FORMATTING_BY_CHAR.put(Character.valueOf(format.getChar()), format);
            FORMATTING_BY_NAME.put(format.getName(), format);
        }
    }
}

