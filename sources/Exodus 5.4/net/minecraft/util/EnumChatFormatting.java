/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 */
package net.minecraft.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

public enum EnumChatFormatting {
    BLACK("BLACK", '0', 0),
    DARK_BLUE("DARK_BLUE", '1', 1),
    DARK_GREEN("DARK_GREEN", '2', 2),
    DARK_AQUA("DARK_AQUA", '3', 3),
    DARK_RED("DARK_RED", '4', 4),
    DARK_PURPLE("DARK_PURPLE", '5', 5),
    GOLD("GOLD", '6', 6),
    GRAY("GRAY", '7', 7),
    DARK_GRAY("DARK_GRAY", '8', 8),
    BLUE("BLUE", '9', 9),
    GREEN("GREEN", 'a', 10),
    AQUA("AQUA", 'b', 11),
    RED("RED", 'c', 12),
    LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13),
    YELLOW("YELLOW", 'e', 14),
    WHITE("WHITE", 'f', 15),
    OBFUSCATED("OBFUSCATED", 'k', true),
    BOLD("BOLD", 'l', true),
    STRIKETHROUGH("STRIKETHROUGH", 'm', true),
    UNDERLINE("UNDERLINE", 'n', true),
    ITALIC("ITALIC", 'o', true),
    RESET("RESET", 'r', -1);

    private static final Pattern formattingCodePattern;
    private final boolean fancyStyling;
    private final int colorIndex;
    private static final Map<String, EnumChatFormatting> nameMapping;
    private final String controlString;
    private final String name;
    private final char formattingCode;

    private EnumChatFormatting(String string2, char c, int n2) {
        this(string2, c, false, n2);
    }

    public static String getTextWithoutFormattingCodes(String string) {
        return string == null ? null : formattingCodePattern.matcher(string).replaceAll("");
    }

    public static Collection<String> getValidValues(boolean bl, boolean bl2) {
        ArrayList arrayList = Lists.newArrayList();
        EnumChatFormatting[] enumChatFormattingArray = EnumChatFormatting.values();
        int n = enumChatFormattingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumChatFormatting enumChatFormatting = enumChatFormattingArray[n2];
            if (!(enumChatFormatting.isColor() && !bl || enumChatFormatting.isFancyStyling() && !bl2)) {
                arrayList.add(enumChatFormatting.getFriendlyName());
            }
            ++n2;
        }
        return arrayList;
    }

    public String toString() {
        return this.controlString;
    }

    public boolean isFancyStyling() {
        return this.fancyStyling;
    }

    public static EnumChatFormatting getValueByName(String string) {
        return string == null ? null : nameMapping.get(EnumChatFormatting.func_175745_c(string));
    }

    public boolean isColor() {
        return !this.fancyStyling && this != RESET;
    }

    static {
        nameMapping = Maps.newHashMap();
        formattingCodePattern = Pattern.compile("(?i)" + String.valueOf('\u00a7') + "[0-9A-FK-OR]");
        EnumChatFormatting[] enumChatFormattingArray = EnumChatFormatting.values();
        int n = enumChatFormattingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumChatFormatting enumChatFormatting = enumChatFormattingArray[n2];
            nameMapping.put(EnumChatFormatting.func_175745_c(enumChatFormatting.name), enumChatFormatting);
            ++n2;
        }
    }

    public String getFriendlyName() {
        return this.name().toLowerCase();
    }

    public static EnumChatFormatting func_175744_a(int n) {
        if (n < 0) {
            return RESET;
        }
        EnumChatFormatting[] enumChatFormattingArray = EnumChatFormatting.values();
        int n2 = enumChatFormattingArray.length;
        int n3 = 0;
        while (n3 < n2) {
            EnumChatFormatting enumChatFormatting = enumChatFormattingArray[n3];
            if (enumChatFormatting.getColorIndex() == n) {
                return enumChatFormatting;
            }
            ++n3;
        }
        return null;
    }

    private EnumChatFormatting(String string2, char c, boolean bl) {
        this(string2, c, bl, -1);
    }

    public int getColorIndex() {
        return this.colorIndex;
    }

    private static String func_175745_c(String string) {
        return string.toLowerCase().replaceAll("[^a-z]", "");
    }

    private EnumChatFormatting(String string2, char c, boolean bl, int n2) {
        this.name = string2;
        this.formattingCode = c;
        this.fancyStyling = bl;
        this.colorIndex = n2;
        this.controlString = "\u00a7" + c;
    }
}

