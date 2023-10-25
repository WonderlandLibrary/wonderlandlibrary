package net.minecraft.block;

import net.minecraft.util.IStringSerializable;

public static enum BlockWall.EnumType implements IStringSerializable
{
    NORMAL(0, "cobblestone", "normal"),
    MOSSY(1, "mossy_cobblestone", "mossy");

    private static final BlockWall.EnumType[] META_LOOKUP;
    private final int meta;
    private final String name;
    private String unlocalizedName;

    private BlockWall.EnumType(int meta, String name, String unlocalizedName) {
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

    public static BlockWall.EnumType byMetadata(int meta) {
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
        META_LOOKUP = new BlockWall.EnumType[BlockWall.EnumType.values().length];
        BlockWall.EnumType[] enumTypeArray = BlockWall.EnumType.values();
        int n = enumTypeArray.length;
        for (int i = 0; i < n; ++i) {
            BlockWall.EnumType blockwall$enumtype;
            BlockWall.EnumType.META_LOOKUP[blockwall$enumtype.getMetadata()] = blockwall$enumtype = enumTypeArray[i];
        }
    }
}
