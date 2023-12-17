// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.IStringSerializable;

public enum EnumDyeColor implements IStringSerializable
{
    WHITE(0, 15, "white", "white", 16383998, TextFormatting.WHITE), 
    ORANGE(1, 14, "orange", "orange", 16351261, TextFormatting.GOLD), 
    MAGENTA(2, 13, "magenta", "magenta", 13061821, TextFormatting.AQUA), 
    LIGHT_BLUE(3, 12, "light_blue", "lightBlue", 3847130, TextFormatting.BLUE), 
    YELLOW(4, 11, "yellow", "yellow", 16701501, TextFormatting.YELLOW), 
    LIME(5, 10, "lime", "lime", 8439583, TextFormatting.GREEN), 
    PINK(6, 9, "pink", "pink", 15961002, TextFormatting.LIGHT_PURPLE), 
    GRAY(7, 8, "gray", "gray", 4673362, TextFormatting.DARK_GRAY), 
    SILVER(8, 7, "silver", "silver", 10329495, TextFormatting.GRAY), 
    CYAN(9, 6, "cyan", "cyan", 1481884, TextFormatting.DARK_AQUA), 
    PURPLE(10, 5, "purple", "purple", 8991416, TextFormatting.DARK_PURPLE), 
    BLUE(11, 4, "blue", "blue", 3949738, TextFormatting.DARK_BLUE), 
    BROWN(12, 3, "brown", "brown", 8606770, TextFormatting.GOLD), 
    GREEN(13, 2, "green", "green", 6192150, TextFormatting.DARK_GREEN), 
    RED(14, 1, "red", "red", 11546150, TextFormatting.DARK_RED), 
    BLACK(15, 0, "black", "black", 1908001, TextFormatting.BLACK);
    
    private static final EnumDyeColor[] META_LOOKUP;
    private static final EnumDyeColor[] DYE_DMG_LOOKUP;
    private final int meta;
    private final int dyeDamage;
    private final String name;
    private final String translationKey;
    private final int colorValue;
    private final float[] colorComponentValues;
    private final TextFormatting chatColor;
    
    private EnumDyeColor(final int metaIn, final int dyeDamageIn, final String nameIn, final String unlocalizedNameIn, final int colorValueIn, final TextFormatting chatColorIn) {
        this.meta = metaIn;
        this.dyeDamage = dyeDamageIn;
        this.name = nameIn;
        this.translationKey = unlocalizedNameIn;
        this.colorValue = colorValueIn;
        this.chatColor = chatColorIn;
        final int i = (colorValueIn & 0xFF0000) >> 16;
        final int j = (colorValueIn & 0xFF00) >> 8;
        final int k = (colorValueIn & 0xFF) >> 0;
        this.colorComponentValues = new float[] { i / 255.0f, j / 255.0f, k / 255.0f };
    }
    
    public int getMetadata() {
        return this.meta;
    }
    
    public int getDyeDamage() {
        return this.dyeDamage;
    }
    
    public String getDyeColorName() {
        return this.name;
    }
    
    public String getTranslationKey() {
        return this.translationKey;
    }
    
    public int getColorValue() {
        return this.colorValue;
    }
    
    public float[] getColorComponentValues() {
        return this.colorComponentValues;
    }
    
    public static EnumDyeColor byDyeDamage(int damage) {
        if (damage < 0 || damage >= EnumDyeColor.DYE_DMG_LOOKUP.length) {
            damage = 0;
        }
        return EnumDyeColor.DYE_DMG_LOOKUP[damage];
    }
    
    public static EnumDyeColor byMetadata(int meta) {
        if (meta < 0 || meta >= EnumDyeColor.META_LOOKUP.length) {
            meta = 0;
        }
        return EnumDyeColor.META_LOOKUP[meta];
    }
    
    @Override
    public String toString() {
        return this.translationKey;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    static {
        META_LOOKUP = new EnumDyeColor[values().length];
        DYE_DMG_LOOKUP = new EnumDyeColor[values().length];
        for (final EnumDyeColor enumdyecolor : values()) {
            EnumDyeColor.META_LOOKUP[enumdyecolor.getMetadata()] = enumdyecolor;
            EnumDyeColor.DYE_DMG_LOOKUP[enumdyecolor.getDyeDamage()] = enumdyecolor;
        }
    }
}
