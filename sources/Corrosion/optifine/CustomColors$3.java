package optifine;

import net.minecraft.util.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;

final class CustomColors$3 implements CustomColors.IColorizer {
    public int getColor(IBlockAccess p_getColor_1_, BlockPos p_getColor_2_) {
        return CustomColors.access$200() != null ? CustomColors.access$200().getColor(p_getColor_1_, p_getColor_2_) : ColorizerFoliage.getFoliageColorPine();
    }

    public boolean isColorConstant() {
        return CustomColors.access$200() == null;
    }
}
