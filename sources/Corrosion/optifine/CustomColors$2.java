package optifine;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;

final class CustomColors$2 implements CustomColors.IColorizer {
    public int getColor(IBlockAccess p_getColor_1_, BlockPos p_getColor_2_) {
        BiomeGenBase biomegenbase = CustomColors.getColorBiome(p_getColor_1_, p_getColor_2_);
        return CustomColors.access$100() != null && biomegenbase == BiomeGenBase.swampland ? CustomColors.access$100().getColor(biomegenbase, p_getColor_2_) : biomegenbase.getFoliageColorAtPos(p_getColor_2_);
    }

    public boolean isColorConstant() {
        return false;
    }
}
