package net.minecraft.block;

import net.minecraft.util.IStringSerializable;

public static enum BlockSandStone.EnumType implements IStringSerializable
{
    DEFAULT(0, "sandstone", "default"),
    CHISELED(1, "chiseled_sandstone", "chiseled"),
    SMOOTH(2, "smooth_sandstone", "smooth");

    private static final BlockSandStone.EnumType[] META_LOOKUP;
    private final int metadata;
    private final String name;
    private final String unlocalizedName;

    private BlockSandStone.EnumType(int meta, String name, String unlocalizedName) {
        this.metadata = meta;
        this.name = name;
        this.unlocalizedName = unlocalizedName;
    }

    public int getMetadata() {
        return this.metadata;
    }

    public String toString() {
        return this.name;
    }

    public static BlockSandStone.EnumType byMetadata(int meta) {
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
        META_LOOKUP = new BlockSandStone.EnumType[BlockSandStone.EnumType.values().length];
        BlockSandStone.EnumType[] enumTypeArray = BlockSandStone.EnumType.values();
        int n = enumTypeArray.length;
        for (int i = 0; i < n; ++i) {
            BlockSandStone.EnumType blocksandstone$enumtype;
            BlockSandStone.EnumType.META_LOOKUP[blocksandstone$enumtype.getMetadata()] = blocksandstone$enumtype = enumTypeArray[i];
        }
    }
}
