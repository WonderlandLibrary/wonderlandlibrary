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
    public static final IBlock wrap(Block block) {
        boolean bl = false;
        return new BlockImpl(block);
    }

    public static final Block unwrap(IBlock iBlock) {
        boolean bl = false;
        return ((BlockImpl)iBlock).getWrapped();
    }
}

