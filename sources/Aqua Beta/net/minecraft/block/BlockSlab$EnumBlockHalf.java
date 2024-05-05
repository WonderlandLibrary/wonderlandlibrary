package net.minecraft.block;

import net.minecraft.util.IStringSerializable;

public static enum BlockSlab.EnumBlockHalf implements IStringSerializable
{
    TOP("top"),
    BOTTOM("bottom");

    private final String name;

    private BlockSlab.EnumBlockHalf(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }
}
