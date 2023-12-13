// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import net.minecraft.entity.Entity;

public class MovingObjectPosition
{
    private BlockPos field_178783_e;
    public MovingObjectType typeOfHit;
    public EnumFacing facing;
    public Vec3 hitVec;
    public Entity entityHit;
    private static final String __OBFID = "CL_00000610";
    
    public MovingObjectPosition(final Vec3 p_i45551_1_, final EnumFacing p_i45551_2_, final BlockPos p_i45551_3_) {
        this(MovingObjectType.BLOCK, p_i45551_1_, p_i45551_2_, p_i45551_3_);
    }
    
    public MovingObjectPosition(final Vec3 p_i45552_1_, final EnumFacing p_i45552_2_) {
        this(MovingObjectType.BLOCK, p_i45552_1_, p_i45552_2_, BlockPos.ORIGIN);
    }
    
    public MovingObjectPosition(final Entity p_i2304_1_) {
        this(p_i2304_1_, new Vec3(p_i2304_1_.posX, p_i2304_1_.posY, p_i2304_1_.posZ));
    }
    
    public MovingObjectPosition(final MovingObjectType p_i45553_1_, final Vec3 p_i45553_2_, final EnumFacing p_i45553_3_, final BlockPos p_i45553_4_) {
        this.typeOfHit = p_i45553_1_;
        this.field_178783_e = p_i45553_4_;
        this.facing = p_i45553_3_;
        this.hitVec = new Vec3(p_i45553_2_.xCoord, p_i45553_2_.yCoord, p_i45553_2_.zCoord);
    }
    
    public MovingObjectPosition(final Entity p_i45482_1_, final Vec3 p_i45482_2_) {
        this.typeOfHit = MovingObjectType.ENTITY;
        this.entityHit = p_i45482_1_;
        this.hitVec = p_i45482_2_;
    }
    
    public BlockPos getBlockPos() {
        return this.field_178783_e;
    }
    
    @Override
    public String toString() {
        return "HitResult{type=" + this.typeOfHit + ", blockpos=" + this.field_178783_e + ", f=" + this.facing + ", pos=" + this.hitVec + ", entity=" + this.entityHit + '}';
    }
    
    public enum MovingObjectType
    {
        MISS("MISS", 0), 
        BLOCK("BLOCK", 1), 
        ENTITY("ENTITY", 2);
        
        private static final MovingObjectType[] $VALUES;
        private static final String __OBFID = "CL_00000611";
        
        private MovingObjectType(final String p_i2302_1_, final int p_i2302_2_) {
        }
        
        static {
            $VALUES = new MovingObjectType[] { MovingObjectType.MISS, MovingObjectType.BLOCK, MovingObjectType.ENTITY };
        }
    }
}
