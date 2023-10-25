/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.pathfinding;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;

public class PathWorldListener
implements IWorldEventListener {
    private final List<PathNavigate> navigations = Lists.newArrayList();

    @Override
    public void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags) {
        if (this.didBlockChange(worldIn, pos, oldState, newState)) {
            int j = this.navigations.size();
            for (int i = 0; i < j; ++i) {
                int k;
                Path path;
                PathNavigate pathnavigate = this.navigations.get(i);
                if (pathnavigate == null || pathnavigate.canUpdatePathOnTimeout() || (path = pathnavigate.getPath()) == null || path.isFinished() || path.getCurrentPathLength() == 0) continue;
                PathPoint pathpoint = pathnavigate.currentPath.getFinalPathPoint();
                double d0 = pos.distanceSq(((double)pathpoint.xCoord + pathnavigate.theEntity.posX) / 2.0, ((double)pathpoint.yCoord + pathnavigate.theEntity.posY) / 2.0, ((double)pathpoint.zCoord + pathnavigate.theEntity.posZ) / 2.0);
                if (!(d0 < (double)(k = (path.getCurrentPathLength() - path.getCurrentPathIndex()) * (path.getCurrentPathLength() - path.getCurrentPathIndex())))) continue;
                pathnavigate.updatePath();
            }
        }
    }

    protected boolean didBlockChange(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState) {
        AxisAlignedBB axisalignedbb1;
        AxisAlignedBB axisalignedbb = oldState.getCollisionBoundingBox(worldIn, pos);
        return axisalignedbb != (axisalignedbb1 = newState.getCollisionBoundingBox(worldIn, pos)) && (axisalignedbb == null || !axisalignedbb.equals(axisalignedbb1));
    }

    @Override
    public void notifyLightSet(BlockPos pos) {
    }

    @Override
    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
    }

    @Override
    public void playSoundToAllNearExcept(@Nullable EntityPlayer player, SoundEvent soundIn, SoundCategory category, double x, double y, double z, float volume, float pitch) {
    }

    @Override
    public void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int ... parameters) {
    }

    @Override
    public void func_190570_a(int p_190570_1_, boolean p_190570_2_, boolean p_190570_3_, double p_190570_4_, double p_190570_6_, double p_190570_8_, double p_190570_10_, double p_190570_12_, double p_190570_14_, int ... p_190570_16_) {
    }

    @Override
    public void onEntityAdded(Entity entityIn) {
        if (entityIn instanceof EntityLiving) {
            this.navigations.add(((EntityLiving)entityIn).getNavigator());
        }
    }

    @Override
    public void onEntityRemoved(Entity entityIn) {
        if (entityIn instanceof EntityLiving) {
            this.navigations.remove(((EntityLiving)entityIn).getNavigator());
        }
    }

    @Override
    public void playRecord(SoundEvent soundIn, BlockPos pos) {
    }

    @Override
    public void broadcastSound(int soundID, BlockPos pos, int data) {
    }

    @Override
    public void playEvent(EntityPlayer player, int type, BlockPos blockPosIn, int data) {
    }

    @Override
    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
    }
}

