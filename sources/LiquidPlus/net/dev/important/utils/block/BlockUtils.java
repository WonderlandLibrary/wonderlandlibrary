/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.utils.block;

import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.utils.MinecraftInstance;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001#B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0018\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0007J\u0018\u0010\f\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0007J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0007J\u0014\u0010\u0010\u001a\u0004\u0018\u00010\u00112\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0007J\u0018\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0015H\u0007J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0014\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0012\u0010\u001c\u001a\u00020\u001d2\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0012\u0010\u001e\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u0012\u0010\u001f\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0007J\u001c\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00110!2\u0006\u0010\"\u001a\u00020\u0015H\u0007\u00a8\u0006$"}, d2={"Lnet/dev/important/utils/block/BlockUtils;", "Lnet/dev/important/utils/MinecraftInstance;", "()V", "canBeClicked", "", "blockPos", "Lnet/minecraft/util/BlockPos;", "collideBlock", "axisAlignedBB", "Lnet/minecraft/util/AxisAlignedBB;", "collide", "Lnet/dev/important/utils/block/BlockUtils$Collidable;", "collideBlockIntersects", "floorVec3", "Lnet/minecraft/util/Vec3;", "vec3", "getBlock", "Lnet/minecraft/block/Block;", "getBlockName", "", "id", "", "getBlockNames", "integer", "getCenterDistance", "", "getMaterial", "Lnet/minecraft/block/material/Material;", "getState", "Lnet/minecraft/block/state/IBlockState;", "isFullBlock", "isReplaceable", "searchBlocks", "", "radius", "Collidable", "LiquidBounce"})
public final class BlockUtils
extends MinecraftInstance {
    @NotNull
    public static final BlockUtils INSTANCE = new BlockUtils();

    private BlockUtils() {
    }

    @JvmStatic
    @Nullable
    public static final Block getBlock(@Nullable BlockPos blockPos) {
        Object object;
        WorldClient worldClient = MinecraftInstance.mc.field_71441_e;
        if (worldClient == null) {
            object = null;
        } else {
            IBlockState iBlockState = worldClient.func_180495_p(blockPos);
            object = iBlockState == null ? null : iBlockState.func_177230_c();
        }
        return object;
    }

    @JvmStatic
    @Nullable
    public static final Material getMaterial(@Nullable BlockPos blockPos) {
        Block block = BlockUtils.getBlock(blockPos);
        return block == null ? null : block.func_149688_o();
    }

    @JvmStatic
    public static final boolean isReplaceable(@Nullable BlockPos blockPos) {
        boolean bl;
        Material material = BlockUtils.getMaterial(blockPos);
        return material == null ? false : (bl = material.func_76222_j());
    }

    @JvmStatic
    @NotNull
    public static final IBlockState getState(@Nullable BlockPos blockPos) {
        IBlockState iBlockState = MinecraftInstance.mc.field_71441_e.func_180495_p(blockPos);
        Intrinsics.checkNotNullExpressionValue(iBlockState, "mc.theWorld.getBlockState(blockPos)");
        return iBlockState;
    }

    @JvmStatic
    public static final boolean canBeClicked(@Nullable BlockPos blockPos) {
        boolean bl;
        Block block = BlockUtils.getBlock(blockPos);
        boolean bl2 = block == null ? false : (bl = block.func_176209_a(BlockUtils.getState(blockPos), false));
        return bl2 && MinecraftInstance.mc.field_71441_e.func_175723_af().func_177746_a(blockPos);
    }

    @JvmStatic
    @NotNull
    public static final String getBlockName(int id) {
        String string = Block.func_149729_e((int)id).func_149732_F();
        Intrinsics.checkNotNullExpressionValue(string, "getBlockById(id).localizedName");
        return string;
    }

    @JvmStatic
    @NotNull
    public static final String getBlockNames(int id, int integer) {
        String string = Block.func_149729_e((int)id).func_149732_F();
        Intrinsics.checkNotNullExpressionValue(string, "getBlockById(id).localizedName");
        return string;
    }

    @JvmStatic
    public static final boolean isFullBlock(@Nullable BlockPos blockPos) {
        AxisAlignedBB axisAlignedBB;
        Block block = BlockUtils.getBlock(blockPos);
        AxisAlignedBB axisAlignedBB2 = block == null ? null : (axisAlignedBB = block.func_180640_a((World)MinecraftInstance.mc.field_71441_e, blockPos, BlockUtils.getState(blockPos)));
        if (axisAlignedBB == null) {
            return false;
        }
        AxisAlignedBB axisAlignedBB3 = axisAlignedBB;
        return axisAlignedBB3.field_72336_d - axisAlignedBB3.field_72340_a == 1.0 && axisAlignedBB3.field_72337_e - axisAlignedBB3.field_72338_b == 1.0 && axisAlignedBB3.field_72334_f - axisAlignedBB3.field_72339_c == 1.0;
    }

    @JvmStatic
    public static final double getCenterDistance(@NotNull BlockPos blockPos) {
        Intrinsics.checkNotNullParameter(blockPos, "blockPos");
        return MinecraftInstance.mc.field_71439_g.func_70011_f((double)blockPos.func_177958_n() + 0.5, (double)blockPos.func_177956_o() + 0.5, (double)blockPos.func_177952_p() + 0.5);
    }

    @JvmStatic
    @NotNull
    public static final Map<BlockPos, Block> searchBlocks(int radius) {
        Map blocks = new LinkedHashMap();
        int n = -radius + 1;
        int n2 = radius;
        if (n <= n2) {
            int x;
            do {
                int y;
                x = n2--;
                int n3 = -radius + 1;
                int n4 = radius;
                if (n3 > n4) continue;
                do {
                    int z;
                    y = n4--;
                    int n5 = -radius + 1;
                    int n6 = radius;
                    if (n5 > n6) continue;
                    do {
                        Block block;
                        z = n6--;
                        BlockPos blockPos = new BlockPos((int)MinecraftInstance.mc.field_71439_g.field_70165_t + x, (int)MinecraftInstance.mc.field_71439_g.field_70163_u + y, (int)MinecraftInstance.mc.field_71439_g.field_70161_v + z);
                        if (BlockUtils.getBlock(blockPos) == null) continue;
                        blocks.put(blockPos, block);
                    } while (z != n5);
                } while (y != n3);
            } while (x != n);
        }
        return blocks;
    }

    @JvmStatic
    public static final boolean collideBlock(@NotNull AxisAlignedBB axisAlignedBB, @NotNull Collidable collide) {
        Intrinsics.checkNotNullParameter(axisAlignedBB, "axisAlignedBB");
        Intrinsics.checkNotNullParameter(collide, "collide");
        int n = MathHelper.func_76128_c((double)MinecraftInstance.mc.field_71439_g.func_174813_aQ().field_72340_a);
        int n2 = MathHelper.func_76128_c((double)MinecraftInstance.mc.field_71439_g.func_174813_aQ().field_72336_d) + 1;
        while (n < n2) {
            int x = n++;
            int n3 = MathHelper.func_76128_c((double)MinecraftInstance.mc.field_71439_g.func_174813_aQ().field_72339_c);
            int n4 = MathHelper.func_76128_c((double)MinecraftInstance.mc.field_71439_g.func_174813_aQ().field_72334_f) + 1;
            while (n3 < n4) {
                int z = n3++;
                Block block = BlockUtils.getBlock(new BlockPos((double)x, axisAlignedBB.field_72338_b, (double)z));
                if (collide.collideBlock(block)) continue;
                return false;
            }
        }
        return true;
    }

    @JvmStatic
    public static final boolean collideBlockIntersects(@NotNull AxisAlignedBB axisAlignedBB, @NotNull Collidable collide) {
        Intrinsics.checkNotNullParameter(axisAlignedBB, "axisAlignedBB");
        Intrinsics.checkNotNullParameter(collide, "collide");
        int n = MathHelper.func_76128_c((double)MinecraftInstance.mc.field_71439_g.func_174813_aQ().field_72340_a);
        int n2 = MathHelper.func_76128_c((double)MinecraftInstance.mc.field_71439_g.func_174813_aQ().field_72336_d) + 1;
        while (n < n2) {
            int x = n++;
            int n3 = MathHelper.func_76128_c((double)MinecraftInstance.mc.field_71439_g.func_174813_aQ().field_72339_c);
            int n4 = MathHelper.func_76128_c((double)MinecraftInstance.mc.field_71439_g.func_174813_aQ().field_72334_f) + 1;
            while (n3 < n4) {
                AxisAlignedBB axisAlignedBB2;
                int z = n3++;
                BlockPos blockPos = new BlockPos((double)x, axisAlignedBB.field_72338_b, (double)z);
                Block block = BlockUtils.getBlock(blockPos);
                if (!collide.collideBlock(block)) continue;
                Block block2 = block;
                AxisAlignedBB axisAlignedBB3 = block2 == null ? null : (axisAlignedBB2 = block2.func_180640_a((World)MinecraftInstance.mc.field_71441_e, blockPos, BlockUtils.getState(blockPos)));
                if (axisAlignedBB2 == null) continue;
                AxisAlignedBB boundingBox = axisAlignedBB2;
                if (!MinecraftInstance.mc.field_71439_g.func_174813_aQ().func_72326_a(boundingBox)) continue;
                return true;
            }
        }
        return false;
    }

    @JvmStatic
    @NotNull
    public static final Vec3 floorVec3(@NotNull Vec3 vec3) {
        Intrinsics.checkNotNullParameter(vec3, "vec3");
        return new Vec3(Math.floor(vec3.field_72450_a), Math.floor(vec3.field_72448_b), Math.floor(vec3.field_72449_c));
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H&\u00a8\u0006\u0006"}, d2={"Lnet/dev/important/utils/block/BlockUtils$Collidable;", "", "collideBlock", "", "block", "Lnet/minecraft/block/Block;", "LiquidBounce"})
    public static interface Collidable {
        public boolean collideBlock(@Nullable Block var1);
    }
}

