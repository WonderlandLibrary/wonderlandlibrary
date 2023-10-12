package wtf.diablo.utils.render;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.Validate;

import java.util.Map;
import java.util.regex.Pattern;

public enum ChatColor
{
    BLACK("BLACK", 0, '0', 0),
    DARK_BLUE("DARK_BLUE", 1, '1', 1),
    DARK_GREEN("DARK_GREEN", 2, '2', 2),
    DARK_AQUA("DARK_AQUA", 3, '3', 3),
    DARK_RED("DARK_RED", 4, '4', 4),
    DARK_PURPLE("DARK_PURPLE", 5, '5', 5),
    GOLD("GOLD", 6, '6', 6),
    GRAY("GRAY", 7, '7', 7),
    DARK_GRAY("DARK_GRAY", 8, '8', 8),
    BLUE("BLUE", 9, '9', 9),
    GREEN("GREEN", 10, 'a', 10),
    AQUA("AQUA", 11, 'b', 11),
    RED("RED", 12, 'c', 12),
    LIGHT_PURPLE("LIGHT_PURPLE", 13, 'd', 13),
    YELLOW("YELLOW", 14, 'e', 14),
    WHITE("WHITE", 15, 'f', 15),
    MAGIC("MAGIC", 16, 'k', 16, true),
    BOLD("BOLD", 17, 'l', 17, true),
    STRIKETHROUGH("STRIKETHROUGH", 18, 'm', 18, true),
    UNDERLINE("UNDERLINE", 19, 'n', 19, true),
    ITALIC("ITALIC", 20, 'o', 20, true),
    RESET("RESET", 21, 'r', 21);

    public static final char COLOR_CHAR = '§';
    private static final Pattern STRIP_COLOR_PATTERN;
    private final int intCode;
    private final char code;
    private final boolean isFormat;
    private final String toString;
    private static final Map<Integer, ChatColor> BY_ID;
    private static final Map<Character, ChatColor> BY_CHAR;

    static {
        STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + '§' + "[0-9A-FK-OR]");
        BY_ID = Maps.newHashMap();
        BY_CHAR = Maps.newHashMap();
        ChatColor[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final ChatColor color = values[i];
            ChatColor.BY_ID.put(color.intCode, color);
            ChatColor.BY_CHAR.put(color.code, color);
        }
    }

    ChatColor(final String s, final int n, final char code, final int intCode) {
        this(s, n, code, intCode, false);
    }

    ChatColor(final String s, final int n, final char code, final int intCode, final boolean isFormat) {
        this.code = code;
        this.intCode = intCode;
        this.isFormat = isFormat;
        this.toString = new String(new char[]{'§', code});
    }

    public int getIntCode() {
        return this.intCode;
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

    public static ChatColor getByChar(final char code) {
        return ChatColor.BY_CHAR.get(code);
    }

    public static ChatColor getByChar(final String code) {
        Validate.notNull(code, "Code cannot be null");
        Validate.isTrue(code.length() > 0, "Code must have at least one char");
        return ChatColor.BY_CHAR.get(code.charAt(0));
    }

    public static String stripColor(final String input) {
        if (input == null) {
            return null;
        }
        return ChatColor.STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public static String translateAlternateColorCodes(final char altColorChar, final String textToTranslate) {
        final char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; ++i) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = '§';
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }

    public static String getLastColors(final String input) {
        String result = "";
        final int length = input.length();
        for (int index = length - 1; index > -1; --index) {
            final char section = input.charAt(index);
            if (section == '§' && index < length - 1) {
                final char c = input.charAt(index + 1);
                final ChatColor color = getByChar(c);
                if (color != null) {
                    result = color.toString() + result;
                    if (color.isColor()) {
                        break;
                    }
                    if (color.equals(ChatColor.RESET)) {
                        break;
                    }
                }
            }
        }
        return result;
    }
}