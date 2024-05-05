package net.optifine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.optifine.CustomColors;

/*
 * Exception performing whole class analysis ignored.
 */
static final class CustomColors.3
implements CustomColors.IColorizer {
    CustomColors.3() {
    }

    public int getColor(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos) {
        return CustomColors.access$200() != null ? CustomColors.access$200().getColor(blockAccess, blockPos) : ColorizerFoliage.getFoliageColorPine();
    }

    public boolean isColorConstant() {
        return CustomColors.access$200() == null;
    }
}
