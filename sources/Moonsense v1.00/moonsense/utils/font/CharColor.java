package moonsense.utils.font;

import java.util.regex.Pattern;

public enum CharColor
{
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

    public static final char COLOR_CHAR = '&';
    private final char code;
    private final boolean isFormat;
    private final String toString;

    private CharColor(final char code) {
        this(code, false);
    }

    private CharColor(final char code, final boolean isFormat) {
        this.code = code;
        this.isFormat = isFormat;
        this.toString = new String(new char[] { '&', code });
    }

    public static String stripColor(final String input) {
        return (input == null) ? null : Pattern.compile("(?i)§[0-9A-FK-OR]").matcher(input).replaceAll("");
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
        return !this.isFormat && this != CharColor.RESET;
    }
}
