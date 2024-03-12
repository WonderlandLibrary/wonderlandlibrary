// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import java.util.Iterator;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BiomeColorHelper
{
    private static final ColorResolver GRASS_COLOR;
    private static final ColorResolver FOLIAGE_COLOR;
    private static final ColorResolver WATER_COLOR_MULTIPLIER;
    
    private static int getColorAtPos(final IBlockAccess blockAccess, final BlockPos pos, final ColorResolver colorResolver) {
        int i = 0;
        int j = 0;
        int k = 0;
        for (final BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-1, 0, -1), pos.add(1, 0, 1))) {
            final int l = colorResolver.getColorAtPos(blockAccess.getBiomeGenForCoords(blockpos$mutableblockpos), blockpos$mutableblockpos);
            i += (l & 0xFF0000) >> 16;
            j += (l & 0xFF00) >> 8;
            k += (l & 0xFF);
        }
        return (i / 9 & 0xFF) << 16 | (j / 9 & 0xFF) << 8 | (k / 9 & 0xFF);
    }
    
    public static int getGrassColorAtPos(final IBlockAccess p_180286_0_, final BlockPos p_180286_1_) {
        return getColorAtPos(p_180286_0_, p_180286_1_, BiomeColorHelper.GRASS_COLOR);
    }
    
    public static int getFoliageColorAtPos(final IBlockAccess p_180287_0_, final BlockPos p_180287_1_) {
        return getColorAtPos(p_180287_0_, p_180287_1_, BiomeColorHelper.FOLIAGE_COLOR);
    }
    
    public static int getWaterColorAtPos(final IBlockAccess p_180288_0_, final BlockPos p_180288_1_) {
        return getColorAtPos(p_180288_0_, p_180288_1_, BiomeColorHelper.WATER_COLOR_MULTIPLIER);
    }
    
    static {
        GRASS_COLOR = new ColorResolver() {
            @Override
            public int getColorAtPos(final BiomeGenBase biome, final BlockPos blockPosition) {
                return biome.getGrassColorAtPos(blockPosition);
            }
        };
        FOLIAGE_COLOR = new ColorResolver() {
            @Override
            public int getColorAtPos(final BiomeGenBase biome, final BlockPos blockPosition) {
                return biome.getFoliageColorAtPos(blockPosition);
            }
        };
        WATER_COLOR_MULTIPLIER = new ColorResolver() {
            @Override
            public int getColorAtPos(final BiomeGenBase biome, final BlockPos blockPosition) {
                return biome.waterColorMultiplier;
            }
        };
    }
    
    interface ColorResolver
    {
        int getColorAtPos(final BiomeGenBase p0, final BlockPos p1);
    }
}
