// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.Map;

public enum EnumChatFormatting
{
    BLACK("BLACK", 0, "BLACK", '0', 0), 
    DARK_BLUE("DARK_BLUE", 1, "DARK_BLUE", '1', 1), 
    DARK_GREEN("DARK_GREEN", 2, "DARK_GREEN", '2', 2), 
    DARK_AQUA("DARK_AQUA", 3, "DARK_AQUA", '3', 3), 
    DARK_RED("DARK_RED", 4, "DARK_RED", '4', 4), 
    DARK_PURPLE("DARK_PURPLE", 5, "DARK_PURPLE", '5', 5), 
    GOLD("GOLD", 6, "GOLD", '6', 6), 
    GRAY("GRAY", 7, "GRAY", '7', 7), 
    DARK_GRAY("DARK_GRAY", 8, "DARK_GRAY", '8', 8), 
    BLUE("BLUE", 9, "BLUE", '9', 9), 
    GREEN("GREEN", 10, "GREEN", 'a', 10), 
    AQUA("AQUA", 11, "AQUA", 'b', 11), 
    RED("RED", 12, "RED", 'c', 12), 
    LIGHT_PURPLE("LIGHT_PURPLE", 13, "LIGHT_PURPLE", 'd', 13), 
    YELLOW("YELLOW", 14, "YELLOW", 'e', 14), 
    WHITE("WHITE", 15, "WHITE", 'f', 15), 
    OBFUSCATED("OBFUSCATED", 16, "OBFUSCATED", 'k', true), 
    BOLD("BOLD", 17, "BOLD", 'l', true), 
    STRIKETHROUGH("STRIKETHROUGH", 18, "STRIKETHROUGH", 'm', true), 
    UNDERLINE("UNDERLINE", 19, "UNDERLINE", 'n', true), 
    ITALIC("ITALIC", 20, "ITALIC", 'o', true), 
    RESET("RESET", 21, "RESET", 'r', -1);
    
    private static final Map nameMapping;
    private static final Pattern formattingCodePattern;
    private final String field_175748_y;
    private final char formattingCode;
    private final boolean fancyStyling;
    private final String controlString;
    private final int field_175747_C;
    private static final EnumChatFormatting[] $VALUES;
    private static final String __OBFID = "CL_00000342";
    
    private static String func_175745_c(final String p_175745_0_) {
        return p_175745_0_.toLowerCase().replaceAll("[^a-z]", "");
    }
    
    private EnumChatFormatting(final String p_i46291_1_, final int p_i46291_2_, final String p_i46291_3_, final char p_i46291_4_, final int p_i46291_5_) {
        this(p_i46291_1_, p_i46291_2_, p_i46291_3_, p_i46291_4_, false, p_i46291_5_);
    }
    
    private EnumChatFormatting(final String p_i46292_1_, final int p_i46292_2_, final String p_i46292_3_, final char p_i46292_4_, final boolean p_i46292_5_) {
        this(p_i46292_1_, p_i46292_2_, p_i46292_3_, p_i46292_4_, p_i46292_5_, -1);
    }
    
    private EnumChatFormatting(final String p_i46293_1_, final int p_i46293_2_, final String p_i46293_3_, final char p_i46293_4_, final boolean p_i46293_5_, final int p_i46293_6_) {
        this.field_175748_y = p_i46293_3_;
        this.formattingCode = p_i46293_4_;
        this.fancyStyling = p_i46293_5_;
        this.field_175747_C = p_i46293_6_;
        this.controlString = "§" + p_i46293_4_;
    }
    
    public int func_175746_b() {
        return this.field_175747_C;
    }
    
    public boolean isFancyStyling() {
        return this.fancyStyling;
    }
    
    public boolean isColor() {
        return !this.fancyStyling && this != EnumChatFormatting.RESET;
    }
    
    public String getFriendlyName() {
        return this.name().toLowerCase();
    }
    
    @Override
    public String toString() {
        return this.controlString;
    }
    
    public static String getTextWithoutFormattingCodes(final String p_110646_0_) {
        return (p_110646_0_ == null) ? null : EnumChatFormatting.formattingCodePattern.matcher(p_110646_0_).replaceAll("");
    }
    
    public static EnumChatFormatting getValueByName(final String p_96300_0_) {
        return (p_96300_0_ == null) ? null : EnumChatFormatting.nameMapping.get(func_175745_c(p_96300_0_));
    }
    
    public static EnumChatFormatting func_175744_a(final int p_175744_0_) {
        if (p_175744_0_ < 0) {
            return EnumChatFormatting.RESET;
        }
        for (final EnumChatFormatting var4 : values()) {
            if (var4.func_175746_b() == p_175744_0_) {
                return var4;
            }
        }
        return null;
    }
    
    public static Collection getValidValues(final boolean p_96296_0_, final boolean p_96296_1_) {
        final ArrayList var2 = Lists.newArrayList();
        for (final EnumChatFormatting var6 : values()) {
            if ((!var6.isColor() || p_96296_0_) && (!var6.isFancyStyling() || p_96296_1_)) {
                var2.add(var6.getFriendlyName());
            }
        }
        return var2;
    }
    
    static {
        nameMapping = Maps.newHashMap();
        formattingCodePattern = Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-OR]");
        $VALUES = new EnumChatFormatting[] { EnumChatFormatting.BLACK, EnumChatFormatting.DARK_BLUE, EnumChatFormatting.DARK_GREEN, EnumChatFormatting.DARK_AQUA, EnumChatFormatting.DARK_RED, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.GOLD, EnumChatFormatting.GRAY, EnumChatFormatting.DARK_GRAY, EnumChatFormatting.BLUE, EnumChatFormatting.GREEN, EnumChatFormatting.AQUA, EnumChatFormatting.RED, EnumChatFormatting.LIGHT_PURPLE, EnumChatFormatting.YELLOW, EnumChatFormatting.WHITE, EnumChatFormatting.OBFUSCATED, EnumChatFormatting.BOLD, EnumChatFormatting.STRIKETHROUGH, EnumChatFormatting.UNDERLINE, EnumChatFormatting.ITALIC, EnumChatFormatting.RESET };
        for (final EnumChatFormatting var4 : values()) {
            EnumChatFormatting.nameMapping.put(func_175745_c(var4.field_175748_y), var4);
        }
    }
}
