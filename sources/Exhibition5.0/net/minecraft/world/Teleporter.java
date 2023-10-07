// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world;

import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPortal;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.LongHashMap;
import java.util.Random;

public class Teleporter
{
    private final WorldServer worldServerInstance;
    private final Random random;
    private final LongHashMap destinationCoordinateCache;
    private final List destinationCoordinateKeys;
    private static final String __OBFID = "CL_00000153";
    
    public Teleporter(final WorldServer worldIn) {
        this.destinationCoordinateCache = new LongHashMap();
        this.destinationCoordinateKeys = Lists.newArrayList();
        this.worldServerInstance = worldIn;
        this.random = new Random(worldIn.getSeed());
    }
    
    public void func_180266_a(final Entity p_180266_1_, final float p_180266_2_) {
        if (this.worldServerInstance.provider.getDimensionId() != 1) {
            if (!this.func_180620_b(p_180266_1_, p_180266_2_)) {
                this.makePortal(p_180266_1_);
                this.func_180620_b(p_180266_1_, p_180266_2_);
            }
        }
        else {
            final int var3 = MathHelper.floor_double(p_180266_1_.posX);
            final int var4 = MathHelper.floor_double(p_180266_1_.posY) - 1;
            final int var5 = MathHelper.floor_double(p_180266_1_.posZ);
            final byte var6 = 1;
            final byte var7 = 0;
            for (int var8 = -2; var8 <= 2; ++var8) {
                for (int var9 = -2; var9 <= 2; ++var9) {
                    for (int var10 = -1; var10 < 3; ++var10) {
                        final int var11 = var3 + var9 * var6 + var8 * var7;
                        final int var12 = var4 + var10;
                        final int var13 = var5 + var9 * var7 - var8 * var6;
                        final boolean var14 = var10 < 0;
                        this.worldServerInstance.setBlockState(new BlockPos(var11, var12, var13), var14 ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
                    }
                }
            }
            p_180266_1_.setLocationAndAngles(var3, var4, var5, p_180266_1_.rotationYaw, 0.0f);
            final double motionX = 0.0;
            p_180266_1_.motionZ = motionX;
            p_180266_1_.motionY = motionX;
            p_180266_1_.motionX = motionX;
        }
    }
    
    public boolean func_180620_b(final Entity p_180620_1_, final float p_180620_2_) {
        final boolean var3 = true;
        double var4 = -1.0;
        final int var5 = MathHelper.floor_double(p_180620_1_.posX);
        final int var6 = MathHelper.floor_double(p_180620_1_.posZ);
        boolean var7 = true;
        Object var8 = BlockPos.ORIGIN;
        final long var9 = ChunkCoordIntPair.chunkXZ2Int(var5, var6);
        if (this.destinationCoordinateCache.containsItem(var9)) {
            final PortalPosition var10 = (PortalPosition)this.destinationCoordinateCache.getValueByKey(var9);
            var4 = 0.0;
            var8 = var10;
            var10.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
            var7 = false;
        }
        else {
            final BlockPos var11 = new BlockPos(p_180620_1_);
            for (int var12 = -128; var12 <= 128; ++var12) {
                for (int var13 = -128; var13 <= 128; ++var13) {
                    BlockPos var15;
                    for (BlockPos var14 = var11.add(var12, this.worldServerInstance.getActualHeight() - 1 - var11.getY(), var13); var14.getY() >= 0; var14 = var15) {
                        var15 = var14.offsetDown();
                        if (this.worldServerInstance.getBlockState(var14).getBlock() == Blocks.portal) {
                            while (this.worldServerInstance.getBlockState(var15 = var14.offsetDown()).getBlock() == Blocks.portal) {
                                var14 = var15;
                            }
                            final double var16 = var14.distanceSq(var11);
                            if (var4 < 0.0 || var16 < var4) {
                                var4 = var16;
                                var8 = var14;
                            }
                        }
                    }
                }
            }
        }
        if (var4 >= 0.0) {
            if (var7) {
                this.destinationCoordinateCache.add(var9, new PortalPosition((BlockPos)var8, this.worldServerInstance.getTotalWorldTime()));
                this.destinationCoordinateKeys.add(var9);
            }
            double var17 = ((BlockPos)var8).getX() + 0.5;
            double var18 = ((BlockPos)var8).getY() + 0.5;
            double var19 = ((BlockPos)var8).getZ() + 0.5;
            EnumFacing var20 = null;
            if (this.worldServerInstance.getBlockState(((BlockPos)var8).offsetWest()).getBlock() == Blocks.portal) {
                var20 = EnumFacing.NORTH;
            }
            if (this.worldServerInstance.getBlockState(((BlockPos)var8).offsetEast()).getBlock() == Blocks.portal) {
                var20 = EnumFacing.SOUTH;
            }
            if (this.worldServerInstance.getBlockState(((BlockPos)var8).offsetNorth()).getBlock() == Blocks.portal) {
                var20 = EnumFacing.EAST;
            }
            if (this.worldServerInstance.getBlockState(((BlockPos)var8).offsetSouth()).getBlock() == Blocks.portal) {
                var20 = EnumFacing.WEST;
            }
            final EnumFacing var21 = EnumFacing.getHorizontal(p_180620_1_.getTeleportDirection());
            if (var20 != null) {
                EnumFacing var22 = var20.rotateYCCW();
                final BlockPos var23 = ((BlockPos)var8).offset(var20);
                boolean var24 = this.func_180265_a(var23);
                boolean var25 = this.func_180265_a(var23.offset(var22));
                if (var25 && var24) {
                    var8 = ((BlockPos)var8).offset(var22);
                    var20 = var20.getOpposite();
                    var22 = var22.getOpposite();
                    final BlockPos var26 = ((BlockPos)var8).offset(var20);
                    var24 = this.func_180265_a(var26);
                    var25 = this.func_180265_a(var26.offset(var22));
                }
                float var27 = 0.5f;
                float var28 = 0.5f;
                if (!var25 && var24) {
                    var27 = 1.0f;
                }
                else if (var25 && !var24) {
                    var27 = 0.0f;
                }
                else if (var25) {
                    var28 = 0.0f;
                }
                var17 = ((BlockPos)var8).getX() + 0.5;
                var18 = ((BlockPos)var8).getY() + 0.5;
                var19 = ((BlockPos)var8).getZ() + 0.5;
                var17 += var22.getFrontOffsetX() * var27 + var20.getFrontOffsetX() * var28;
                var19 += var22.getFrontOffsetZ() * var27 + var20.getFrontOffsetZ() * var28;
                float var29 = 0.0f;
                float var30 = 0.0f;
                float var31 = 0.0f;
                float var32 = 0.0f;
                if (var20 == var21) {
                    var29 = 1.0f;
                    var30 = 1.0f;
                }
                else if (var20 == var21.getOpposite()) {
                    var29 = -1.0f;
                    var30 = -1.0f;
                }
                else if (var20 == var21.rotateY()) {
                    var31 = 1.0f;
                    var32 = -1.0f;
                }
                else {
                    var31 = -1.0f;
                    var32 = 1.0f;
                }
                final double var33 = p_180620_1_.motionX;
                final double var34 = p_180620_1_.motionZ;
                p_180620_1_.motionX = var33 * var29 + var34 * var32;
                p_180620_1_.motionZ = var33 * var31 + var34 * var30;
                p_180620_1_.rotationYaw = p_180620_2_ - var21.getHorizontalIndex() * 90 + var20.getHorizontalIndex() * 90;
            }
            else {
                final double motionX = 0.0;
                p_180620_1_.motionZ = motionX;
                p_180620_1_.motionY = motionX;
                p_180620_1_.motionX = motionX;
            }
            p_180620_1_.setLocationAndAngles(var17, var18, var19, p_180620_1_.rotationYaw, p_180620_1_.rotationPitch);
            return true;
        }
        return false;
    }
    
    private boolean func_180265_a(final BlockPos p_180265_1_) {
        return !this.worldServerInstance.isAirBlock(p_180265_1_) || !this.worldServerInstance.isAirBlock(p_180265_1_.offsetUp());
    }
    
    public boolean makePortal(final Entity p_85188_1_) {
        final byte var2 = 16;
        double var3 = -1.0;
        final int var4 = MathHelper.floor_double(p_85188_1_.posX);
        final int var5 = MathHelper.floor_double(p_85188_1_.posY);
        final int var6 = MathHelper.floor_double(p_85188_1_.posZ);
        int var7 = var4;
        int var8 = var5;
        int var9 = var6;
        int var10 = 0;
        final int var11 = this.random.nextInt(4);
        for (int var12 = var4 - var2; var12 <= var4 + var2; ++var12) {
            final double var13 = var12 + 0.5 - p_85188_1_.posX;
            for (int var14 = var6 - var2; var14 <= var6 + var2; ++var14) {
                final double var15 = var14 + 0.5 - p_85188_1_.posZ;
            Label_0466:
                for (int var16 = this.worldServerInstance.getActualHeight() - 1; var16 >= 0; --var16) {
                    if (this.worldServerInstance.isAirBlock(new BlockPos(var12, var16, var14))) {
                        while (var16 > 0 && this.worldServerInstance.isAirBlock(new BlockPos(var12, var16 - 1, var14))) {
                            --var16;
                        }
                        for (int var17 = var11; var17 < var11 + 4; ++var17) {
                            int var18 = var17 % 2;
                            int var19 = 1 - var18;
                            if (var17 % 4 >= 2) {
                                var18 = -var18;
                                var19 = -var19;
                            }
                            for (int var20 = 0; var20 < 3; ++var20) {
                                for (int var21 = 0; var21 < 4; ++var21) {
                                    for (int var22 = -1; var22 < 4; ++var22) {
                                        final int var23 = var12 + (var21 - 1) * var18 + var20 * var19;
                                        final int var24 = var16 + var22;
                                        final int var25 = var14 + (var21 - 1) * var19 - var20 * var18;
                                        if (var22 < 0 && !this.worldServerInstance.getBlockState(new BlockPos(var23, var24, var25)).getBlock().getMaterial().isSolid()) {
                                            continue Label_0466;
                                        }
                                        if (var22 >= 0 && !this.worldServerInstance.isAirBlock(new BlockPos(var23, var24, var25))) {
                                            continue Label_0466;
                                        }
                                    }
                                }
                            }
                            final double var26 = var16 + 0.5 - p_85188_1_.posY;
                            final double var27 = var13 * var13 + var26 * var26 + var15 * var15;
                            if (var3 < 0.0 || var27 < var3) {
                                var3 = var27;
                                var7 = var12;
                                var8 = var16;
                                var9 = var14;
                                var10 = var17 % 4;
                            }
                        }
                    }
                }
            }
        }
        if (var3 < 0.0) {
            for (int var12 = var4 - var2; var12 <= var4 + var2; ++var12) {
                final double var13 = var12 + 0.5 - p_85188_1_.posX;
                for (int var14 = var6 - var2; var14 <= var6 + var2; ++var14) {
                    final double var15 = var14 + 0.5 - p_85188_1_.posZ;
                Label_0852:
                    for (int var16 = this.worldServerInstance.getActualHeight() - 1; var16 >= 0; --var16) {
                        if (this.worldServerInstance.isAirBlock(new BlockPos(var12, var16, var14))) {
                            while (var16 > 0 && this.worldServerInstance.isAirBlock(new BlockPos(var12, var16 - 1, var14))) {
                                --var16;
                            }
                            for (int var17 = var11; var17 < var11 + 2; ++var17) {
                                final int var18 = var17 % 2;
                                final int var19 = 1 - var18;
                                for (int var20 = 0; var20 < 4; ++var20) {
                                    for (int var21 = -1; var21 < 4; ++var21) {
                                        final int var22 = var12 + (var20 - 1) * var18;
                                        final int var23 = var16 + var21;
                                        final int var24 = var14 + (var20 - 1) * var19;
                                        if (var21 < 0 && !this.worldServerInstance.getBlockState(new BlockPos(var22, var23, var24)).getBlock().getMaterial().isSolid()) {
                                            continue Label_0852;
                                        }
                                        if (var21 >= 0 && !this.worldServerInstance.isAirBlock(new BlockPos(var22, var23, var24))) {
                                            continue Label_0852;
                                        }
                                    }
                                }
                                final double var26 = var16 + 0.5 - p_85188_1_.posY;
                                final double var27 = var13 * var13 + var26 * var26 + var15 * var15;
                                if (var3 < 0.0 || var27 < var3) {
                                    var3 = var27;
                                    var7 = var12;
                                    var8 = var16;
                                    var9 = var14;
                                    var10 = var17 % 2;
                                }
                            }
                        }
                    }
                }
            }
        }
        final int var28 = var7;
        int var29 = var8;
        int var14 = var9;
        int var30 = var10 % 2;
        int var31 = 1 - var30;
        if (var10 % 4 >= 2) {
            var30 = -var30;
            var31 = -var31;
        }
        if (var3 < 0.0) {
            var8 = (var29 = MathHelper.clamp_int(var8, 70, this.worldServerInstance.getActualHeight() - 10));
            for (int var16 = -1; var16 <= 1; ++var16) {
                for (int var17 = 1; var17 < 3; ++var17) {
                    for (int var18 = -1; var18 < 3; ++var18) {
                        final int var19 = var28 + (var17 - 1) * var30 + var16 * var31;
                        final int var20 = var29 + var18;
                        final int var21 = var14 + (var17 - 1) * var31 - var16 * var30;
                        final boolean var32 = var18 < 0;
                        this.worldServerInstance.setBlockState(new BlockPos(var19, var20, var21), var32 ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
                    }
                }
            }
        }
        final IBlockState var33 = Blocks.portal.getDefaultState().withProperty(BlockPortal.field_176550_a, (var30 != 0) ? EnumFacing.Axis.X : EnumFacing.Axis.Z);
        for (int var17 = 0; var17 < 4; ++var17) {
            for (int var18 = 0; var18 < 4; ++var18) {
                for (int var19 = -1; var19 < 4; ++var19) {
                    final int var20 = var28 + (var18 - 1) * var30;
                    final int var21 = var29 + var19;
                    final int var22 = var14 + (var18 - 1) * var31;
                    final boolean var34 = var18 == 0 || var18 == 3 || var19 == -1 || var19 == 3;
                    this.worldServerInstance.setBlockState(new BlockPos(var20, var21, var22), var34 ? Blocks.obsidian.getDefaultState() : var33, 2);
                }
            }
            for (int var18 = 0; var18 < 4; ++var18) {
                for (int var19 = -1; var19 < 4; ++var19) {
                    final int var20 = var28 + (var18 - 1) * var30;
                    final int var21 = var29 + var19;
                    final int var22 = var14 + (var18 - 1) * var31;
                    this.worldServerInstance.notifyNeighborsOfStateChange(new BlockPos(var20, var21, var22), this.worldServerInstance.getBlockState(new BlockPos(var20, var21, var22)).getBlock());
                }
            }
        }
        return true;
    }
    
    public void removeStalePortalLocations(final long p_85189_1_) {
        if (p_85189_1_ % 100L == 0L) {
            final Iterator var3 = this.destinationCoordinateKeys.iterator();
            final long var4 = p_85189_1_ - 600L;
            while (var3.hasNext()) {
                final Long var5 = var3.next();
                final PortalPosition var6 = (PortalPosition)this.destinationCoordinateCache.getValueByKey(var5);
                if (var6 == null || var6.lastUpdateTime < var4) {
                    var3.remove();
                    this.destinationCoordinateCache.remove(var5);
                }
            }
        }
    }
    
    public class PortalPosition extends BlockPos
    {
        public long lastUpdateTime;
        private static final String __OBFID = "CL_00000154";
        
        public PortalPosition(final BlockPos p_i45747_2_, final long p_i45747_3_) {
            super(p_i45747_2_.getX(), p_i45747_2_.getY(), p_i45747_2_.getZ());
            this.lastUpdateTime = p_i45747_3_;
        }
    }
}
