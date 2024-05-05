/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
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
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001!B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0018\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0007J\u0018\u0010\f\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0007J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0007J\u0014\u0010\u0010\u001a\u0004\u0018\u00010\u00112\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0014\u0010\u0010\u001a\u0004\u0018\u00010\u00112\b\u0010\u000f\u001a\u0004\u0018\u00010\u000eH\u0007J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0007J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0014\u0010\u0018\u001a\u0004\u0018\u00010\u00192\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0012\u0010\u001a\u001a\u00020\u001b2\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0012\u0010\u001c\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0012\u0010\u001d\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u001c\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00110\u001f2\u0006\u0010 \u001a\u00020\u0015H\u0007\u00a8\u0006\""}, d2={"Lnet/ccbluex/liquidbounce/utils/block/BlockUtils2;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "canBeClicked", "", "blockPos", "Lnet/minecraft/util/BlockPos;", "collideBlock", "axisAlignedBB", "Lnet/minecraft/util/AxisAlignedBB;", "collide", "Lnet/ccbluex/liquidbounce/utils/block/BlockUtils2$Collidable;", "collideBlockIntersects", "floorVec3", "Lnet/minecraft/util/Vec3;", "vec3", "getBlock", "Lnet/minecraft/block/Block;", "getBlockName", "", "id", "", "getCenterDistance", "", "getMaterial", "Lnet/minecraft/block/material/Material;", "getState", "Lnet/minecraft/block/state/IBlockState;", "isFullBlock", "isReplaceable", "searchBlocks", "", "radius", "Collidable", "KyinoClient"})
public final class BlockUtils2
extends MinecraftInstance {
    public static final BlockUtils2 INSTANCE;

    @JvmStatic
    @Nullable
    public static final Block getBlock(@Nullable Vec3 vec3) {
        return BlockUtils2.getBlock(new BlockPos(vec3));
    }

    @JvmStatic
    @Nullable
    public static final Block getBlock(@Nullable BlockPos blockPos) {
        WorldClient worldClient = BlockUtils2.access$getMc$p$s1046033730().field_71441_e;
        return worldClient != null && (worldClient = worldClient.func_180495_p(blockPos)) != null ? worldClient.func_177230_c() : null;
    }

    @JvmStatic
    @Nullable
    public static final Material getMaterial(@Nullable BlockPos blockPos) {
        Block block = BlockUtils2.getBlock(blockPos);
        return block != null ? block.func_149688_o() : null;
    }

    @JvmStatic
    public static final boolean isReplaceable(@Nullable BlockPos blockPos) {
        Material material = BlockUtils2.getMaterial(blockPos);
        return material != null ? material.func_76222_j() : false;
    }

    @JvmStatic
    @NotNull
    public static final IBlockState getState(@Nullable BlockPos blockPos) {
        IBlockState iBlockState = BlockUtils2.access$getMc$p$s1046033730().field_71441_e.func_180495_p(blockPos);
        Intrinsics.checkExpressionValueIsNotNull(iBlockState, "mc.theWorld.getBlockState(blockPos)");
        return iBlockState;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @JvmStatic
    public static final boolean canBeClicked(@Nullable BlockPos blockPos) {
        Block block = BlockUtils2.getBlock(blockPos);
        if (block == null) return false;
        boolean bl = block.func_176209_a(BlockUtils2.getState(blockPos), false);
        if (!bl) return false;
        WorldClient worldClient = BlockUtils2.access$getMc$p$s1046033730().field_71441_e;
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
        Block block = BlockUtils2.getBlock(blockPos);
        if (block == null || (block = block.func_180640_a((World)BlockUtils2.access$getMc$p$s1046033730().field_71441_e, blockPos, BlockUtils2.getState(blockPos))) == null) {
            return false;
        }
        Block axisAlignedBB = block;
        return axisAlignedBB.field_72336_d - axisAlignedBB.field_72340_a == 1.0 && axisAlignedBB.field_72337_e - axisAlignedBB.field_72338_b == 1.0 && axisAlignedBB.field_72334_f - axisAlignedBB.field_72339_c == 1.0;
    }

    @JvmStatic
    public static final double getCenterDistance(@NotNull BlockPos blockPos) {
        Intrinsics.checkParameterIsNotNull(blockPos, "blockPos");
        return BlockUtils2.access$getMc$p$s1046033730().field_71439_g.func_70011_f((double)blockPos.func_177958_n() + 0.5, (double)blockPos.func_177956_o() + 0.5, (double)blockPos.func_177952_p() + 0.5);
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
                                if (BlockUtils2.getBlock(blockPos = new BlockPos((int)BlockUtils2.access$getMc$p$s1046033730().field_71439_g.field_70165_t + x, (int)BlockUtils2.access$getMc$p$s1046033730().field_71439_g.field_70163_u + y, (int)BlockUtils2.access$getMc$p$s1046033730().field_71439_g.field_70161_v + z)) == null) {
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
        EntityPlayerSP entityPlayerSP = BlockUtils2.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        int n = MathHelper.func_76128_c((double)entityPlayerSP.func_174813_aQ().field_72340_a);
        EntityPlayerSP entityPlayerSP2 = BlockUtils2.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
        int n2 = MathHelper.func_76128_c((double)entityPlayerSP2.func_174813_aQ().field_72336_d) + 1;
        while (n < n2) {
            void x;
            EntityPlayerSP entityPlayerSP3 = BlockUtils2.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
            int n3 = MathHelper.func_76128_c((double)entityPlayerSP3.func_174813_aQ().field_72339_c);
            EntityPlayerSP entityPlayerSP4 = BlockUtils2.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP4, "mc.thePlayer");
            int n4 = MathHelper.func_76128_c((double)entityPlayerSP4.func_174813_aQ().field_72334_f) + 1;
            while (n3 < n4) {
                void z;
                Block block = BlockUtils2.getBlock(new BlockPos((double)x, axisAlignedBB.field_72338_b, (double)z));
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
        EntityPlayerSP entityPlayerSP = BlockUtils2.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        int n = MathHelper.func_76128_c((double)entityPlayerSP.func_174813_aQ().field_72340_a);
        EntityPlayerSP entityPlayerSP2 = BlockUtils2.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
        int n2 = MathHelper.func_76128_c((double)entityPlayerSP2.func_174813_aQ().field_72336_d) + 1;
        while (n < n2) {
            void x;
            EntityPlayerSP entityPlayerSP3 = BlockUtils2.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
            int n3 = MathHelper.func_76128_c((double)entityPlayerSP3.func_174813_aQ().field_72339_c);
            EntityPlayerSP entityPlayerSP4 = BlockUtils2.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP4, "mc.thePlayer");
            int n4 = MathHelper.func_76128_c((double)entityPlayerSP4.func_174813_aQ().field_72334_f) + 1;
            while (n3 < n4) {
                void z;
                BlockPos blockPos = new BlockPos((double)x, axisAlignedBB.field_72338_b, (double)z);
                Block block = BlockUtils2.getBlock(blockPos);
                if (collide.collideBlock(block)) {
                    Block block2 = block;
                    if (block2 == null || (block2 = block2.func_180640_a((World)BlockUtils2.access$getMc$p$s1046033730().field_71441_e, blockPos, BlockUtils2.getState(blockPos))) == null) {
                    } else {
                        Block boundingBox = block2;
                        EntityPlayerSP entityPlayerSP5 = BlockUtils2.access$getMc$p$s1046033730().field_71439_g;
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

    @JvmStatic
    @NotNull
    public static final Vec3 floorVec3(@NotNull Vec3 vec3) {
        double d;
        Intrinsics.checkParameterIsNotNull(vec3, "vec3");
        double d2 = vec3.field_72450_a;
        boolean bl = false;
        double d3 = Math.floor(d2);
        d2 = vec3.field_72448_b;
        bl = false;
        double d4 = Math.floor(d2);
        d2 = vec3.field_72449_c;
        bl = false;
        double d5 = d = Math.floor(d2);
        double d6 = d4;
        double d7 = d3;
        return new Vec3(d7, d6, d5);
    }

    private BlockUtils2() {
    }

    static {
        BlockUtils2 blockUtils2;
        INSTANCE = blockUtils2 = new BlockUtils2();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H&\u00a8\u0006\u0006"}, d2={"Lnet/ccbluex/liquidbounce/utils/block/BlockUtils2$Collidable;", "", "collideBlock", "", "block", "Lnet/minecraft/block/Block;", "KyinoClient"})
    public static interface Collidable {
        public boolean collideBlock(@Nullable Block var1);
    }
}

