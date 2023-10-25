/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenMutated;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;

public class BiomeGenSavanna
extends BiomeGenBase {
    private static final WorldGenSavannaTree field_150627_aC = new WorldGenSavannaTree(false);

    @Override
    public WorldGenAbstractTree genBigTreeChance(Random random) {
        return random.nextInt(5) > 0 ? field_150627_aC : this.worldGeneratorTrees;
    }

    protected BiomeGenSavanna(int n) {
        super(n);
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityHorse.class, 1, 2, 6));
        this.theBiomeDecorator.treesPerChunk = 1;
        this.theBiomeDecorator.flowersPerChunk = 4;
        this.theBiomeDecorator.grassPerChunk = 20;
    }

    @Override
    protected BiomeGenBase createMutatedBiome(int n) {
        Mutated mutated = new Mutated(n, this);
        mutated.temperature = (this.temperature + 1.0f) * 0.5f;
        mutated.minHeight = this.minHeight * 0.5f + 0.3f;
        mutated.maxHeight = this.maxHeight * 0.5f + 1.2f;
        return mutated;
    }

    @Override
    public void decorate(World world, Random random, BlockPos blockPos) {
        DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);
        int n = 0;
        while (n < 7) {
            int n2 = random.nextInt(16) + 8;
            int n3 = random.nextInt(16) + 8;
            int n4 = random.nextInt(world.getHeight(blockPos.add(n2, 0, n3)).getY() + 32);
            DOUBLE_PLANT_GENERATOR.generate(world, random, blockPos.add(n2, n4, n3));
            ++n;
        }
        super.decorate(world, random, blockPos);
    }

    public static class Mutated
    extends BiomeGenMutated {
        @Override
        public void genTerrainBlocks(World world, Random random, ChunkPrimer chunkPrimer, int n, int n2, double d) {
            this.topBlock = Blocks.grass.getDefaultState();
            this.fillerBlock = Blocks.dirt.getDefaultState();
            if (d > 1.75) {
                this.topBlock = Blocks.stone.getDefaultState();
                this.fillerBlock = Blocks.stone.getDefaultState();
            } else if (d > -0.5) {
                this.topBlock = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
            }
            this.generateBiomeTerrain(world, random, chunkPrimer, n, n2, d);
        }

        public Mutated(int n, BiomeGenBase biomeGenBase) {
            super(n, biomeGenBase);
            this.theBiomeDecorator.treesPerChunk = 2;
            this.theBiomeDecorator.flowersPerChunk = 2;
            this.theBiomeDecorator.grassPerChunk = 5;
        }

        @Override
        public void decorate(World world, Random random, BlockPos blockPos) {
            this.theBiomeDecorator.decorate(world, random, this, blockPos);
        }
    }
}

