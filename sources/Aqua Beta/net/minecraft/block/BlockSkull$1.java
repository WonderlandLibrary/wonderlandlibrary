package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySkull;

static final class BlockSkull.1
implements Predicate<BlockWorldState> {
    BlockSkull.1() {
    }

    public boolean apply(BlockWorldState p_apply_1_) {
        return p_apply_1_.getBlockState() != null && p_apply_1_.getBlockState().getBlock() == Blocks.skull && p_apply_1_.getTileEntity() instanceof TileEntitySkull && ((TileEntitySkull)p_apply_1_.getTileEntity()).getSkullType() == 1;
    }
}
