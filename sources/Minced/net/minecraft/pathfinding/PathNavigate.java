// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ChunkCache;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;

public abstract class PathNavigate
{
    protected EntityLiving entity;
    protected World world;
    @Nullable
    protected Path currentPath;
    protected double speed;
    private final IAttributeInstance pathSearchRange;
    protected int totalTicks;
    private int ticksAtLastPos;
    private Vec3d lastPosCheck;
    private Vec3d timeoutCachedNode;
    private long timeoutTimer;
    private long lastTimeoutCheck;
    private double timeoutLimit;
    protected float maxDistanceToWaypoint;
    protected boolean tryUpdatePath;
    private long lastTimeUpdated;
    protected NodeProcessor nodeProcessor;
    private BlockPos targetPos;
    private final PathFinder pathFinder;
    
    public PathNavigate(final EntityLiving entityIn, final World worldIn) {
        this.lastPosCheck = Vec3d.ZERO;
        this.timeoutCachedNode = Vec3d.ZERO;
        this.maxDistanceToWaypoint = 0.5f;
        this.entity = entityIn;
        this.world = worldIn;
        this.pathSearchRange = entityIn.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        this.pathFinder = this.getPathFinder();
    }
    
    protected abstract PathFinder getPathFinder();
    
    public void setSpeed(final double speedIn) {
        this.speed = speedIn;
    }
    
    public float getPathSearchRange() {
        return (float)this.pathSearchRange.getAttributeValue();
    }
    
    public boolean canUpdatePathOnTimeout() {
        return this.tryUpdatePath;
    }
    
    public void updatePath() {
        if (this.world.getTotalWorldTime() - this.lastTimeUpdated > 20L) {
            if (this.targetPos != null) {
                this.currentPath = null;
                this.currentPath = this.getPathToPos(this.targetPos);
                this.lastTimeUpdated = this.world.getTotalWorldTime();
                this.tryUpdatePath = false;
            }
        }
        else {
            this.tryUpdatePath = true;
        }
    }
    
    @Nullable
    public final Path getPathToXYZ(final double x, final double y, final double z) {
        return this.getPathToPos(new BlockPos(x, y, z));
    }
    
    @Nullable
    public Path getPathToPos(final BlockPos pos) {
        if (!this.canNavigate()) {
            return null;
        }
        if (this.currentPath != null && !this.currentPath.isFinished() && pos.equals(this.targetPos)) {
            return this.currentPath;
        }
        this.targetPos = pos;
        final float f = this.getPathSearchRange();
        this.world.profiler.startSection("pathfind");
        final BlockPos blockpos = new BlockPos(this.entity);
        final int i = (int)(f + 8.0f);
        final ChunkCache chunkcache = new ChunkCache(this.world, blockpos.add(-i, -i, -i), blockpos.add(i, i, i), 0);
        final Path path = this.pathFinder.findPath(chunkcache, this.entity, this.targetPos, f);
        this.world.profiler.endSection();
        return path;
    }
    
    @Nullable
    public Path getPathToEntityLiving(final Entity entityIn) {
        if (!this.canNavigate()) {
            return null;
        }
        final BlockPos blockpos = new BlockPos(entityIn);
        if (this.currentPath != null && !this.currentPath.isFinished() && blockpos.equals(this.targetPos)) {
            return this.currentPath;
        }
        this.targetPos = blockpos;
        final float f = this.getPathSearchRange();
        this.world.profiler.startSection("pathfind");
        final BlockPos blockpos2 = new BlockPos(this.entity).up();
        final int i = (int)(f + 16.0f);
        final ChunkCache chunkcache = new ChunkCache(this.world, blockpos2.add(-i, -i, -i), blockpos2.add(i, i, i), 0);
        final Path path = this.pathFinder.findPath(chunkcache, this.entity, entityIn, f);
        this.world.profiler.endSection();
        return path;
    }
    
    public boolean tryMoveToXYZ(final double x, final double y, final double z, final double speedIn) {
        return this.setPath(this.getPathToXYZ(x, y, z), speedIn);
    }
    
    public boolean tryMoveToEntityLiving(final Entity entityIn, final double speedIn) {
        final Path path = this.getPathToEntityLiving(entityIn);
        return path != null && this.setPath(path, speedIn);
    }
    
    public boolean setPath(@Nullable final Path pathentityIn, final double speedIn) {
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
        final Vec3d vec3d = this.getEntityPosition();
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
            }
            else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()) {
                final Vec3d vec3d = this.getEntityPosition();
                final Vec3d vec3d2 = this.currentPath.getVectorFromIndex(this.entity, this.currentPath.getCurrentPathIndex());
                if (vec3d.y > vec3d2.y && !this.entity.onGround && MathHelper.floor(vec3d.x) == MathHelper.floor(vec3d2.x) && MathHelper.floor(vec3d.z) == MathHelper.floor(vec3d2.z)) {
                    this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
                }
            }
            this.debugPathFinding();
            if (!this.noPath()) {
                Vec3d vec3d3 = this.currentPath.getPosition(this.entity);
                final BlockPos blockpos = new BlockPos(vec3d3).down();
                final AxisAlignedBB axisalignedbb = this.world.getBlockState(blockpos).getBoundingBox(this.world, blockpos);
                vec3d3 = vec3d3.subtract(0.0, 1.0 - axisalignedbb.maxY, 0.0);
                this.entity.getMoveHelper().setMoveTo(vec3d3.x, vec3d3.y, vec3d3.z, this.speed);
            }
        }
    }
    
    protected void debugPathFinding() {
    }
    
    protected void pathFollow() {
        final Vec3d vec3d = this.getEntityPosition();
        int i = this.currentPath.getCurrentPathLength();
        for (int j = this.currentPath.getCurrentPathIndex(); j < this.currentPath.getCurrentPathLength(); ++j) {
            if (this.currentPath.getPathPointFromIndex(j).y != Math.floor(vec3d.y)) {
                i = j;
                break;
            }
        }
        this.maxDistanceToWaypoint = ((this.entity.width > 0.75f) ? (this.entity.width / 2.0f) : (0.75f - this.entity.width / 2.0f));
        final Vec3d vec3d2 = this.currentPath.getCurrentPos();
        if (MathHelper.abs((float)(this.entity.posX - (vec3d2.x + 0.5))) < this.maxDistanceToWaypoint && MathHelper.abs((float)(this.entity.posZ - (vec3d2.z + 0.5))) < this.maxDistanceToWaypoint && Math.abs(this.entity.posY - vec3d2.y) < 1.0) {
            this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
        }
        final int k = MathHelper.ceil(this.entity.width);
        final int l = MathHelper.ceil(this.entity.height);
        final int i2 = k;
        for (int j2 = i - 1; j2 >= this.currentPath.getCurrentPathIndex(); --j2) {
            if (this.isDirectPathBetweenPoints(vec3d, this.currentPath.getVectorFromIndex(this.entity, j2), k, l, i2)) {
                this.currentPath.setCurrentPathIndex(j2);
                break;
            }
        }
        this.checkForStuck(vec3d);
    }
    
    protected void checkForStuck(final Vec3d positionVec3) {
        if (this.totalTicks - this.ticksAtLastPos > 100) {
            if (positionVec3.squareDistanceTo(this.lastPosCheck) < 2.25) {
                this.clearPath();
            }
            this.ticksAtLastPos = this.totalTicks;
            this.lastPosCheck = positionVec3;
        }
        if (this.currentPath != null && !this.currentPath.isFinished()) {
            final Vec3d vec3d = this.currentPath.getCurrentPos();
            if (vec3d.equals(this.timeoutCachedNode)) {
                this.timeoutTimer += System.currentTimeMillis() - this.lastTimeoutCheck;
            }
            else {
                this.timeoutCachedNode = vec3d;
                final double d0 = positionVec3.distanceTo(this.timeoutCachedNode);
                this.timeoutLimit = ((this.entity.getAIMoveSpeed() > 0.0f) ? (d0 / this.entity.getAIMoveSpeed() * 1000.0) : 0.0);
            }
            if (this.timeoutLimit > 0.0 && this.timeoutTimer > this.timeoutLimit * 3.0) {
                this.timeoutCachedNode = Vec3d.ZERO;
                this.timeoutTimer = 0L;
                this.timeoutLimit = 0.0;
                this.clearPath();
            }
            this.lastTimeoutCheck = System.currentTimeMillis();
        }
    }
    
    public boolean noPath() {
        return this.currentPath == null || this.currentPath.isFinished();
    }
    
    public void clearPath() {
        this.currentPath = null;
    }
    
    protected abstract Vec3d getEntityPosition();
    
    protected abstract boolean canNavigate();
    
    protected boolean isInLiquid() {
        return this.entity.isInWater() || this.entity.isInLava();
    }
    
    protected void removeSunnyPath() {
        if (this.currentPath != null) {
            for (int i = 0; i < this.currentPath.getCurrentPathLength(); ++i) {
                final PathPoint pathpoint = this.currentPath.getPathPointFromIndex(i);
                final PathPoint pathpoint2 = (i + 1 < this.currentPath.getCurrentPathLength()) ? this.currentPath.getPathPointFromIndex(i + 1) : null;
                final IBlockState iblockstate = this.world.getBlockState(new BlockPos(pathpoint.x, pathpoint.y, pathpoint.z));
                final Block block = iblockstate.getBlock();
                if (block == Blocks.CAULDRON) {
                    this.currentPath.setPoint(i, pathpoint.cloneMove(pathpoint.x, pathpoint.y + 1, pathpoint.z));
                    if (pathpoint2 != null && pathpoint.y >= pathpoint2.y) {
                        this.currentPath.setPoint(i + 1, pathpoint2.cloneMove(pathpoint2.x, pathpoint.y + 1, pathpoint2.z));
                    }
                }
            }
        }
    }
    
    protected abstract boolean isDirectPathBetweenPoints(final Vec3d p0, final Vec3d p1, final int p2, final int p3, final int p4);
    
    public boolean canEntityStandOnPos(final BlockPos pos) {
        return this.world.getBlockState(pos.down()).isFullBlock();
    }
    
    public NodeProcessor getNodeProcessor() {
        return this.nodeProcessor;
    }
}
