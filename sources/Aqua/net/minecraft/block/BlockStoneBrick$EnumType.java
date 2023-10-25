package net.minecraft.block;

import net.minecraft.util.IStringSerializable;

public static enum BlockStoneBrick.EnumType implements IStringSerializable
{
    DEFAULT(0, "stonebrick", "default"),
    MOSSY(1, "mossy_stonebrick", "mossy"),
    CRACKED(2, "cracked_stonebrick", "cracked"),
    CHISELED(3, "chiseled_stonebrick", "chiseled");

    private static final BlockStoneBrick.EnumType[] META_LOOKUP;
    private final int meta;
    private final String name;
    private final String unlocalizedName;

    private BlockStoneBrick.EnumType(int meta, String name, String unlocalizedName) {
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

    public static BlockStoneBrick.EnumType byMetadata(int meta) {
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
        META_LOOKUP = new BlockStoneBrick.EnumType[BlockStoneBrick.EnumType.values().length];
        BlockStoneBrick.EnumType[] enumTypeArray = BlockStoneBrick.EnumType.values();
        int n = enumTypeArray.length;
        for (int i = 0; i < n; ++i) {
            BlockStoneBrick.EnumType blockstonebrick$enumtype;
            BlockStoneBrick.EnumType.META_LOOKUP[blockstonebrick$enumtype.getMetadata()] = blockstonebrick$enumtype = enumTypeArray[i];
        }
    }
}
