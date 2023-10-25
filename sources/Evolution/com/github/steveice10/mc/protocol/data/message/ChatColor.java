/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.message;

public enum ChatColor {
    BLACK,
    DARK_BLUE,
    DARK_GREEN,
    DARK_AQUA,
    DARK_RED,
    DARK_PURPLE,
    GOLD,
    GRAY,
    DARK_GRAY,
    BLUE,
    GREEN,
    AQUA,
    RED,
    LIGHT_PURPLE,
    YELLOW,
    WHITE,
    RESET;


    public static ChatColor byName(String name) {
        name = name.toLowerCase();
        ChatColor[] chatColorArray = ChatColor.values();
        int n = chatColorArray.length;
        int n2 = 0;
        while (n2 < n) {
            ChatColor color = chatColorArray[n2];
            if (color.toString().equals(name)) {
                return color;
            }
            ++n2;
        }
        return null;
    }

    public String toString() {
        return this.name().toLowerCase();
    }
}

