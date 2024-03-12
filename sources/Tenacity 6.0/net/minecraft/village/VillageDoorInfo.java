// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.village;

import net.minecraft.util.Vec3i;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;

public class VillageDoorInfo
{
    private final BlockPos doorBlockPos;
    private final BlockPos insideBlock;
    private final EnumFacing insideDirection;
    private int lastActivityTimestamp;
    private boolean isDetachedFromVillageFlag;
    private int doorOpeningRestrictionCounter;
    
    public VillageDoorInfo(final BlockPos pos, final int p_i45871_2_, final int p_i45871_3_, final int p_i45871_4_) {
        this(pos, getFaceDirection(p_i45871_2_, p_i45871_3_), p_i45871_4_);
    }
    
    private static EnumFacing getFaceDirection(final int deltaX, final int deltaZ) {
        return (deltaX < 0) ? EnumFacing.WEST : ((deltaX > 0) ? EnumFacing.EAST : ((deltaZ < 0) ? EnumFacing.NORTH : EnumFacing.SOUTH));
    }
    
    public VillageDoorInfo(final BlockPos pos, final EnumFacing facing, final int timestamp) {
        this.doorBlockPos = pos;
        this.insideDirection = facing;
        this.insideBlock = pos.offset(facing, 2);
        this.lastActivityTimestamp = timestamp;
    }
    
    public int getDistanceSquared(final int x, final int y, final int z) {
        return (int)this.doorBlockPos.distanceSq(x, y, z);
    }
    
    public int getDistanceToDoorBlockSq(final BlockPos pos) {
        return (int)pos.distanceSq(this.getDoorBlockPos());
    }
    
    public int getDistanceToInsideBlockSq(final BlockPos pos) {
        return (int)this.insideBlock.distanceSq(pos);
    }
    
    public boolean func_179850_c(final BlockPos pos) {
        final int i = pos.getX() - this.doorBlockPos.getX();
        final int j = pos.getZ() - this.doorBlockPos.getY();
        return i * this.insideDirection.getFrontOffsetX() + j * this.insideDirection.getFrontOffsetZ() >= 0;
    }
    
    public void resetDoorOpeningRestrictionCounter() {
        this.doorOpeningRestrictionCounter = 0;
    }
    
    public void incrementDoorOpeningRestrictionCounter() {
        ++this.doorOpeningRestrictionCounter;
    }
    
    public int getDoorOpeningRestrictionCounter() {
        return this.doorOpeningRestrictionCounter;
    }
    
    public BlockPos getDoorBlockPos() {
        return this.doorBlockPos;
    }
    
    public BlockPos getInsideBlockPos() {
        return this.insideBlock;
    }
    
    public int getInsideOffsetX() {
        return this.insideDirection.getFrontOffsetX() * 2;
    }
    
    public int getInsideOffsetZ() {
        return this.insideDirection.getFrontOffsetZ() * 2;
    }
    
    public int getInsidePosY() {
        return this.lastActivityTimestamp;
    }
    
    public void func_179849_a(final int timestamp) {
        this.lastActivityTimestamp = timestamp;
    }
    
    public boolean getIsDetachedFromVillageFlag() {
        return this.isDetachedFromVillageFlag;
    }
    
    public void setIsDetachedFromVillageFlag(final boolean detached) {
        this.isDetachedFromVillageFlag = detached;
    }
}
