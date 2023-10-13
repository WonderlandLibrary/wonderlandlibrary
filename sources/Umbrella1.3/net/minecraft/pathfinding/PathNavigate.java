/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

public abstract class PathNavigate {
    protected EntityLiving theEntity;
    protected World worldObj;
    protected PathEntity currentPath;
    protected double speed;
    private final IAttributeInstance pathSearchRange;
    private int totalTicks;
    private int ticksAtLastPos;
    private Vec3 lastPosCheck = new Vec3(0.0, 0.0, 0.0);
    private float field_179682_i = 1.0f;
    private final PathFinder field_179681_j;
    private static final String __OBFID = "CL_00001627";

    public PathNavigate(EntityLiving p_i1671_1_, World worldIn) {
        this.theEntity = p_i1671_1_;
        this.worldObj = worldIn;
        this.pathSearchRange = p_i1671_1_.getEntityAttribute(SharedMonsterAttributes.followRange);
        this.field_179681_j = this.func_179679_a();
    }

    protected abstract PathFinder func_179679_a();

    public void setSpeed(double p_75489_1_) {
        this.speed = p_75489_1_;
    }

    public float getPathSearchRange() {
        return (float)this.pathSearchRange.getAttributeValue();
    }

    public final PathEntity getPathToXYZ(double p_75488_1_, double p_75488_3_, double p_75488_5_) {
        return this.func_179680_a(new BlockPos(MathHelper.floor_double(p_75488_1_), (int)p_75488_3_, MathHelper.floor_double(p_75488_5_)));
    }

    public PathEntity func_179680_a(BlockPos p_179680_1_) {
        if (!this.canNavigate()) {
            return null;
        }
        float var2 = this.getPathSearchRange();
        this.worldObj.theProfiler.startSection("pathfind");
        BlockPos var3 = new BlockPos(this.theEntity);
        int var4 = (int)(var2 + 8.0f);
        ChunkCache var5 = new ChunkCache(this.worldObj, var3.add(-var4, -var4, -var4), var3.add(var4, var4, var4), 0);
        PathEntity var6 = this.field_179681_j.func_180782_a(var5, this.theEntity, p_179680_1_, var2);
        this.worldObj.theProfiler.endSection();
        return var6;
    }

    public boolean tryMoveToXYZ(double p_75492_1_, double p_75492_3_, double p_75492_5_, double p_75492_7_) {
        PathEntity var9 = this.getPathToXYZ(MathHelper.floor_double(p_75492_1_), (int)p_75492_3_, MathHelper.floor_double(p_75492_5_));
        return this.setPath(var9, p_75492_7_);
    }

    public void func_179678_a(float p_179678_1_) {
        this.field_179682_i = p_179678_1_;
    }

    public PathEntity getPathToEntityLiving(Entity p_75494_1_) {
        if (!this.canNavigate()) {
            return null;
        }
        float var2 = this.getPathSearchRange();
        this.worldObj.theProfiler.startSection("pathfind");
        BlockPos var3 = new BlockPos(this.theEntity).offsetUp();
        int var4 = (int)(var2 + 16.0f);
        ChunkCache var5 = new ChunkCache(this.worldObj, var3.add(-var4, -var4, -var4), var3.add(var4, var4, var4), 0);
        PathEntity var6 = this.field_179681_j.func_176188_a(var5, this.theEntity, p_75494_1_, var2);
        this.worldObj.theProfiler.endSection();
        return var6;
    }

    public boolean tryMoveToEntityLiving(Entity p_75497_1_, double p_75497_2_) {
        PathEntity var4 = this.getPathToEntityLiving(p_75497_1_);
        return var4 != null ? this.setPath(var4, p_75497_2_) : false;
    }

    public boolean setPath(PathEntity p_75484_1_, double p_75484_2_) {
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
        Vec3 var4 = this.getEntityPosition();
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
            Vec3 var1;
            if (this.canNavigate()) {
                this.pathFollow();
            } else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()) {
                var1 = this.getEntityPosition();
                Vec3 var2 = this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex());
                if (var1.yCoord > var2.yCoord && !this.theEntity.onGround && MathHelper.floor_double(var1.xCoord) == MathHelper.floor_double(var2.xCoord) && MathHelper.floor_double(var1.zCoord) == MathHelper.floor_double(var2.zCoord)) {
                    this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
                }
            }
            if (!this.noPath() && (var1 = this.currentPath.getPosition(this.theEntity)) != null) {
                this.theEntity.getMoveHelper().setMoveTo(var1.xCoord, var1.yCoord, var1.zCoord, this.speed);
            }
        }
    }

    protected void pathFollow() {
        int var4;
        Vec3 var1 = this.getEntityPosition();
        int var2 = this.currentPath.getCurrentPathLength();
        for (int var3 = this.currentPath.getCurrentPathIndex(); var3 < this.currentPath.getCurrentPathLength(); ++var3) {
            if (this.currentPath.getPathPointFromIndex((int)var3).yCoord == (int)var1.yCoord) continue;
            var2 = var3;
            break;
        }
        float var8 = this.theEntity.width * this.theEntity.width * this.field_179682_i;
        for (var4 = this.currentPath.getCurrentPathIndex(); var4 < var2; ++var4) {
            Vec3 var5 = this.currentPath.getVectorFromIndex(this.theEntity, var4);
            if (!(var1.squareDistanceTo(var5) < (double)var8)) continue;
            this.currentPath.setCurrentPathIndex(var4 + 1);
        }
        var4 = MathHelper.ceiling_float_int(this.theEntity.width);
        int var9 = (int)this.theEntity.height + 1;
        int var6 = var4;
        for (int var7 = var2 - 1; var7 >= this.currentPath.getCurrentPathIndex(); --var7) {
            if (!this.isDirectPathBetweenPoints(var1, this.currentPath.getVectorFromIndex(this.theEntity, var7), var4, var9, var6)) continue;
            this.currentPath.setCurrentPathIndex(var7);
            break;
        }
        this.func_179677_a(var1);
    }

    protected void func_179677_a(Vec3 p_179677_1_) {
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

    protected abstract boolean isDirectPathBetweenPoints(Vec3 var1, Vec3 var2, int var3, int var4, int var5);
}

