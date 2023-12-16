// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public enum BlockDir
{
    DOWN("DOWN", 0, EnumFacing.DOWN), 
    UP("UP", 1, EnumFacing.UP), 
    NORTH("NORTH", 2, EnumFacing.NORTH), 
    SOUTH("SOUTH", 3, EnumFacing.SOUTH), 
    WEST("WEST", 4, EnumFacing.WEST), 
    EAST("EAST", 5, EnumFacing.EAST), 
    NORTH_WEST("NORTH_WEST", 6, EnumFacing.NORTH, EnumFacing.WEST), 
    NORTH_EAST("NORTH_EAST", 7, EnumFacing.NORTH, EnumFacing.EAST), 
    SOUTH_WEST("SOUTH_WEST", 8, EnumFacing.SOUTH, EnumFacing.WEST), 
    SOUTH_EAST("SOUTH_EAST", 9, EnumFacing.SOUTH, EnumFacing.EAST), 
    DOWN_NORTH("DOWN_NORTH", 10, EnumFacing.DOWN, EnumFacing.NORTH), 
    DOWN_SOUTH("DOWN_SOUTH", 11, EnumFacing.DOWN, EnumFacing.SOUTH), 
    UP_NORTH("UP_NORTH", 12, EnumFacing.UP, EnumFacing.NORTH), 
    UP_SOUTH("UP_SOUTH", 13, EnumFacing.UP, EnumFacing.SOUTH), 
    DOWN_WEST("DOWN_WEST", 14, EnumFacing.DOWN, EnumFacing.WEST), 
    DOWN_EAST("DOWN_EAST", 15, EnumFacing.DOWN, EnumFacing.EAST), 
    UP_WEST("UP_WEST", 16, EnumFacing.UP, EnumFacing.WEST), 
    UP_EAST("UP_EAST", 17, EnumFacing.UP, EnumFacing.EAST);
    
    private EnumFacing facing1;
    private EnumFacing facing2;
    
    private BlockDir(final String s, final int n, final EnumFacing facing1) {
        this.facing1 = facing1;
    }
    
    private BlockDir(final String s, final int n, final EnumFacing facing1, final EnumFacing facing2) {
        this.facing1 = facing1;
        this.facing2 = facing2;
    }
    
    public EnumFacing getFacing1() {
        return this.facing1;
    }
    
    public EnumFacing getFacing2() {
        return this.facing2;
    }
    
    BlockPos offset(BlockPos pos) {
        pos = pos.offset(this.facing1, 1);
        if (this.facing2 != null) {
            pos = pos.offset(this.facing2, 1);
        }
        return pos;
    }
    
    public int getOffsetX() {
        int i = this.facing1.getFrontOffsetX();
        if (this.facing2 != null) {
            i += this.facing2.getFrontOffsetX();
        }
        return i;
    }
    
    public int getOffsetY() {
        int i = this.facing1.getFrontOffsetY();
        if (this.facing2 != null) {
            i += this.facing2.getFrontOffsetY();
        }
        return i;
    }
    
    public int getOffsetZ() {
        int i = this.facing1.getFrontOffsetZ();
        if (this.facing2 != null) {
            i += this.facing2.getFrontOffsetZ();
        }
        return i;
    }
    
    public boolean isDouble() {
        return this.facing2 != null;
    }
}
