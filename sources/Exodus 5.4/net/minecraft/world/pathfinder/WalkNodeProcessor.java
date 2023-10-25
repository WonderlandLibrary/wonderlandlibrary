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
    private boolean canSwim;
    private boolean avoidsWater;
    private boolean shouldAvoidWater;
    private boolean canBreakDoors;
    private boolean canEnterDoors;

    public boolean getEnterDoors() {
        return this.canEnterDoors;
    }

    public void setBreakDoors(boolean bl) {
        this.canBreakDoors = bl;
    }

    public boolean getAvoidsWater() {
        return this.avoidsWater;
    }

    @Override
    public PathPoint getPathPointToCoords(Entity entity, double d, double d2, double d3) {
        return this.openPoint(MathHelper.floor_double(d - (double)(entity.width / 2.0f)), MathHelper.floor_double(d2), MathHelper.floor_double(d3 - (double)(entity.width / 2.0f)));
    }

    public void setCanSwim(boolean bl) {
        this.canSwim = bl;
    }

    public void setEnterDoors(boolean bl) {
        this.canEnterDoors = bl;
    }

    @Override
    public int findPathOptions(PathPoint[] pathPointArray, Entity entity, PathPoint pathPoint, PathPoint pathPoint2, float f) {
        int n = 0;
        int n2 = 0;
        if (this.getVerticalOffset(entity, pathPoint.xCoord, pathPoint.yCoord + 1, pathPoint.zCoord) == 1) {
            n2 = 1;
        }
        PathPoint pathPoint3 = this.getSafePoint(entity, pathPoint.xCoord, pathPoint.yCoord, pathPoint.zCoord + 1, n2);
        PathPoint pathPoint4 = this.getSafePoint(entity, pathPoint.xCoord - 1, pathPoint.yCoord, pathPoint.zCoord, n2);
        PathPoint pathPoint5 = this.getSafePoint(entity, pathPoint.xCoord + 1, pathPoint.yCoord, pathPoint.zCoord, n2);
        PathPoint pathPoint6 = this.getSafePoint(entity, pathPoint.xCoord, pathPoint.yCoord, pathPoint.zCoord - 1, n2);
        if (pathPoint3 != null && !pathPoint3.visited && pathPoint3.distanceTo(pathPoint2) < f) {
            pathPointArray[n++] = pathPoint3;
        }
        if (pathPoint4 != null && !pathPoint4.visited && pathPoint4.distanceTo(pathPoint2) < f) {
            pathPointArray[n++] = pathPoint4;
        }
        if (pathPoint5 != null && !pathPoint5.visited && pathPoint5.distanceTo(pathPoint2) < f) {
            pathPointArray[n++] = pathPoint5;
        }
        if (pathPoint6 != null && !pathPoint6.visited && pathPoint6.distanceTo(pathPoint2) < f) {
            pathPointArray[n++] = pathPoint6;
        }
        return n;
    }

    private PathPoint getSafePoint(Entity entity, int n, int n2, int n3, int n4) {
        PathPoint pathPoint = null;
        int n5 = this.getVerticalOffset(entity, n, n2, n3);
        if (n5 == 2) {
            return this.openPoint(n, n2, n3);
        }
        if (n5 == 1) {
            pathPoint = this.openPoint(n, n2, n3);
        }
        if (pathPoint == null && n4 > 0 && n5 != -3 && n5 != -4 && this.getVerticalOffset(entity, n, n2 + n4, n3) == 1) {
            pathPoint = this.openPoint(n, n2 + n4, n3);
            n2 += n4;
        }
        if (pathPoint != null) {
            int n6 = 0;
            int n7 = 0;
            while (n2 > 0) {
                n7 = this.getVerticalOffset(entity, n, n2 - 1, n3);
                if (this.avoidsWater && n7 == -1) {
                    return null;
                }
                if (n7 != 1) break;
                if (n6++ >= entity.getMaxFallHeight()) {
                    return null;
                }
                if (--n2 <= 0) {
                    return null;
                }
                pathPoint = this.openPoint(n, n2, n3);
            }
            if (n7 == -2) {
                return null;
            }
        }
        return pathPoint;
    }

    public void setAvoidsWater(boolean bl) {
        this.avoidsWater = bl;
    }

    public static int func_176170_a(IBlockAccess iBlockAccess, Entity entity, int n, int n2, int n3, int n4, int n5, int n6, boolean bl, boolean bl2, boolean bl3) {
        boolean bl4 = false;
        BlockPos blockPos = new BlockPos(entity);
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int n7 = n;
        while (n7 < n + n4) {
            int n8 = n2;
            while (n8 < n2 + n5) {
                int n9 = n3;
                while (n9 < n3 + n6) {
                    mutableBlockPos.func_181079_c(n7, n8, n9);
                    Block block = iBlockAccess.getBlockState(mutableBlockPos).getBlock();
                    if (block.getMaterial() != Material.air) {
                        if (block != Blocks.trapdoor && block != Blocks.iron_trapdoor) {
                            if (block != Blocks.flowing_water && block != Blocks.water) {
                                if (!bl3 && block instanceof BlockDoor && block.getMaterial() == Material.wood) {
                                    return 0;
                                }
                            } else {
                                if (bl) {
                                    return -1;
                                }
                                bl4 = true;
                            }
                        } else {
                            bl4 = true;
                        }
                        if (entity.worldObj.getBlockState(mutableBlockPos).getBlock() instanceof BlockRailBase) {
                            if (!(entity.worldObj.getBlockState(blockPos).getBlock() instanceof BlockRailBase) && !(entity.worldObj.getBlockState(blockPos.down()).getBlock() instanceof BlockRailBase)) {
                                return -3;
                            }
                        } else if (!(block.isPassable(iBlockAccess, mutableBlockPos) || bl2 && block instanceof BlockDoor && block.getMaterial() == Material.wood)) {
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
                            if (!entity.isInLava()) {
                                return -2;
                            }
                        }
                    }
                    ++n9;
                }
                ++n8;
            }
            ++n7;
        }
        return bl4 ? 2 : 1;
    }

    @Override
    public void postProcess() {
        super.postProcess();
        this.avoidsWater = this.shouldAvoidWater;
    }

    public boolean getCanSwim() {
        return this.canSwim;
    }

    @Override
    public PathPoint getPathPointTo(Entity entity) {
        int n;
        if (this.canSwim && entity.isInWater()) {
            n = (int)entity.getEntityBoundingBox().minY;
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(MathHelper.floor_double(entity.posX), n, MathHelper.floor_double(entity.posZ));
            Block block = this.blockaccess.getBlockState(mutableBlockPos).getBlock();
            while (block == Blocks.flowing_water || block == Blocks.water) {
                mutableBlockPos.func_181079_c(MathHelper.floor_double(entity.posX), ++n, MathHelper.floor_double(entity.posZ));
                block = this.blockaccess.getBlockState(mutableBlockPos).getBlock();
            }
            this.avoidsWater = false;
        } else {
            n = MathHelper.floor_double(entity.getEntityBoundingBox().minY + 0.5);
        }
        return this.openPoint(MathHelper.floor_double(entity.getEntityBoundingBox().minX), n, MathHelper.floor_double(entity.getEntityBoundingBox().minZ));
    }

    private int getVerticalOffset(Entity entity, int n, int n2, int n3) {
        return WalkNodeProcessor.func_176170_a(this.blockaccess, entity, n, n2, n3, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.avoidsWater, this.canBreakDoors, this.canEnterDoors);
    }

    @Override
    public void initProcessor(IBlockAccess iBlockAccess, Entity entity) {
        super.initProcessor(iBlockAccess, entity);
        this.shouldAvoidWater = this.avoidsWater;
    }
}

