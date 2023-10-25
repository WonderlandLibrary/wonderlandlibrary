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
    private boolean shouldAvoidSun;
    protected WalkNodeProcessor nodeProcessor;

    public void setBreakDoors(boolean bl) {
        this.nodeProcessor.setBreakDoors(bl);
    }

    public PathNavigateGround(EntityLiving entityLiving, World world) {
        super(entityLiving, world);
    }

    @Override
    protected boolean isDirectPathBetweenPoints(Vec3 vec3, Vec3 vec32, int n, int n2, int n3) {
        int n4 = MathHelper.floor_double(vec3.xCoord);
        int n5 = MathHelper.floor_double(vec3.zCoord);
        double d = vec32.xCoord - vec3.xCoord;
        double d2 = vec32.zCoord - vec3.zCoord;
        double d3 = d * d + d2 * d2;
        if (d3 < 1.0E-8) {
            return false;
        }
        double d4 = 1.0 / Math.sqrt(d3);
        if (!this.isSafeToStandAt(n4, (int)vec3.yCoord, n5, n += 2, n2, n3 += 2, vec3, d *= d4, d2 *= d4)) {
            return false;
        }
        n -= 2;
        n3 -= 2;
        double d5 = 1.0 / Math.abs(d);
        double d6 = 1.0 / Math.abs(d2);
        double d7 = (double)(n4 * 1) - vec3.xCoord;
        double d8 = (double)(n5 * 1) - vec3.zCoord;
        if (d >= 0.0) {
            d7 += 1.0;
        }
        if (d2 >= 0.0) {
            d8 += 1.0;
        }
        d7 /= d;
        d8 /= d2;
        int n6 = d < 0.0 ? -1 : 1;
        int n7 = d2 < 0.0 ? -1 : 1;
        int n8 = MathHelper.floor_double(vec32.xCoord);
        int n9 = MathHelper.floor_double(vec32.zCoord);
        int n10 = n8 - n4;
        int n11 = n9 - n5;
        while (n10 * n6 > 0 || n11 * n7 > 0) {
            if (d7 < d8) {
                d7 += d5;
                n10 = n8 - (n4 += n6);
            } else {
                d8 += d6;
                n11 = n9 - (n5 += n7);
            }
            if (this.isSafeToStandAt(n4, (int)vec3.yCoord, n5, n, n2, n3, vec3, d, d2)) continue;
            return false;
        }
        return true;
    }

    private boolean isPositionClear(int n, int n2, int n3, int n4, int n5, int n6, Vec3 vec3, double d, double d2) {
        for (BlockPos blockPos : BlockPos.getAllInBox(new BlockPos(n, n2, n3), new BlockPos(n + n4 - 1, n2 + n5 - 1, n3 + n6 - 1))) {
            Block block;
            double d3;
            double d4 = (double)blockPos.getX() + 0.5 - vec3.xCoord;
            if (!(d4 * d + (d3 = (double)blockPos.getZ() + 0.5 - vec3.zCoord) * d2 >= 0.0) || (block = this.worldObj.getBlockState(blockPos).getBlock()).isPassable(this.worldObj, blockPos)) continue;
            return false;
        }
        return true;
    }

    public boolean getAvoidsWater() {
        return this.nodeProcessor.getAvoidsWater();
    }

    public void setCanSwim(boolean bl) {
        this.nodeProcessor.setCanSwim(bl);
    }

    private int getPathablePosY() {
        if (this.theEntity.isInWater() && this.getCanSwim()) {
            int n = (int)this.theEntity.getEntityBoundingBox().minY;
            Block block = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.theEntity.posX), n, MathHelper.floor_double(this.theEntity.posZ))).getBlock();
            int n2 = 0;
            while (block == Blocks.flowing_water || block == Blocks.water) {
                block = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.theEntity.posX), ++n, MathHelper.floor_double(this.theEntity.posZ))).getBlock();
                if (++n2 <= 16) continue;
                return (int)this.theEntity.getEntityBoundingBox().minY;
            }
            return n;
        }
        return (int)(this.theEntity.getEntityBoundingBox().minY + 0.5);
    }

    public boolean getEnterDoors() {
        return this.nodeProcessor.getEnterDoors();
    }

    public void setAvoidsWater(boolean bl) {
        this.nodeProcessor.setAvoidsWater(bl);
    }

    @Override
    protected Vec3 getEntityPosition() {
        return new Vec3(this.theEntity.posX, this.getPathablePosY(), this.theEntity.posZ);
    }

    @Override
    protected boolean canNavigate() {
        return this.theEntity.onGround || this.getCanSwim() && this.isInLiquid() || this.theEntity.isRiding() && this.theEntity instanceof EntityZombie && this.theEntity.ridingEntity instanceof EntityChicken;
    }

    public boolean getCanSwim() {
        return this.nodeProcessor.getCanSwim();
    }

    private boolean isSafeToStandAt(int n, int n2, int n3, int n4, int n5, int n6, Vec3 vec3, double d, double d2) {
        int n7 = n - n4 / 2;
        int n8 = n3 - n6 / 2;
        if (!this.isPositionClear(n7, n2, n8, n4, n5, n6, vec3, d, d2)) {
            return false;
        }
        int n9 = n7;
        while (n9 < n7 + n4) {
            int n10 = n8;
            while (n10 < n8 + n6) {
                double d3 = (double)n9 + 0.5 - vec3.xCoord;
                double d4 = (double)n10 + 0.5 - vec3.zCoord;
                if (d3 * d + d4 * d2 >= 0.0) {
                    Block block = this.worldObj.getBlockState(new BlockPos(n9, n2 - 1, n10)).getBlock();
                    Material material = block.getMaterial();
                    if (material == Material.air) {
                        return false;
                    }
                    if (material == Material.water && !this.theEntity.isInWater()) {
                        return false;
                    }
                    if (material == Material.lava) {
                        return false;
                    }
                }
                ++n10;
            }
            ++n9;
        }
        return true;
    }

    public void setAvoidSun(boolean bl) {
        this.shouldAvoidSun = bl;
    }

    @Override
    protected void removeSunnyPath() {
        super.removeSunnyPath();
        if (this.shouldAvoidSun) {
            if (this.worldObj.canSeeSky(new BlockPos(MathHelper.floor_double(this.theEntity.posX), (int)(this.theEntity.getEntityBoundingBox().minY + 0.5), MathHelper.floor_double(this.theEntity.posZ)))) {
                return;
            }
            int n = 0;
            while (n < this.currentPath.getCurrentPathLength()) {
                PathPoint pathPoint = this.currentPath.getPathPointFromIndex(n);
                if (this.worldObj.canSeeSky(new BlockPos(pathPoint.xCoord, pathPoint.yCoord, pathPoint.zCoord))) {
                    this.currentPath.setCurrentPathLength(n - 1);
                    return;
                }
                ++n;
            }
        }
    }

    @Override
    protected PathFinder getPathFinder() {
        this.nodeProcessor = new WalkNodeProcessor();
        this.nodeProcessor.setEnterDoors(true);
        return new PathFinder(this.nodeProcessor);
    }

    public void setEnterDoors(boolean bl) {
        this.nodeProcessor.setEnterDoors(bl);
    }
}

