// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.util.Random;

public class Teleporter
{
    private final WorldServer world;
    private final Random random;
    private final Long2ObjectMap<PortalPosition> destinationCoordinateCache;
    
    public Teleporter(final WorldServer worldIn) {
        this.destinationCoordinateCache = (Long2ObjectMap<PortalPosition>)new Long2ObjectOpenHashMap(4096);
        this.world = worldIn;
        this.random = new Random(worldIn.getSeed());
    }
    
    public void placeInPortal(final Entity entityIn, final float rotationYaw) {
        if (this.world.provider.getDimensionType().getId() != 1) {
            if (!this.placeInExistingPortal(entityIn, rotationYaw)) {
                this.makePortal(entityIn);
                this.placeInExistingPortal(entityIn, rotationYaw);
            }
        }
        else {
            final int i = MathHelper.floor(entityIn.posX);
            final int j = MathHelper.floor(entityIn.posY) - 1;
            final int k = MathHelper.floor(entityIn.posZ);
            final int l = 1;
            final int i2 = 0;
            for (int j2 = -2; j2 <= 2; ++j2) {
                for (int k2 = -2; k2 <= 2; ++k2) {
                    for (int l2 = -1; l2 < 3; ++l2) {
                        final int i3 = i + k2 * 1 + j2 * 0;
                        final int j3 = j + l2;
                        final int k3 = k + k2 * 0 - j2 * 1;
                        final boolean flag = l2 < 0;
                        this.world.setBlockState(new BlockPos(i3, j3, k3), flag ? Blocks.OBSIDIAN.getDefaultState() : Blocks.AIR.getDefaultState());
                    }
                }
            }
            entityIn.setLocationAndAngles(i, j, k, entityIn.rotationYaw, 0.0f);
            entityIn.motionX = 0.0;
            entityIn.motionY = 0.0;
            entityIn.motionZ = 0.0;
        }
    }
    
    public boolean placeInExistingPortal(final Entity entityIn, final float rotationYaw) {
        final int i = 128;
        double d0 = -1.0;
        final int j = MathHelper.floor(entityIn.posX);
        final int k = MathHelper.floor(entityIn.posZ);
        boolean flag = true;
        BlockPos blockpos = BlockPos.ORIGIN;
        final long l = ChunkPos.asLong(j, k);
        if (this.destinationCoordinateCache.containsKey(l)) {
            final PortalPosition teleporter$portalposition = (PortalPosition)this.destinationCoordinateCache.get(l);
            d0 = 0.0;
            blockpos = teleporter$portalposition;
            teleporter$portalposition.lastUpdateTime = this.world.getTotalWorldTime();
            flag = false;
        }
        else {
            final BlockPos blockpos2 = new BlockPos(entityIn);
            for (int i2 = -128; i2 <= 128; ++i2) {
                for (int j2 = -128; j2 <= 128; ++j2) {
                    BlockPos blockpos4;
                    for (BlockPos blockpos3 = blockpos2.add(i2, this.world.getActualHeight() - 1 - blockpos2.getY(), j2); blockpos3.getY() >= 0; blockpos3 = blockpos4) {
                        blockpos4 = blockpos3.down();
                        if (this.world.getBlockState(blockpos3).getBlock() == Blocks.PORTAL) {
                            for (blockpos4 = blockpos3.down(); this.world.getBlockState(blockpos4).getBlock() == Blocks.PORTAL; blockpos4 = blockpos4.down()) {
                                blockpos3 = blockpos4;
                            }
                            final double d2 = blockpos3.distanceSq(blockpos2);
                            if (d0 < 0.0 || d2 < d0) {
                                d0 = d2;
                                blockpos = blockpos3;
                            }
                        }
                    }
                }
            }
        }
        if (d0 >= 0.0) {
            if (flag) {
                this.destinationCoordinateCache.put(l, (Object)new PortalPosition(blockpos, this.world.getTotalWorldTime()));
            }
            double d3 = blockpos.getX() + 0.5;
            double d4 = blockpos.getZ() + 0.5;
            final BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.PORTAL.createPatternHelper(this.world, blockpos);
            final boolean flag2 = blockpattern$patternhelper.getForwards().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE;
            double d5 = (blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X) ? blockpattern$patternhelper.getFrontTopLeft().getZ() : ((double)blockpattern$patternhelper.getFrontTopLeft().getX());
            final double d6 = blockpattern$patternhelper.getFrontTopLeft().getY() + 1 - entityIn.getLastPortalVec().y * blockpattern$patternhelper.getHeight();
            if (flag2) {
                ++d5;
            }
            if (blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X) {
                d4 = d5 + (1.0 - entityIn.getLastPortalVec().x) * blockpattern$patternhelper.getWidth() * blockpattern$patternhelper.getForwards().rotateY().getAxisDirection().getOffset();
            }
            else {
                d3 = d5 + (1.0 - entityIn.getLastPortalVec().x) * blockpattern$patternhelper.getWidth() * blockpattern$patternhelper.getForwards().rotateY().getAxisDirection().getOffset();
            }
            float f = 0.0f;
            float f2 = 0.0f;
            float f3 = 0.0f;
            float f4 = 0.0f;
            if (blockpattern$patternhelper.getForwards().getOpposite() == entityIn.getTeleportDirection()) {
                f = 1.0f;
                f2 = 1.0f;
            }
            else if (blockpattern$patternhelper.getForwards().getOpposite() == entityIn.getTeleportDirection().getOpposite()) {
                f = -1.0f;
                f2 = -1.0f;
            }
            else if (blockpattern$patternhelper.getForwards().getOpposite() == entityIn.getTeleportDirection().rotateY()) {
                f3 = 1.0f;
                f4 = -1.0f;
            }
            else {
                f3 = -1.0f;
                f4 = 1.0f;
            }
            final double d7 = entityIn.motionX;
            final double d8 = entityIn.motionZ;
            entityIn.motionX = d7 * f + d8 * f4;
            entityIn.motionZ = d7 * f3 + d8 * f2;
            entityIn.rotationYaw = rotationYaw - entityIn.getTeleportDirection().getOpposite().getHorizontalIndex() * 90 + blockpattern$patternhelper.getForwards().getHorizontalIndex() * 90;
            if (entityIn instanceof EntityPlayerMP) {
                ((EntityPlayerMP)entityIn).connection.setPlayerLocation(d3, d6, d4, entityIn.rotationYaw, entityIn.rotationPitch);
            }
            else {
                entityIn.setLocationAndAngles(d3, d6, d4, entityIn.rotationYaw, entityIn.rotationPitch);
            }
            return true;
        }
        return false;
    }
    
    public boolean makePortal(final Entity entityIn) {
        final int i = 16;
        double d0 = -1.0;
        final int j = MathHelper.floor(entityIn.posX);
        final int k = MathHelper.floor(entityIn.posY);
        final int l = MathHelper.floor(entityIn.posZ);
        int i2 = j;
        int j2 = k;
        int k2 = l;
        int l2 = 0;
        final int i3 = this.random.nextInt(4);
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int j3 = j - 16; j3 <= j + 16; ++j3) {
            final double d2 = j3 + 0.5 - entityIn.posX;
            for (int l3 = l - 16; l3 <= l + 16; ++l3) {
                final double d3 = l3 + 0.5 - entityIn.posZ;
            Label_0462:
                for (int j4 = this.world.getActualHeight() - 1; j4 >= 0; --j4) {
                    if (this.world.isAirBlock(blockpos$mutableblockpos.setPos(j3, j4, l3))) {
                        while (j4 > 0 && this.world.isAirBlock(blockpos$mutableblockpos.setPos(j3, j4 - 1, l3))) {
                            --j4;
                        }
                        for (int k3 = i3; k3 < i3 + 4; ++k3) {
                            int l4 = k3 % 2;
                            int i4 = 1 - l4;
                            if (k3 % 4 >= 2) {
                                l4 = -l4;
                                i4 = -i4;
                            }
                            for (int j5 = 0; j5 < 3; ++j5) {
                                for (int k4 = 0; k4 < 4; ++k4) {
                                    for (int l5 = -1; l5 < 4; ++l5) {
                                        final int i5 = j3 + (k4 - 1) * l4 + j5 * i4;
                                        final int j6 = j4 + l5;
                                        final int k5 = l3 + (k4 - 1) * i4 - j5 * l4;
                                        blockpos$mutableblockpos.setPos(i5, j6, k5);
                                        if (l5 < 0 && !this.world.getBlockState(blockpos$mutableblockpos).getMaterial().isSolid()) {
                                            continue Label_0462;
                                        }
                                        if (l5 >= 0 && !this.world.isAirBlock(blockpos$mutableblockpos)) {
                                            continue Label_0462;
                                        }
                                    }
                                }
                            }
                            final double d4 = j4 + 0.5 - entityIn.posY;
                            final double d5 = d2 * d2 + d4 * d4 + d3 * d3;
                            if (d0 < 0.0 || d5 < d0) {
                                d0 = d5;
                                i2 = j3;
                                j2 = j4;
                                k2 = l3;
                                l2 = k3 % 4;
                            }
                        }
                    }
                }
            }
        }
        if (d0 < 0.0) {
            for (int l6 = j - 16; l6 <= j + 16; ++l6) {
                final double d6 = l6 + 0.5 - entityIn.posX;
                for (int j7 = l - 16; j7 <= l + 16; ++j7) {
                    final double d7 = j7 + 0.5 - entityIn.posZ;
                Label_0835:
                    for (int i6 = this.world.getActualHeight() - 1; i6 >= 0; --i6) {
                        if (this.world.isAirBlock(blockpos$mutableblockpos.setPos(l6, i6, j7))) {
                            while (i6 > 0 && this.world.isAirBlock(blockpos$mutableblockpos.setPos(l6, i6 - 1, j7))) {
                                --i6;
                            }
                            for (int k6 = i3; k6 < i3 + 2; ++k6) {
                                final int j8 = k6 % 2;
                                final int j9 = 1 - j8;
                                for (int j10 = 0; j10 < 4; ++j10) {
                                    for (int j11 = -1; j11 < 4; ++j11) {
                                        final int j12 = l6 + (j10 - 1) * j8;
                                        final int i7 = i6 + j11;
                                        final int j13 = j7 + (j10 - 1) * j9;
                                        blockpos$mutableblockpos.setPos(j12, i7, j13);
                                        if (j11 < 0 && !this.world.getBlockState(blockpos$mutableblockpos).getMaterial().isSolid()) {
                                            continue Label_0835;
                                        }
                                        if (j11 >= 0 && !this.world.isAirBlock(blockpos$mutableblockpos)) {
                                            continue Label_0835;
                                        }
                                    }
                                }
                                final double d8 = i6 + 0.5 - entityIn.posY;
                                final double d9 = d6 * d6 + d8 * d8 + d7 * d7;
                                if (d0 < 0.0 || d9 < d0) {
                                    d0 = d9;
                                    i2 = l6;
                                    j2 = i6;
                                    k2 = j7;
                                    l2 = k6 % 2;
                                }
                            }
                        }
                    }
                }
            }
        }
        final int i8 = i2;
        int k7 = j2;
        final int k8 = k2;
        int l7 = l2 % 2;
        int i9 = 1 - l7;
        if (l2 % 4 >= 2) {
            l7 = -l7;
            i9 = -i9;
        }
        if (d0 < 0.0) {
            j2 = (k7 = MathHelper.clamp(j2, 70, this.world.getActualHeight() - 10));
            for (int j14 = -1; j14 <= 1; ++j14) {
                for (int l8 = 1; l8 < 3; ++l8) {
                    for (int k9 = -1; k9 < 3; ++k9) {
                        final int k10 = i8 + (l8 - 1) * l7 + j14 * i9;
                        final int k11 = k7 + k9;
                        final int k12 = k8 + (l8 - 1) * i9 - j14 * l7;
                        final boolean flag = k9 < 0;
                        this.world.setBlockState(new BlockPos(k10, k11, k12), flag ? Blocks.OBSIDIAN.getDefaultState() : Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
        final IBlockState iblockstate = Blocks.PORTAL.getDefaultState().withProperty(BlockPortal.AXIS, (l7 == 0) ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
        for (int i10 = 0; i10 < 4; ++i10) {
            for (int l9 = 0; l9 < 4; ++l9) {
                for (int l10 = -1; l10 < 4; ++l10) {
                    final int l11 = i8 + (l9 - 1) * l7;
                    final int l12 = k7 + l10;
                    final int k13 = k8 + (l9 - 1) * i9;
                    final boolean flag2 = l9 == 0 || l9 == 3 || l10 == -1 || l10 == 3;
                    this.world.setBlockState(new BlockPos(l11, l12, k13), flag2 ? Blocks.OBSIDIAN.getDefaultState() : iblockstate, 2);
                }
            }
            for (int i11 = 0; i11 < 4; ++i11) {
                for (int i12 = -1; i12 < 4; ++i12) {
                    final int i13 = i8 + (i11 - 1) * l7;
                    final int i14 = k7 + i12;
                    final int l13 = k8 + (i11 - 1) * i9;
                    final BlockPos blockpos = new BlockPos(i13, i14, l13);
                    this.world.notifyNeighborsOfStateChange(blockpos, this.world.getBlockState(blockpos).getBlock(), false);
                }
            }
        }
        return true;
    }
    
    public void removeStalePortalLocations(final long worldTime) {
        if (worldTime % 100L == 0L) {
            final long i = worldTime - 300L;
            final ObjectIterator<PortalPosition> objectiterator = (ObjectIterator<PortalPosition>)this.destinationCoordinateCache.values().iterator();
            while (objectiterator.hasNext()) {
                final PortalPosition teleporter$portalposition = (PortalPosition)objectiterator.next();
                if (teleporter$portalposition == null || teleporter$portalposition.lastUpdateTime < i) {
                    objectiterator.remove();
                }
            }
        }
    }
    
    public class PortalPosition extends BlockPos
    {
        public long lastUpdateTime;
        
        public PortalPosition(final BlockPos pos, final long lastUpdate) {
            super(pos.getX(), pos.getY(), pos.getZ());
            this.lastUpdateTime = lastUpdate;
        }
    }
}
