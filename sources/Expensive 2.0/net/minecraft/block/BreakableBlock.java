package net.minecraft.block;

import net.minecraft.util.Direction;

public class BreakableBlock extends Block
{
    protected BreakableBlock(Properties properties)
    {
        super(properties);
    }

    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side)
    {
        return adjacentBlockState.isIn(this) ? true : super.isSideInvisible(state, adjacentBlockState, side);
    }
}
