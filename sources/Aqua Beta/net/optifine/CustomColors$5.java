package net.optifine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.CustomColors;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorMethod;

/*
 * Exception performing whole class analysis ignored.
 */
static final class CustomColors.5
implements CustomColors.IColorizer {
    CustomColors.5() {
    }

    public int getColor(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos) {
        BiomeGenBase biomegenbase = CustomColors.getColorBiome((IBlockAccess)blockAccess, (BlockPos)blockPos);
        return CustomColors.access$400() != null ? CustomColors.access$400().getColor(biomegenbase, blockPos) : (Reflector.ForgeBiome_getWaterColorMultiplier.exists() ? Reflector.callInt((Object)biomegenbase, (ReflectorMethod)Reflector.ForgeBiome_getWaterColorMultiplier, (Object[])new Object[0]) : biomegenbase.waterColorMultiplier);
    }

    public boolean isColorConstant() {
        return false;
    }
}
