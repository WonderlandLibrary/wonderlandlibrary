package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.util.IStringSerializable;

public static enum BlockPlanks.EnumType implements IStringSerializable
{
    OAK(0, "oak", MapColor.woodColor),
    SPRUCE(1, "spruce", MapColor.obsidianColor),
    BIRCH(2, "birch", MapColor.sandColor),
    JUNGLE(3, "jungle", MapColor.dirtColor),
    ACACIA(4, "acacia", MapColor.adobeColor),
    DARK_OAK(5, "dark_oak", "big_oak", MapColor.brownColor);

    private static final BlockPlanks.EnumType[] META_LOOKUP;
    private final int meta;
    private final String name;
    private final String unlocalizedName;
    private final MapColor mapColor;

    private BlockPlanks.EnumType(int p_i46388_3_, String p_i46388_4_, MapColor p_i46388_5_) {
        this(p_i46388_3_, p_i46388_4_, p_i46388_4_, p_i46388_5_);
    }

    private BlockPlanks.EnumType(int p_i46389_3_, String p_i46389_4_, String p_i46389_5_, MapColor p_i46389_6_) {
        this.meta = p_i46389_3_;
        this.name = p_i46389_4_;
        this.unlocalizedName = p_i46389_5_;
        this.mapColor = p_i46389_6_;
    }

    public int getMetadata() {
        return this.meta;
    }

    public MapColor getMapColor() {
        return this.mapColor;
    }

    public String toString() {
        return this.name;
    }

    public static BlockPlanks.EnumType byMetadata(int meta) {
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
        META_LOOKUP = new BlockPlanks.EnumType[BlockPlanks.EnumType.values().length];
        BlockPlanks.EnumType[] enumTypeArray = BlockPlanks.EnumType.values();
        int n = enumTypeArray.length;
        for (int i = 0; i < n; ++i) {
            BlockPlanks.EnumType blockplanks$enumtype;
            BlockPlanks.EnumType.META_LOOKUP[blockplanks$enumtype.getMetadata()] = blockplanks$enumtype = enumTypeArray[i];
        }
    }
}
