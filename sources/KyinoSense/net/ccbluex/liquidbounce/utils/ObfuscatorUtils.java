/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

public class ObfuscatorUtils {
    public static String obf(String name) {
        char[] c = name.toCharArray();
        for (int i = 0; i < c.length; ++i) {
            c[i] = (char)(c[i] ^ 0x4E20);
        }
        return new String(c);
    }
}

