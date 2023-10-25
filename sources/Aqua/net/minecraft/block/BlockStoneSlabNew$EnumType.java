package net.minecraft.block;

import net.minecraft.block.BlockSand;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.IStringSerializable;

public static enum BlockStoneSlabNew.EnumType implements IStringSerializable
{
    RED_SANDSTONE(0, "red_sandstone", BlockSand.EnumType.RED_SAND.getMapColor());

    private static final BlockStoneSlabNew.EnumType[] META_LOOKUP;
    private final int meta;
    private final String name;
    private final MapColor field_181069_e;

    private BlockStoneSlabNew.EnumType(int p_i46391_3_, String p_i46391_4_, MapColor p_i46391_5_) {
        this.meta = p_i46391_3_;
        this.name = p_i46391_4_;
        this.field_181069_e = p_i46391_5_;
    }

    public int getMetadata() {
        return this.meta;
    }

    public MapColor func_181068_c() {
        return this.field_181069_e;
    }

    public String toString() {
        return this.name;
    }

    public static BlockStoneSlabNew.EnumType byMetadata(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }
        return META_LOOKUP[meta];
    }

    public String getName() {
        return this.name;
    }

    public String getUnlocalizedName() {
        return this.name;
    }

    static {
        META_LOOKUP = new BlockStoneSlabNew.EnumType[BlockStoneSlabNew.EnumType.values().length];
        BlockStoneSlabNew.EnumType[] enumTypeArray = BlockStoneSlabNew.EnumType.values();
        int n = enumTypeArray.length;
        for (int i = 0; i < n; ++i) {
            BlockStoneSlabNew.EnumType blockstoneslabnew$enumtype;
            BlockStoneSlabNew.EnumType.META_LOOKUP[blockstoneslabnew$enumtype.getMetadata()] = blockstoneslabnew$enumtype = enumTypeArray[i];
        }
    }
}
