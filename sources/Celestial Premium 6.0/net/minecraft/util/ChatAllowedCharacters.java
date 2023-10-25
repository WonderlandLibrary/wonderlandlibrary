/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.util.ResourceLeakDetector
 *  io.netty.util.ResourceLeakDetector$Level
 */
package net.minecraft.util;

import io.netty.util.ResourceLeakDetector;

public class ChatAllowedCharacters {
    public static final ResourceLeakDetector.Level NETTY_LEAK_DETECTION = ResourceLeakDetector.Level.DISABLED;
    public static final char[] ILLEGAL_STRUCTURE_CHARACTERS = new char[]{'.', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"'};
    public static final char[] ILLEGAL_FILE_CHARACTERS = new char[]{'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};

    public static boolean isAllowedCharacter(char character) {
        return character != '\u00a7' && character >= ' ' && character != '\u007f';
    }

    public static String filterAllowedCharacters(String input) {
        StringBuilder stringbuilder = new StringBuilder();
        for (char c0 : input.toCharArray()) {
            if (!ChatAllowedCharacters.isAllowedCharacter(c0)) continue;
            stringbuilder.append(c0);
        }
        return stringbuilder.toString();
    }

    static {
        ResourceLeakDetector.setLevel((ResourceLeakDetector.Level)NETTY_LEAK_DETECTION);
    }
}

