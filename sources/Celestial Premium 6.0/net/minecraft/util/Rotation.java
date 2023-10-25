/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import net.minecraft.util.EnumFacing;

public enum Rotation {
    NONE("rotate_0"),
    CLOCKWISE_90("rotate_90"),
    CLOCKWISE_180("rotate_180"),
    COUNTERCLOCKWISE_90("rotate_270");

    private final String name;
    private static final String[] rotationNames;

    private Rotation(String nameIn) {
        this.name = nameIn;
    }

    public Rotation add(Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180: {
                switch (this) {
                    case NONE: {
                        return CLOCKWISE_180;
                    }
                    case CLOCKWISE_90: {
                        return COUNTERCLOCKWISE_90;
                    }
                    case CLOCKWISE_180: {
                        return NONE;
                    }
                    case COUNTERCLOCKWISE_90: {
                        return CLOCKWISE_90;
                    }
                }
            }
            case COUNTERCLOCKWISE_90: {
                switch (this) {
                    case NONE: {
                        return COUNTERCLOCKWISE_90;
                    }
                    case CLOCKWISE_90: {
                        return NONE;
                    }
                    case CLOCKWISE_180: {
                        return CLOCKWISE_90;
                    }
                    case COUNTERCLOCKWISE_90: {
                        return CLOCKWISE_180;
                    }
                }
            }
            case CLOCKWISE_90: {
                switch (this) {
                    case NONE: {
                        return CLOCKWISE_90;
                    }
                    case CLOCKWISE_90: {
                        return CLOCKWISE_180;
                    }
                    case CLOCKWISE_180: {
                        return COUNTERCLOCKWISE_90;
                    }
                    case COUNTERCLOCKWISE_90: {
                        return NONE;
                    }
                }
            }
        }
        return this;
    }

    public EnumFacing rotate(EnumFacing facing) {
        if (facing.getAxis() == EnumFacing.Axis.Y) {
            return facing;
        }
        switch (this) {
            case CLOCKWISE_90: {
                return facing.rotateY();
            }
            case CLOCKWISE_180: {
                return facing.getOpposite();
            }
            case COUNTERCLOCKWISE_90: {
                return facing.rotateYCCW();
            }
        }
        return facing;
    }

    public int rotate(int p_185833_1_, int p_185833_2_) {
        switch (this) {
            case CLOCKWISE_90: {
                return (p_185833_1_ + p_185833_2_ / 4) % p_185833_2_;
            }
            case CLOCKWISE_180: {
                return (p_185833_1_ + p_185833_2_ / 2) % p_185833_2_;
            }
            case COUNTERCLOCKWISE_90: {
                return (p_185833_1_ + p_185833_2_ * 3 / 4) % p_185833_2_;
            }
        }
        return p_185833_1_;
    }

    static {
        rotationNames = new String[Rotation.values().length];
        int i = 0;
        for (Rotation rotation : Rotation.values()) {
            Rotation.rotationNames[i++] = rotation.name;
        }
    }
}

