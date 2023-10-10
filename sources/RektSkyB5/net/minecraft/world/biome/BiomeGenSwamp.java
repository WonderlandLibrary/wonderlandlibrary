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
    protected BiomeGenSwamp(int p_i1988_1_) {
        super(p_i1988_1_);
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
    public WorldGenAbstractTree genBigTreeChance(Random rand) {
        return this.worldGeneratorSwamp;
    }

    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        double d0 = GRASS_COLOR_NOISE.func_151601_a((double)pos.getX() * 0.0225, (double)pos.getZ() * 0.0225);
        return d0 < -0.1 ? 5011004 : 6975545;
    }

    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        return 6975545;
    }

    @Override
    public BlockFlower.EnumFlowerType pickRandomFlower(Random rand, BlockPos pos) {
        return BlockFlower.EnumFlowerType.BLUE_ORCHID;
    }

    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int p_180622_4_, int p_180622_5_, double p_180622_6_) {
        double d0 = GRASS_COLOR_NOISE.func_151601_a((double)p_180622_4_ * 0.25, (double)p_180622_5_ * 0.25);
        if (d0 > 0.0) {
            int i2 = p_180622_4_ & 0xF;
            int j2 = p_180622_5_ & 0xF;
            for (int k2 = 255; k2 >= 0; --k2) {
                if (chunkPrimerIn.getBlockState(j2, k2, i2).getBlock().getMaterial() == Material.air) continue;
                if (k2 != 62 || chunkPrimerIn.getBlockState(j2, k2, i2).getBlock() == Blocks.water) break;
                chunkPrimerIn.setBlockState(j2, k2, i2, Blocks.water.getDefaultState());
                if (!(d0 < 0.12)) break;
                chunkPrimerIn.setBlockState(j2, k2 + 1, i2, Blocks.waterlily.getDefaultState());
                break;
            }
        }
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, p_180622_4_, p_180622_5_, p_180622_6_);
    }
}

