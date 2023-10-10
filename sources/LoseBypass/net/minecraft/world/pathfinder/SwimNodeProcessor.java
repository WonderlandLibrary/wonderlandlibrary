/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.pathfinder;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.pathfinder.NodeProcessor;

public class SwimNodeProcessor
extends NodeProcessor {
    @Override
    public void initProcessor(IBlockAccess iblockaccessIn, Entity entityIn) {
        super.initProcessor(iblockaccessIn, entityIn);
    }

    @Override
    public void postProcess() {
        super.postProcess();
    }

    @Override
    public PathPoint getPathPointTo(Entity entityIn) {
        return this.openPoint(MathHelper.floor_double(entityIn.getEntityBoundingBox().minX), MathHelper.floor_double(entityIn.getEntityBoundingBox().minY + 0.5), MathHelper.floor_double(entityIn.getEntityBoundingBox().minZ));
    }

    @Override
    public PathPoint getPathPointToCoords(Entity entityIn, double x, double y, double target) {
        return this.openPoint(MathHelper.floor_double(x - (double)(entityIn.width / 2.0f)), MathHelper.floor_double(y + 0.5), MathHelper.floor_double(target - (double)(entityIn.width / 2.0f)));
    }

    @Override
    public int findPathOptions(PathPoint[] pathOptions, Entity entityIn, PathPoint currentPoint, PathPoint targetPoint, float maxDistance) {
        int i = 0;
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumfacing = enumFacingArray[n2];
            PathPoint pathpoint = this.getSafePoint(entityIn, currentPoint.xCoord + enumfacing.getFrontOffsetX(), currentPoint.yCoord + enumfacing.getFrontOffsetY(), currentPoint.zCoord + enumfacing.getFrontOffsetZ());
            if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint;
            }
            ++n2;
        }
        return i;
    }

    private PathPoint getSafePoint(Entity entityIn, int x, int y, int z) {
        int i = this.func_176186_b(entityIn, x, y, z);
        if (i != -1) return null;
        PathPoint pathPoint = this.openPoint(x, y, z);
        return pathPoint;
    }

    private int func_176186_b(Entity entityIn, int x, int y, int z) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int i = x;
        block0: while (i < x + this.entitySizeX) {
            int j = y;
            while (true) {
                if (j < y + this.entitySizeY) {
                } else {
                    ++i;
                    continue block0;
                }
                for (int k = z; k < z + this.entitySizeZ; ++k) {
                    Block block = this.blockaccess.getBlockState(blockpos$mutableblockpos.func_181079_c(i, j, k)).getBlock();
                    if (block.getMaterial() == Material.water) continue;
                    return 0;
                }
                ++j;
            }
            break;
        }
        return -1;
    }
}

