package net.optifine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public static interface CustomColors.IColorizer {
    public int getColor(IBlockState var1, IBlockAccess var2, BlockPos var3);

    public boolean isColorConstant();
}
