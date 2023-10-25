package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenMutated;

class BiomeGenForest.1
extends BiomeGenMutated {
    BiomeGenForest.1(int id, BiomeGenBase biome) {
        super(id, biome);
    }

    public void decorate(World worldIn, Random rand, BlockPos pos) {
        this.baseBiome.decorate(worldIn, rand, pos);
    }
}
