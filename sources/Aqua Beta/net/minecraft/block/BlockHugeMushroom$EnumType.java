package net.minecraft.block;

import net.minecraft.util.IStringSerializable;

public static enum BlockHugeMushroom.EnumType implements IStringSerializable
{
    NORTH_WEST(1, "north_west"),
    NORTH(2, "north"),
    NORTH_EAST(3, "north_east"),
    WEST(4, "west"),
    CENTER(5, "center"),
    EAST(6, "east"),
    SOUTH_WEST(7, "south_west"),
    SOUTH(8, "south"),
    SOUTH_EAST(9, "south_east"),
    STEM(10, "stem"),
    ALL_INSIDE(0, "all_inside"),
    ALL_OUTSIDE(14, "all_outside"),
    ALL_STEM(15, "all_stem");

    private static final BlockHugeMushroom.EnumType[] META_LOOKUP;
    private final int meta;
    private final String name;

    private BlockHugeMushroom.EnumType(int meta, String name) {
        this.meta = meta;
        this.name = name;
    }

    public int getMetadata() {
        return this.meta;
    }

    public String toString() {
        return this.name;
    }

    public static BlockHugeMushroom.EnumType byMetadata(int meta) {
        BlockHugeMushroom.EnumType blockhugemushroom$enumtype;
        if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }
        return (blockhugemushroom$enumtype = META_LOOKUP[meta]) == null ? META_LOOKUP[0] : blockhugemushroom$enumtype;
    }

    public String getName() {
        return this.name;
    }

    static {
        META_LOOKUP = new BlockHugeMushroom.EnumType[16];
        BlockHugeMushroom.EnumType[] enumTypeArray = BlockHugeMushroom.EnumType.values();
        int n = enumTypeArray.length;
        for (int i = 0; i < n; ++i) {
            BlockHugeMushroom.EnumType blockhugemushroom$enumtype;
            BlockHugeMushroom.EnumType.META_LOOKUP[blockhugemushroom$enumtype.getMetadata()] = blockhugemushroom$enumtype = enumTypeArray[i];
        }
    }
}
