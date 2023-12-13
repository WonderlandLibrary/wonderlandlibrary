/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

public class ChatAllowedCharacters {
    public static final char[] allowedCharactersArray;
    private static final String __OBFID = "CL_00001606";

    static {
        char[] arrc = new char[15];
        arrc[0] = 47;
        arrc[1] = 10;
        arrc[2] = 13;
        arrc[3] = 9;
        arrc[5] = 12;
        arrc[6] = 96;
        arrc[7] = 63;
        arrc[8] = 42;
        arrc[9] = 92;
        arrc[10] = 60;
        arrc[11] = 62;
        arrc[12] = 124;
        arrc[13] = 34;
        arrc[14] = 58;
        allowedCharactersArray = arrc;
    }

    public static boolean isAllowedCharacter(char character) {
        return character != '\u00a7' && character >= ' ' && character != '\u007f';
    }

    public static String filterAllowedCharacters(String input) {
        StringBuilder var1 = new StringBuilder();
        for (char var5 : input.toCharArray()) {
            if (!ChatAllowedCharacters.isAllowedCharacter(var5)) continue;
            var1.append(var5);
        }
        return var1.toString();
    }
}

