package net.minecraft.block;

import net.minecraft.util.IStringSerializable;

public static enum BlockRedSandstone.EnumType implements IStringSerializable
{
    DEFAULT(0, "red_sandstone", "default"),
    CHISELED(1, "chiseled_red_sandstone", "chiseled"),
    SMOOTH(2, "smooth_red_sandstone", "smooth");

    private static final BlockRedSandstone.EnumType[] META_LOOKUP;
    private final int meta;
    private final String name;
    private final String unlocalizedName;

    private BlockRedSandstone.EnumType(int meta, String name, String unlocalizedName) {
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

    public static BlockRedSandstone.EnumType byMetadata(int meta) {
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
        META_LOOKUP = new BlockRedSandstone.EnumType[BlockRedSandstone.EnumType.values().length];
        BlockRedSandstone.EnumType[] enumTypeArray = BlockRedSandstone.EnumType.values();
        int n = enumTypeArray.length;
        for (int i = 0; i < n; ++i) {
            BlockRedSandstone.EnumType blockredsandstone$enumtype;
            BlockRedSandstone.EnumType.META_LOOKUP[blockredsandstone$enumtype.getMetadata()] = blockredsandstone$enumtype = enumTypeArray[i];
        }
    }
}
