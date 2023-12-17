// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import io.netty.util.ResourceLeakDetector;

public class ChatAllowedCharacters
{
    public static final ResourceLeakDetector.Level NETTY_LEAK_DETECTION;
    public static final char[] ILLEGAL_STRUCTURE_CHARACTERS;
    public static final char[] ILLEGAL_FILE_CHARACTERS;
    
    public static boolean isAllowedCharacter(final char character) {
        return character != '§' && character >= ' ' && character != '\u007f';
    }
    
    public static String filterAllowedCharacters(final String input) {
        final StringBuilder stringbuilder = new StringBuilder();
        for (final char c0 : input.toCharArray()) {
            if (isAllowedCharacter(c0)) {
                stringbuilder.append(c0);
            }
        }
        return stringbuilder.toString();
    }
    
    static {
        NETTY_LEAK_DETECTION = ResourceLeakDetector.Level.DISABLED;
        ILLEGAL_STRUCTURE_CHARACTERS = new char[] { '.', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"' };
        ILLEGAL_FILE_CHARACTERS = new char[] { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };
        ResourceLeakDetector.setLevel(ChatAllowedCharacters.NETTY_LEAK_DETECTION);
    }
}
