package net.minecraft.block;

import net.minecraft.util.IStringSerializable;

public static enum BlockRailBase.EnumRailDirection implements IStringSerializable
{
    NORTH_SOUTH(0, "north_south"),
    EAST_WEST(1, "east_west"),
    ASCENDING_EAST(2, "ascending_east"),
    ASCENDING_WEST(3, "ascending_west"),
    ASCENDING_NORTH(4, "ascending_north"),
    ASCENDING_SOUTH(5, "ascending_south"),
    SOUTH_EAST(6, "south_east"),
    SOUTH_WEST(7, "south_west"),
    NORTH_WEST(8, "north_west"),
    NORTH_EAST(9, "north_east");

    private static final BlockRailBase.EnumRailDirection[] META_LOOKUP;
    private final int meta;
    private final String name;

    private BlockRailBase.EnumRailDirection(int meta, String name) {
        this.meta = meta;
        this.name = name;
    }

    public int getMetadata() {
        return this.meta;
    }

    public String toString() {
        return this.name;
    }

    public boolean isAscending() {
        return this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST;
    }

    public static BlockRailBase.EnumRailDirection byMetadata(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }
        return META_LOOKUP[meta];
    }

    public String getName() {
        return this.name;
    }

    static {
        META_LOOKUP = new BlockRailBase.EnumRailDirection[BlockRailBase.EnumRailDirection.values().length];
        BlockRailBase.EnumRailDirection[] enumRailDirectionArray = BlockRailBase.EnumRailDirection.values();
        int n = enumRailDirectionArray.length;
        for (int i = 0; i < n; ++i) {
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection;
            BlockRailBase.EnumRailDirection.META_LOOKUP[blockrailbase$enumraildirection.getMetadata()] = blockrailbase$enumraildirection = enumRailDirectionArray[i];
        }
    }
}
