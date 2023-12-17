// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.math;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

public class RayTraceResult
{
    private BlockPos blockPos;
    public Type typeOfHit;
    public EnumFacing sideHit;
    public Vec3d hitVec;
    public Entity entityHit;
    
    public RayTraceResult(final Vec3d hitVecIn, final EnumFacing sideHitIn, final BlockPos blockPosIn) {
        this(Type.BLOCK, hitVecIn, sideHitIn, blockPosIn);
    }
    
    public RayTraceResult(final Vec3d hitVecIn, final EnumFacing sideHitIn) {
        this(Type.BLOCK, hitVecIn, sideHitIn, BlockPos.ORIGIN);
    }
    
    public RayTraceResult(final Entity entityIn) {
        this(entityIn, new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ));
    }
    
    public RayTraceResult(final Type typeIn, final Vec3d hitVecIn, final EnumFacing sideHitIn, final BlockPos blockPosIn) {
        this.typeOfHit = typeIn;
        this.blockPos = blockPosIn;
        this.sideHit = sideHitIn;
        this.hitVec = new Vec3d(hitVecIn.x, hitVecIn.y, hitVecIn.z);
    }
    
    public RayTraceResult(final Entity entityHitIn, final Vec3d hitVecIn) {
        this.typeOfHit = Type.ENTITY;
        this.entityHit = entityHitIn;
        this.hitVec = hitVecIn;
    }
    
    public BlockPos getBlockPos() {
        return this.blockPos;
    }
    
    @Override
    public String toString() {
        return "HitResult{type=" + this.typeOfHit + ", blockpos=" + this.blockPos + ", f=" + this.sideHit + ", pos=" + this.hitVec + ", entity=" + this.entityHit + '}';
    }
    
    public enum Type
    {
        MISS, 
        BLOCK, 
        ENTITY;
    }
}
