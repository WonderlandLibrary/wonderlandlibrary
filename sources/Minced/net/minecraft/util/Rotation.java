// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public enum Rotation
{
    NONE("rotate_0"), 
    CLOCKWISE_90("rotate_90"), 
    CLOCKWISE_180("rotate_180"), 
    COUNTERCLOCKWISE_90("rotate_270");
    
    private final String name;
    private static final String[] rotationNames;
    
    private Rotation(final String nameIn) {
        this.name = nameIn;
    }
    
    public Rotation add(final Rotation rotation) {
        Label_0148: {
            switch (rotation) {
                case CLOCKWISE_180: {
                    switch (this) {
                        case NONE: {
                            return Rotation.CLOCKWISE_180;
                        }
                        case CLOCKWISE_90: {
                            return Rotation.COUNTERCLOCKWISE_90;
                        }
                        case CLOCKWISE_180: {
                            return Rotation.NONE;
                        }
                        case COUNTERCLOCKWISE_90: {
                            return Rotation.CLOCKWISE_90;
                        }
                        default: {
                            break Label_0148;
                        }
                    }
                    break;
                }
                case COUNTERCLOCKWISE_90: {
                    switch (this) {
                        case NONE: {
                            return Rotation.COUNTERCLOCKWISE_90;
                        }
                        case CLOCKWISE_90: {
                            return Rotation.NONE;
                        }
                        case CLOCKWISE_180: {
                            return Rotation.CLOCKWISE_90;
                        }
                        case COUNTERCLOCKWISE_90: {
                            return Rotation.CLOCKWISE_180;
                        }
                        default: {
                            break Label_0148;
                        }
                    }
                    break;
                }
                case CLOCKWISE_90: {
                    switch (this) {
                        case NONE: {
                            return Rotation.CLOCKWISE_90;
                        }
                        case CLOCKWISE_90: {
                            return Rotation.CLOCKWISE_180;
                        }
                        case CLOCKWISE_180: {
                            return Rotation.COUNTERCLOCKWISE_90;
                        }
                        case COUNTERCLOCKWISE_90: {
                            return Rotation.NONE;
                        }
                        default: {
                            break Label_0148;
                        }
                    }
                    break;
                }
            }
        }
        return this;
    }
    
    public EnumFacing rotate(final EnumFacing facing) {
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
            default: {
                return facing;
            }
        }
    }
    
    public int rotate(final int p_185833_1_, final int p_185833_2_) {
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
            default: {
                return p_185833_1_;
            }
        }
    }
    
    static {
        rotationNames = new String[values().length];
        int i = 0;
        for (final Rotation rotation : values()) {
            Rotation.rotationNames[i++] = rotation.name;
        }
    }
}
