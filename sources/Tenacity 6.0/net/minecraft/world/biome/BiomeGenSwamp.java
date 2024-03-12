// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.World;
import net.minecraft.block.BlockFlower;
import net.minecraft.util.BlockPos;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySlime;

public class BiomeGenSwamp extends BiomeGenBase
{
    protected BiomeGenSwamp(final int id) {
        super(id);
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
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 1, 1, 1));
    }
    
    @Override
    public WorldGenAbstractTree genBigTreeChance(final Random rand) {
        return this.worldGeneratorSwamp;
    }
    
    @Override
    public int getGrassColorAtPos(final BlockPos pos) {
        final double d0 = BiomeGenSwamp.GRASS_COLOR_NOISE.func_151601_a(pos.getX() * 0.0225, pos.getZ() * 0.0225);
        return (d0 < -0.1) ? 5011004 : 6975545;
    }
    
    @Override
    public int getFoliageColorAtPos(final BlockPos pos) {
        return 6975545;
    }
    
    @Override
    public BlockFlower.EnumFlowerType pickRandomFlower(final Random rand, final BlockPos pos) {
        return BlockFlower.EnumFlowerType.BLUE_ORCHID;
    }
    
    @Override
    public void genTerrainBlocks(final World worldIn, final Random rand, final ChunkPrimer chunkPrimerIn, final int x, final int z, final double noiseVal) {
        final double d0 = BiomeGenSwamp.GRASS_COLOR_NOISE.func_151601_a(x * 0.25, z * 0.25);
        if (d0 > 0.0) {
            final int i = x & 0xF;
            final int j = z & 0xF;
            int k = 255;
            while (k >= 0) {
                if (chunkPrimerIn.getBlockState(j, k, i).getBlock().getMaterial() != Material.air) {
                    if (k != 62 || chunkPrimerIn.getBlockState(j, k, i).getBlock() == Blocks.water) {
                        break;
                    }
                    chunkPrimerIn.setBlockState(j, k, i, Blocks.water.getDefaultState());
                    if (d0 < 0.12) {
                        chunkPrimerIn.setBlockState(j, k + 1, i, Blocks.waterlily.getDefaultState());
                        break;
                    }
                    break;
                }
                else {
                    --k;
                }
            }
        }
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }
}
