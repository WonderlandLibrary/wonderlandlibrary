/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;

public enum Mirror {
    NONE("no_mirror"),
    LEFT_RIGHT("mirror_left_right"),
    FRONT_BACK("mirror_front_back");

    private final String name;
    private static final String[] mirrorNames;

    private Mirror(String nameIn) {
        this.name = nameIn;
    }

    public int mirrorRotation(int rotationIn, int rotationCount) {
        int i = rotationCount / 2;
        int j = rotationIn > i ? rotationIn - rotationCount : rotationIn;
        switch (this) {
            case FRONT_BACK: {
                return (rotationCount - j) % rotationCount;
            }
            case LEFT_RIGHT: {
                return (i - j + rotationCount) % rotationCount;
            }
        }
        return rotationIn;
    }

    public Rotation toRotation(EnumFacing facing) {
        EnumFacing.Axis enumfacing$axis = facing.getAxis();
        return !(this == LEFT_RIGHT && enumfacing$axis == EnumFacing.Axis.Z || this == FRONT_BACK && enumfacing$axis == EnumFacing.Axis.X) ? Rotation.NONE : Rotation.CLOCKWISE_180;
    }

    public EnumFacing mirror(EnumFacing facing) {
        switch (this) {
            case FRONT_BACK: {
                if (facing == EnumFacing.WEST) {
                    return EnumFacing.EAST;
                }
                if (facing == EnumFacing.EAST) {
                    return EnumFacing.WEST;
                }
                return facing;
            }
            case LEFT_RIGHT: {
                if (facing == EnumFacing.NORTH) {
                    return EnumFacing.SOUTH;
                }
                if (facing == EnumFacing.SOUTH) {
                    return EnumFacing.NORTH;
                }
                return facing;
            }
        }
        return facing;
    }

    static {
        mirrorNames = new String[Mirror.values().length];
        int i = 0;
        for (Mirror mirror : Mirror.values()) {
            Mirror.mirrorNames[i++] = mirror.name;
        }
    }
}

