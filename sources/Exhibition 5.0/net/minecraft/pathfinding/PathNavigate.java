// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.pathfinding;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ChunkCache;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.Vec3;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;

public abstract class PathNavigate
{
    protected EntityLiving theEntity;
    protected World worldObj;
    protected PathEntity currentPath;
    protected double speed;
    private final IAttributeInstance pathSearchRange;
    private int totalTicks;
    private int ticksAtLastPos;
    private Vec3 lastPosCheck;
    private float field_179682_i;
    private final PathFinder field_179681_j;
    private static final String __OBFID = "CL_00001627";
    
    public PathNavigate(final EntityLiving p_i1671_1_, final World worldIn) {
        this.lastPosCheck = new Vec3(0.0, 0.0, 0.0);
        this.field_179682_i = 1.0f;
        this.theEntity = p_i1671_1_;
        this.worldObj = worldIn;
        this.pathSearchRange = p_i1671_1_.getEntityAttribute(SharedMonsterAttributes.followRange);
        this.field_179681_j = this.func_179679_a();
    }
    
    protected abstract PathFinder func_179679_a();
    
    public void setSpeed(final double p_75489_1_) {
        this.speed = p_75489_1_;
    }
    
    public float getPathSearchRange() {
        return (float)this.pathSearchRange.getAttributeValue();
    }
    
    public final PathEntity getPathToXYZ(final double p_75488_1_, final double p_75488_3_, final double p_75488_5_) {
        return this.func_179680_a(new BlockPos(MathHelper.floor_double(p_75488_1_), (int)p_75488_3_, MathHelper.floor_double(p_75488_5_)));
    }
    
    public PathEntity func_179680_a(final BlockPos p_179680_1_) {
        if (!this.canNavigate()) {
            return null;
        }
        final float var2 = this.getPathSearchRange();
        this.worldObj.theProfiler.startSection("pathfind");
        final BlockPos var3 = new BlockPos(this.theEntity);
        final int var4 = (int)(var2 + 8.0f);
        final ChunkCache var5 = new ChunkCache(this.worldObj, var3.add(-var4, -var4, -var4), var3.add(var4, var4, var4), 0);
        final PathEntity var6 = this.field_179681_j.func_180782_a(var5, this.theEntity, p_179680_1_, var2);
        this.worldObj.theProfiler.endSection();
        return var6;
    }
    
    public boolean tryMoveToXYZ(final double p_75492_1_, final double p_75492_3_, final double p_75492_5_, final double p_75492_7_) {
        final PathEntity var9 = this.getPathToXYZ(MathHelper.floor_double(p_75492_1_), (int)p_75492_3_, MathHelper.floor_double(p_75492_5_));
        return this.setPath(var9, p_75492_7_);
    }
    
    public void func_179678_a(final float p_179678_1_) {
        this.field_179682_i = p_179678_1_;
    }
    
    public PathEntity getPathToEntityLiving(final Entity p_75494_1_) {
        if (!this.canNavigate()) {
            return null;
        }
        final float var2 = this.getPathSearchRange();
        this.worldObj.theProfiler.startSection("pathfind");
        final BlockPos var3 = new BlockPos(this.theEntity).offsetUp();
        final int var4 = (int)(var2 + 16.0f);
        final ChunkCache var5 = new ChunkCache(this.worldObj, var3.add(-var4, -var4, -var4), var3.add(var4, var4, var4), 0);
        final PathEntity var6 = this.field_179681_j.func_176188_a(var5, this.theEntity, p_75494_1_, var2);
        this.worldObj.theProfiler.endSection();
        return var6;
    }
    
    public boolean tryMoveToEntityLiving(final Entity p_75497_1_, final double p_75497_2_) {
        final PathEntity var4 = this.getPathToEntityLiving(p_75497_1_);
        return var4 != null && this.setPath(var4, p_75497_2_);
    }
    
    public boolean setPath(final PathEntity p_75484_1_, final double p_75484_2_) {
        if (p_75484_1_ == null) {
            this.currentPath = null;
            return false;
        }
        if (!p_75484_1_.isSamePath(this.currentPath)) {
            this.currentPath = p_75484_1_;
        }
        this.removeSunnyPath();
        if (this.currentPath.getCurrentPathLength() == 0) {
            return false;
        }
        this.speed = p_75484_2_;
        final Vec3 var4 = this.getEntityPosition();
        this.ticksAtLastPos = this.totalTicks;
        this.lastPosCheck = var4;
        return true;
    }
    
    public PathEntity getPath() {
        return this.currentPath;
    }
    
    public void onUpdateNavigation() {
        ++this.totalTicks;
        if (!this.noPath()) {
            if (this.canNavigate()) {
                this.pathFollow();
            }
            else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()) {
                final Vec3 var1 = this.getEntityPosition();
                final Vec3 var2 = this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex());
                if (var1.yCoord > var2.yCoord && !this.theEntity.onGround && MathHelper.floor_double(var1.xCoord) == MathHelper.floor_double(var2.xCoord) && MathHelper.floor_double(var1.zCoord) == MathHelper.floor_double(var2.zCoord)) {
                    this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
                }
            }
            if (!this.noPath()) {
                final Vec3 var1 = this.currentPath.getPosition(this.theEntity);
                if (var1 != null) {
                    this.theEntity.getMoveHelper().setMoveTo(var1.xCoord, var1.yCoord, var1.zCoord, this.speed);
                }
            }
        }
    }
    
    protected void pathFollow() {
        final Vec3 var1 = this.getEntityPosition();
        int var2 = this.currentPath.getCurrentPathLength();
        for (int var3 = this.currentPath.getCurrentPathIndex(); var3 < this.currentPath.getCurrentPathLength(); ++var3) {
            if (this.currentPath.getPathPointFromIndex(var3).yCoord != (int)var1.yCoord) {
                var2 = var3;
                break;
            }
        }
        final float var4 = this.theEntity.width * this.theEntity.width * this.field_179682_i;
        for (int var5 = this.currentPath.getCurrentPathIndex(); var5 < var2; ++var5) {
            final Vec3 var6 = this.currentPath.getVectorFromIndex(this.theEntity, var5);
            if (var1.squareDistanceTo(var6) < var4) {
                this.currentPath.setCurrentPathIndex(var5 + 1);
            }
        }
        int var5 = MathHelper.ceiling_float_int(this.theEntity.width);
        final int var7 = (int)this.theEntity.height + 1;
        final int var8 = var5;
        for (int var9 = var2 - 1; var9 >= this.currentPath.getCurrentPathIndex(); --var9) {
            if (this.isDirectPathBetweenPoints(var1, this.currentPath.getVectorFromIndex(this.theEntity, var9), var5, var7, var8)) {
                this.currentPath.setCurrentPathIndex(var9);
                break;
            }
        }
        this.func_179677_a(var1);
    }
    
    protected void func_179677_a(final Vec3 p_179677_1_) {
        if (this.totalTicks - this.ticksAtLastPos > 100) {
            if (p_179677_1_.squareDistanceTo(this.lastPosCheck) < 2.25) {
                this.clearPathEntity();
            }
            this.ticksAtLastPos = this.totalTicks;
            this.lastPosCheck = p_179677_1_;
        }
    }
    
    public boolean noPath() {
        return this.currentPath == null || this.currentPath.isFinished();
    }
    
    public void clearPathEntity() {
        this.currentPath = null;
    }
    
    protected abstract Vec3 getEntityPosition();
    
    protected abstract boolean canNavigate();
    
    protected boolean isInLiquid() {
        return this.theEntity.isInWater() || this.theEntity.func_180799_ab();
    }
    
    protected void removeSunnyPath() {
    }
    
    protected abstract boolean isDirectPathBetweenPoints(final Vec3 p0, final Vec3 p1, final int p2, final int p3, final int p4);
}
