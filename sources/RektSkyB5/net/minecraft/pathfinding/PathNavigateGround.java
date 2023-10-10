/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.pathfinding;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.pathfinder.WalkNodeProcessor;

public class PathNavigateGround
extends PathNavigate {
    protected WalkNodeProcessor nodeProcessor;
    private boolean shouldAvoidSun;

    public PathNavigateGround(EntityLiving entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
    }

    @Override
    protected PathFinder getPathFinder() {
        this.nodeProcessor = new WalkNodeProcessor();
        this.nodeProcessor.setEnterDoors(true);
        return new PathFinder(this.nodeProcessor);
    }

    @Override
    protected boolean canNavigate() {
        return this.theEntity.onGround || this.getCanSwim() && this.isInLiquid() || this.theEntity.isRiding() && this.theEntity instanceof EntityZombie && this.theEntity.ridingEntity instanceof EntityChicken;
    }

    @Override
    protected Vec3 getEntityPosition() {
        return new Vec3(this.theEntity.posX, this.getPathablePosY(), this.theEntity.posZ);
    }

    private int getPathablePosY() {
        if (this.theEntity.isInWater() && this.getCanSwim()) {
            int i2 = (int)this.theEntity.getEntityBoundingBox().minY;
            Block block = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.theEntity.posX), i2, MathHelper.floor_double(this.theEntity.posZ))).getBlock();
            int j2 = 0;
            while (block == Blocks.flowing_water || block == Blocks.water) {
                block = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.theEntity.posX), ++i2, MathHelper.floor_double(this.theEntity.posZ))).getBlock();
                if (++j2 <= 16) continue;
                return (int)this.theEntity.getEntityBoundingBox().minY;
            }
            return i2;
        }
        return (int)(this.theEntity.getEntityBoundingBox().minY + 0.5);
    }

    @Override
    protected void removeSunnyPath() {
        super.removeSunnyPath();
        if (this.shouldAvoidSun) {
            if (this.worldObj.canSeeSky(new BlockPos(MathHelper.floor_double(this.theEntity.posX), (int)(this.theEntity.getEntityBoundingBox().minY + 0.5), MathHelper.floor_double(this.theEntity.posZ)))) {
                return;
            }
            for (int i2 = 0; i2 < this.currentPath.getCurrentPathLength(); ++i2) {
                PathPoint pathpoint = this.currentPath.getPathPointFromIndex(i2);
                if (!this.worldObj.canSeeSky(new BlockPos(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord))) continue;
                this.currentPath.setCurrentPathLength(i2 - 1);
                return;
            }
        }
    }

    @Override
    protected boolean isDirectPathBetweenPoints(Vec3 posVec31, Vec3 posVec32, int sizeX, int sizeY, int sizeZ) {
        int i2 = MathHelper.floor_double(posVec31.xCoord);
        int j2 = MathHelper.floor_double(posVec31.zCoord);
        double d0 = posVec32.xCoord - posVec31.xCoord;
        double d1 = posVec32.zCoord - posVec31.zCoord;
        double d2 = d0 * d0 + d1 * d1;
        if (d2 < 1.0E-8) {
            return false;
        }
        double d3 = 1.0 / Math.sqrt(d2);
        if (!this.isSafeToStandAt(i2, (int)posVec31.yCoord, j2, sizeX += 2, sizeY, sizeZ += 2, posVec31, d0 *= d3, d1 *= d3)) {
            return false;
        }
        sizeX -= 2;
        sizeZ -= 2;
        double d4 = 1.0 / Math.abs(d0);
        double d5 = 1.0 / Math.abs(d1);
        double d6 = (double)(i2 * 1) - posVec31.xCoord;
        double d7 = (double)(j2 * 1) - posVec31.zCoord;
        if (d0 >= 0.0) {
            d6 += 1.0;
        }
        if (d1 >= 0.0) {
            d7 += 1.0;
        }
        d6 /= d0;
        d7 /= d1;
        int k2 = d0 < 0.0 ? -1 : 1;
        int l2 = d1 < 0.0 ? -1 : 1;
        int i1 = MathHelper.floor_double(posVec32.xCoord);
        int j1 = MathHelper.floor_double(posVec32.zCoord);
        int k1 = i1 - i2;
        int l1 = j1 - j2;
        while (k1 * k2 > 0 || l1 * l2 > 0) {
            if (d6 < d7) {
                d6 += d4;
                k1 = i1 - (i2 += k2);
            } else {
                d7 += d5;
                l1 = j1 - (j2 += l2);
            }
            if (this.isSafeToStandAt(i2, (int)posVec31.yCoord, j2, sizeX, sizeY, sizeZ, posVec31, d0, d1)) continue;
            return false;
        }
        return true;
    }

    private boolean isSafeToStandAt(int x2, int y2, int z2, int sizeX, int sizeY, int sizeZ, Vec3 vec31, double p_179683_8_, double p_179683_10_) {
        int i2 = x2 - sizeX / 2;
        int j2 = z2 - sizeZ / 2;
        if (!this.isPositionClear(i2, y2, j2, sizeX, sizeY, sizeZ, vec31, p_179683_8_, p_179683_10_)) {
            return false;
        }
        for (int k2 = i2; k2 < i2 + sizeX; ++k2) {
            for (int l2 = j2; l2 < j2 + sizeZ; ++l2) {
                double d0 = (double)k2 + 0.5 - vec31.xCoord;
                double d1 = (double)l2 + 0.5 - vec31.zCoord;
                if (!(d0 * p_179683_8_ + d1 * p_179683_10_ >= 0.0)) continue;
                Block block = this.worldObj.getBlockState(new BlockPos(k2, y2 - 1, l2)).getBlock();
                Material material = block.getMaterial();
                if (material == Material.air) {
                    return false;
                }
                if (material == Material.water && !this.theEntity.isInWater()) {
                    return false;
                }
                if (material != Material.lava) continue;
                return false;
            }
        }
        return true;
    }

    private boolean isPositionClear(int p_179692_1_, int p_179692_2_, int p_179692_3_, int p_179692_4_, int p_179692_5_, int p_179692_6_, Vec3 p_179692_7_, double p_179692_8_, double p_179692_10_) {
        for (BlockPos blockpos : BlockPos.getAllInBox(new BlockPos(p_179692_1_, p_179692_2_, p_179692_3_), new BlockPos(p_179692_1_ + p_179692_4_ - 1, p_179692_2_ + p_179692_5_ - 1, p_179692_3_ + p_179692_6_ - 1))) {
            Block block;
            double d1;
            double d0 = (double)blockpos.getX() + 0.5 - p_179692_7_.xCoord;
            if (!(d0 * p_179692_8_ + (d1 = (double)blockpos.getZ() + 0.5 - p_179692_7_.zCoord) * p_179692_10_ >= 0.0) || (block = this.worldObj.getBlockState(blockpos).getBlock()).isPassable(this.worldObj, blockpos)) continue;
            return false;
        }
        return true;
    }

    public void setAvoidsWater(boolean avoidsWater) {
        this.nodeProcessor.setAvoidsWater(avoidsWater);
    }

    public boolean getAvoidsWater() {
        return this.nodeProcessor.getAvoidsWater();
    }

    public void setBreakDoors(boolean canBreakDoors) {
        this.nodeProcessor.setBreakDoors(canBreakDoors);
    }

    public void setEnterDoors(boolean par1) {
        this.nodeProcessor.setEnterDoors(par1);
    }

    public boolean getEnterDoors() {
        return this.nodeProcessor.getEnterDoors();
    }

    public void setCanSwim(boolean canSwim) {
        this.nodeProcessor.setCanSwim(canSwim);
    }

    public boolean getCanSwim() {
        return this.nodeProcessor.getCanSwim();
    }

    public void setAvoidSun(boolean par1) {
        this.shouldAvoidSun = par1;
    }
}

