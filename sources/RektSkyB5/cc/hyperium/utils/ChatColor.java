/*
 * Decompiled with CFR 0.152.
 */
package cc.hyperium.utils;

import java.util.regex.Pattern;

public enum ChatColor {
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
    MAGIC('k', true),
    BOLD('l', true),
    STRIKETHROUGH('m', true),
    UNDERLINE('n', true),
    ITALIC('o', true),
    RESET('r');

    public static final char COLOR_CHAR = '\u00a7';
    private final char code;
    private final boolean isFormat;
    private final String toString;

    private ChatColor(char code) {
        this(code, false);
    }

    private ChatColor(char code, boolean isFormat) {
        this.code = code;
        this.isFormat = isFormat;
        this.toString = new String(new char[]{'\u00a7', code});
    }

    public static String stripColor(String input) {
        return input == null ? null : Pattern.compile("(?i)\u00a7[0-9A-FK-OR]").matcher(input).replaceAll("");
    }

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        char[] b2 = textToTranslate.toCharArray();
        int bound = b2.length - 1;
        for (int i2 = 0; i2 < bound; ++i2) {
            if (b2[i2] != altColorChar || "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b2[i2 + 1]) <= -1) continue;
            b2[i2] = 167;
            b2[i2 + 1] = Character.toLowerCase(b2[i2 + 1]);
        }
        return new String(b2);
    }

    public char getChar() {
        return this.code;
    }

    public String toString() {
        return this.toString;
    }

    public boolean isFormat() {
        return this.isFormat;
    }

    public boolean isColor() {
        return !this.isFormat && this != RESET;
    }
}

