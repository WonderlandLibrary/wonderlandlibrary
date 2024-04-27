/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.injection.backend.BlockImpl;
import net.minecraft.block.Block;

public final class BlockImplKt {
    public static final Block unwrap(IBlock $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((BlockImpl)$this$unwrap).getWrapped();
    }

    public static final IBlock wrap(Block $this$wrap) {
        int $i$f$wrap = 0;
        return new BlockImpl($this$wrap);
    }
}

