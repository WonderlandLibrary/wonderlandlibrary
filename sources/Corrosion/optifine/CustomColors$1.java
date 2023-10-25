package optifine;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;

final class CustomColors$1 implements CustomColors.IColorizer {
    public int getColor(IBlockAccess p_getColor_1_, BlockPos p_getColor_2_) {
        BiomeGenBase biomegenbase = CustomColors.getColorBiome(p_getColor_1_, p_getColor_2_);
        return CustomColors.access$000() != null && biomegenbase == BiomeGenBase.swampland ? CustomColors.access$000().getColor(biomegenbase, p_getColor_2_) : biomegenbase.getGrassColorAtPos(p_getColor_2_);
    }

    public boolean isColorConstant() {
        return false;
    }
}
