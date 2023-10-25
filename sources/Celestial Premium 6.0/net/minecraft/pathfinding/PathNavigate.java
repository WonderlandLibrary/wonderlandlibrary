/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.pathfinding;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class PathNavigate {
    protected EntityLiving theEntity;
    protected World worldObj;
    @Nullable
    protected Path currentPath;
    protected double speed;
    private final IAttributeInstance pathSearchRange;
    protected int totalTicks;
    private int ticksAtLastPos;
    private Vec3d lastPosCheck = Vec3d.ZERO;
    private Vec3d timeoutCachedNode = Vec3d.ZERO;
    private long timeoutTimer;
    private long lastTimeoutCheck;
    private double timeoutLimit;
    protected float maxDistanceToWaypoint = 0.5f;
    protected boolean tryUpdatePath;
    private long lastTimeUpdated;
    protected NodeProcessor nodeProcessor;
    private BlockPos targetPos;
    private final PathFinder pathFinder;

    public PathNavigate(EntityLiving entitylivingIn, World worldIn) {
        this.theEntity = entitylivingIn;
        this.worldObj = worldIn;
        this.pathSearchRange = entitylivingIn.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        this.pathFinder = this.getPathFinder();
    }

    protected abstract PathFinder getPathFinder();

    public void setSpeed(double speedIn) {
        this.speed = speedIn;
    }

    public float getPathSearchRange() {
        return (float)this.pathSearchRange.getAttributeValue();
    }

    public boolean canUpdatePathOnTimeout() {
        return this.tryUpdatePath;
    }

    public void updatePath() {
        if (this.worldObj.getTotalWorldTime() - this.lastTimeUpdated > 20L) {
            if (this.targetPos != null) {
                this.currentPath = null;
                this.currentPath = this.getPathToPos(this.targetPos);
                this.lastTimeUpdated = this.worldObj.getTotalWorldTime();
                this.tryUpdatePath = false;
            }
        } else {
            this.tryUpdatePath = true;
        }
    }

    @Nullable
    public final Path getPathToXYZ(double x, double y, double z) {
        return this.getPathToPos(new BlockPos(x, y, z));
    }

    @Nullable
    public Path getPathToPos(BlockPos pos) {
        if (!this.canNavigate()) {
            return null;
        }
        if (this.currentPath != null && !this.currentPath.isFinished() && pos.equals(this.targetPos)) {
            return this.currentPath;
        }
        this.targetPos = pos;
        float f = this.getPathSearchRange();
        this.worldObj.theProfiler.startSection("pathfind");
        BlockPos blockpos = new BlockPos(this.theEntity);
        int i = (int)(f + 8.0f);
        ChunkCache chunkcache = new ChunkCache(this.worldObj, blockpos.add(-i, -i, -i), blockpos.add(i, i, i), 0);
        Path path = this.pathFinder.findPath((IBlockAccess)chunkcache, this.theEntity, this.targetPos, f);
        this.worldObj.theProfiler.endSection();
        return path;
    }

    @Nullable
    public Path getPathToEntityLiving(Entity entityIn) {
        if (!this.canNavigate()) {
            return null;
        }
        BlockPos blockpos = new BlockPos(entityIn);
        if (this.currentPath != null && !this.currentPath.isFinished() && blockpos.equals(this.targetPos)) {
            return this.currentPath;
        }
        this.targetPos = blockpos;
        float f = this.getPathSearchRange();
        this.worldObj.theProfiler.startSection("pathfind");
        BlockPos blockpos1 = new BlockPos(this.theEntity).up();
        int i = (int)(f + 16.0f);
        ChunkCache chunkcache = new ChunkCache(this.worldObj, blockpos1.add(-i, -i, -i), blockpos1.add(i, i, i), 0);
        Path path = this.pathFinder.findPath((IBlockAccess)chunkcache, this.theEntity, entityIn, f);
        this.worldObj.theProfiler.endSection();
        return path;
    }

    public boolean tryMoveToXYZ(double x, double y, double z, double speedIn) {
        return this.setPath(this.getPathToXYZ(x, y, z), speedIn);
    }

    public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
        Path path = this.getPathToEntityLiving(entityIn);
        return path != null && this.setPath(path, speedIn);
    }

    public boolean setPath(@Nullable Path pathentityIn, double speedIn) {
        if (pathentityIn == null) {
            this.currentPath = null;
            return false;
        }
        if (!pathentityIn.isSamePath(this.currentPath)) {
            this.currentPath = pathentityIn;
        }
        this.removeSunnyPath();
        if (this.currentPath.getCurrentPathLength() <= 0) {
            return false;
        }
        this.speed = speedIn;
        Vec3d vec3d = this.getEntityPosition();
        this.ticksAtLastPos = this.totalTicks;
        this.lastPosCheck = vec3d;
        return true;
    }

    @Nullable
    public Path getPath() {
        return this.currentPath;
    }

    public void onUpdateNavigation() {
        ++this.totalTicks;
        if (this.tryUpdatePath) {
            this.updatePath();
        }
        if (!this.noPath()) {
            if (this.canNavigate()) {
                this.pathFollow();
            } else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()) {
                Vec3d vec3d = this.getEntityPosition();
                Vec3d vec3d1 = this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex());
                if (vec3d.y > vec3d1.y && !this.theEntity.onGround && MathHelper.floor(vec3d.x) == MathHelper.floor(vec3d1.x) && MathHelper.floor(vec3d.z) == MathHelper.floor(vec3d1.z)) {
                    this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
                }
            }
            this.func_192876_m();
            if (!this.noPath()) {
                Vec3d vec3d2 = this.currentPath.getPosition(this.theEntity);
                BlockPos blockpos = new BlockPos(vec3d2).down();
                AxisAlignedBB axisalignedbb = this.worldObj.getBlockState(blockpos).getBoundingBox(this.worldObj, blockpos);
                vec3d2 = vec3d2.subtract(0.0, 1.0 - axisalignedbb.maxY, 0.0);
                this.theEntity.getMoveHelper().setMoveTo(vec3d2.x, vec3d2.y, vec3d2.z, this.speed);
            }
        }
    }

    protected void func_192876_m() {
    }

    protected void pathFollow() {
        Vec3d vec3d = this.getEntityPosition();
        int i = this.currentPath.getCurrentPathLength();
        for (int j = this.currentPath.getCurrentPathIndex(); j < this.currentPath.getCurrentPathLength(); ++j) {
            if ((double)this.currentPath.getPathPointFromIndex((int)j).yCoord == Math.floor(vec3d.y)) continue;
            i = j;
            break;
        }
        this.maxDistanceToWaypoint = this.theEntity.width > 0.75f ? this.theEntity.width / 2.0f : 0.75f - this.theEntity.width / 2.0f;
        Vec3d vec3d1 = this.currentPath.getCurrentPos();
        if (MathHelper.abs((float)(this.theEntity.posX - (vec3d1.x + 0.5))) < this.maxDistanceToWaypoint && MathHelper.abs((float)(this.theEntity.posZ - (vec3d1.z + 0.5))) < this.maxDistanceToWaypoint && Math.abs(this.theEntity.posY - vec3d1.y) < 1.0) {
            this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
        }
        int k = MathHelper.ceil(this.theEntity.width);
        int l = MathHelper.ceil(this.theEntity.height);
        int i1 = k;
        for (int j1 = i - 1; j1 >= this.currentPath.getCurrentPathIndex(); --j1) {
            if (!this.isDirectPathBetweenPoints(vec3d, this.currentPath.getVectorFromIndex(this.theEntity, j1), k, l, i1)) continue;
            this.currentPath.setCurrentPathIndex(j1);
            break;
        }
        this.checkForStuck(vec3d);
    }

    protected void checkForStuck(Vec3d positionVec3) {
        if (this.totalTicks - this.ticksAtLastPos > 100) {
            if (positionVec3.squareDistanceTo(this.lastPosCheck) < 2.25) {
                this.clearPathEntity();
            }
            this.ticksAtLastPos = this.totalTicks;
            this.lastPosCheck = positionVec3;
        }
        if (this.currentPath != null && !this.currentPath.isFinished()) {
            Vec3d vec3d = this.currentPath.getCurrentPos();
            if (vec3d.equals(this.timeoutCachedNode)) {
                this.timeoutTimer += System.currentTimeMillis() - this.lastTimeoutCheck;
            } else {
                this.timeoutCachedNode = vec3d;
                double d0 = positionVec3.distanceTo(this.timeoutCachedNode);
                double d = this.timeoutLimit = this.theEntity.getAIMoveSpeed() > 0.0f ? d0 / (double)this.theEntity.getAIMoveSpeed() * 1000.0 : 0.0;
            }
            if (this.timeoutLimit > 0.0 && (double)this.timeoutTimer > this.timeoutLimit * 3.0) {
                this.timeoutCachedNode = Vec3d.ZERO;
                this.timeoutTimer = 0L;
                this.timeoutLimit = 0.0;
                this.clearPathEntity();
            }
            this.lastTimeoutCheck = System.currentTimeMillis();
        }
    }

    public boolean noPath() {
        return this.currentPath == null || this.currentPath.isFinished();
    }

    public void clearPathEntity() {
        this.currentPath = null;
    }

    protected abstract Vec3d getEntityPosition();

    protected abstract boolean canNavigate();

    protected boolean isInLiquid() {
        return this.theEntity.isInWater() || this.theEntity.isInLava();
    }

    protected void removeSunnyPath() {
        if (this.currentPath != null) {
            for (int i = 0; i < this.currentPath.getCurrentPathLength(); ++i) {
                PathPoint pathpoint = this.currentPath.getPathPointFromIndex(i);
                PathPoint pathpoint1 = i + 1 < this.currentPath.getCurrentPathLength() ? this.currentPath.getPathPointFromIndex(i + 1) : null;
                IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord));
                Block block = iblockstate.getBlock();
                if (block != Blocks.CAULDRON) continue;
                this.currentPath.setPoint(i, pathpoint.cloneMove(pathpoint.xCoord, pathpoint.yCoord + 1, pathpoint.zCoord));
                if (pathpoint1 == null || pathpoint.yCoord < pathpoint1.yCoord) continue;
                this.currentPath.setPoint(i + 1, pathpoint1.cloneMove(pathpoint1.xCoord, pathpoint.yCoord + 1, pathpoint1.zCoord));
            }
        }
    }

    protected abstract boolean isDirectPathBetweenPoints(Vec3d var1, Vec3d var2, int var3, int var4, int var5);

    public boolean canEntityStandOnPos(BlockPos pos) {
        return this.worldObj.getBlockState(pos.down()).isFullBlock();
    }

    public NodeProcessor getNodeProcessor() {
        return this.nodeProcessor;
    }
}

