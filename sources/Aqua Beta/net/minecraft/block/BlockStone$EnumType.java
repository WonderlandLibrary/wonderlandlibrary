package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.util.IStringSerializable;

public static enum BlockStone.EnumType implements IStringSerializable
{
    STONE(0, MapColor.stoneColor, "stone"),
    GRANITE(1, MapColor.dirtColor, "granite"),
    GRANITE_SMOOTH(2, MapColor.dirtColor, "smooth_granite", "graniteSmooth"),
    DIORITE(3, MapColor.quartzColor, "diorite"),
    DIORITE_SMOOTH(4, MapColor.quartzColor, "smooth_diorite", "dioriteSmooth"),
    ANDESITE(5, MapColor.stoneColor, "andesite"),
    ANDESITE_SMOOTH(6, MapColor.stoneColor, "smooth_andesite", "andesiteSmooth");

    private static final BlockStone.EnumType[] META_LOOKUP;
    private final int meta;
    private final String name;
    private final String unlocalizedName;
    private final MapColor field_181073_l;

    private BlockStone.EnumType(int p_i46383_3_, MapColor p_i46383_4_, String p_i46383_5_) {
        this(p_i46383_3_, p_i46383_4_, p_i46383_5_, p_i46383_5_);
    }

    private BlockStone.EnumType(int p_i46384_3_, MapColor p_i46384_4_, String p_i46384_5_, String p_i46384_6_) {
        this.meta = p_i46384_3_;
        this.name = p_i46384_5_;
        this.unlocalizedName = p_i46384_6_;
        this.field_181073_l = p_i46384_4_;
    }

    public int getMetadata() {
        return this.meta;
    }

    public MapColor func_181072_c() {
        return this.field_181073_l;
    }

    public String toString() {
        return this.name;
    }

    public static BlockStone.EnumType byMetadata(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }
        return META_LOOKUP[meta];
    }

    public String getName() {
        return this.name;
    }

    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    static {
        META_LOOKUP = new BlockStone.EnumType[BlockStone.EnumType.values().length];
        BlockStone.EnumType[] enumTypeArray = BlockStone.EnumType.values();
        int n = enumTypeArray.length;
        for (int i = 0; i < n; ++i) {
            BlockStone.EnumType blockstone$enumtype;
            BlockStone.EnumType.META_LOOKUP[blockstone$enumtype.getMetadata()] = blockstone$enumtype = enumTypeArray[i];
        }
    }
}
