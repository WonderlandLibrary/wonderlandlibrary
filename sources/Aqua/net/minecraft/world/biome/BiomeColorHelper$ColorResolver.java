package net.minecraft.world.biome;

import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;

static interface BiomeColorHelper.ColorResolver {
    public int getColorAtPos(BiomeGenBase var1, BlockPos var2);
}
