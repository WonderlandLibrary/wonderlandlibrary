package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenForest;
import net.minecraft.world.biome.BiomeGenMutated;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

class BiomeGenForest.2
extends BiomeGenMutated {
    BiomeGenForest.2(int id, BiomeGenBase biome) {
        super(id, biome);
    }

    public WorldGenAbstractTree genBigTreeChance(Random rand) {
        return rand.nextBoolean() ? BiomeGenForest.field_150629_aC : BiomeGenForest.field_150630_aD;
    }
}
