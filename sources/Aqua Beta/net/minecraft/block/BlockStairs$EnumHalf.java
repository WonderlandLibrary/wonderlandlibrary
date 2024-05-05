package net.minecraft.block;

import net.minecraft.util.IStringSerializable;

public static enum BlockStairs.EnumHalf implements IStringSerializable
{
    TOP("top"),
    BOTTOM("bottom");

    private final String name;

    private BlockStairs.EnumHalf(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }
}
