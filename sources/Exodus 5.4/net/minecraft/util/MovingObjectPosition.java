/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class MovingObjectPosition {
    public Entity entityHit;
    private BlockPos blockPos;
    public EnumFacing sideHit;
    public MovingObjectType typeOfHit;
    public Vec3 hitVec;

    public MovingObjectPosition(MovingObjectType movingObjectType, Vec3 vec3, EnumFacing enumFacing, BlockPos blockPos) {
        this.typeOfHit = movingObjectType;
        this.blockPos = blockPos;
        this.sideHit = enumFacing;
        this.hitVec = new Vec3(vec3.xCoord, vec3.yCoord, vec3.zCoord);
    }

    public MovingObjectPosition(Vec3 vec3, EnumFacing enumFacing) {
        this(MovingObjectType.BLOCK, vec3, enumFacing, BlockPos.ORIGIN);
    }

    public MovingObjectPosition(Entity entity) {
        this(entity, new Vec3(entity.posX, entity.posY, entity.posZ));
    }

    public MovingObjectPosition(Vec3 vec3, EnumFacing enumFacing, BlockPos blockPos) {
        this(MovingObjectType.BLOCK, vec3, enumFacing, blockPos);
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public String toString() {
        return "HitResult{type=" + (Object)((Object)this.typeOfHit) + ", blockpos=" + this.blockPos + ", f=" + this.sideHit + ", pos=" + this.hitVec + ", entity=" + this.entityHit + '}';
    }

    public MovingObjectPosition(Entity entity, Vec3 vec3) {
        this.typeOfHit = MovingObjectType.ENTITY;
        this.entityHit = entity;
        this.hitVec = vec3;
    }

    public static enum MovingObjectType {
        MISS,
        BLOCK,
        ENTITY;

    }
}

