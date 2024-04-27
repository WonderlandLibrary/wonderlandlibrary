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
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
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
    public static final IMaterial getMaterial(WBlockPos wBlockPos) {
        IIBlockState iIBlockState = BlockUtils.getState(wBlockPos);
        Object object = iIBlockState;
        return object != null && (object = object.getBlock()) != null ? object.getMaterial(iIBlockState) : null;
    }

    @JvmStatic
    public static final boolean collideBlockIntersects(IAxisAlignedBB iAxisAlignedBB, Function1 function1) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IWorldClient iWorldClient2 = iWorldClient;
        double d = iEntityPlayerSP2.getEntityBoundingBox().getMinX();
        boolean bl = false;
        int n = (int)Math.floor(d);
        d = iEntityPlayerSP2.getEntityBoundingBox().getMaxX();
        bl = false;
        int n2 = (int)Math.floor(d) + 1;
        while (n < n2) {
            double d2 = iEntityPlayerSP2.getEntityBoundingBox().getMinZ();
            boolean bl2 = false;
            int n3 = (int)Math.floor(d2);
            d2 = iEntityPlayerSP2.getEntityBoundingBox().getMaxZ();
            bl2 = false;
            int n4 = (int)Math.floor(d2) + 1;
            while (n3 < n4) {
                WBlockPos wBlockPos = new WBlockPos((double)n, iAxisAlignedBB.getMinY(), (double)n3);
                IBlock iBlock = BlockUtils.getBlock(wBlockPos);
                if (((Boolean)function1.invoke((Object)iBlock)).booleanValue()) {
                    Object object = BlockUtils.getState(wBlockPos);
                    if (object != null) {
                        IIBlockState iIBlockState = object;
                        boolean bl3 = false;
                        boolean bl4 = false;
                        IIBlockState iIBlockState2 = iIBlockState;
                        boolean bl5 = false;
                        IBlock iBlock2 = iBlock;
                        object = iBlock2 != null ? iBlock2.getCollisionBoundingBox(iWorldClient2, wBlockPos, iIBlockState2) : null;
                        if (object == null) {
                        } else {
                            IIBlockState iIBlockState3 = object;
                            if (iEntityPlayerSP2.getEntityBoundingBox().intersectsWith((IAxisAlignedBB)((Object)iIBlockState3))) {
                                return true;
                            }
                        }
                    }
                }
                ++n3;
            }
            ++n;
        }
        return false;
    }

    @JvmStatic
    public static final boolean isFullBlock(WBlockPos wBlockPos) {
        Object object;
        block6: {
            block5: {
                object = BlockUtils.getBlock(wBlockPos);
                if (object == null) break block5;
                IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient == null) {
                    Intrinsics.throwNpe();
                }
                IIBlockState iIBlockState = BlockUtils.getState(wBlockPos);
                if (iIBlockState == null) {
                    return false;
                }
                if ((object = object.getCollisionBoundingBox(iWorldClient, wBlockPos, iIBlockState)) != null) break block6;
            }
            return false;
        }
        Object object2 = object;
        return object2.getMaxX() - object2.getMinX() == 1.0 && object2.getMaxY() - object2.getMinY() == 1.0 && object2.getMaxZ() - object2.getMinZ() == 1.0;
    }

    @JvmStatic
    public static final IBlock getBlock(WBlockPos wBlockPos) {
        Object object = MinecraftInstance.mc.getTheWorld();
        return object != null && (object = object.getBlockState(wBlockPos)) != null ? object.getBlock() : null;
    }

    @JvmStatic
    public static final boolean isReplaceable(WBlockPos wBlockPos) {
        boolean bl = false;
        IMaterial iMaterial = BlockUtils.getMaterial(wBlockPos);
        return iMaterial != null ? iMaterial.isReplaceable() : false;
    }

    @JvmStatic
    public static final IIBlockState getState(WBlockPos wBlockPos) {
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        return iWorldClient != null ? iWorldClient.getBlockState(wBlockPos) : null;
    }

    @JvmStatic
    public static final String getBlockName(int n) {
        IBlock iBlock = BlockUtils.access$getFunctions$p$s1046033730().getBlockById(n);
        if (iBlock == null) {
            Intrinsics.throwNpe();
        }
        return iBlock.getLocalizedName();
    }

    @JvmStatic
    public static final boolean collideBlock(IAxisAlignedBB iAxisAlignedBB, Function1 function1) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        double d = iEntityPlayerSP2.getEntityBoundingBox().getMinX();
        boolean bl = false;
        long l = (int)Math.floor(d);
        d = iEntityPlayerSP2.getEntityBoundingBox().getMaxX();
        bl = false;
        long l2 = (long)((int)Math.floor(d)) + 1L;
        while (l < l2) {
            double d2 = iEntityPlayerSP2.getEntityBoundingBox().getMinZ();
            boolean bl2 = false;
            int n = (int)Math.floor(d2);
            d2 = iEntityPlayerSP2.getEntityBoundingBox().getMaxZ();
            bl2 = false;
            int n2 = (int)Math.floor(d2) + 1;
            while (n < n2) {
                IBlock iBlock = BlockUtils.getBlock(new WBlockPos(l, iAxisAlignedBB.getMinY(), (double)n));
                if (!((Boolean)function1.invoke((Object)iBlock)).booleanValue()) {
                    return false;
                }
                ++n;
            }
            ++l;
        }
        return true;
    }

    public static final IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
    }

    static {
        BlockUtils blockUtils;
        INSTANCE = blockUtils = new BlockUtils();
    }

    @JvmStatic
    public static final double getCenterDistance(WBlockPos wBlockPos) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        return iEntityPlayerSP.getDistance((double)wBlockPos.getX() + 0.5, (double)wBlockPos.getY() + 0.5, (double)wBlockPos.getZ() + 0.5);
    }

    private BlockUtils() {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @JvmStatic
    public static final boolean canBeClicked(WBlockPos wBlockPos) {
        IBlock iBlock = BlockUtils.getBlock(wBlockPos);
        if (iBlock == null) return false;
        boolean bl = iBlock.canCollideCheck(BlockUtils.getState(wBlockPos), false);
        if (!bl) return false;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        if (!iWorldClient.getWorldBorder().contains(wBlockPos)) return false;
        return true;
    }

    @JvmStatic
    public static final Map searchBlocks(int n) {
        boolean bl = false;
        Map map = new LinkedHashMap();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return map;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        int n2 = n;
        int n3 = -n + 1;
        if (n2 >= n3) {
            while (true) {
                int n4;
                int n5;
                if ((n5 = n) >= (n4 = -n + 1)) {
                    while (true) {
                        int n6;
                        int n7;
                        if ((n7 = n) >= (n6 = -n + 1)) {
                            while (true) {
                                WBlockPos wBlockPos;
                                if (BlockUtils.getBlock(wBlockPos = new WBlockPos((int)iEntityPlayerSP2.getPosX() + n2, (int)iEntityPlayerSP2.getPosY() + n5, (int)iEntityPlayerSP2.getPosZ() + n7)) == null) {
                                } else {
                                    IBlock iBlock;
                                    map.put(wBlockPos, iBlock);
                                }
                                if (n7 == n6) break;
                                --n7;
                            }
                        }
                        if (n5 == n4) break;
                        --n5;
                    }
                }
                if (n2 == n3) break;
                --n2;
            }
        }
        return map;
    }
}

