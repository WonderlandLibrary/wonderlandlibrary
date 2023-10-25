/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.pathfinding;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class PathNavigate {
    private final IAttributeInstance pathSearchRange;
    private int ticksAtLastPos;
    protected EntityLiving theEntity;
    protected double speed;
    private final PathFinder pathFinder;
    protected PathEntity currentPath;
    private float heightRequirement = 1.0f;
    private Vec3 lastPosCheck = new Vec3(0.0, 0.0, 0.0);
    private int totalTicks;
    protected World worldObj;

    protected boolean isInLiquid() {
        return this.theEntity.isInWater() || this.theEntity.isInLava();
    }

    public boolean tryMoveToEntityLiving(Entity entity, double d) {
        PathEntity pathEntity = this.getPathToEntityLiving(entity);
        return pathEntity != null ? this.setPath(pathEntity, d) : false;
    }

    public void onUpdateNavigation() {
        ++this.totalTicks;
        if (!this.noPath()) {
            Object object;
            Vec3 vec3;
            if (this.canNavigate()) {
                this.pathFollow();
            } else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()) {
                vec3 = this.getEntityPosition();
                object = this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex());
                if (vec3.yCoord > ((Vec3)object).yCoord && !this.theEntity.onGround && MathHelper.floor_double(vec3.xCoord) == MathHelper.floor_double(((Vec3)object).xCoord) && MathHelper.floor_double(vec3.zCoord) == MathHelper.floor_double(((Vec3)object).zCoord)) {
                    this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
                }
            }
            if (!this.noPath() && (vec3 = this.currentPath.getPosition(this.theEntity)) != null) {
                object = new AxisAlignedBB(vec3.xCoord, vec3.yCoord, vec3.zCoord, vec3.xCoord, vec3.yCoord, vec3.zCoord).expand(0.5, 0.5, 0.5);
                List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this.theEntity, ((AxisAlignedBB)object).addCoord(0.0, -1.0, 0.0));
                double d = -1.0;
                object = ((AxisAlignedBB)object).offset(0.0, 1.0, 0.0);
                for (AxisAlignedBB axisAlignedBB : list) {
                    d = axisAlignedBB.calculateYOffset((AxisAlignedBB)object, d);
                }
                this.theEntity.getMoveHelper().setMoveTo(vec3.xCoord, vec3.yCoord + d, vec3.zCoord, this.speed);
            }
        }
    }

    protected abstract boolean canNavigate();

    public boolean tryMoveToXYZ(double d, double d2, double d3, double d4) {
        PathEntity pathEntity = this.getPathToXYZ(MathHelper.floor_double(d), (int)d2, MathHelper.floor_double(d3));
        return this.setPath(pathEntity, d4);
    }

    public PathEntity getPath() {
        return this.currentPath;
    }

    public float getPathSearchRange() {
        return (float)this.pathSearchRange.getAttributeValue();
    }

    protected abstract boolean isDirectPathBetweenPoints(Vec3 var1, Vec3 var2, int var3, int var4, int var5);

    public PathEntity getPathToPos(BlockPos blockPos) {
        if (!this.canNavigate()) {
            return null;
        }
        float f = this.getPathSearchRange();
        this.worldObj.theProfiler.startSection("pathfind");
        BlockPos blockPos2 = new BlockPos(this.theEntity);
        int n = (int)(f + 8.0f);
        ChunkCache chunkCache = new ChunkCache(this.worldObj, blockPos2.add(-n, -n, -n), blockPos2.add(n, n, n), 0);
        PathEntity pathEntity = this.pathFinder.createEntityPathTo((IBlockAccess)chunkCache, (Entity)this.theEntity, blockPos, f);
        this.worldObj.theProfiler.endSection();
        return pathEntity;
    }

    public PathEntity getPathToEntityLiving(Entity entity) {
        if (!this.canNavigate()) {
            return null;
        }
        float f = this.getPathSearchRange();
        this.worldObj.theProfiler.startSection("pathfind");
        BlockPos blockPos = new BlockPos(this.theEntity).up();
        int n = (int)(f + 16.0f);
        ChunkCache chunkCache = new ChunkCache(this.worldObj, blockPos.add(-n, -n, -n), blockPos.add(n, n, n), 0);
        PathEntity pathEntity = this.pathFinder.createEntityPathTo((IBlockAccess)chunkCache, (Entity)this.theEntity, entity, f);
        this.worldObj.theProfiler.endSection();
        return pathEntity;
    }

    public final PathEntity getPathToXYZ(double d, double d2, double d3) {
        return this.getPathToPos(new BlockPos(MathHelper.floor_double(d), (int)d2, MathHelper.floor_double(d3)));
    }

    public void clearPathEntity() {
        this.currentPath = null;
    }

    public boolean noPath() {
        return this.currentPath == null || this.currentPath.isFinished();
    }

    public PathNavigate(EntityLiving entityLiving, World world) {
        this.theEntity = entityLiving;
        this.worldObj = world;
        this.pathSearchRange = entityLiving.getEntityAttribute(SharedMonsterAttributes.followRange);
        this.pathFinder = this.getPathFinder();
    }

    protected abstract PathFinder getPathFinder();

    protected void removeSunnyPath() {
    }

    protected void pathFollow() {
        Vec3 vec3 = this.getEntityPosition();
        int n = this.currentPath.getCurrentPathLength();
        int n2 = this.currentPath.getCurrentPathIndex();
        while (n2 < this.currentPath.getCurrentPathLength()) {
            if (this.currentPath.getPathPointFromIndex((int)n2).yCoord != (int)vec3.yCoord) {
                n = n2;
                break;
            }
            ++n2;
        }
        float f = this.theEntity.width * this.theEntity.width * this.heightRequirement;
        int n3 = this.currentPath.getCurrentPathIndex();
        while (n3 < n) {
            Vec3 vec32 = this.currentPath.getVectorFromIndex(this.theEntity, n3);
            if (vec3.squareDistanceTo(vec32) < (double)f) {
                this.currentPath.setCurrentPathIndex(n3 + 1);
            }
            ++n3;
        }
        n3 = MathHelper.ceiling_float_int(this.theEntity.width);
        int n4 = (int)this.theEntity.height + 1;
        int n5 = n3;
        int n6 = n - 1;
        while (n6 >= this.currentPath.getCurrentPathIndex()) {
            if (this.isDirectPathBetweenPoints(vec3, this.currentPath.getVectorFromIndex(this.theEntity, n6), n3, n4, n5)) {
                this.currentPath.setCurrentPathIndex(n6);
                break;
            }
            --n6;
        }
        this.checkForStuck(vec3);
    }

    public void setHeightRequirement(float f) {
        this.heightRequirement = f;
    }

    public void setSpeed(double d) {
        this.speed = d;
    }

    protected void checkForStuck(Vec3 vec3) {
        if (this.totalTicks - this.ticksAtLastPos > 100) {
            if (vec3.squareDistanceTo(this.lastPosCheck) < 2.25) {
                this.clearPathEntity();
            }
            this.ticksAtLastPos = this.totalTicks;
            this.lastPosCheck = vec3;
        }
    }

    protected abstract Vec3 getEntityPosition();

    public boolean setPath(PathEntity pathEntity, double d) {
        if (pathEntity == null) {
            this.currentPath = null;
            return false;
        }
        if (!pathEntity.isSamePath(this.currentPath)) {
            this.currentPath = pathEntity;
        }
        this.removeSunnyPath();
        if (this.currentPath.getCurrentPathLength() == 0) {
            return false;
        }
        this.speed = d;
        Vec3 vec3 = this.getEntityPosition();
        this.ticksAtLastPos = this.totalTicks;
        this.lastPosCheck = vec3;
        return true;
    }
}

