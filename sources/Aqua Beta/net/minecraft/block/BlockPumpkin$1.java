package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

static final class BlockPumpkin.1
implements Predicate<IBlockState> {
    BlockPumpkin.1() {
    }

    public boolean apply(IBlockState p_apply_1_) {
        return p_apply_1_ != null && (p_apply_1_.getBlock() == Blocks.pumpkin || p_apply_1_.getBlock() == Blocks.lit_pumpkin);
    }
}
