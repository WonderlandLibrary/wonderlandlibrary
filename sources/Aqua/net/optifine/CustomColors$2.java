package net.optifine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.CustomColors;

/*
 * Exception performing whole class analysis ignored.
 */
static final class CustomColors.2
implements CustomColors.IColorizer {
    CustomColors.2() {
    }

    public int getColor(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos) {
        BiomeGenBase biomegenbase = CustomColors.getColorBiome((IBlockAccess)blockAccess, (BlockPos)blockPos);
        return CustomColors.access$100() != null && biomegenbase == BiomeGenBase.swampland ? CustomColors.access$100().getColor(biomegenbase, blockPos) : biomegenbase.getFoliageColorAtPos(blockPos);
    }

    public boolean isColorConstant() {
        return false;
    }
}
