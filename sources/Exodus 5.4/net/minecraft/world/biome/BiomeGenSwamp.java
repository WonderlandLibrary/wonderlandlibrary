/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeGenSwamp
extends BiomeGenBase {
    @Override
    public int getFoliageColorAtPos(BlockPos blockPos) {
        return 6975545;
    }

    @Override
    public int getGrassColorAtPos(BlockPos blockPos) {
        double d = GRASS_COLOR_NOISE.func_151601_a((double)blockPos.getX() * 0.0225, (double)blockPos.getZ() * 0.0225);
        return d < -0.1 ? 5011004 : 6975545;
    }

    @Override
    public void genTerrainBlocks(World world, Random random, ChunkPrimer chunkPrimer, int n, int n2, double d) {
        double d2 = GRASS_COLOR_NOISE.func_151601_a((double)n * 0.25, (double)n2 * 0.25);
        if (d2 > 0.0) {
            int n3 = n & 0xF;
            int n4 = n2 & 0xF;
            int n5 = 255;
            while (n5 >= 0) {
                if (chunkPrimer.getBlockState(n4, n5, n3).getBlock().getMaterial() != Material.air) {
                    if (n5 != 62 || chunkPrimer.getBlockState(n4, n5, n3).getBlock() == Blocks.water) break;
                    chunkPrimer.setBlockState(n4, n5, n3, Blocks.water.getDefaultState());
                    if (!(d2 < 0.12)) break;
                    chunkPrimer.setBlockState(n4, n5 + 1, n3, Blocks.waterlily.getDefaultState());
                    break;
                }
                --n5;
            }
        }
        this.generateBiomeTerrain(world, random, chunkPrimer, n, n2, d);
    }

    @Override
    public WorldGenAbstractTree genBigTreeChance(Random random) {
        return this.worldGeneratorSwamp;
    }

    protected BiomeGenSwamp(int n) {
        super(n);
        this.theBiomeDecorator.treesPerChunk = 2;
        this.theBiomeDecorator.flowersPerChunk = 1;
        this.theBiomeDecorator.deadBushPerChunk = 1;
        this.theBiomeDecorator.mushroomsPerChunk = 8;
        this.theBiomeDecorator.reedsPerChunk = 10;
        this.theBiomeDecorator.clayPerChunk = 1;
        this.theBiomeDecorator.waterlilyPerChunk = 4;
        this.theBiomeDecorator.sandPerChunk2 = 0;
        this.theBiomeDecorator.sandPerChunk = 0;
        this.theBiomeDecorator.grassPerChunk = 5;
        this.waterColorMultiplier = 14745518;
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySlime.class, 1, 1, 1));
    }

    @Override
    public BlockFlower.EnumFlowerType pickRandomFlower(Random random, BlockPos blockPos) {
        return BlockFlower.EnumFlowerType.BLUE_ORCHID;
    }
}

