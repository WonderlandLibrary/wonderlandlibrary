package tech.atani.client.utility.java;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtil {

    public static String removeFormattingCodes(String input) {
        // Regular expression to match Minecraft text formatting codes
        String pattern = "§[0-9A-FK-ORa-fk-or]";
        return input.replaceAll(pattern, "");
    }

    public static String multiplyString(String input, int multiplier) {
        if (multiplier <= 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < multiplier; i++) {
            result.append(input);
        }

        return result.toString();
    }

    public static String removeSpecialLatinCharacters(String input) {
        if (input == null) {
            return null;
        }

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        String pattern = "[^\\p{InBasicLatin}]";

        return Pattern.compile(pattern).matcher(normalized).replaceAll("");
    }

}
