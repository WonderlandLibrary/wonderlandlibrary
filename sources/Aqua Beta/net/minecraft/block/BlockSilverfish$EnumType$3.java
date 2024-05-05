package net.minecraft.block;

import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

static final class BlockSilverfish.EnumType.3
extends BlockSilverfish.EnumType {
    BlockSilverfish.EnumType.3(int meta, String name, String unlocalizedName) {
        super(string, n, meta, name, unlocalizedName, null);
    }

    public IBlockState getModelBlock() {
        return Blocks.stonebrick.getDefaultState().withProperty((IProperty)BlockStoneBrick.VARIANT, (Comparable)BlockStoneBrick.EnumType.DEFAULT);
    }
}
