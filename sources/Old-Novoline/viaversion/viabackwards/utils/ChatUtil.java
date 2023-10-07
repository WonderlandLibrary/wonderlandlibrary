package viaversion.viabackwards.utils;

import java.util.regex.Pattern;

public class ChatUtil {
    private static final Pattern UNUSED_COLOR_PATTERN = Pattern.compile("(?>(?>\u00A7[0-fk-or])*(\u00A7r|\\Z))|(?>(?>\u00A7[0-f])*(\u00A7[0-f]))");
    private static final Pattern UNUSED_COLOR_PATTERN_PREFIX = Pattern.compile("(?>(?>\u00A7[0-fk-or])*(\u00A7r))|(?>(?>\u00A7[0-f])*(\u00A7[0-f]))");

    public static String removeUnusedColor(String legacy, char defaultColor) {
        return removeUnusedColor(legacy, defaultColor, false);
    }

    public static String removeUnusedColor(String legacy, char defaultColor, boolean isPrefix) {
        if (legacy == null) return null;

        Pattern pattern = isPrefix ? UNUSED_COLOR_PATTERN_PREFIX : UNUSED_COLOR_PATTERN;
        legacy = pattern.matcher(legacy).replaceAll("$1$2");
        StringBuilder builder = new StringBuilder();
        char last = defaultColor;
        for (int i = 0; i < legacy.length(); i++) {
            char current = legacy.charAt(i);
            if (current != '\u00A7' || i == legacy.length() - 1) {
                builder.append(current);
                continue;
            }

            current = legacy.charAt(++i);
            if (current != last) {
                builder.append('\u00A7').append(current);
                last = current;
            }
        }
        return builder.toString();
    }
}
