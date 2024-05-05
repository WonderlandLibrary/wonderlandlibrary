/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockBasePressurePlate
 *  net.minecraft.block.BlockCactus
 *  net.minecraft.block.BlockCarpet
 *  net.minecraft.block.BlockContainer
 *  net.minecraft.block.BlockFalling
 *  net.minecraft.block.BlockLadder
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockSlab
 *  net.minecraft.block.BlockSnow
 *  net.minecraft.block.BlockStainedGlassPane
 *  net.minecraft.block.BlockStairs
 *  net.minecraft.block.BlockWall
 *  net.minecraft.block.BlockWeb
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils.block;

import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010$\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\"B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0018\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0007J\u0018\u0010\f\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0007J\u0014\u0010\r\u001a\u0004\u0018\u00010\u000e2\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0007J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0014\u0010\u0015\u001a\u0004\u0018\u00010\u00162\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0012\u0010\u0017\u001a\u00020\u00182\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0012\u0010\u0019\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\b\u0010\u001a\u001a\u00020\u0004H\u0007J\u0012\u0010\u001b\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0018\u0010\u001c\u001a\u00020\u00042\u0006\u0010\u001d\u001a\u00020\u000e2\u0006\u0010\u001e\u001a\u00020\u0004H\u0007J\u001c\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u000e0 2\u0006\u0010!\u001a\u00020\u0012H\u0007\u00a8\u0006#"}, d2={"Lnet/ccbluex/liquidbounce/utils/block/BlockUtils;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "canBeClicked", "", "blockPos", "Lnet/minecraft/util/BlockPos;", "collideBlock", "axisAlignedBB", "Lnet/minecraft/util/AxisAlignedBB;", "collide", "Lnet/ccbluex/liquidbounce/utils/block/BlockUtils$Collidable;", "collideBlockIntersects", "getBlock", "Lnet/minecraft/block/Block;", "getBlockName", "", "id", "", "getCenterDistance", "", "getMaterial", "Lnet/minecraft/block/material/Material;", "getState", "Lnet/minecraft/block/state/IBlockState;", "isFullBlock", "isInLiquid", "isReplaceable", "isValidBlock", "block", "placing", "searchBlocks", "", "radius", "Collidable", "KyinoClient"})
public final class BlockUtils
extends MinecraftInstance {
    public static final BlockUtils INSTANCE;

    @JvmStatic
    @Nullable
    public static final Block getBlock(@Nullable BlockPos blockPos) {
        WorldClient worldClient = BlockUtils.access$getMc$p$s1046033730().field_71441_e;
        return worldClient != null && (worldClient = worldClient.func_180495_p(blockPos)) != null ? worldClient.func_177230_c() : null;
    }

    @JvmStatic
    @Nullable
    public static final Material getMaterial(@Nullable BlockPos blockPos) {
        Block block = BlockUtils.getBlock(blockPos);
        return block != null ? block.func_149688_o() : null;
    }

    @JvmStatic
    public static final boolean isReplaceable(@Nullable BlockPos blockPos) {
        Material material = BlockUtils.getMaterial(blockPos);
        return material != null ? material.func_76222_j() : false;
    }

    @JvmStatic
    @NotNull
    public static final IBlockState getState(@Nullable BlockPos blockPos) {
        IBlockState iBlockState = BlockUtils.access$getMc$p$s1046033730().field_71441_e.func_180495_p(blockPos);
        Intrinsics.checkExpressionValueIsNotNull(iBlockState, "mc.theWorld.getBlockState(blockPos)");
        return iBlockState;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @JvmStatic
    public static final boolean canBeClicked(@Nullable BlockPos blockPos) {
        Block block = BlockUtils.getBlock(blockPos);
        if (block == null) return false;
        boolean bl = block.func_176209_a(BlockUtils.getState(blockPos), false);
        if (!bl) return false;
        WorldClient worldClient = BlockUtils.access$getMc$p$s1046033730().field_71441_e;
        Intrinsics.checkExpressionValueIsNotNull(worldClient, "mc.theWorld");
        if (!worldClient.func_175723_af().func_177746_a(blockPos)) return false;
        return true;
    }

    @JvmStatic
    @NotNull
    public static final String getBlockName(int id) {
        Block block = Block.func_149729_e((int)id);
        Intrinsics.checkExpressionValueIsNotNull(block, "Block.getBlockById(id)");
        String string = block.func_149732_F();
        Intrinsics.checkExpressionValueIsNotNull(string, "Block.getBlockById(id).localizedName");
        return string;
    }

    @JvmStatic
    public static final boolean isFullBlock(@Nullable BlockPos blockPos) {
        Block block = BlockUtils.getBlock(blockPos);
        if (block == null || (block = block.func_180640_a((World)BlockUtils.access$getMc$p$s1046033730().field_71441_e, blockPos, BlockUtils.getState(blockPos))) == null) {
            return false;
        }
        Block axisAlignedBB = block;
        return axisAlignedBB.field_72336_d - axisAlignedBB.field_72340_a == 1.0 && axisAlignedBB.field_72337_e - axisAlignedBB.field_72338_b == 1.0 && axisAlignedBB.field_72334_f - axisAlignedBB.field_72339_c == 1.0;
    }

    /*
     * WARNING - void declaration
     */
    @JvmStatic
    public static final boolean isInLiquid() {
        EntityPlayerSP entityPlayerSP = BlockUtils.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        AxisAlignedBB axisAlignedBB = entityPlayerSP.func_174813_aQ().func_72331_e(0.001, 0.001, 0.001);
        Intrinsics.checkExpressionValueIsNotNull(axisAlignedBB, "mc.thePlayer.entityBound\u20260.001,\n            0.001)");
        AxisAlignedBB par1AxisAlignedBB = axisAlignedBB;
        int var4 = MathHelper.func_76128_c((double)par1AxisAlignedBB.field_72340_a);
        int var5 = MathHelper.func_76128_c((double)(par1AxisAlignedBB.field_72336_d + 1.0));
        int var6 = MathHelper.func_76128_c((double)par1AxisAlignedBB.field_72338_b);
        int var7 = MathHelper.func_76128_c((double)(par1AxisAlignedBB.field_72337_e + 1.0));
        int var8 = MathHelper.func_76128_c((double)par1AxisAlignedBB.field_72339_c);
        int var9 = MathHelper.func_76128_c((double)(par1AxisAlignedBB.field_72334_f + 1.0));
        int n = var4;
        int n2 = var5;
        while (n < n2) {
            void var11;
            int n3 = var6;
            int n4 = var7;
            while (n3 < n4) {
                void var12;
                int n5 = var8;
                int n6 = var9;
                while (n5 < n6) {
                    Block var14;
                    void var13;
                    BlockPos pos = new BlockPos((int)var11, (int)var12, (int)var13);
                    IBlockState iBlockState = BlockUtils.access$getMc$p$s1046033730().field_71441_e.func_180495_p(pos);
                    Intrinsics.checkExpressionValueIsNotNull(iBlockState, "mc.theWorld.getBlockState(pos)");
                    Intrinsics.checkExpressionValueIsNotNull(iBlockState.func_177230_c(), "mc.theWorld.getBlockState(pos).block");
                    if (var14 instanceof BlockLiquid) {
                        return true;
                    }
                    ++var13;
                }
                ++var12;
            }
            ++var11;
        }
        return false;
    }

    @JvmStatic
    public static final double getCenterDistance(@NotNull BlockPos blockPos) {
        Intrinsics.checkParameterIsNotNull(blockPos, "blockPos");
        return BlockUtils.access$getMc$p$s1046033730().field_71439_g.func_70011_f((double)blockPos.func_177958_n() + 0.5, (double)blockPos.func_177956_o() + 0.5, (double)blockPos.func_177952_p() + 0.5);
    }

    @JvmStatic
    public static final boolean isValidBlock(@NotNull Block block, boolean placing) {
        boolean bl;
        block6: {
            block5: {
                Intrinsics.checkParameterIsNotNull(block, "block");
                if (block instanceof BlockCarpet || block instanceof BlockSnow || block instanceof BlockContainer || block instanceof BlockBasePressurePlate) break block5;
                Material material = block.func_149688_o();
                Intrinsics.checkExpressionValueIsNotNull(material, "block.material");
                if (!material.func_76224_d()) break block6;
            }
            return false;
        }
        if (placing && (block instanceof BlockSlab || block instanceof BlockStairs || block instanceof BlockLadder || block instanceof BlockStainedGlassPane || block instanceof BlockWall || block instanceof BlockWeb || block instanceof BlockCactus || block instanceof BlockFalling || block == Blocks.field_150410_aZ || block == Blocks.field_150411_aY)) {
            bl = false;
        } else {
            Material material = block.func_149688_o();
            Intrinsics.checkExpressionValueIsNotNull(material, "block.material");
            bl = material.func_76220_a() || !block.func_149751_l() || block.func_149730_j();
        }
        return bl;
    }

    /*
     * WARNING - void declaration
     */
    @JvmStatic
    @NotNull
    public static final Map<BlockPos, Block> searchBlocks(int radius) {
        int n = 0;
        Map blocks = new LinkedHashMap();
        n = radius;
        int n2 = -radius + 1;
        if (n >= n2) {
            while (true) {
                void x;
                int n3;
                int n4;
                if ((n4 = radius) >= (n3 = -radius + 1)) {
                    while (true) {
                        void y;
                        int n5;
                        int n6;
                        if ((n6 = radius) >= (n5 = -radius + 1)) {
                            while (true) {
                                void z;
                                BlockPos blockPos;
                                if (BlockUtils.getBlock(blockPos = new BlockPos((int)BlockUtils.access$getMc$p$s1046033730().field_71439_g.field_70165_t + x, (int)BlockUtils.access$getMc$p$s1046033730().field_71439_g.field_70163_u + y, (int)BlockUtils.access$getMc$p$s1046033730().field_71439_g.field_70161_v + z)) == null) {
                                } else {
                                    Block block;
                                    blocks.put(blockPos, block);
                                }
                                if (z == n5) break;
                                --z;
                            }
                        }
                        if (y == n3) break;
                        --y;
                    }
                }
                if (x == n2) break;
                --x;
            }
        }
        return blocks;
    }

    /*
     * WARNING - void declaration
     */
    @JvmStatic
    public static final boolean collideBlock(@NotNull AxisAlignedBB axisAlignedBB, @NotNull Collidable collide) {
        Intrinsics.checkParameterIsNotNull(axisAlignedBB, "axisAlignedBB");
        Intrinsics.checkParameterIsNotNull(collide, "collide");
        EntityPlayerSP entityPlayerSP = BlockUtils.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        int n = MathHelper.func_76128_c((double)entityPlayerSP.func_174813_aQ().field_72340_a);
        EntityPlayerSP entityPlayerSP2 = BlockUtils.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
        int n2 = MathHelper.func_76128_c((double)entityPlayerSP2.func_174813_aQ().field_72336_d) + 1;
        while (n < n2) {
            void x;
            EntityPlayerSP entityPlayerSP3 = BlockUtils.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
            int n3 = MathHelper.func_76128_c((double)entityPlayerSP3.func_174813_aQ().field_72339_c);
            EntityPlayerSP entityPlayerSP4 = BlockUtils.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP4, "mc.thePlayer");
            int n4 = MathHelper.func_76128_c((double)entityPlayerSP4.func_174813_aQ().field_72334_f) + 1;
            while (n3 < n4) {
                void z;
                Block block = BlockUtils.getBlock(new BlockPos((double)x, axisAlignedBB.field_72338_b, (double)z));
                if (!collide.collideBlock(block)) {
                    return false;
                }
                ++z;
            }
            ++x;
        }
        return true;
    }

    /*
     * WARNING - void declaration
     */
    @JvmStatic
    public static final boolean collideBlockIntersects(@NotNull AxisAlignedBB axisAlignedBB, @NotNull Collidable collide) {
        Intrinsics.checkParameterIsNotNull(axisAlignedBB, "axisAlignedBB");
        Intrinsics.checkParameterIsNotNull(collide, "collide");
        EntityPlayerSP entityPlayerSP = BlockUtils.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        int n = MathHelper.func_76128_c((double)entityPlayerSP.func_174813_aQ().field_72340_a);
        EntityPlayerSP entityPlayerSP2 = BlockUtils.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
        int n2 = MathHelper.func_76128_c((double)entityPlayerSP2.func_174813_aQ().field_72336_d) + 1;
        while (n < n2) {
            void x;
            EntityPlayerSP entityPlayerSP3 = BlockUtils.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
            int n3 = MathHelper.func_76128_c((double)entityPlayerSP3.func_174813_aQ().field_72339_c);
            EntityPlayerSP entityPlayerSP4 = BlockUtils.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP4, "mc.thePlayer");
            int n4 = MathHelper.func_76128_c((double)entityPlayerSP4.func_174813_aQ().field_72334_f) + 1;
            while (n3 < n4) {
                void z;
                BlockPos blockPos = new BlockPos((double)x, axisAlignedBB.field_72338_b, (double)z);
                Block block = BlockUtils.getBlock(blockPos);
                if (collide.collideBlock(block)) {
                    Block block2 = block;
                    if (block2 == null || (block2 = block2.func_180640_a((World)BlockUtils.access$getMc$p$s1046033730().field_71441_e, blockPos, BlockUtils.getState(blockPos))) == null) {
                    } else {
                        Block boundingBox = block2;
                        EntityPlayerSP entityPlayerSP5 = BlockUtils.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP5, "mc.thePlayer");
                        if (entityPlayerSP5.func_174813_aQ().func_72326_a((AxisAlignedBB)boundingBox)) {
                            return true;
                        }
                    }
                }
                ++z;
            }
            ++x;
        }
        return false;
    }

    private BlockUtils() {
    }

    static {
        BlockUtils blockUtils;
        INSTANCE = blockUtils = new BlockUtils();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H&\u00a8\u0006\u0006"}, d2={"Lnet/ccbluex/liquidbounce/utils/block/BlockUtils$Collidable;", "", "collideBlock", "", "block", "Lnet/minecraft/block/Block;", "KyinoClient"})
    public static interface Collidable {
        public boolean collideBlock(@Nullable Block var1);
    }
}

