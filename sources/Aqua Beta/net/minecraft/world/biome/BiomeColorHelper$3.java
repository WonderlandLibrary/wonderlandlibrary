package net.minecraft.world.biome;

import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.biome.BiomeGenBase;

static final class BiomeColorHelper.3
implements BiomeColorHelper.ColorResolver {
    BiomeColorHelper.3() {
    }

    public int getColorAtPos(BiomeGenBase biome, BlockPos blockPosition) {
        return biome.waterColorMultiplier;
    }
}
