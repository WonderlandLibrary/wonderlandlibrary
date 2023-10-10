/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

public class ChatAllowedCharacters {
    public static final char[] allowedCharactersArray = new char[]{'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};

    public static boolean isAllowedCharacter(char character) {
        if (character == '\u00a7') return false;
        if (character < ' ') return false;
        if (character == '\u007f') return false;
        return true;
    }

    public static String filterAllowedCharacters(String input) {
        StringBuilder stringbuilder = new StringBuilder();
        char[] cArray = input.toCharArray();
        int n = cArray.length;
        int n2 = 0;
        while (n2 < n) {
            char c0 = cArray[n2];
            if (ChatAllowedCharacters.isAllowedCharacter(c0)) {
                stringbuilder.append(c0);
            }
            ++n2;
        }
        return stringbuilder.toString();
    }
}

