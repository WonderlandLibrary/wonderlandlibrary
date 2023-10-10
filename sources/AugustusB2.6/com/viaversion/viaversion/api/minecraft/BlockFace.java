// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.minecraft;

import java.util.HashMap;
import java.util.Map;

public enum BlockFace
{
    NORTH((byte)0, (byte)0, (byte)(-1), EnumAxis.Z), 
    SOUTH((byte)0, (byte)0, (byte)1, EnumAxis.Z), 
    EAST((byte)1, (byte)0, (byte)0, EnumAxis.X), 
    WEST((byte)(-1), (byte)0, (byte)0, EnumAxis.X), 
    TOP((byte)0, (byte)1, (byte)0, EnumAxis.Y), 
    BOTTOM((byte)0, (byte)(-1), (byte)0, EnumAxis.Y);
    
    public static final BlockFace[] HORIZONTAL;
    private static final Map<BlockFace, BlockFace> opposites;
    private final byte modX;
    private final byte modY;
    private final byte modZ;
    private final EnumAxis axis;
    
    private BlockFace(final byte modX, final byte modY, final byte modZ, final EnumAxis axis) {
        this.modX = modX;
        this.modY = modY;
        this.modZ = modZ;
        this.axis = axis;
    }
    
    public BlockFace opposite() {
        return BlockFace.opposites.get(this);
    }
    
    public byte modX() {
        return this.modX;
    }
    
    public byte modY() {
        return this.modY;
    }
    
    public byte modZ() {
        return this.modZ;
    }
    
    public EnumAxis axis() {
        return this.axis;
    }
    
    @Deprecated
    public byte getModX() {
        return this.modX;
    }
    
    @Deprecated
    public byte getModY() {
        return this.modY;
    }
    
    @Deprecated
    public byte getModZ() {
        return this.modZ;
    }
    
    @Deprecated
    public EnumAxis getAxis() {
        return this.axis;
    }
    
    static {
        HORIZONTAL = new BlockFace[] { BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST };
        (opposites = new HashMap<BlockFace, BlockFace>()).put(BlockFace.NORTH, BlockFace.SOUTH);
        BlockFace.opposites.put(BlockFace.SOUTH, BlockFace.NORTH);
        BlockFace.opposites.put(BlockFace.EAST, BlockFace.WEST);
        BlockFace.opposites.put(BlockFace.WEST, BlockFace.EAST);
        BlockFace.opposites.put(BlockFace.TOP, BlockFace.BOTTOM);
        BlockFace.opposites.put(BlockFace.BOTTOM, BlockFace.TOP);
    }
    
    public enum EnumAxis
    {
        X, 
        Y, 
        Z;
    }
}
