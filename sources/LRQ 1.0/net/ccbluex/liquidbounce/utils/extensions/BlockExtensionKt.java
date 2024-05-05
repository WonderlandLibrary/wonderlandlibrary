/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.extensions;

import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;

public final class BlockExtensionKt {
    public static final IBlock getBlock(WBlockPos $this$getBlock) {
        return BlockUtils.getBlock($this$getBlock);
    }

    public static final WVec3 getVec(WBlockPos $this$getVec) {
        return new WVec3((double)$this$getVec.getX() + 0.5, (double)$this$getVec.getY() + 0.5, (double)$this$getVec.getZ() + 0.5);
    }
}

