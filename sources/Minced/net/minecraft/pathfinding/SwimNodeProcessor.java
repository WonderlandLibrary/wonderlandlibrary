// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class SwimNodeProcessor extends NodeProcessor
{
    @Override
    public PathPoint getStart() {
        return this.openPoint(MathHelper.floor(this.entity.getEntityBoundingBox().minX), MathHelper.floor(this.entity.getEntityBoundingBox().minY + 0.5), MathHelper.floor(this.entity.getEntityBoundingBox().minZ));
    }
    
    @Override
    public PathPoint getPathPointToCoords(final double x, final double y, final double z) {
        return this.openPoint(MathHelper.floor(x - this.entity.width / 2.0f), MathHelper.floor(y + 0.5), MathHelper.floor(z - this.entity.width / 2.0f));
    }
    
    @Override
    public int findPathOptions(final PathPoint[] pathOptions, final PathPoint currentPoint, final PathPoint targetPoint, final float maxDistance) {
        int i = 0;
        for (final EnumFacing enumfacing : EnumFacing.values()) {
            final PathPoint pathpoint = this.getWaterNode(currentPoint.x + enumfacing.getXOffset(), currentPoint.y + enumfacing.getYOffset(), currentPoint.z + enumfacing.getZOffset());
            if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint;
            }
        }
        return i;
    }
    
    @Override
    public PathNodeType getPathNodeType(final IBlockAccess blockaccessIn, final int x, final int y, final int z, final EntityLiving entitylivingIn, final int xSize, final int ySize, final int zSize, final boolean canBreakDoorsIn, final boolean canEnterDoorsIn) {
        return PathNodeType.WATER;
    }
    
    @Override
    public PathNodeType getPathNodeType(final IBlockAccess blockaccessIn, final int x, final int y, final int z) {
        return PathNodeType.WATER;
    }
    
    @Nullable
    private PathPoint getWaterNode(final int p_186328_1_, final int p_186328_2_, final int p_186328_3_) {
        final PathNodeType pathnodetype = this.isFree(p_186328_1_, p_186328_2_, p_186328_3_);
        return (pathnodetype == PathNodeType.WATER) ? this.openPoint(p_186328_1_, p_186328_2_, p_186328_3_) : null;
    }
    
    private PathNodeType isFree(final int p_186327_1_, final int p_186327_2_, final int p_186327_3_) {
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int i = p_186327_1_; i < p_186327_1_ + this.entitySizeX; ++i) {
            for (int j = p_186327_2_; j < p_186327_2_ + this.entitySizeY; ++j) {
                for (int k = p_186327_3_; k < p_186327_3_ + this.entitySizeZ; ++k) {
                    final IBlockState iblockstate = this.blockaccess.getBlockState(blockpos$mutableblockpos.setPos(i, j, k));
                    if (iblockstate.getMaterial() != Material.WATER) {
                        return PathNodeType.BLOCKED;
                    }
                }
            }
        }
        return PathNodeType.WATER;
    }
}
