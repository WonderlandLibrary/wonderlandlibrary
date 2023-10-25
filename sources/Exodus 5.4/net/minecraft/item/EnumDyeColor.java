/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.block.material.MapColor;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IStringSerializable;

public enum EnumDyeColor implements IStringSerializable
{
    WHITE(0, 15, "white", "white", MapColor.snowColor, EnumChatFormatting.WHITE),
    ORANGE(1, 14, "orange", "orange", MapColor.adobeColor, EnumChatFormatting.GOLD),
    MAGENTA(2, 13, "magenta", "magenta", MapColor.magentaColor, EnumChatFormatting.AQUA),
    LIGHT_BLUE(3, 12, "light_blue", "lightBlue", MapColor.lightBlueColor, EnumChatFormatting.BLUE),
    YELLOW(4, 11, "yellow", "yellow", MapColor.yellowColor, EnumChatFormatting.YELLOW),
    LIME(5, 10, "lime", "lime", MapColor.limeColor, EnumChatFormatting.GREEN),
    PINK(6, 9, "pink", "pink", MapColor.pinkColor, EnumChatFormatting.LIGHT_PURPLE),
    GRAY(7, 8, "gray", "gray", MapColor.grayColor, EnumChatFormatting.DARK_GRAY),
    SILVER(8, 7, "silver", "silver", MapColor.silverColor, EnumChatFormatting.GRAY),
    CYAN(9, 6, "cyan", "cyan", MapColor.cyanColor, EnumChatFormatting.DARK_AQUA),
    PURPLE(10, 5, "purple", "purple", MapColor.purpleColor, EnumChatFormatting.DARK_PURPLE),
    BLUE(11, 4, "blue", "blue", MapColor.blueColor, EnumChatFormatting.DARK_BLUE),
    BROWN(12, 3, "brown", "brown", MapColor.brownColor, EnumChatFormatting.GOLD),
    GREEN(13, 2, "green", "green", MapColor.greenColor, EnumChatFormatting.DARK_GREEN),
    RED(14, 1, "red", "red", MapColor.redColor, EnumChatFormatting.DARK_RED),
    BLACK(15, 0, "black", "black", MapColor.blackColor, EnumChatFormatting.BLACK);

    private final String unlocalizedName;
    private final String name;
    private final int dyeDamage;
    private final EnumChatFormatting chatColor;
    private final int meta;
    private static final EnumDyeColor[] DYE_DMG_LOOKUP;
    private final MapColor mapColor;
    private static final EnumDyeColor[] META_LOOKUP;

    @Override
    public String getName() {
        return this.name;
    }

    static {
        META_LOOKUP = new EnumDyeColor[EnumDyeColor.values().length];
        DYE_DMG_LOOKUP = new EnumDyeColor[EnumDyeColor.values().length];
        EnumDyeColor[] enumDyeColorArray = EnumDyeColor.values();
        int n = enumDyeColorArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumDyeColor enumDyeColor;
            EnumDyeColor.META_LOOKUP[enumDyeColor.getMetadata()] = enumDyeColor = enumDyeColorArray[n2];
            EnumDyeColor.DYE_DMG_LOOKUP[enumDyeColor.getDyeDamage()] = enumDyeColor;
            ++n2;
        }
    }

    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    public String toString() {
        return this.unlocalizedName;
    }

    public static EnumDyeColor byMetadata(int n) {
        if (n < 0 || n >= META_LOOKUP.length) {
            n = 0;
        }
        return META_LOOKUP[n];
    }

    public int getDyeDamage() {
        return this.dyeDamage;
    }

    public static EnumDyeColor byDyeDamage(int n) {
        if (n < 0 || n >= DYE_DMG_LOOKUP.length) {
            n = 0;
        }
        return DYE_DMG_LOOKUP[n];
    }

    private EnumDyeColor(int n2, int n3, String string2, String string3, MapColor mapColor, EnumChatFormatting enumChatFormatting) {
        this.meta = n2;
        this.dyeDamage = n3;
        this.name = string2;
        this.unlocalizedName = string3;
        this.mapColor = mapColor;
        this.chatColor = enumChatFormatting;
    }

    public int getMetadata() {
        return this.meta;
    }

    public MapColor getMapColor() {
        return this.mapColor;
    }
}

