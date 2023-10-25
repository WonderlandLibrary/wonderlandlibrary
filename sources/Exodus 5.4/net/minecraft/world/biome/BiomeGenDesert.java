/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenDesertWells;

public class BiomeGenDesert
extends BiomeGenBase {
    @Override
    public void decorate(World world, Random random, BlockPos blockPos) {
        super.decorate(world, random, blockPos);
        if (random.nextInt(1000) == 0) {
            int n = random.nextInt(16) + 8;
            int n2 = random.nextInt(16) + 8;
            BlockPos blockPos2 = world.getHeight(blockPos.add(n, 0, n2)).up();
            new WorldGenDesertWells().generate(world, random, blockPos2);
        }
    }

    public BiomeGenDesert(int n) {
        super(n);
        this.spawnableCreatureList.clear();
        this.topBlock = Blocks.sand.getDefaultState();
        this.fillerBlock = Blocks.sand.getDefaultState();
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.deadBushPerChunk = 2;
        this.theBiomeDecorator.reedsPerChunk = 50;
        this.theBiomeDecorator.cactiPerChunk = 10;
        this.spawnableCreatureList.clear();
    }
}

