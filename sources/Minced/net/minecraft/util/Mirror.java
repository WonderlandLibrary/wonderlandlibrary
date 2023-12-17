// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public enum Mirror
{
    NONE("no_mirror"), 
    LEFT_RIGHT("mirror_left_right"), 
    FRONT_BACK("mirror_front_back");
    
    private final String name;
    private static final String[] mirrorNames;
    
    private Mirror(final String nameIn) {
        this.name = nameIn;
    }
    
    public int mirrorRotation(final int rotationIn, final int rotationCount) {
        final int i = rotationCount / 2;
        final int j = (rotationIn > i) ? (rotationIn - rotationCount) : rotationIn;
        switch (this) {
            case FRONT_BACK: {
                return (rotationCount - j) % rotationCount;
            }
            case LEFT_RIGHT: {
                return (i - j + rotationCount) % rotationCount;
            }
            default: {
                return rotationIn;
            }
        }
    }
    
    public Rotation toRotation(final EnumFacing facing) {
        final EnumFacing.Axis enumfacing$axis = facing.getAxis();
        return ((this != Mirror.LEFT_RIGHT || enumfacing$axis != EnumFacing.Axis.Z) && (this != Mirror.FRONT_BACK || enumfacing$axis != EnumFacing.Axis.X)) ? Rotation.NONE : Rotation.CLOCKWISE_180;
    }
    
    public EnumFacing mirror(final EnumFacing facing) {
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
            default: {
                return facing;
            }
        }
    }
    
    static {
        mirrorNames = new String[values().length];
        int i = 0;
        for (final Mirror mirror : values()) {
            Mirror.mirrorNames[i++] = mirror.name;
        }
    }
}
