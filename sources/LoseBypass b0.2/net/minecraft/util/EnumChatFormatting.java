/*
 * Decompiled with CFR 0.152.
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

    private static final Map<String, EnumChatFormatting> nameMapping;
    private static final Pattern formattingCodePattern;
    private final String name;
    private final char formattingCode;
    private final boolean fancyStyling;
    private final String controlString;
    private final int colorIndex;

    private static String func_175745_c(String p_175745_0_) {
        return p_175745_0_.toLowerCase().replaceAll("[^a-z]", "");
    }

    private EnumChatFormatting(String formattingName, char formattingCodeIn, int colorIndex) {
        this(formattingName, formattingCodeIn, false, colorIndex);
    }

    private EnumChatFormatting(String formattingName, char formattingCodeIn, boolean fancyStylingIn) {
        this(formattingName, formattingCodeIn, fancyStylingIn, -1);
    }

    private EnumChatFormatting(String formattingName, char formattingCodeIn, boolean fancyStylingIn, int colorIndex) {
        this.name = formattingName;
        this.formattingCode = formattingCodeIn;
        this.fancyStyling = fancyStylingIn;
        this.colorIndex = colorIndex;
        this.controlString = "\u00a7" + formattingCodeIn;
    }

    public int getColorIndex() {
        return this.colorIndex;
    }

    public boolean isFancyStyling() {
        return this.fancyStyling;
    }

    public boolean isColor() {
        if (this.fancyStyling) return false;
        if (this == RESET) return false;
        return true;
    }

    public String getFriendlyName() {
        return this.name().toLowerCase();
    }

    public String toString() {
        return this.controlString;
    }

    public static String getTextWithoutFormattingCodes(String text) {
        if (text == null) {
            return null;
        }
        String string = formattingCodePattern.matcher(text).replaceAll("");
        return string;
    }

    public static EnumChatFormatting getValueByName(String friendlyName) {
        if (friendlyName == null) {
            return null;
        }
        EnumChatFormatting enumChatFormatting = nameMapping.get(EnumChatFormatting.func_175745_c(friendlyName));
        return enumChatFormatting;
    }

    public static EnumChatFormatting func_175744_a(int p_175744_0_) {
        if (p_175744_0_ < 0) {
            return RESET;
        }
        EnumChatFormatting[] enumChatFormattingArray = EnumChatFormatting.values();
        int n = enumChatFormattingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumChatFormatting enumchatformatting = enumChatFormattingArray[n2];
            if (enumchatformatting.getColorIndex() == p_175744_0_) {
                return enumchatformatting;
            }
            ++n2;
        }
        return null;
    }

    public static Collection<String> getValidValues(boolean p_96296_0_, boolean p_96296_1_) {
        ArrayList<String> list = Lists.newArrayList();
        EnumChatFormatting[] enumChatFormattingArray = EnumChatFormatting.values();
        int n = enumChatFormattingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumChatFormatting enumchatformatting = enumChatFormattingArray[n2];
            if (!(enumchatformatting.isColor() && !p_96296_0_ || enumchatformatting.isFancyStyling() && !p_96296_1_)) {
                list.add(enumchatformatting.getFriendlyName());
            }
            ++n2;
        }
        return list;
    }

    static {
        nameMapping = Maps.newHashMap();
        formattingCodePattern = Pattern.compile("(?i)" + String.valueOf('\u00a7') + "[0-9A-FK-OR]");
        EnumChatFormatting[] enumChatFormattingArray = EnumChatFormatting.values();
        int n = enumChatFormattingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumChatFormatting enumchatformatting = enumChatFormattingArray[n2];
            nameMapping.put(EnumChatFormatting.func_175745_c(enumchatformatting.name), enumchatformatting);
            ++n2;
        }
    }
}

