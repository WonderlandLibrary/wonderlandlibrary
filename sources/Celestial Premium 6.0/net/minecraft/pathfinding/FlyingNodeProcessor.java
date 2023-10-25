/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.pathfinding;

import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.HashSet;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class FlyingNodeProcessor
extends WalkNodeProcessor {
    @Override
    public void initProcessor(IBlockAccess sourceIn, EntityLiving mob) {
        super.initProcessor(sourceIn, mob);
        this.avoidsWater = mob.getPathPriority(PathNodeType.WATER);
    }

    @Override
    public void postProcess() {
        this.entity.setPathPriority(PathNodeType.WATER, this.avoidsWater);
        super.postProcess();
    }

    @Override
    public PathPoint getStart() {
        BlockPos blockpos1;
        PathNodeType pathnodetype1;
        int i;
        if (this.getCanSwim() && this.entity.isInWater()) {
            i = (int)this.entity.getEntityBoundingBox().minY;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ));
            Block block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock();
            while (block == Blocks.FLOWING_WATER || block == Blocks.WATER) {
                blockpos$mutableblockpos.setPos(MathHelper.floor(this.entity.posX), ++i, MathHelper.floor(this.entity.posZ));
                block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock();
            }
        } else {
            i = MathHelper.floor(this.entity.getEntityBoundingBox().minY + 0.5);
        }
        if (this.entity.getPathPriority(pathnodetype1 = this.func_192558_a(this.entity, (blockpos1 = new BlockPos(this.entity)).getX(), i, blockpos1.getZ())) < 0.0f) {
            HashSet<BlockPos> set = Sets.newHashSet();
            set.add(new BlockPos(this.entity.getEntityBoundingBox().minX, (double)i, this.entity.getEntityBoundingBox().minZ));
            set.add(new BlockPos(this.entity.getEntityBoundingBox().minX, (double)i, this.entity.getEntityBoundingBox().maxZ));
            set.add(new BlockPos(this.entity.getEntityBoundingBox().maxX, (double)i, this.entity.getEntityBoundingBox().minZ));
            set.add(new BlockPos(this.entity.getEntityBoundingBox().maxX, (double)i, this.entity.getEntityBoundingBox().maxZ));
            for (BlockPos blockpos : set) {
                PathNodeType pathnodetype = this.func_192559_a(this.entity, blockpos);
                if (!(this.entity.getPathPriority(pathnodetype) >= 0.0f)) continue;
                return super.openPoint(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            }
        }
        return super.openPoint(blockpos1.getX(), i, blockpos1.getZ());
    }

    @Override
    public PathPoint getPathPointToCoords(double x, double y, double z) {
        return super.openPoint(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
    }

    @Override
    public int findPathOptions(PathPoint[] pathOptions, PathPoint currentPoint, PathPoint targetPoint, float maxDistance) {
        PathPoint pathpoint17;
        PathPoint pathpoint16;
        PathPoint pathpoint15;
        PathPoint pathpoint14;
        PathPoint pathpoint13;
        PathPoint pathpoint12;
        PathPoint pathpoint11;
        PathPoint pathpoint10;
        PathPoint pathpoint9;
        PathPoint pathpoint8;
        PathPoint pathpoint7;
        PathPoint pathpoint6;
        boolean flag5;
        int i = 0;
        PathPoint pathpoint = this.openPoint(currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord + 1);
        PathPoint pathpoint1 = this.openPoint(currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord);
        PathPoint pathpoint2 = this.openPoint(currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord);
        PathPoint pathpoint3 = this.openPoint(currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord - 1);
        PathPoint pathpoint4 = this.openPoint(currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord);
        PathPoint pathpoint5 = this.openPoint(currentPoint.xCoord, currentPoint.yCoord - 1, currentPoint.zCoord);
        if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint;
        }
        if (pathpoint1 != null && !pathpoint1.visited && pathpoint1.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint1;
        }
        if (pathpoint2 != null && !pathpoint2.visited && pathpoint2.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint2;
        }
        if (pathpoint3 != null && !pathpoint3.visited && pathpoint3.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint3;
        }
        if (pathpoint4 != null && !pathpoint4.visited && pathpoint4.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint4;
        }
        if (pathpoint5 != null && !pathpoint5.visited && pathpoint5.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint5;
        }
        boolean flag = pathpoint3 == null || pathpoint3.costMalus != 0.0f;
        boolean flag1 = pathpoint == null || pathpoint.costMalus != 0.0f;
        boolean flag2 = pathpoint2 == null || pathpoint2.costMalus != 0.0f;
        boolean flag3 = pathpoint1 == null || pathpoint1.costMalus != 0.0f;
        boolean flag4 = pathpoint4 == null || pathpoint4.costMalus != 0.0f;
        boolean bl = flag5 = pathpoint5 == null || pathpoint5.costMalus != 0.0f;
        if (flag && flag3 && (pathpoint6 = this.openPoint(currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord - 1)) != null && !pathpoint6.visited && pathpoint6.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint6;
        }
        if (flag && flag2 && (pathpoint7 = this.openPoint(currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord - 1)) != null && !pathpoint7.visited && pathpoint7.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint7;
        }
        if (flag1 && flag3 && (pathpoint8 = this.openPoint(currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord + 1)) != null && !pathpoint8.visited && pathpoint8.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint8;
        }
        if (flag1 && flag2 && (pathpoint9 = this.openPoint(currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord + 1)) != null && !pathpoint9.visited && pathpoint9.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint9;
        }
        if (flag && flag4 && (pathpoint10 = this.openPoint(currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord - 1)) != null && !pathpoint10.visited && pathpoint10.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint10;
        }
        if (flag1 && flag4 && (pathpoint11 = this.openPoint(currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord + 1)) != null && !pathpoint11.visited && pathpoint11.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint11;
        }
        if (flag2 && flag4 && (pathpoint12 = this.openPoint(currentPoint.xCoord + 1, currentPoint.yCoord + 1, currentPoint.zCoord)) != null && !pathpoint12.visited && pathpoint12.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint12;
        }
        if (flag3 && flag4 && (pathpoint13 = this.openPoint(currentPoint.xCoord - 1, currentPoint.yCoord + 1, currentPoint.zCoord)) != null && !pathpoint13.visited && pathpoint13.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint13;
        }
        if (flag && flag5 && (pathpoint14 = this.openPoint(currentPoint.xCoord, currentPoint.yCoord - 1, currentPoint.zCoord - 1)) != null && !pathpoint14.visited && pathpoint14.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint14;
        }
        if (flag1 && flag5 && (pathpoint15 = this.openPoint(currentPoint.xCoord, currentPoint.yCoord - 1, currentPoint.zCoord + 1)) != null && !pathpoint15.visited && pathpoint15.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint15;
        }
        if (flag2 && flag5 && (pathpoint16 = this.openPoint(currentPoint.xCoord + 1, currentPoint.yCoord - 1, currentPoint.zCoord)) != null && !pathpoint16.visited && pathpoint16.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint16;
        }
        if (flag3 && flag5 && (pathpoint17 = this.openPoint(currentPoint.xCoord - 1, currentPoint.yCoord - 1, currentPoint.zCoord)) != null && !pathpoint17.visited && pathpoint17.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint17;
        }
        return i;
    }

    @Override
    @Nullable
    protected PathPoint openPoint(int x, int y, int z) {
        PathPoint pathpoint = null;
        PathNodeType pathnodetype = this.func_192558_a(this.entity, x, y, z);
        float f = this.entity.getPathPriority(pathnodetype);
        if (f >= 0.0f) {
            pathpoint = super.openPoint(x, y, z);
            pathpoint.nodeType = pathnodetype;
            pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
            if (pathnodetype == PathNodeType.WALKABLE) {
                pathpoint.costMalus += 1.0f;
            }
        }
        return pathnodetype != PathNodeType.OPEN && pathnodetype != PathNodeType.WALKABLE ? pathpoint : pathpoint;
    }

    @Override
    public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z, EntityLiving entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn) {
        EnumSet<PathNodeType> enumset = EnumSet.noneOf(PathNodeType.class);
        PathNodeType pathnodetype = PathNodeType.BLOCKED;
        BlockPos blockpos = new BlockPos(entitylivingIn);
        pathnodetype = this.func_193577_a(blockaccessIn, x, y, z, xSize, ySize, zSize, canBreakDoorsIn, canEnterDoorsIn, enumset, pathnodetype, blockpos);
        if (enumset.contains((Object)PathNodeType.FENCE)) {
            return PathNodeType.FENCE;
        }
        PathNodeType pathnodetype1 = PathNodeType.BLOCKED;
        for (PathNodeType pathnodetype2 : enumset) {
            if (entitylivingIn.getPathPriority(pathnodetype2) < 0.0f) {
                return pathnodetype2;
            }
            if (!(entitylivingIn.getPathPriority(pathnodetype2) >= entitylivingIn.getPathPriority(pathnodetype1))) continue;
            pathnodetype1 = pathnodetype2;
        }
        if (pathnodetype == PathNodeType.OPEN && entitylivingIn.getPathPriority(pathnodetype1) == 0.0f) {
            return PathNodeType.OPEN;
        }
        return pathnodetype1;
    }

    @Override
    public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z) {
        PathNodeType pathnodetype = this.getPathNodeTypeRaw(blockaccessIn, x, y, z);
        if (pathnodetype == PathNodeType.OPEN && y >= 1) {
            Block block = blockaccessIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
            PathNodeType pathnodetype1 = this.getPathNodeTypeRaw(blockaccessIn, x, y - 1, z);
            pathnodetype = pathnodetype1 != PathNodeType.DAMAGE_FIRE && block != Blocks.MAGMA && pathnodetype1 != PathNodeType.LAVA ? (pathnodetype1 == PathNodeType.DAMAGE_CACTUS ? PathNodeType.DAMAGE_CACTUS : (pathnodetype1 != PathNodeType.WALKABLE && pathnodetype1 != PathNodeType.OPEN && pathnodetype1 != PathNodeType.WATER ? PathNodeType.WALKABLE : PathNodeType.OPEN)) : PathNodeType.DAMAGE_FIRE;
        }
        pathnodetype = this.func_193578_a(blockaccessIn, x, y, z, pathnodetype);
        return pathnodetype;
    }

    private PathNodeType func_192559_a(EntityLiving p_192559_1_, BlockPos p_192559_2_) {
        return this.func_192558_a(p_192559_1_, p_192559_2_.getX(), p_192559_2_.getY(), p_192559_2_.getZ());
    }

    private PathNodeType func_192558_a(EntityLiving p_192558_1_, int p_192558_2_, int p_192558_3_, int p_192558_4_) {
        return this.getPathNodeType(this.blockaccess, p_192558_2_, p_192558_3_, p_192558_4_, p_192558_1_, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.getCanBreakDoors(), this.getCanEnterDoors());
    }
}

