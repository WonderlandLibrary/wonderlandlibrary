/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.pathfinding;

import java.util.Iterator;
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
        if (this.theEntity.onGround) return true;
        if (this.getCanSwim()) {
            if (this.isInLiquid()) return true;
        }
        if (!this.theEntity.isRiding()) return false;
        if (!(this.theEntity instanceof EntityZombie)) return false;
        if (!(this.theEntity.ridingEntity instanceof EntityChicken)) return false;
        return true;
    }

    @Override
    protected Vec3 getEntityPosition() {
        return new Vec3(this.theEntity.posX, this.getPathablePosY(), this.theEntity.posZ);
    }

    private int getPathablePosY() {
        if (!this.theEntity.isInWater()) return (int)(this.theEntity.getEntityBoundingBox().minY + 0.5);
        if (!this.getCanSwim()) return (int)(this.theEntity.getEntityBoundingBox().minY + 0.5);
        int i = (int)this.theEntity.getEntityBoundingBox().minY;
        Block block = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.theEntity.posX), i, MathHelper.floor_double(this.theEntity.posZ))).getBlock();
        int j = 0;
        do {
            if (block != Blocks.flowing_water) {
                if (block != Blocks.water) return i;
            }
            block = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.theEntity.posX), ++i, MathHelper.floor_double(this.theEntity.posZ))).getBlock();
        } while (++j <= 16);
        return (int)this.theEntity.getEntityBoundingBox().minY;
    }

    @Override
    protected void removeSunnyPath() {
        super.removeSunnyPath();
        if (!this.shouldAvoidSun) return;
        if (this.worldObj.canSeeSky(new BlockPos(MathHelper.floor_double(this.theEntity.posX), (int)(this.theEntity.getEntityBoundingBox().minY + 0.5), MathHelper.floor_double(this.theEntity.posZ)))) {
            return;
        }
        int i = 0;
        while (i < this.currentPath.getCurrentPathLength()) {
            PathPoint pathpoint = this.currentPath.getPathPointFromIndex(i);
            if (this.worldObj.canSeeSky(new BlockPos(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord))) {
                this.currentPath.setCurrentPathLength(i - 1);
                return;
            }
            ++i;
        }
    }

    @Override
    protected boolean isDirectPathBetweenPoints(Vec3 posVec31, Vec3 posVec32, int sizeX, int sizeY, int sizeZ) {
        int i = MathHelper.floor_double(posVec31.xCoord);
        int j = MathHelper.floor_double(posVec31.zCoord);
        double d0 = posVec32.xCoord - posVec31.xCoord;
        double d1 = posVec32.zCoord - posVec31.zCoord;
        double d2 = d0 * d0 + d1 * d1;
        if (d2 < 1.0E-8) {
            return false;
        }
        double d3 = 1.0 / Math.sqrt(d2);
        if (!this.isSafeToStandAt(i, (int)posVec31.yCoord, j, sizeX += 2, sizeY, sizeZ += 2, posVec31, d0 *= d3, d1 *= d3)) {
            return false;
        }
        sizeX -= 2;
        sizeZ -= 2;
        double d4 = 1.0 / Math.abs(d0);
        double d5 = 1.0 / Math.abs(d1);
        double d6 = (double)(i * 1) - posVec31.xCoord;
        double d7 = (double)(j * 1) - posVec31.zCoord;
        if (d0 >= 0.0) {
            d6 += 1.0;
        }
        if (d1 >= 0.0) {
            d7 += 1.0;
        }
        d6 /= d0;
        d7 /= d1;
        int k = d0 < 0.0 ? -1 : 1;
        int l = d1 < 0.0 ? -1 : 1;
        int i1 = MathHelper.floor_double(posVec32.xCoord);
        int j1 = MathHelper.floor_double(posVec32.zCoord);
        int k1 = i1 - i;
        int l1 = j1 - j;
        do {
            if (k1 * k <= 0) {
                if (l1 * l <= 0) return true;
            }
            if (d6 < d7) {
                d6 += d4;
                k1 = i1 - (i += k);
                continue;
            }
            d7 += d5;
            l1 = j1 - (j += l);
        } while (this.isSafeToStandAt(i, (int)posVec31.yCoord, j, sizeX, sizeY, sizeZ, posVec31, d0, d1));
        return false;
    }

    private boolean isSafeToStandAt(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vec3 vec31, double p_179683_8_, double p_179683_10_) {
        int i = x - sizeX / 2;
        int j = z - sizeZ / 2;
        if (!this.isPositionClear(i, y, j, sizeX, sizeY, sizeZ, vec31, p_179683_8_, p_179683_10_)) {
            return false;
        }
        int k = i;
        while (k < i + sizeX) {
            for (int l = j; l < j + sizeZ; ++l) {
                double d0 = (double)k + 0.5 - vec31.xCoord;
                double d1 = (double)l + 0.5 - vec31.zCoord;
                if (!(d0 * p_179683_8_ + d1 * p_179683_10_ >= 0.0)) continue;
                Block block = this.worldObj.getBlockState(new BlockPos(k, y - 1, l)).getBlock();
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
            ++k;
        }
        return true;
    }

    private boolean isPositionClear(int p_179692_1_, int p_179692_2_, int p_179692_3_, int p_179692_4_, int p_179692_5_, int p_179692_6_, Vec3 p_179692_7_, double p_179692_8_, double p_179692_10_) {
        Block block;
        double d1;
        BlockPos blockpos;
        double d0;
        Iterator<BlockPos> iterator = BlockPos.getAllInBox(new BlockPos(p_179692_1_, p_179692_2_, p_179692_3_), new BlockPos(p_179692_1_ + p_179692_4_ - 1, p_179692_2_ + p_179692_5_ - 1, p_179692_3_ + p_179692_6_ - 1)).iterator();
        do {
            if (!iterator.hasNext()) return true;
        } while (!((d0 = (double)(blockpos = iterator.next()).getX() + 0.5 - p_179692_7_.xCoord) * p_179692_8_ + (d1 = (double)blockpos.getZ() + 0.5 - p_179692_7_.zCoord) * p_179692_10_ >= 0.0) || (block = this.worldObj.getBlockState(blockpos).getBlock()).isPassable(this.worldObj, blockpos));
        return false;
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

