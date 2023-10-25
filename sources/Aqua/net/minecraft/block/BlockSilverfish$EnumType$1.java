package net.minecraft.block;

import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStone;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

static final class BlockSilverfish.EnumType.1
extends BlockSilverfish.EnumType {
    BlockSilverfish.EnumType.1(int meta, String name) {
        super(string, n, meta, name, null);
    }

    public IBlockState getModelBlock() {
        return Blocks.stone.getDefaultState().withProperty((IProperty)BlockStone.VARIANT, (Comparable)BlockStone.EnumType.STONE);
    }
}
