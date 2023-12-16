// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils;

import java.util.regex.Pattern;

public enum ChatColor
{
    BLACK("BLACK", 0, '0'), 
    DARK_BLUE("DARK_BLUE", 1, '1'), 
    DARK_GREEN("DARK_GREEN", 2, '2'), 
    DARK_AQUA("DARK_AQUA", 3, '3'), 
    DARK_RED("DARK_RED", 4, '4'), 
    DARK_PURPLE("DARK_PURPLE", 5, '5'), 
    GOLD("GOLD", 6, '6'), 
    GRAY("GRAY", 7, '7'), 
    DARK_GRAY("DARK_GRAY", 8, '8'), 
    BLUE("BLUE", 9, '9'), 
    GREEN("GREEN", 10, 'a'), 
    AQUA("AQUA", 11, 'b'), 
    RED("RED", 12, 'c'), 
    LIGHT_PURPLE("LIGHT_PURPLE", 13, 'd'), 
    YELLOW("YELLOW", 14, 'e'), 
    WHITE("WHITE", 15, 'f'), 
    MAGIC("MAGIC", 16, 'k', true), 
    BOLD("BOLD", 17, 'l', true), 
    STRIKETHROUGH("STRIKETHROUGH", 18, 'm', true), 
    UNDERLINE("UNDERLINE", 19, 'n', true), 
    ITALIC("ITALIC", 20, 'o', true), 
    RESET("RESET", 21, 'r');
    
    public static final char COLOR_CHAR = '§';
    private final char code;
    private final boolean isFormat;
    private final String toString;
    
    private ChatColor(final String s, final int n, final char code) {
        this(s, n, code, false);
    }
    
    private ChatColor(final String name, final int ordinal, final char code, final boolean isFormat) {
        this.code = code;
        this.isFormat = isFormat;
        this.toString = new String(new char[] { '§', code });
    }
    
    public static String stripColor(final String input) {
        return (input == null) ? null : Pattern.compile("(?i)§[0-9A-FK-OR]").matcher(input).replaceAll("");
    }
    
    public static String translateAlternateColorCodes(final char altColorChar, final String textToTranslate) {
        final char[] b = textToTranslate.toCharArray();
        for (int bound = b.length - 1, i = 0; i < bound; ++i) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = '§';
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }
    
    public char getChar() {
        return this.code;
    }
    
    @Override
    public String toString() {
        return this.toString;
    }
    
    public boolean isFormat() {
        return this.isFormat;
    }
    
    public boolean isColor() {
        return !this.isFormat && this != ChatColor.RESET;
    }
}
