/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class BlockHelper
implements Predicate<IBlockState> {
    private final Block block;

    public boolean apply(IBlockState iBlockState) {
        return iBlockState != null && iBlockState.getBlock() == this.block;
    }

    private BlockHelper(Block block) {
        this.block = block;
    }

    public static BlockHelper forBlock(Block block) {
        return new BlockHelper(block);
    }
}

