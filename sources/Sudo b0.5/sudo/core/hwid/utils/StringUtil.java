package sudo.core.hwid.utils;

import java.util.ArrayList;

/*
 *
 * @Author Vili (https://github.com/v1li)
 * Code is free to use :)
 *
 */

public class StringUtil {
    public static int convertToString(String letter) {
        Alphabet.LETTERS letters = null;
        for (int i = 0; i < Alphabet.LETTERS.values().length; i++) {
            if (Alphabet.get(i).equalsIgnoreCase(letter)) {
                letters = Alphabet.LETTERS.values()[i];
            }
        }

        if (letters == null) {
            return -1;
        }

        return letters.ordinal();
    }

    public static ArrayList<String> getSubstrings(String s) {
        ArrayList<String> substrings = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            String substring = s.substring(i, i + 1);
            substrings.add(substring);
        }
        return substrings;
    }
}