// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.IWorldEventListener;

public class PathWorldListener implements IWorldEventListener
{
    private final List<PathNavigate> navigations;
    
    public PathWorldListener() {
        this.navigations = (List<PathNavigate>)Lists.newArrayList();
    }
    
    @Override
    public void notifyBlockUpdate(final World worldIn, final BlockPos pos, final IBlockState oldState, final IBlockState newState, final int flags) {
        if (this.didBlockChange(worldIn, pos, oldState, newState)) {
            for (int i = 0, j = this.navigations.size(); i < j; ++i) {
                final PathNavigate pathnavigate = this.navigations.get(i);
                if (pathnavigate != null && !pathnavigate.canUpdatePathOnTimeout()) {
                    final Path path = pathnavigate.getPath();
                    if (path != null && !path.isFinished() && path.getCurrentPathLength() != 0) {
                        final PathPoint pathpoint = pathnavigate.currentPath.getFinalPathPoint();
                        final double d0 = pos.distanceSq((pathpoint.x + pathnavigate.entity.posX) / 2.0, (pathpoint.y + pathnavigate.entity.posY) / 2.0, (pathpoint.z + pathnavigate.entity.posZ) / 2.0);
                        final int k = (path.getCurrentPathLength() - path.getCurrentPathIndex()) * (path.getCurrentPathLength() - path.getCurrentPathIndex());
                        if (d0 < k) {
                            pathnavigate.updatePath();
                        }
                    }
                }
            }
        }
    }
    
    protected boolean didBlockChange(final World worldIn, final BlockPos pos, final IBlockState oldState, final IBlockState newState) {
        final AxisAlignedBB axisalignedbb = oldState.getCollisionBoundingBox(worldIn, pos);
        final AxisAlignedBB axisalignedbb2 = newState.getCollisionBoundingBox(worldIn, pos);
        return axisalignedbb != axisalignedbb2 && (axisalignedbb == null || !axisalignedbb.equals(axisalignedbb2));
    }
    
    @Override
    public void notifyLightSet(final BlockPos pos) {
    }
    
    @Override
    public void markBlockRangeForRenderUpdate(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
    }
    
    @Override
    public void playSoundToAllNearExcept(@Nullable final EntityPlayer player, final SoundEvent soundIn, final SoundCategory category, final double x, final double y, final double z, final float volume, final float pitch) {
    }
    
    @Override
    public void spawnParticle(final int particleID, final boolean ignoreRange, final double xCoord, final double yCoord, final double zCoord, final double xSpeed, final double ySpeed, final double zSpeed, final int... parameters) {
    }
    
    @Override
    public void spawnParticle(final int id, final boolean ignoreRange, final boolean minimiseParticleLevel, final double x, final double y, final double z, final double xSpeed, final double ySpeed, final double zSpeed, final int... parameters) {
    }
    
    @Override
    public void onEntityAdded(final Entity entityIn) {
        if (entityIn instanceof EntityLiving) {
            this.navigations.add(((EntityLiving)entityIn).getNavigator());
        }
    }
    
    @Override
    public void onEntityRemoved(final Entity entityIn) {
        if (entityIn instanceof EntityLiving) {
            this.navigations.remove(((EntityLiving)entityIn).getNavigator());
        }
    }
    
    @Override
    public void playRecord(final SoundEvent soundIn, final BlockPos pos) {
    }
    
    @Override
    public void broadcastSound(final int soundID, final BlockPos pos, final int data) {
    }
    
    @Override
    public void playEvent(final EntityPlayer player, final int type, final BlockPos blockPosIn, final int data) {
    }
    
    @Override
    public void sendBlockBreakProgress(final int breakerId, final BlockPos pos, final int progress) {
    }
}
