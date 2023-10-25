/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import java.util.Random;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeGenMutated
extends BiomeGenBase {
    protected BiomeGenBase baseBiome;

    @Override
    public boolean isEqualTo(BiomeGenBase biomeGenBase) {
        return this.baseBiome.isEqualTo(biomeGenBase);
    }

    @Override
    public Class<? extends BiomeGenBase> getBiomeClass() {
        return this.baseBiome.getBiomeClass();
    }

    @Override
    public int getGrassColorAtPos(BlockPos blockPos) {
        return this.baseBiome.getGrassColorAtPos(blockPos);
    }

    @Override
    public float getSpawningChance() {
        return this.baseBiome.getSpawningChance();
    }

    @Override
    public BiomeGenBase.TempCategory getTempCategory() {
        return this.baseBiome.getTempCategory();
    }

    @Override
    public int getFoliageColorAtPos(BlockPos blockPos) {
        return this.baseBiome.getFoliageColorAtPos(blockPos);
    }

    @Override
    public WorldGenAbstractTree genBigTreeChance(Random random) {
        return this.baseBiome.genBigTreeChance(random);
    }

    public BiomeGenMutated(int n, BiomeGenBase biomeGenBase) {
        super(n);
        this.baseBiome = biomeGenBase;
        this.func_150557_a(biomeGenBase.color, true);
        this.biomeName = String.valueOf(biomeGenBase.biomeName) + " M";
        this.topBlock = biomeGenBase.topBlock;
        this.fillerBlock = biomeGenBase.fillerBlock;
        this.fillerBlockMetadata = biomeGenBase.fillerBlockMetadata;
        this.minHeight = biomeGenBase.minHeight;
        this.maxHeight = biomeGenBase.maxHeight;
        this.temperature = biomeGenBase.temperature;
        this.rainfall = biomeGenBase.rainfall;
        this.waterColorMultiplier = biomeGenBase.waterColorMultiplier;
        this.enableSnow = biomeGenBase.enableSnow;
        this.enableRain = biomeGenBase.enableRain;
        this.spawnableCreatureList = Lists.newArrayList(biomeGenBase.spawnableCreatureList);
        this.spawnableMonsterList = Lists.newArrayList(biomeGenBase.spawnableMonsterList);
        this.spawnableCaveCreatureList = Lists.newArrayList(biomeGenBase.spawnableCaveCreatureList);
        this.spawnableWaterCreatureList = Lists.newArrayList(biomeGenBase.spawnableWaterCreatureList);
        this.temperature = biomeGenBase.temperature;
        this.rainfall = biomeGenBase.rainfall;
        this.minHeight = biomeGenBase.minHeight + 0.1f;
        this.maxHeight = biomeGenBase.maxHeight + 0.2f;
    }

    @Override
    public void genTerrainBlocks(World world, Random random, ChunkPrimer chunkPrimer, int n, int n2, double d) {
        this.baseBiome.genTerrainBlocks(world, random, chunkPrimer, n, n2, d);
    }

    @Override
    public void decorate(World world, Random random, BlockPos blockPos) {
        this.baseBiome.theBiomeDecorator.decorate(world, random, this, blockPos);
    }
}

