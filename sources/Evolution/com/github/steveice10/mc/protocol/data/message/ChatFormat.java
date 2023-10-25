/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.message;

public enum ChatFormat {
    BOLD,
    UNDERLINED,
    STRIKETHROUGH,
    ITALIC,
    OBFUSCATED;


    public static ChatFormat byName(String name) {
        name = name.toLowerCase();
        ChatFormat[] chatFormatArray = ChatFormat.values();
        int n = chatFormatArray.length;
        int n2 = 0;
        while (n2 < n) {
            ChatFormat format = chatFormatArray[n2];
            if (format.toString().equals(name)) {
                return format;
            }
            ++n2;
        }
        return null;
    }

    public String toString() {
        return this.name().toLowerCase();
    }
}

