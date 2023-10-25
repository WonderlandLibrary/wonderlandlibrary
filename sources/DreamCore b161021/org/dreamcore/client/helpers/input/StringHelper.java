package org.dreamcore.client.helpers.input;

import java.util.regex.Pattern;

public class StringHelper {

    public static String format(String text) {
        return Pattern.compile("(?i)§[0-9A-FK-OR]").matcher(text).replaceAll("");
    }

}
