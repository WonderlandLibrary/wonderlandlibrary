package net.minecraft.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

public static enum BlockSilverfish.EnumType implements IStringSerializable
{
    STONE/* Unavailable Anonymous Inner Class!! */,
    COBBLESTONE/* Unavailable Anonymous Inner Class!! */,
    STONEBRICK/* Unavailable Anonymous Inner Class!! */,
    MOSSY_STONEBRICK/* Unavailable Anonymous Inner Class!! */,
    CRACKED_STONEBRICK/* Unavailable Anonymous Inner Class!! */,
    CHISELED_STONEBRICK/* Unavailable Anonymous Inner Class!! */;

    private static final BlockSilverfish.EnumType[] META_LOOKUP;
    private final int meta;
    private final String name;
    private final String unlocalizedName;

    private BlockSilverfish.EnumType(int meta, String name) {
        this(meta, name, name);
    }

    private BlockSilverfish.EnumType(int meta, String name, String unlocalizedName) {
        this.meta = meta;
        this.name = name;
        this.unlocalizedName = unlocalizedName;
    }

    public int getMetadata() {
        return this.meta;
    }

    public String toString() {
        return this.name;
    }

    public static BlockSilverfish.EnumType byMetadata(int meta) {
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

    public abstract IBlockState getModelBlock();

    public static BlockSilverfish.EnumType forModelBlock(IBlockState model) {
        for (BlockSilverfish.EnumType blocksilverfish$enumtype : BlockSilverfish.EnumType.values()) {
            if (model != blocksilverfish$enumtype.getModelBlock()) continue;
            return blocksilverfish$enumtype;
        }
        return STONE;
    }

    static {
        META_LOOKUP = new BlockSilverfish.EnumType[BlockSilverfish.EnumType.values().length];
        BlockSilverfish.EnumType[] enumTypeArray = BlockSilverfish.EnumType.values();
        int n = enumTypeArray.length;
        for (int i = 0; i < n; ++i) {
            BlockSilverfish.EnumType blocksilverfish$enumtype;
            BlockSilverfish.EnumType.META_LOOKUP[blocksilverfish$enumtype.getMetadata()] = blocksilverfish$enumtype = enumTypeArray[i];
        }
    }
}
