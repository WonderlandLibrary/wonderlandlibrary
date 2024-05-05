package net.minecraft.block;

import net.minecraft.util.IStringSerializable;

public static enum BlockDoublePlant.EnumBlockHalf implements IStringSerializable
{
    UPPER,
    LOWER;


    public String toString() {
        return this.getName();
    }

    public String getName() {
        return this == UPPER ? "upper" : "lower";
    }
}
