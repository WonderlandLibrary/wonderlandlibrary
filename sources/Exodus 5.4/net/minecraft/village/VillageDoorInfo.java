/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.village;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class VillageDoorInfo {
    private final EnumFacing insideDirection;
    private int doorOpeningRestrictionCounter;
    private int lastActivityTimestamp;
    private final BlockPos insideBlock;
    private final BlockPos doorBlockPos;
    private boolean isDetachedFromVillageFlag;

    public int getInsideOffsetX() {
        return this.insideDirection.getFrontOffsetX() * 2;
    }

    public boolean func_179850_c(BlockPos blockPos) {
        int n = blockPos.getX() - this.doorBlockPos.getX();
        int n2 = blockPos.getZ() - this.doorBlockPos.getY();
        return n * this.insideDirection.getFrontOffsetX() + n2 * this.insideDirection.getFrontOffsetZ() >= 0;
    }

    public int getDoorOpeningRestrictionCounter() {
        return this.doorOpeningRestrictionCounter;
    }

    public boolean getIsDetachedFromVillageFlag() {
        return this.isDetachedFromVillageFlag;
    }

    public BlockPos getDoorBlockPos() {
        return this.doorBlockPos;
    }

    public int getDistanceToInsideBlockSq(BlockPos blockPos) {
        return (int)this.insideBlock.distanceSq(blockPos);
    }

    public int getInsideOffsetZ() {
        return this.insideDirection.getFrontOffsetZ() * 2;
    }

    public void setIsDetachedFromVillageFlag(boolean bl) {
        this.isDetachedFromVillageFlag = bl;
    }

    public int getInsidePosY() {
        return this.lastActivityTimestamp;
    }

    private static EnumFacing getFaceDirection(int n, int n2) {
        return n < 0 ? EnumFacing.WEST : (n > 0 ? EnumFacing.EAST : (n2 < 0 ? EnumFacing.NORTH : EnumFacing.SOUTH));
    }

    public void func_179849_a(int n) {
        this.lastActivityTimestamp = n;
    }

    public void incrementDoorOpeningRestrictionCounter() {
        ++this.doorOpeningRestrictionCounter;
    }

    public VillageDoorInfo(BlockPos blockPos, int n, int n2, int n3) {
        this(blockPos, VillageDoorInfo.getFaceDirection(n, n2), n3);
    }

    public void resetDoorOpeningRestrictionCounter() {
        this.doorOpeningRestrictionCounter = 0;
    }

    public BlockPos getInsideBlockPos() {
        return this.insideBlock;
    }

    public int getDistanceToDoorBlockSq(BlockPos blockPos) {
        return (int)blockPos.distanceSq(this.getDoorBlockPos());
    }

    public int getDistanceSquared(int n, int n2, int n3) {
        return (int)this.doorBlockPos.distanceSq(n, n2, n3);
    }

    public VillageDoorInfo(BlockPos blockPos, EnumFacing enumFacing, int n) {
        this.doorBlockPos = blockPos;
        this.insideDirection = enumFacing;
        this.insideBlock = blockPos.offset(enumFacing, 2);
        this.lastActivityTimestamp = n;
    }
}

