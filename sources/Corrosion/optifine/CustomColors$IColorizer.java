package optifine;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IColorizer {
    int getColor(IBlockAccess var1, BlockPos var2);

    boolean isColorConstant();
}
