package net.minecraft.world.biome;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;

public class BiomeColorHelper {
    private static final ColorResolver GRASS_COLOR = new /* Unavailable Anonymous Inner Class!! */;
    private static final ColorResolver FOLIAGE_COLOR = new /* Unavailable Anonymous Inner Class!! */;
    private static final ColorResolver WATER_COLOR_MULTIPLIER = new /* Unavailable Anonymous Inner Class!! */;

    private static int getColorAtPos(IBlockAccess blockAccess, BlockPos pos, ColorResolver colorResolver) {
        int i = 0;
        int j = 0;
        int k = 0;
        for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable((BlockPos)pos.add(-1, 0, -1), (BlockPos)pos.add(1, 0, 1))) {
            int l = colorResolver.getColorAtPos(blockAccess.getBiomeGenForCoords((BlockPos)blockpos$mutableblockpos), (BlockPos)blockpos$mutableblockpos);
            i += (l & 0xFF0000) >> 16;
            j += (l & 0xFF00) >> 8;
            k += l & 0xFF;
        }
        return (i / 9 & 0xFF) << 16 | (j / 9 & 0xFF) << 8 | k / 9 & 0xFF;
    }

    public static int getGrassColorAtPos(IBlockAccess p_180286_0_, BlockPos p_180286_1_) {
        return BiomeColorHelper.getColorAtPos(p_180286_0_, p_180286_1_, GRASS_COLOR);
    }

    public static int getFoliageColorAtPos(IBlockAccess p_180287_0_, BlockPos p_180287_1_) {
        return BiomeColorHelper.getColorAtPos(p_180287_0_, p_180287_1_, FOLIAGE_COLOR);
    }

    public static int getWaterColorAtPos(IBlockAccess p_180288_0_, BlockPos p_180288_1_) {
        return BiomeColorHelper.getColorAtPos(p_180288_0_, p_180288_1_, WATER_COLOR_MULTIPLIER);
    }
}
