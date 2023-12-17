// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockFence;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockRailBase;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.block.Block;
import com.google.common.collect.Sets;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.IBlockAccess;

public class WalkNodeProcessor extends NodeProcessor
{
    protected float avoidsWater;
    
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
        else if (this.entity.onGround) {
            i = MathHelper.floor(this.entity.getEntityBoundingBox().minY + 0.5);
        }
        else {
            BlockPos blockpos;
            for (blockpos = new BlockPos(this.entity); (this.blockaccess.getBlockState(blockpos).getMaterial() == Material.AIR || this.blockaccess.getBlockState(blockpos).getBlock().isPassable(this.blockaccess, blockpos)) && blockpos.getY() > 0; blockpos = blockpos.down()) {}
            i = blockpos.up().getY();
        }
        final BlockPos blockpos2 = new BlockPos(this.entity);
        final PathNodeType pathnodetype1 = this.getPathNodeType(this.entity, blockpos2.getX(), i, blockpos2.getZ());
        if (this.entity.getPathPriority(pathnodetype1) < 0.0f) {
            final Set<BlockPos> set = (Set<BlockPos>)Sets.newHashSet();
            set.add(new BlockPos(this.entity.getEntityBoundingBox().minX, i, this.entity.getEntityBoundingBox().minZ));
            set.add(new BlockPos(this.entity.getEntityBoundingBox().minX, i, this.entity.getEntityBoundingBox().maxZ));
            set.add(new BlockPos(this.entity.getEntityBoundingBox().maxX, i, this.entity.getEntityBoundingBox().minZ));
            set.add(new BlockPos(this.entity.getEntityBoundingBox().maxX, i, this.entity.getEntityBoundingBox().maxZ));
            for (final BlockPos blockpos3 : set) {
                final PathNodeType pathnodetype2 = this.getPathNodeType(this.entity, blockpos3);
                if (this.entity.getPathPriority(pathnodetype2) >= 0.0f) {
                    return this.openPoint(blockpos3.getX(), blockpos3.getY(), blockpos3.getZ());
                }
            }
        }
        return this.openPoint(blockpos2.getX(), i, blockpos2.getZ());
    }
    
    @Override
    public PathPoint getPathPointToCoords(final double x, final double y, final double z) {
        return this.openPoint(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
    }
    
    @Override
    public int findPathOptions(final PathPoint[] pathOptions, final PathPoint currentPoint, final PathPoint targetPoint, final float maxDistance) {
        int i = 0;
        int j = 0;
        final PathNodeType pathnodetype = this.getPathNodeType(this.entity, currentPoint.x, currentPoint.y + 1, currentPoint.z);
        if (this.entity.getPathPriority(pathnodetype) >= 0.0f) {
            j = MathHelper.floor(Math.max(1.0f, this.entity.stepHeight));
        }
        final BlockPos blockpos = new BlockPos(currentPoint.x, currentPoint.y, currentPoint.z).down();
        final double d0 = currentPoint.y - (1.0 - this.blockaccess.getBlockState(blockpos).getBoundingBox(this.blockaccess, blockpos).maxY);
        final PathPoint pathpoint = this.getSafePoint(currentPoint.x, currentPoint.y, currentPoint.z + 1, j, d0, EnumFacing.SOUTH);
        final PathPoint pathpoint2 = this.getSafePoint(currentPoint.x - 1, currentPoint.y, currentPoint.z, j, d0, EnumFacing.WEST);
        final PathPoint pathpoint3 = this.getSafePoint(currentPoint.x + 1, currentPoint.y, currentPoint.z, j, d0, EnumFacing.EAST);
        final PathPoint pathpoint4 = this.getSafePoint(currentPoint.x, currentPoint.y, currentPoint.z - 1, j, d0, EnumFacing.NORTH);
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
        final boolean flag = pathpoint4 == null || pathpoint4.nodeType == PathNodeType.OPEN || pathpoint4.costMalus != 0.0f;
        final boolean flag2 = pathpoint == null || pathpoint.nodeType == PathNodeType.OPEN || pathpoint.costMalus != 0.0f;
        final boolean flag3 = pathpoint3 == null || pathpoint3.nodeType == PathNodeType.OPEN || pathpoint3.costMalus != 0.0f;
        final boolean flag4 = pathpoint2 == null || pathpoint2.nodeType == PathNodeType.OPEN || pathpoint2.costMalus != 0.0f;
        if (flag && flag4) {
            final PathPoint pathpoint5 = this.getSafePoint(currentPoint.x - 1, currentPoint.y, currentPoint.z - 1, j, d0, EnumFacing.NORTH);
            if (pathpoint5 != null && !pathpoint5.visited && pathpoint5.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint5;
            }
        }
        if (flag && flag3) {
            final PathPoint pathpoint6 = this.getSafePoint(currentPoint.x + 1, currentPoint.y, currentPoint.z - 1, j, d0, EnumFacing.NORTH);
            if (pathpoint6 != null && !pathpoint6.visited && pathpoint6.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint6;
            }
        }
        if (flag2 && flag4) {
            final PathPoint pathpoint7 = this.getSafePoint(currentPoint.x - 1, currentPoint.y, currentPoint.z + 1, j, d0, EnumFacing.SOUTH);
            if (pathpoint7 != null && !pathpoint7.visited && pathpoint7.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint7;
            }
        }
        if (flag2 && flag3) {
            final PathPoint pathpoint8 = this.getSafePoint(currentPoint.x + 1, currentPoint.y, currentPoint.z + 1, j, d0, EnumFacing.SOUTH);
            if (pathpoint8 != null && !pathpoint8.visited && pathpoint8.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint8;
            }
        }
        return i;
    }
    
    @Nullable
    private PathPoint getSafePoint(final int x, int y, final int z, final int p_186332_4_, final double p_186332_5_, final EnumFacing facing) {
        PathPoint pathpoint = null;
        final BlockPos blockpos = new BlockPos(x, y, z);
        final BlockPos blockpos2 = blockpos.down();
        final double d0 = y - (1.0 - this.blockaccess.getBlockState(blockpos2).getBoundingBox(this.blockaccess, blockpos2).maxY);
        if (d0 - p_186332_5_ > 1.125) {
            return null;
        }
        PathNodeType pathnodetype = this.getPathNodeType(this.entity, x, y, z);
        float f = this.entity.getPathPriority(pathnodetype);
        final double d2 = this.entity.width / 2.0;
        if (f >= 0.0f) {
            pathpoint = this.openPoint(x, y, z);
            pathpoint.nodeType = pathnodetype;
            pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
        }
        if (pathnodetype == PathNodeType.WALKABLE) {
            return pathpoint;
        }
        if (pathpoint == null && p_186332_4_ > 0 && pathnodetype != PathNodeType.FENCE && pathnodetype != PathNodeType.TRAPDOOR) {
            pathpoint = this.getSafePoint(x, y + 1, z, p_186332_4_ - 1, p_186332_5_, facing);
            if (pathpoint != null && (pathpoint.nodeType == PathNodeType.OPEN || pathpoint.nodeType == PathNodeType.WALKABLE) && this.entity.width < 1.0f) {
                final double d3 = x - facing.getXOffset() + 0.5;
                final double d4 = z - facing.getZOffset() + 0.5;
                final AxisAlignedBB axisalignedbb = new AxisAlignedBB(d3 - d2, y + 0.001, d4 - d2, d3 + d2, y + this.entity.height, d4 + d2);
                final AxisAlignedBB axisalignedbb2 = this.blockaccess.getBlockState(blockpos).getBoundingBox(this.blockaccess, blockpos);
                final AxisAlignedBB axisalignedbb3 = axisalignedbb.expand(0.0, axisalignedbb2.maxY - 0.002, 0.0);
                if (this.entity.world.collidesWithAnyBlock(axisalignedbb3)) {
                    pathpoint = null;
                }
            }
        }
        if (pathnodetype == PathNodeType.OPEN) {
            final AxisAlignedBB axisalignedbb4 = new AxisAlignedBB(x - d2 + 0.5, y + 0.001, z - d2 + 0.5, x + d2 + 0.5, y + this.entity.height, z + d2 + 0.5);
            if (this.entity.world.collidesWithAnyBlock(axisalignedbb4)) {
                return null;
            }
            if (this.entity.width >= 1.0f) {
                final PathNodeType pathnodetype2 = this.getPathNodeType(this.entity, x, y - 1, z);
                if (pathnodetype2 == PathNodeType.BLOCKED) {
                    pathpoint = this.openPoint(x, y, z);
                    pathpoint.nodeType = PathNodeType.WALKABLE;
                    pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
                    return pathpoint;
                }
            }
            int i = 0;
            while (y > 0 && pathnodetype == PathNodeType.OPEN) {
                --y;
                if (i++ >= this.entity.getMaxFallHeight()) {
                    return null;
                }
                pathnodetype = this.getPathNodeType(this.entity, x, y, z);
                f = this.entity.getPathPriority(pathnodetype);
                if (pathnodetype != PathNodeType.OPEN && f >= 0.0f) {
                    pathpoint = this.openPoint(x, y, z);
                    pathpoint.nodeType = pathnodetype;
                    pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
                    break;
                }
                if (f < 0.0f) {
                    return null;
                }
            }
        }
        return pathpoint;
    }
    
    @Override
    public PathNodeType getPathNodeType(final IBlockAccess blockaccessIn, final int x, final int y, final int z, final EntityLiving entitylivingIn, final int xSize, final int ySize, final int zSize, final boolean canBreakDoorsIn, final boolean canEnterDoorsIn) {
        final EnumSet<PathNodeType> enumset = EnumSet.noneOf(PathNodeType.class);
        PathNodeType pathnodetype = PathNodeType.BLOCKED;
        final double d0 = entitylivingIn.width / 2.0;
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
    
    public PathNodeType getPathNodeType(final IBlockAccess p_193577_1_, final int x, final int y, final int z, final int xSize, final int ySize, final int zSize, final boolean canOpenDoorsIn, final boolean canEnterDoorsIn, final EnumSet<PathNodeType> p_193577_10_, PathNodeType p_193577_11_, final BlockPos p_193577_12_) {
        for (int i = 0; i < xSize; ++i) {
            for (int j = 0; j < ySize; ++j) {
                for (int k = 0; k < zSize; ++k) {
                    final int l = i + x;
                    final int i2 = j + y;
                    final int j2 = k + z;
                    PathNodeType pathnodetype = this.getPathNodeType(p_193577_1_, l, i2, j2);
                    if (pathnodetype == PathNodeType.DOOR_WOOD_CLOSED && canOpenDoorsIn && canEnterDoorsIn) {
                        pathnodetype = PathNodeType.WALKABLE;
                    }
                    if (pathnodetype == PathNodeType.DOOR_OPEN && !canEnterDoorsIn) {
                        pathnodetype = PathNodeType.BLOCKED;
                    }
                    if (pathnodetype == PathNodeType.RAIL && !(p_193577_1_.getBlockState(p_193577_12_).getBlock() instanceof BlockRailBase) && !(p_193577_1_.getBlockState(p_193577_12_.down()).getBlock() instanceof BlockRailBase)) {
                        pathnodetype = PathNodeType.FENCE;
                    }
                    if (i == 0 && j == 0 && k == 0) {
                        p_193577_11_ = pathnodetype;
                    }
                    p_193577_10_.add(pathnodetype);
                }
            }
        }
        return p_193577_11_;
    }
    
    private PathNodeType getPathNodeType(final EntityLiving entitylivingIn, final BlockPos pos) {
        return this.getPathNodeType(entitylivingIn, pos.getX(), pos.getY(), pos.getZ());
    }
    
    private PathNodeType getPathNodeType(final EntityLiving entitylivingIn, final int x, final int y, final int z) {
        return this.getPathNodeType(this.blockaccess, x, y, z, entitylivingIn, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.getCanOpenDoors(), this.getCanEnterDoors());
    }
    
    @Override
    public PathNodeType getPathNodeType(final IBlockAccess blockaccessIn, final int x, final int y, final int z) {
        PathNodeType pathnodetype = this.getPathNodeTypeRaw(blockaccessIn, x, y, z);
        if (pathnodetype == PathNodeType.OPEN && y >= 1) {
            final Block block = blockaccessIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
            final PathNodeType pathnodetype2 = this.getPathNodeTypeRaw(blockaccessIn, x, y - 1, z);
            pathnodetype = ((pathnodetype2 != PathNodeType.WALKABLE && pathnodetype2 != PathNodeType.OPEN && pathnodetype2 != PathNodeType.WATER && pathnodetype2 != PathNodeType.LAVA) ? PathNodeType.WALKABLE : PathNodeType.OPEN);
            if (pathnodetype2 == PathNodeType.DAMAGE_FIRE || block == Blocks.MAGMA) {
                pathnodetype = PathNodeType.DAMAGE_FIRE;
            }
            if (pathnodetype2 == PathNodeType.DAMAGE_CACTUS) {
                pathnodetype = PathNodeType.DAMAGE_CACTUS;
            }
        }
        pathnodetype = this.checkNeighborBlocks(blockaccessIn, x, y, z, pathnodetype);
        return pathnodetype;
    }
    
    public PathNodeType checkNeighborBlocks(final IBlockAccess p_193578_1_, final int p_193578_2_, final int p_193578_3_, final int p_193578_4_, PathNodeType p_193578_5_) {
        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        if (p_193578_5_ == PathNodeType.WALKABLE) {
            for (int i = -1; i <= 1; ++i) {
                for (int j = -1; j <= 1; ++j) {
                    if (i != 0 || j != 0) {
                        final Block block = p_193578_1_.getBlockState(blockpos$pooledmutableblockpos.setPos(i + p_193578_2_, p_193578_3_, j + p_193578_4_)).getBlock();
                        if (block == Blocks.CACTUS) {
                            p_193578_5_ = PathNodeType.DANGER_CACTUS;
                        }
                        else if (block == Blocks.FIRE) {
                            p_193578_5_ = PathNodeType.DANGER_FIRE;
                        }
                    }
                }
            }
        }
        blockpos$pooledmutableblockpos.release();
        return p_193578_5_;
    }
    
    protected PathNodeType getPathNodeTypeRaw(final IBlockAccess p_189553_1_, final int p_189553_2_, final int p_189553_3_, final int p_189553_4_) {
        final BlockPos blockpos = new BlockPos(p_189553_2_, p_189553_3_, p_189553_4_);
        final IBlockState iblockstate = p_189553_1_.getBlockState(blockpos);
        final Block block = iblockstate.getBlock();
        final Material material = iblockstate.getMaterial();
        if (material == Material.AIR) {
            return PathNodeType.OPEN;
        }
        if (block == Blocks.TRAPDOOR || block == Blocks.IRON_TRAPDOOR || block == Blocks.WATERLILY) {
            return PathNodeType.TRAPDOOR;
        }
        if (block == Blocks.FIRE) {
            return PathNodeType.DAMAGE_FIRE;
        }
        if (block == Blocks.CACTUS) {
            return PathNodeType.DAMAGE_CACTUS;
        }
        if (block instanceof BlockDoor && material == Material.WOOD && !iblockstate.getValue((IProperty<Boolean>)BlockDoor.OPEN)) {
            return PathNodeType.DOOR_WOOD_CLOSED;
        }
        if (block instanceof BlockDoor && material == Material.IRON && !iblockstate.getValue((IProperty<Boolean>)BlockDoor.OPEN)) {
            return PathNodeType.DOOR_IRON_CLOSED;
        }
        if (block instanceof BlockDoor && iblockstate.getValue((IProperty<Boolean>)BlockDoor.OPEN)) {
            return PathNodeType.DOOR_OPEN;
        }
        if (block instanceof BlockRailBase) {
            return PathNodeType.RAIL;
        }
        if (block instanceof BlockFence || block instanceof BlockWall || (block instanceof BlockFenceGate && !iblockstate.getValue((IProperty<Boolean>)BlockFenceGate.OPEN))) {
            return PathNodeType.FENCE;
        }
        if (material == Material.WATER) {
            return PathNodeType.WATER;
        }
        if (material == Material.LAVA) {
            return PathNodeType.LAVA;
        }
        return block.isPassable(p_189553_1_, blockpos) ? PathNodeType.OPEN : PathNodeType.BLOCKED;
    }
}
