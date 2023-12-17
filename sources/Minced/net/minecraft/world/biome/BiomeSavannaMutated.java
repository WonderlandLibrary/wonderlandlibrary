// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;
import java.util.Random;
import net.minecraft.world.World;

public class BiomeSavannaMutated extends BiomeSavanna
{
    public BiomeSavannaMutated(final BiomeProperties properties) {
        super(properties);
        this.decorator.treesPerChunk = 2;
        this.decorator.flowersPerChunk = 2;
        this.decorator.grassPerChunk = 5;
    }
    
    @Override
    public void genTerrainBlocks(final World worldIn, final Random rand, final ChunkPrimer chunkPrimerIn, final int x, final int z, final double noiseVal) {
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        if (noiseVal > 1.75) {
            this.topBlock = Blocks.STONE.getDefaultState();
            this.fillerBlock = Blocks.STONE.getDefaultState();
        }
        else if (noiseVal > -0.5) {
            this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
        }
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }
    
    @Override
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        this.decorator.decorate(worldIn, rand, this, pos);
    }
}
