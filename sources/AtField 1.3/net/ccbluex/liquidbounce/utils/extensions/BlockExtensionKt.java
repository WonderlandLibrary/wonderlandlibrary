/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.extensions;

import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;

public final class BlockExtensionKt {
    public static final IBlock getBlock(WBlockPos wBlockPos) {
        return BlockUtils.getBlock(wBlockPos);
    }

    public static final WVec3 getVec(WBlockPos wBlockPos) {
        return new WVec3((double)wBlockPos.getX() + 0.5, (double)wBlockPos.getY() + 0.5, (double)wBlockPos.getZ() + 0.5);
    }
}

