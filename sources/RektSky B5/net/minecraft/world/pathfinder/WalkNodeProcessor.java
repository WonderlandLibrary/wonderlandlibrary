/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.pathfinder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.pathfinder.NodeProcessor;

public class WalkNodeProcessor
extends NodeProcessor {
    private boolean canEnterDoors;
    private boolean canBreakDoors;
    private boolean avoidsWater;
    private boolean canSwim;
    private boolean shouldAvoidWater;

    @Override
    public void initProcessor(IBlockAccess iblockaccessIn, Entity entityIn) {
        super.initProcessor(iblockaccessIn, entityIn);
        this.shouldAvoidWater = this.avoidsWater;
    }

    @Override
    public void postProcess() {
        super.postProcess();
        this.avoidsWater = this.shouldAvoidWater;
    }

    @Override
    public PathPoint getPathPointTo(Entity entityIn) {
        int i2;
        if (this.canSwim && entityIn.isInWater()) {
            i2 = (int)entityIn.getEntityBoundingBox().minY;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor_double(entityIn.posX), i2, MathHelper.floor_double(entityIn.posZ));
            Block block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock();
            while (block == Blocks.flowing_water || block == Blocks.water) {
                blockpos$mutableblockpos.func_181079_c(MathHelper.floor_double(entityIn.posX), ++i2, MathHelper.floor_double(entityIn.posZ));
                block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock();
            }
            this.avoidsWater = false;
        } else {
            i2 = MathHelper.floor_double(entityIn.getEntityBoundingBox().minY + 0.5);
        }
        return this.openPoint(MathHelper.floor_double(entityIn.getEntityBoundingBox().minX), i2, MathHelper.floor_double(entityIn.getEntityBoundingBox().minZ));
    }

    @Override
    public PathPoint getPathPointToCoords(Entity entityIn, double x2, double y2, double target) {
        return this.openPoint(MathHelper.floor_double(x2 - (double)(entityIn.width / 2.0f)), MathHelper.floor_double(y2), MathHelper.floor_double(target - (double)(entityIn.width / 2.0f)));
    }

    @Override
    public int findPathOptions(PathPoint[] pathOptions, Entity entityIn, PathPoint currentPoint, PathPoint targetPoint, float maxDistance) {
        int i2 = 0;
        int j2 = 0;
        if (this.getVerticalOffset(entityIn, currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord) == 1) {
            j2 = 1;
        }
        PathPoint pathpoint = this.getSafePoint(entityIn, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord + 1, j2);
        PathPoint pathpoint1 = this.getSafePoint(entityIn, currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord, j2);
        PathPoint pathpoint2 = this.getSafePoint(entityIn, currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord, j2);
        PathPoint pathpoint3 = this.getSafePoint(entityIn, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord - 1, j2);
        if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i2++] = pathpoint;
        }
        if (pathpoint1 != null && !pathpoint1.visited && pathpoint1.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i2++] = pathpoint1;
        }
        if (pathpoint2 != null && !pathpoint2.visited && pathpoint2.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i2++] = pathpoint2;
        }
        if (pathpoint3 != null && !pathpoint3.visited && pathpoint3.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i2++] = pathpoint3;
        }
        return i2;
    }

    private PathPoint getSafePoint(Entity entityIn, int x2, int y2, int z2, int p_176171_5_) {
        PathPoint pathpoint = null;
        int i2 = this.getVerticalOffset(entityIn, x2, y2, z2);
        if (i2 == 2) {
            return this.openPoint(x2, y2, z2);
        }
        if (i2 == 1) {
            pathpoint = this.openPoint(x2, y2, z2);
        }
        if (pathpoint == null && p_176171_5_ > 0 && i2 != -3 && i2 != -4 && this.getVerticalOffset(entityIn, x2, y2 + p_176171_5_, z2) == 1) {
            pathpoint = this.openPoint(x2, y2 + p_176171_5_, z2);
            y2 += p_176171_5_;
        }
        if (pathpoint != null) {
            int j2 = 0;
            int k2 = 0;
            while (y2 > 0) {
                k2 = this.getVerticalOffset(entityIn, x2, y2 - 1, z2);
                if (this.avoidsWater && k2 == -1) {
                    return null;
                }
                if (k2 != 1) break;
                if (j2++ >= entityIn.getMaxFallHeight()) {
                    return null;
                }
                if (--y2 <= 0) {
                    return null;
                }
                pathpoint = this.openPoint(x2, y2, z2);
            }
            if (k2 == -2) {
                return null;
            }
        }
        return pathpoint;
    }

    private int getVerticalOffset(Entity entityIn, int x2, int y2, int z2) {
        return WalkNodeProcessor.func_176170_a(this.blockaccess, entityIn, x2, y2, z2, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.avoidsWater, this.canBreakDoors, this.canEnterDoors);
    }

    public static int func_176170_a(IBlockAccess blockaccessIn, Entity entityIn, int x2, int y2, int z2, int sizeX, int sizeY, int sizeZ, boolean avoidWater, boolean breakDoors, boolean enterDoors) {
        boolean flag = false;
        BlockPos blockpos = new BlockPos(entityIn);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int i2 = x2; i2 < x2 + sizeX; ++i2) {
            for (int j2 = y2; j2 < y2 + sizeY; ++j2) {
                for (int k2 = z2; k2 < z2 + sizeZ; ++k2) {
                    blockpos$mutableblockpos.func_181079_c(i2, j2, k2);
                    Block block = blockaccessIn.getBlockState(blockpos$mutableblockpos).getBlock();
                    if (block.getMaterial() == Material.air) continue;
                    if (block != Blocks.trapdoor && block != Blocks.iron_trapdoor) {
                        if (block != Blocks.flowing_water && block != Blocks.water) {
                            if (!enterDoors && block instanceof BlockDoor && block.getMaterial() == Material.wood) {
                                return 0;
                            }
                        } else {
                            if (avoidWater) {
                                return -1;
                            }
                            flag = true;
                        }
                    } else {
                        flag = true;
                    }
                    if (entityIn.worldObj.getBlockState(blockpos$mutableblockpos).getBlock() instanceof BlockRailBase) {
                        if (entityIn.worldObj.getBlockState(blockpos).getBlock() instanceof BlockRailBase || entityIn.worldObj.getBlockState(blockpos.down()).getBlock() instanceof BlockRailBase) continue;
                        return -3;
                    }
                    if (block.isPassable(blockaccessIn, blockpos$mutableblockpos) || breakDoors && block instanceof BlockDoor && block.getMaterial() == Material.wood) continue;
                    if (block instanceof BlockFence || block instanceof BlockFenceGate || block instanceof BlockWall) {
                        return -3;
                    }
                    if (block == Blocks.trapdoor || block == Blocks.iron_trapdoor) {
                        return -4;
                    }
                    Material material = block.getMaterial();
                    if (material != Material.lava) {
                        return 0;
                    }
                    if (entityIn.isInLava()) continue;
                    return -2;
                }
            }
        }
        return flag ? 2 : 1;
    }

    public void setEnterDoors(boolean canEnterDoorsIn) {
        this.canEnterDoors = canEnterDoorsIn;
    }

    public void setBreakDoors(boolean canBreakDoorsIn) {
        this.canBreakDoors = canBreakDoorsIn;
    }

    public void setAvoidsWater(boolean avoidsWaterIn) {
        this.avoidsWater = avoidsWaterIn;
    }

    public void setCanSwim(boolean canSwimIn) {
        this.canSwim = canSwimIn;
    }

    public boolean getEnterDoors() {
        return this.canEnterDoors;
    }

    public boolean getCanSwim() {
        return this.canSwim;
    }

    public boolean getAvoidsWater() {
        return this.avoidsWater;
    }
}

