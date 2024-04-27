/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.utils.block;

import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.block.material.IMaterial;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public final class BlockUtils
extends MinecraftInstance {
    public static final BlockUtils INSTANCE;

    @JvmStatic
    public static final IBlock getBlock(WBlockPos blockPos) {
        Object object = MinecraftInstance.mc.getTheWorld();
        return object != null && (object = object.getBlockState(blockPos)) != null ? object.getBlock() : null;
    }

    @JvmStatic
    public static final IMaterial getMaterial(WBlockPos blockPos) {
        IIBlockState state = BlockUtils.getState(blockPos);
        Object object = state;
        return object != null && (object = object.getBlock()) != null ? object.getMaterial(state) : null;
    }

    @JvmStatic
    public static final boolean isReplaceable(WBlockPos blockPos) {
        int $i$f$isReplaceable = 0;
        IMaterial iMaterial = BlockUtils.getMaterial(blockPos);
        return iMaterial != null ? iMaterial.isReplaceable() : false;
    }

    @JvmStatic
    public static final IIBlockState getState(WBlockPos blockPos) {
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        return iWorldClient != null ? iWorldClient.getBlockState(blockPos) : null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @JvmStatic
    public static final boolean canBeClicked(WBlockPos blockPos) {
        IBlock iBlock = BlockUtils.getBlock(blockPos);
        if (iBlock == null) return false;
        boolean bl = iBlock.canCollideCheck(BlockUtils.getState(blockPos), false);
        if (!bl) return false;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        if (!iWorldClient.getWorldBorder().contains(blockPos)) return false;
        return true;
    }

    @JvmStatic
    public static final String getBlockName(int id) {
        IBlock iBlock = MinecraftInstance.functions.getBlockById(id);
        if (iBlock == null) {
            Intrinsics.throwNpe();
        }
        return iBlock.getLocalizedName();
    }

    @JvmStatic
    public static final boolean isFullBlock(WBlockPos blockPos) {
        Object object;
        block6: {
            block5: {
                object = BlockUtils.getBlock(blockPos);
                if (object == null) break block5;
                IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient == null) {
                    Intrinsics.throwNpe();
                }
                IIBlockState iIBlockState = BlockUtils.getState(blockPos);
                if (iIBlockState == null) {
                    return false;
                }
                if ((object = object.getCollisionBoundingBox(iWorldClient, blockPos, iIBlockState)) != null) break block6;
            }
            return false;
        }
        Object axisAlignedBB = object;
        return axisAlignedBB.getMaxX() - axisAlignedBB.getMinX() == 1.0 && axisAlignedBB.getMaxY() - axisAlignedBB.getMinY() == 1.0 && axisAlignedBB.getMaxZ() - axisAlignedBB.getMinZ() == 1.0;
    }

    @JvmStatic
    public static final double getCenterDistance(WBlockPos blockPos) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        return iEntityPlayerSP.getDistance((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5);
    }

    /*
     * WARNING - void declaration
     */
    @JvmStatic
    public static final Map<WBlockPos, IBlock> searchBlocks(int radius) {
        boolean bl = false;
        Map blocks = new LinkedHashMap();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return blocks;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        int n = radius;
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
                                WBlockPos blockPos;
                                if (BlockUtils.getBlock(blockPos = new WBlockPos((int)thePlayer.getPosX() + x, (int)thePlayer.getPosY() + y, (int)thePlayer.getPosZ() + z)) == null) {
                                } else {
                                    IBlock block;
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
    public static final boolean collideBlock(IAxisAlignedBB axisAlignedBB, Function1<? super IBlock, Boolean> collide) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        double d = thePlayer.getEntityBoundingBox().getMinX();
        boolean bl = false;
        long l = (int)Math.floor(d);
        d = thePlayer.getEntityBoundingBox().getMaxX();
        bl = false;
        long l2 = (long)((int)Math.floor(d)) + 1L;
        while (l < l2) {
            void x;
            double d2 = thePlayer.getEntityBoundingBox().getMinZ();
            boolean bl2 = false;
            int n = (int)Math.floor(d2);
            d2 = thePlayer.getEntityBoundingBox().getMaxZ();
            bl2 = false;
            int n2 = (int)Math.floor(d2) + 1;
            while (n < n2) {
                void z;
                IBlock block = BlockUtils.getBlock(new WBlockPos((double)x, axisAlignedBB.getMinY(), (double)z));
                if (!((Boolean)collide.invoke((Object)block)).booleanValue()) {
                    return false;
                }
                ++z;
            }
            l = x + 1L;
        }
        return true;
    }

    /*
     * WARNING - void declaration
     */
    @JvmStatic
    public static final boolean collideBlockIntersects(IAxisAlignedBB axisAlignedBB, Function1<? super IBlock, Boolean> collide) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IWorldClient world = iWorldClient;
        double d = thePlayer.getEntityBoundingBox().getMinX();
        boolean bl = false;
        int n = (int)Math.floor(d);
        d = thePlayer.getEntityBoundingBox().getMaxX();
        bl = false;
        int n2 = (int)Math.floor(d) + 1;
        while (n < n2) {
            void x;
            double d2 = thePlayer.getEntityBoundingBox().getMinZ();
            boolean bl2 = false;
            int n3 = (int)Math.floor(d2);
            d2 = thePlayer.getEntityBoundingBox().getMaxZ();
            bl2 = false;
            int n4 = (int)Math.floor(d2) + 1;
            while (n3 < n4) {
                void z;
                WBlockPos blockPos = new WBlockPos((double)x, axisAlignedBB.getMinY(), (double)z);
                IBlock block = BlockUtils.getBlock(blockPos);
                if (((Boolean)collide.invoke((Object)block)).booleanValue()) {
                    Object object = BlockUtils.getState(blockPos);
                    if (object != null) {
                        IIBlockState iIBlockState = object;
                        boolean bl3 = false;
                        boolean bl4 = false;
                        IIBlockState it = iIBlockState;
                        boolean bl5 = false;
                        IBlock iBlock = block;
                        object = iBlock != null ? iBlock.getCollisionBoundingBox(world, blockPos, it) : null;
                        if (object == null) {
                        } else {
                            IIBlockState boundingBox = object;
                            if (thePlayer.getEntityBoundingBox().intersectsWith((IAxisAlignedBB)((Object)boundingBox))) {
                                return true;
                            }
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
}

