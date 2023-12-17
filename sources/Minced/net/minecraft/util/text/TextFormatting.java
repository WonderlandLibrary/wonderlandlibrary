// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.text;

import com.google.common.collect.Maps;
import java.util.List;
import com.google.common.collect.Lists;
import java.util.Collection;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.Map;

public enum TextFormatting
{
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
    
    private static final Map<String, TextFormatting> NAME_MAPPING;
    private static final Pattern FORMATTING_CODE_PATTERN;
    private final String name;
    private final char formattingCode;
    private final boolean fancyStyling;
    private final String controlString;
    private final int colorIndex;
    
    private static String lowercaseAlpha(final String p_175745_0_) {
        return p_175745_0_.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
    }
    
    private TextFormatting(final String formattingName, final char formattingCodeIn, final int colorIndex) {
        this(formattingName, formattingCodeIn, false, colorIndex);
    }
    
    private TextFormatting(final String formattingName, final char formattingCodeIn, final boolean fancyStylingIn) {
        this(formattingName, formattingCodeIn, fancyStylingIn, -1);
    }
    
    private TextFormatting(final String formattingName, final char formattingCodeIn, final boolean fancyStylingIn, final int colorIndex) {
        this.name = formattingName;
        this.formattingCode = formattingCodeIn;
        this.fancyStyling = fancyStylingIn;
        this.colorIndex = colorIndex;
        this.controlString = "§" + formattingCodeIn;
    }
    
    public int getColorIndex() {
        return this.colorIndex;
    }
    
    public boolean isFancyStyling() {
        return this.fancyStyling;
    }
    
    public boolean isColor() {
        return !this.fancyStyling && this != TextFormatting.RESET;
    }
    
    public String getFriendlyName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
    
    @Override
    public String toString() {
        return this.controlString;
    }
    
    @Nullable
    public static String getTextWithoutFormattingCodes(@Nullable final String text) {
        return (text == null) ? null : TextFormatting.FORMATTING_CODE_PATTERN.matcher(text).replaceAll("");
    }
    
    @Nullable
    public static TextFormatting getValueByName(@Nullable final String friendlyName) {
        return (friendlyName == null) ? null : TextFormatting.NAME_MAPPING.get(lowercaseAlpha(friendlyName));
    }
    
    @Nullable
    public static TextFormatting fromColorIndex(final int index) {
        if (index < 0) {
            return TextFormatting.RESET;
        }
        for (final TextFormatting textformatting : values()) {
            if (textformatting.getColorIndex() == index) {
                return textformatting;
            }
        }
        return null;
    }
    
    public static Collection<String> getValidValues(final boolean p_96296_0_, final boolean p_96296_1_) {
        final List<String> list = (List<String>)Lists.newArrayList();
        for (final TextFormatting textformatting : values()) {
            if ((!textformatting.isColor() || p_96296_0_) && (!textformatting.isFancyStyling() || p_96296_1_)) {
                list.add(textformatting.getFriendlyName());
            }
        }
        return list;
    }
    
    static {
        NAME_MAPPING = Maps.newHashMap();
        FORMATTING_CODE_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
        for (final TextFormatting textformatting : values()) {
            TextFormatting.NAME_MAPPING.put(lowercaseAlpha(textformatting.name), textformatting);
        }
    }
}
