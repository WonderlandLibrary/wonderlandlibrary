package net.minecraft.block;

import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

static final class BlockSilverfish.EnumType.2
extends BlockSilverfish.EnumType {
    BlockSilverfish.EnumType.2(int meta, String name, String unlocalizedName) {
        super(string, n, meta, name, unlocalizedName, null);
    }

    public IBlockState getModelBlock() {
        return Blocks.cobblestone.getDefaultState();
    }
}
