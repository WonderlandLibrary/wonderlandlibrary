/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.world;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;

public class Teleporter {
    private final LongHashMap<PortalPosition> destinationCoordinateCache = new LongHashMap();
    private final WorldServer worldServerInstance;
    private final List<Long> destinationCoordinateKeys = Lists.newArrayList();
    private final Random random;

    public void placeInPortal(Entity entity, float f) {
        if (this.worldServerInstance.provider.getDimensionId() != 1) {
            if (!this.placeInExistingPortal(entity, f)) {
                this.makePortal(entity);
                this.placeInExistingPortal(entity, f);
            }
        } else {
            int n = MathHelper.floor_double(entity.posX);
            int n2 = MathHelper.floor_double(entity.posY) - 1;
            int n3 = MathHelper.floor_double(entity.posZ);
            int n4 = 1;
            int n5 = 0;
            int n6 = -2;
            while (n6 <= 2) {
                int n7 = -2;
                while (n7 <= 2) {
                    int n8 = -1;
                    while (n8 < 3) {
                        int n9 = n + n7 * n4 + n6 * n5;
                        int n10 = n2 + n8;
                        int n11 = n3 + n7 * n5 - n6 * n4;
                        boolean bl = n8 < 0;
                        this.worldServerInstance.setBlockState(new BlockPos(n9, n10, n11), bl ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
                        ++n8;
                    }
                    ++n7;
                }
                ++n6;
            }
            entity.setLocationAndAngles(n, n2, n3, entity.rotationYaw, 0.0f);
            entity.motionZ = 0.0;
            entity.motionY = 0.0;
            entity.motionX = 0.0;
        }
    }

    public void removeStalePortalLocations(long l) {
        if (l % 100L == 0L) {
            Iterator<Long> iterator = this.destinationCoordinateKeys.iterator();
            long l2 = l - 300L;
            while (iterator.hasNext()) {
                Long l3 = iterator.next();
                PortalPosition portalPosition = this.destinationCoordinateCache.getValueByKey(l3);
                if (portalPosition != null && portalPosition.lastUpdateTime >= l2) continue;
                iterator.remove();
                this.destinationCoordinateCache.remove(l3);
            }
        }
    }

    public boolean placeInExistingPortal(Entity entity, float f) {
        BlockPos blockPos;
        int n = 128;
        double d = -1.0;
        int n2 = MathHelper.floor_double(entity.posX);
        int n3 = MathHelper.floor_double(entity.posZ);
        boolean bl = true;
        BlockPos blockPos2 = BlockPos.ORIGIN;
        long l = ChunkCoordIntPair.chunkXZ2Int(n2, n3);
        if (this.destinationCoordinateCache.containsItem(l)) {
            blockPos = this.destinationCoordinateCache.getValueByKey(l);
            d = 0.0;
            blockPos2 = blockPos;
            ((PortalPosition)blockPos).lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
            bl = false;
        } else {
            blockPos = new BlockPos(entity);
            int n4 = -128;
            while (n4 <= 128) {
                int n5 = -128;
                while (n5 <= 128) {
                    BlockPos blockPos3 = blockPos.add(n4, this.worldServerInstance.getActualHeight() - 1 - blockPos.getY(), n5);
                    while (blockPos3.getY() >= 0) {
                        BlockPos blockPos4 = blockPos3.down();
                        if (this.worldServerInstance.getBlockState(blockPos3).getBlock() == Blocks.portal) {
                            while (this.worldServerInstance.getBlockState(blockPos4 = blockPos3.down()).getBlock() == Blocks.portal) {
                                blockPos3 = blockPos4;
                            }
                            double d2 = blockPos3.distanceSq(blockPos);
                            if (d < 0.0 || d2 < d) {
                                d = d2;
                                blockPos2 = blockPos3;
                            }
                        }
                        blockPos3 = blockPos4;
                    }
                    ++n5;
                }
                ++n4;
            }
        }
        if (d >= 0.0) {
            if (bl) {
                this.destinationCoordinateCache.add(l, new PortalPosition(blockPos2, this.worldServerInstance.getTotalWorldTime()));
                this.destinationCoordinateKeys.add(l);
            }
            double d3 = (double)blockPos2.getX() + 0.5;
            double d4 = (double)blockPos2.getY() + 0.5;
            double d5 = (double)blockPos2.getZ() + 0.5;
            BlockPattern.PatternHelper patternHelper = Blocks.portal.func_181089_f(this.worldServerInstance, blockPos2);
            boolean bl2 = patternHelper.getFinger().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE;
            double d6 = patternHelper.getFinger().getAxis() == EnumFacing.Axis.X ? (double)patternHelper.func_181117_a().getZ() : (double)patternHelper.func_181117_a().getX();
            d4 = (double)(patternHelper.func_181117_a().getY() + 1) - entity.func_181014_aG().yCoord * (double)patternHelper.func_181119_e();
            if (bl2) {
                d6 += 1.0;
            }
            if (patternHelper.getFinger().getAxis() == EnumFacing.Axis.X) {
                d5 = d6 + (1.0 - entity.func_181014_aG().xCoord) * (double)patternHelper.func_181118_d() * (double)patternHelper.getFinger().rotateY().getAxisDirection().getOffset();
            } else {
                d3 = d6 + (1.0 - entity.func_181014_aG().xCoord) * (double)patternHelper.func_181118_d() * (double)patternHelper.getFinger().rotateY().getAxisDirection().getOffset();
            }
            float f2 = 0.0f;
            float f3 = 0.0f;
            float f4 = 0.0f;
            float f5 = 0.0f;
            if (patternHelper.getFinger().getOpposite() == entity.func_181012_aH()) {
                f2 = 1.0f;
                f3 = 1.0f;
            } else if (patternHelper.getFinger().getOpposite() == entity.func_181012_aH().getOpposite()) {
                f2 = -1.0f;
                f3 = -1.0f;
            } else if (patternHelper.getFinger().getOpposite() == entity.func_181012_aH().rotateY()) {
                f4 = 1.0f;
                f5 = -1.0f;
            } else {
                f4 = -1.0f;
                f5 = 1.0f;
            }
            double d7 = entity.motionX;
            double d8 = entity.motionZ;
            entity.motionX = d7 * (double)f2 + d8 * (double)f5;
            entity.motionZ = d7 * (double)f4 + d8 * (double)f3;
            entity.rotationYaw = f - (float)(entity.func_181012_aH().getOpposite().getHorizontalIndex() * 90) + (float)(patternHelper.getFinger().getHorizontalIndex() * 90);
            entity.setLocationAndAngles(d3, d4, d5, entity.rotationYaw, entity.rotationPitch);
            return true;
        }
        return false;
    }

    public Teleporter(WorldServer worldServer) {
        this.worldServerInstance = worldServer;
        this.random = new Random(worldServer.getSeed());
    }

    public boolean makePortal(Entity entity) {
        int n;
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        double d;
        int n8;
        double d2;
        int n9 = 16;
        double d3 = -1.0;
        int n10 = MathHelper.floor_double(entity.posX);
        int n11 = MathHelper.floor_double(entity.posY);
        int n12 = MathHelper.floor_double(entity.posZ);
        int n13 = n10;
        int n14 = n11;
        int n15 = n12;
        int n16 = 0;
        int n17 = this.random.nextInt(4);
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int n18 = n10 - n9;
        while (n18 <= n10 + n9) {
            d2 = (double)n18 + 0.5 - entity.posX;
            n8 = n12 - n9;
            while (n8 <= n12 + n9) {
                d = (double)n8 + 0.5 - entity.posZ;
                n7 = this.worldServerInstance.getActualHeight() - 1;
                while (n7 >= 0) {
                    if (this.worldServerInstance.isAirBlock(mutableBlockPos.func_181079_c(n18, n7, n8))) {
                        while (n7 > 0 && this.worldServerInstance.isAirBlock(mutableBlockPos.func_181079_c(n18, n7 - 1, n8))) {
                            --n7;
                        }
                        n6 = n17;
                        block4: while (n6 < n17 + 4) {
                            n5 = n6 % 2;
                            n4 = 1 - n5;
                            if (n6 % 4 >= 2) {
                                n5 = -n5;
                                n4 = -n4;
                            }
                            int n19 = 0;
                            while (n19 < 3) {
                                n3 = 0;
                                while (n3 < 4) {
                                    int n20 = -1;
                                    while (n20 < 4) {
                                        n2 = n18 + (n3 - 1) * n5 + n19 * n4;
                                        n = n7 + n20;
                                        int n21 = n8 + (n3 - 1) * n4 - n19 * n5;
                                        mutableBlockPos.func_181079_c(n2, n, n21);
                                        if (n20 < 0 && !this.worldServerInstance.getBlockState(mutableBlockPos).getBlock().getMaterial().isSolid() || n20 >= 0 && !this.worldServerInstance.isAirBlock(mutableBlockPos)) break block4;
                                        ++n20;
                                    }
                                    ++n3;
                                }
                                ++n19;
                            }
                            double d4 = (double)n7 + 0.5 - entity.posY;
                            double d5 = d2 * d2 + d4 * d4 + d * d;
                            if (d3 < 0.0 || d5 < d3) {
                                d3 = d5;
                                n13 = n18;
                                n14 = n7;
                                n15 = n8;
                                n16 = n6 % 4;
                            }
                            ++n6;
                        }
                    }
                    --n7;
                }
                ++n8;
            }
            ++n18;
        }
        if (d3 < 0.0) {
            n18 = n10 - n9;
            while (n18 <= n10 + n9) {
                d2 = (double)n18 + 0.5 - entity.posX;
                n8 = n12 - n9;
                while (n8 <= n12 + n9) {
                    d = (double)n8 + 0.5 - entity.posZ;
                    n7 = this.worldServerInstance.getActualHeight() - 1;
                    while (n7 >= 0) {
                        if (this.worldServerInstance.isAirBlock(mutableBlockPos.func_181079_c(n18, n7, n8))) {
                            while (n7 > 0 && this.worldServerInstance.isAirBlock(mutableBlockPos.func_181079_c(n18, n7 - 1, n8))) {
                                --n7;
                            }
                            n6 = n17;
                            block12: while (n6 < n17 + 2) {
                                n5 = n6 % 2;
                                n4 = 1 - n5;
                                int n22 = 0;
                                while (n22 < 4) {
                                    n3 = -1;
                                    while (n3 < 4) {
                                        int n23 = n18 + (n22 - 1) * n5;
                                        n2 = n7 + n3;
                                        n = n8 + (n22 - 1) * n4;
                                        mutableBlockPos.func_181079_c(n23, n2, n);
                                        if (n3 < 0 && !this.worldServerInstance.getBlockState(mutableBlockPos).getBlock().getMaterial().isSolid() || n3 >= 0 && !this.worldServerInstance.isAirBlock(mutableBlockPos)) break block12;
                                        ++n3;
                                    }
                                    ++n22;
                                }
                                double d6 = (double)n7 + 0.5 - entity.posY;
                                double d7 = d2 * d2 + d6 * d6 + d * d;
                                if (d3 < 0.0 || d7 < d3) {
                                    d3 = d7;
                                    n13 = n18;
                                    n14 = n7;
                                    n15 = n8;
                                    n16 = n6 % 2;
                                }
                                ++n6;
                            }
                        }
                        --n7;
                    }
                    ++n8;
                }
                ++n18;
            }
        }
        n18 = n13;
        int n24 = n14;
        int n25 = n15;
        n8 = n16 % 2;
        int n26 = 1 - n8;
        if (n16 % 4 >= 2) {
            n8 = -n8;
            n26 = -n26;
        }
        if (d3 < 0.0) {
            n24 = n14 = MathHelper.clamp_int(n14, 70, this.worldServerInstance.getActualHeight() - 10);
            int n27 = -1;
            while (n27 <= 1) {
                n7 = 1;
                while (n7 < 3) {
                    n6 = -1;
                    while (n6 < 3) {
                        n5 = n18 + (n7 - 1) * n8 + n27 * n26;
                        n4 = n24 + n6;
                        int n28 = n25 + (n7 - 1) * n26 - n27 * n8;
                        n3 = n6 < 0 ? 1 : 0;
                        this.worldServerInstance.setBlockState(new BlockPos(n5, n4, n28), n3 != 0 ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
                        ++n6;
                    }
                    ++n7;
                }
                ++n27;
            }
        }
        IBlockState iBlockState = Blocks.portal.getDefaultState().withProperty(BlockPortal.AXIS, n8 != 0 ? EnumFacing.Axis.X : EnumFacing.Axis.Z);
        n7 = 0;
        while (n7 < 4) {
            n6 = 0;
            while (n6 < 4) {
                n5 = -1;
                while (n5 < 4) {
                    n4 = n18 + (n6 - 1) * n8;
                    int n29 = n24 + n5;
                    n3 = n25 + (n6 - 1) * n26;
                    boolean bl = n6 == 0 || n6 == 3 || n5 == -1 || n5 == 3;
                    this.worldServerInstance.setBlockState(new BlockPos(n4, n29, n3), bl ? Blocks.obsidian.getDefaultState() : iBlockState, 2);
                    ++n5;
                }
                ++n6;
            }
            n6 = 0;
            while (n6 < 4) {
                n5 = -1;
                while (n5 < 4) {
                    n4 = n18 + (n6 - 1) * n8;
                    int n30 = n24 + n5;
                    n3 = n25 + (n6 - 1) * n26;
                    BlockPos blockPos = new BlockPos(n4, n30, n3);
                    this.worldServerInstance.notifyNeighborsOfStateChange(blockPos, this.worldServerInstance.getBlockState(blockPos).getBlock());
                    ++n5;
                }
                ++n6;
            }
            ++n7;
        }
        return true;
    }

    public class PortalPosition
    extends BlockPos {
        public long lastUpdateTime;

        public PortalPosition(BlockPos blockPos, long l) {
            super(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            this.lastUpdateTime = l;
        }
    }
}

