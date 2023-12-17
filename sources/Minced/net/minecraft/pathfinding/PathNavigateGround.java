// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

import java.util.Iterator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;

public class PathNavigateGround extends PathNavigate
{
    private boolean shouldAvoidSun;
    
    public PathNavigateGround(final EntityLiving entitylivingIn, final World worldIn) {
        super(entitylivingIn, worldIn);
    }
    
    @Override
    protected PathFinder getPathFinder() {
        (this.nodeProcessor = new WalkNodeProcessor()).setCanEnterDoors(true);
        return new PathFinder(this.nodeProcessor);
    }
    
    @Override
    protected boolean canNavigate() {
        return this.entity.onGround || (this.getCanSwim() && this.isInLiquid()) || this.entity.isRiding();
    }
    
    @Override
    protected Vec3d getEntityPosition() {
        return new Vec3d(this.entity.posX, this.getPathablePosY(), this.entity.posZ);
    }
    
    @Override
    public Path getPathToPos(BlockPos pos) {
        if (this.world.getBlockState(pos).getMaterial() == Material.AIR) {
            BlockPos blockpos;
            for (blockpos = pos.down(); blockpos.getY() > 0 && this.world.getBlockState(blockpos).getMaterial() == Material.AIR; blockpos = blockpos.down()) {}
            if (blockpos.getY() > 0) {
                return super.getPathToPos(blockpos.up());
            }
            while (blockpos.getY() < this.world.getHeight() && this.world.getBlockState(blockpos).getMaterial() == Material.AIR) {
                blockpos = blockpos.up();
            }
            pos = blockpos;
        }
        if (!this.world.getBlockState(pos).getMaterial().isSolid()) {
            return super.getPathToPos(pos);
        }
        BlockPos blockpos2;
        for (blockpos2 = pos.up(); blockpos2.getY() < this.world.getHeight() && this.world.getBlockState(blockpos2).getMaterial().isSolid(); blockpos2 = blockpos2.up()) {}
        return super.getPathToPos(blockpos2);
    }
    
    @Override
    public Path getPathToEntityLiving(final Entity entityIn) {
        return this.getPathToPos(new BlockPos(entityIn));
    }
    
    private int getPathablePosY() {
        if (this.entity.isInWater() && this.getCanSwim()) {
            int i = (int)this.entity.getEntityBoundingBox().minY;
            Block block = this.world.getBlockState(new BlockPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ))).getBlock();
            int j = 0;
            while (block == Blocks.FLOWING_WATER || block == Blocks.WATER) {
                ++i;
                block = this.world.getBlockState(new BlockPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ))).getBlock();
                if (++j > 16) {
                    return (int)this.entity.getEntityBoundingBox().minY;
                }
            }
            return i;
        }
        return (int)(this.entity.getEntityBoundingBox().minY + 0.5);
    }
    
    @Override
    protected void removeSunnyPath() {
        super.removeSunnyPath();
        if (this.shouldAvoidSun) {
            if (this.world.canSeeSky(new BlockPos(MathHelper.floor(this.entity.posX), (int)(this.entity.getEntityBoundingBox().minY + 0.5), MathHelper.floor(this.entity.posZ)))) {
                return;
            }
            for (int i = 0; i < this.currentPath.getCurrentPathLength(); ++i) {
                final PathPoint pathpoint = this.currentPath.getPathPointFromIndex(i);
                if (this.world.canSeeSky(new BlockPos(pathpoint.x, pathpoint.y, pathpoint.z))) {
                    this.currentPath.setCurrentPathLength(i - 1);
                    return;
                }
            }
        }
    }
    
    @Override
    protected boolean isDirectPathBetweenPoints(final Vec3d posVec31, final Vec3d posVec32, int sizeX, final int sizeY, int sizeZ) {
        int i = MathHelper.floor(posVec31.x);
        int j = MathHelper.floor(posVec31.z);
        double d0 = posVec32.x - posVec31.x;
        double d2 = posVec32.z - posVec31.z;
        final double d3 = d0 * d0 + d2 * d2;
        if (d3 < 1.0E-8) {
            return false;
        }
        final double d4 = 1.0 / Math.sqrt(d3);
        d0 *= d4;
        d2 *= d4;
        sizeX += 2;
        sizeZ += 2;
        if (!this.isSafeToStandAt(i, (int)posVec31.y, j, sizeX, sizeY, sizeZ, posVec31, d0, d2)) {
            return false;
        }
        sizeX -= 2;
        sizeZ -= 2;
        final double d5 = 1.0 / Math.abs(d0);
        final double d6 = 1.0 / Math.abs(d2);
        double d7 = i - posVec31.x;
        double d8 = j - posVec31.z;
        if (d0 >= 0.0) {
            ++d7;
        }
        if (d2 >= 0.0) {
            ++d8;
        }
        d7 /= d0;
        d8 /= d2;
        final int k = (d0 < 0.0) ? -1 : 1;
        final int l = (d2 < 0.0) ? -1 : 1;
        final int i2 = MathHelper.floor(posVec32.x);
        final int j2 = MathHelper.floor(posVec32.z);
        int k2 = i2 - i;
        int l2 = j2 - j;
        while (k2 * k > 0 || l2 * l > 0) {
            if (d7 < d8) {
                d7 += d5;
                i += k;
                k2 = i2 - i;
            }
            else {
                d8 += d6;
                j += l;
                l2 = j2 - j;
            }
            if (!this.isSafeToStandAt(i, (int)posVec31.y, j, sizeX, sizeY, sizeZ, posVec31, d0, d2)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isSafeToStandAt(final int x, final int y, final int z, final int sizeX, final int sizeY, final int sizeZ, final Vec3d vec31, final double p_179683_8_, final double p_179683_10_) {
        final int i = x - sizeX / 2;
        final int j = z - sizeZ / 2;
        if (!this.isPositionClear(i, y, j, sizeX, sizeY, sizeZ, vec31, p_179683_8_, p_179683_10_)) {
            return false;
        }
        for (int k = i; k < i + sizeX; ++k) {
            for (int l = j; l < j + sizeZ; ++l) {
                final double d0 = k + 0.5 - vec31.x;
                final double d2 = l + 0.5 - vec31.z;
                if (d0 * p_179683_8_ + d2 * p_179683_10_ >= 0.0) {
                    PathNodeType pathnodetype = this.nodeProcessor.getPathNodeType(this.world, k, y - 1, l, this.entity, sizeX, sizeY, sizeZ, true, true);
                    if (pathnodetype == PathNodeType.WATER) {
                        return false;
                    }
                    if (pathnodetype == PathNodeType.LAVA) {
                        return false;
                    }
                    if (pathnodetype == PathNodeType.OPEN) {
                        return false;
                    }
                    pathnodetype = this.nodeProcessor.getPathNodeType(this.world, k, y, l, this.entity, sizeX, sizeY, sizeZ, true, true);
                    final float f = this.entity.getPathPriority(pathnodetype);
                    if (f < 0.0f || f >= 8.0f) {
                        return false;
                    }
                    if (pathnodetype == PathNodeType.DAMAGE_FIRE || pathnodetype == PathNodeType.DANGER_FIRE || pathnodetype == PathNodeType.DAMAGE_OTHER) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private boolean isPositionClear(final int x, final int y, final int z, final int sizeX, final int sizeY, final int sizeZ, final Vec3d p_179692_7_, final double p_179692_8_, final double p_179692_10_) {
        for (final BlockPos blockpos : BlockPos.getAllInBox(new BlockPos(x, y, z), new BlockPos(x + sizeX - 1, y + sizeY - 1, z + sizeZ - 1))) {
            final double d0 = blockpos.getX() + 0.5 - p_179692_7_.x;
            final double d2 = blockpos.getZ() + 0.5 - p_179692_7_.z;
            if (d0 * p_179692_8_ + d2 * p_179692_10_ >= 0.0) {
                final Block block = this.world.getBlockState(blockpos).getBlock();
                if (!block.isPassable(this.world, blockpos)) {
                    return false;
                }
                continue;
            }
        }
        return true;
    }
    
    public void setBreakDoors(final boolean canBreakDoors) {
        this.nodeProcessor.setCanOpenDoors(canBreakDoors);
    }
    
    public void setEnterDoors(final boolean enterDoors) {
        this.nodeProcessor.setCanEnterDoors(enterDoors);
    }
    
    public boolean getEnterDoors() {
        return this.nodeProcessor.getCanEnterDoors();
    }
    
    public void setCanSwim(final boolean canSwim) {
        this.nodeProcessor.setCanSwim(canSwim);
    }
    
    public boolean getCanSwim() {
        return this.nodeProcessor.getCanSwim();
    }
    
    public void setAvoidSun(final boolean avoidSun) {
        this.shouldAvoidSun = avoidSun;
    }
}
