package net.minecraft.block;

import net.minecraft.block.BlockLog;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

public static enum BlockLog.EnumAxis implements IStringSerializable
{
    X("x"),
    Y("y"),
    Z("z"),
    NONE("none");

    private final String name;

    private BlockLog.EnumAxis(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public static BlockLog.EnumAxis fromFacingAxis(EnumFacing.Axis axis) {
        switch (BlockLog.1.$SwitchMap$net$minecraft$util$EnumFacing$Axis[axis.ordinal()]) {
            case 1: {
                return X;
            }
            case 2: {
                return Y;
            }
            case 3: {
                return Z;
            }
        }
        return NONE;
    }

    public String getName() {
        return this.name;
    }
}
