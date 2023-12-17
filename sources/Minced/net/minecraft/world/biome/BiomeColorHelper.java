// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import java.util.Iterator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BiomeColorHelper
{
    private static final ColorResolver GRASS_COLOR;
    private static final ColorResolver FOLIAGE_COLOR;
    private static final ColorResolver WATER_COLOR;
    
    private static int getColorAtPos(final IBlockAccess blockAccess, final BlockPos pos, final ColorResolver colorResolver) {
        int i = 0;
        int j = 0;
        int k = 0;
        for (final BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-1, 0, -1), pos.add(1, 0, 1))) {
            final int l = colorResolver.getColorAtPos(blockAccess.getBiome(blockpos$mutableblockpos), blockpos$mutableblockpos);
            i += (l & 0xFF0000) >> 16;
            j += (l & 0xFF00) >> 8;
            k += (l & 0xFF);
        }
        return (i / 9 & 0xFF) << 16 | (j / 9 & 0xFF) << 8 | (k / 9 & 0xFF);
    }
    
    public static int getGrassColorAtPos(final IBlockAccess blockAccess, final BlockPos pos) {
        return getColorAtPos(blockAccess, pos, BiomeColorHelper.GRASS_COLOR);
    }
    
    public static int getFoliageColorAtPos(final IBlockAccess blockAccess, final BlockPos pos) {
        return getColorAtPos(blockAccess, pos, BiomeColorHelper.FOLIAGE_COLOR);
    }
    
    public static int getWaterColorAtPos(final IBlockAccess blockAccess, final BlockPos pos) {
        return getColorAtPos(blockAccess, pos, BiomeColorHelper.WATER_COLOR);
    }
    
    static {
        GRASS_COLOR = new ColorResolver() {
            @Override
            public int getColorAtPos(final Biome biome, final BlockPos blockPosition) {
                return biome.getGrassColorAtPos(blockPosition);
            }
        };
        FOLIAGE_COLOR = new ColorResolver() {
            @Override
            public int getColorAtPos(final Biome biome, final BlockPos blockPosition) {
                return biome.getFoliageColorAtPos(blockPosition);
            }
        };
        WATER_COLOR = new ColorResolver() {
            @Override
            public int getColorAtPos(final Biome biome, final BlockPos blockPosition) {
                return biome.getWaterColor();
            }
        };
    }
    
    interface ColorResolver
    {
        int getColorAtPos(final Biome p0, final BlockPos p1);
    }
}
