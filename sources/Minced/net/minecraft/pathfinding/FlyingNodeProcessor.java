// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

import java.util.EnumSet;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.block.Block;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.IBlockAccess;

public class FlyingNodeProcessor extends WalkNodeProcessor
{
    @Override
    public void init(final IBlockAccess sourceIn, final EntityLiving mob) {
        super.init(sourceIn, mob);
        this.avoidsWater = mob.getPathPriority(PathNodeType.WATER);
    }
    
    @Override
    public void postProcess() {
        this.entity.setPathPriority(PathNodeType.WATER, this.avoidsWater);
        super.postProcess();
    }
    
    @Override
    public PathPoint getStart() {
        int i;
        if (this.getCanSwim() && this.entity.isInWater()) {
            i = (int)this.entity.getEntityBoundingBox().minY;
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ));
            for (Block block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock(); block == Blocks.FLOWING_WATER || block == Blocks.WATER; block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock()) {
                ++i;
                blockpos$mutableblockpos.setPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ));
            }
        }
        else {
            i = MathHelper.floor(this.entity.getEntityBoundingBox().minY + 0.5);
        }
        final BlockPos blockpos1 = new BlockPos(this.entity);
        final PathNodeType pathnodetype1 = this.getPathNodeType(this.entity, blockpos1.getX(), i, blockpos1.getZ());
        if (this.entity.getPathPriority(pathnodetype1) < 0.0f) {
            final Set<BlockPos> set = (Set<BlockPos>)Sets.newHashSet();
            set.add(new BlockPos(this.entity.getEntityBoundingBox().minX, i, this.entity.getEntityBoundingBox().minZ));
            set.add(new BlockPos(this.entity.getEntityBoundingBox().minX, i, this.entity.getEntityBoundingBox().maxZ));
            set.add(new BlockPos(this.entity.getEntityBoundingBox().maxX, i, this.entity.getEntityBoundingBox().minZ));
            set.add(new BlockPos(this.entity.getEntityBoundingBox().maxX, i, this.entity.getEntityBoundingBox().maxZ));
            for (final BlockPos blockpos2 : set) {
                final PathNodeType pathnodetype2 = this.getPathNodeType(this.entity, blockpos2);
                if (this.entity.getPathPriority(pathnodetype2) >= 0.0f) {
                    return super.openPoint(blockpos2.getX(), blockpos2.getY(), blockpos2.getZ());
                }
            }
        }
        return super.openPoint(blockpos1.getX(), i, blockpos1.getZ());
    }
    
    @Override
    public PathPoint getPathPointToCoords(final double x, final double y, final double z) {
        return super.openPoint(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
    }
    
    @Override
    public int findPathOptions(final PathPoint[] pathOptions, final PathPoint currentPoint, final PathPoint targetPoint, final float maxDistance) {
        int i = 0;
        final PathPoint pathpoint = this.openPoint(currentPoint.x, currentPoint.y, currentPoint.z + 1);
        final PathPoint pathpoint2 = this.openPoint(currentPoint.x - 1, currentPoint.y, currentPoint.z);
        final PathPoint pathpoint3 = this.openPoint(currentPoint.x + 1, currentPoint.y, currentPoint.z);
        final PathPoint pathpoint4 = this.openPoint(currentPoint.x, currentPoint.y, currentPoint.z - 1);
        final PathPoint pathpoint5 = this.openPoint(currentPoint.x, currentPoint.y + 1, currentPoint.z);
        final PathPoint pathpoint6 = this.openPoint(currentPoint.x, currentPoint.y - 1, currentPoint.z);
        if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint;
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
        if (pathpoint6 != null && !pathpoint6.visited && pathpoint6.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint6;
        }
        final boolean flag = pathpoint4 == null || pathpoint4.costMalus != 0.0f;
        final boolean flag2 = pathpoint == null || pathpoint.costMalus != 0.0f;
        final boolean flag3 = pathpoint3 == null || pathpoint3.costMalus != 0.0f;
        final boolean flag4 = pathpoint2 == null || pathpoint2.costMalus != 0.0f;
        final boolean flag5 = pathpoint5 == null || pathpoint5.costMalus != 0.0f;
        final boolean flag6 = pathpoint6 == null || pathpoint6.costMalus != 0.0f;
        if (flag && flag4) {
            final PathPoint pathpoint7 = this.openPoint(currentPoint.x - 1, currentPoint.y, currentPoint.z - 1);
            if (pathpoint7 != null && !pathpoint7.visited && pathpoint7.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint7;
            }
        }
        if (flag && flag3) {
            final PathPoint pathpoint8 = this.openPoint(currentPoint.x + 1, currentPoint.y, currentPoint.z - 1);
            if (pathpoint8 != null && !pathpoint8.visited && pathpoint8.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint8;
            }
        }
        if (flag2 && flag4) {
            final PathPoint pathpoint9 = this.openPoint(currentPoint.x - 1, currentPoint.y, currentPoint.z + 1);
            if (pathpoint9 != null && !pathpoint9.visited && pathpoint9.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint9;
            }
        }
        if (flag2 && flag3) {
            final PathPoint pathpoint10 = this.openPoint(currentPoint.x + 1, currentPoint.y, currentPoint.z + 1);
            if (pathpoint10 != null && !pathpoint10.visited && pathpoint10.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint10;
            }
        }
        if (flag && flag5) {
            final PathPoint pathpoint11 = this.openPoint(currentPoint.x, currentPoint.y + 1, currentPoint.z - 1);
            if (pathpoint11 != null && !pathpoint11.visited && pathpoint11.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint11;
            }
        }
        if (flag2 && flag5) {
            final PathPoint pathpoint12 = this.openPoint(currentPoint.x, currentPoint.y + 1, currentPoint.z + 1);
            if (pathpoint12 != null && !pathpoint12.visited && pathpoint12.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint12;
            }
        }
        if (flag3 && flag5) {
            final PathPoint pathpoint13 = this.openPoint(currentPoint.x + 1, currentPoint.y + 1, currentPoint.z);
            if (pathpoint13 != null && !pathpoint13.visited && pathpoint13.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint13;
            }
        }
        if (flag4 && flag5) {
            final PathPoint pathpoint14 = this.openPoint(currentPoint.x - 1, currentPoint.y + 1, currentPoint.z);
            if (pathpoint14 != null && !pathpoint14.visited && pathpoint14.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint14;
            }
        }
        if (flag && flag6) {
            final PathPoint pathpoint15 = this.openPoint(currentPoint.x, currentPoint.y - 1, currentPoint.z - 1);
            if (pathpoint15 != null && !pathpoint15.visited && pathpoint15.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint15;
            }
        }
        if (flag2 && flag6) {
            final PathPoint pathpoint16 = this.openPoint(currentPoint.x, currentPoint.y - 1, currentPoint.z + 1);
            if (pathpoint16 != null && !pathpoint16.visited && pathpoint16.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint16;
            }
        }
        if (flag3 && flag6) {
            final PathPoint pathpoint17 = this.openPoint(currentPoint.x + 1, currentPoint.y - 1, currentPoint.z);
            if (pathpoint17 != null && !pathpoint17.visited && pathpoint17.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint17;
            }
        }
        if (flag4 && flag6) {
            final PathPoint pathpoint18 = this.openPoint(currentPoint.x - 1, currentPoint.y - 1, currentPoint.z);
            if (pathpoint18 != null && !pathpoint18.visited && pathpoint18.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint18;
            }
        }
        return i;
    }
    
    @Nullable
    @Override
    protected PathPoint openPoint(final int x, final int y, final int z) {
        PathPoint pathpoint = null;
        final PathNodeType pathnodetype = this.getPathNodeType(this.entity, x, y, z);
        final float f = this.entity.getPathPriority(pathnodetype);
        if (f >= 0.0f) {
            pathpoint = super.openPoint(x, y, z);
            pathpoint.nodeType = pathnodetype;
            pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
            if (pathnodetype == PathNodeType.WALKABLE) {
                final PathPoint pathPoint = pathpoint;
                ++pathPoint.costMalus;
            }
        }
        return (pathnodetype != PathNodeType.OPEN && pathnodetype != PathNodeType.WALKABLE) ? pathpoint : pathpoint;
    }
    
    @Override
    public PathNodeType getPathNodeType(final IBlockAccess blockaccessIn, final int x, final int y, final int z, final EntityLiving entitylivingIn, final int xSize, final int ySize, final int zSize, final boolean canBreakDoorsIn, final boolean canEnterDoorsIn) {
        final EnumSet<PathNodeType> enumset = EnumSet.noneOf(PathNodeType.class);
        PathNodeType pathnodetype = PathNodeType.BLOCKED;
        final BlockPos blockpos = new BlockPos(entitylivingIn);
        pathnodetype = this.getPathNodeType(blockaccessIn, x, y, z, xSize, ySize, zSize, canBreakDoorsIn, canEnterDoorsIn, enumset, pathnodetype, blockpos);
        if (enumset.contains(PathNodeType.FENCE)) {
            return PathNodeType.FENCE;
        }
        PathNodeType pathnodetype2 = PathNodeType.BLOCKED;
        for (final PathNodeType pathnodetype3 : enumset) {
            if (entitylivingIn.getPathPriority(pathnodetype3) < 0.0f) {
                return pathnodetype3;
            }
            if (entitylivingIn.getPathPriority(pathnodetype3) < entitylivingIn.getPathPriority(pathnodetype2)) {
                continue;
            }
            pathnodetype2 = pathnodetype3;
        }
        if (pathnodetype == PathNodeType.OPEN && entitylivingIn.getPathPriority(pathnodetype2) == 0.0f) {
            return PathNodeType.OPEN;
        }
        return pathnodetype2;
    }
    
    @Override
    public PathNodeType getPathNodeType(final IBlockAccess blockaccessIn, final int x, final int y, final int z) {
        PathNodeType pathnodetype = this.getPathNodeTypeRaw(blockaccessIn, x, y, z);
        if (pathnodetype == PathNodeType.OPEN && y >= 1) {
            final Block block = blockaccessIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
            final PathNodeType pathnodetype2 = this.getPathNodeTypeRaw(blockaccessIn, x, y - 1, z);
            if (pathnodetype2 != PathNodeType.DAMAGE_FIRE && block != Blocks.MAGMA && pathnodetype2 != PathNodeType.LAVA) {
                if (pathnodetype2 == PathNodeType.DAMAGE_CACTUS) {
                    pathnodetype = PathNodeType.DAMAGE_CACTUS;
                }
                else {
                    pathnodetype = ((pathnodetype2 != PathNodeType.WALKABLE && pathnodetype2 != PathNodeType.OPEN && pathnodetype2 != PathNodeType.WATER) ? PathNodeType.WALKABLE : PathNodeType.OPEN);
                }
            }
            else {
                pathnodetype = PathNodeType.DAMAGE_FIRE;
            }
        }
        pathnodetype = this.checkNeighborBlocks(blockaccessIn, x, y, z, pathnodetype);
        return pathnodetype;
    }
    
    private PathNodeType getPathNodeType(final EntityLiving p_192559_1_, final BlockPos p_192559_2_) {
        return this.getPathNodeType(p_192559_1_, p_192559_2_.getX(), p_192559_2_.getY(), p_192559_2_.getZ());
    }
    
    private PathNodeType getPathNodeType(final EntityLiving p_192558_1_, final int p_192558_2_, final int p_192558_3_, final int p_192558_4_) {
        return this.getPathNodeType(this.blockaccess, p_192558_2_, p_192558_3_, p_192558_4_, p_192558_1_, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.getCanOpenDoors(), this.getCanEnterDoors());
    }
}
